package lapr.project.shared;

import lapr.project.model.Position;
import lapr.project.model.Ship;

import java.time.LocalDateTime;
import java.util.*;

public class DistanceCalculation {

    /**
     * Constructor.
     */
    public DistanceCalculation() {
        //Empty constructor.
    }

    /**
     * Calculates the distance between two ship positions.
     *
     * @param pos  the first position
     * @param pos2 the second position
     * @return the distance between two ship positions
     */
    public static double distanceTo(Position pos, Position pos2) {
        if (pos == null && pos2 == null) return 0;

        double radius = 6371000;

        double latShipRadians = pos.getLatitude() * (3.1416 / 180);
        double latShip2Radians = pos.getLatitude() * (3.1416 / 180);
        double latDiff = (pos2.getLatitude() - pos.getLatitude()) * (3.1416 / 180);
        double lonDiff = (pos2.getLongitude() - pos.getLongitude()) * (3.1416 / 180);

        double aux = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(latShipRadians) * Math.cos(latShip2Radians) * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);

        double c = 2 * Math.atan2(Math.sqrt(aux), Math.sqrt(1 - aux));

        double distance = radius * c;

        return (Math.round(distance * 100) / 100.0);
    }

    /**
     * Calculates the travelled distance in a period of time.
     *
     * @param ship          the ship
     * @param localinitiald the initial date
     * @param localfinald   the final date
     * @return the travelled distance in a period of time
     */
    public double travelledDistanceBaseDateTime(Ship ship, LocalDateTime localinitiald, LocalDateTime localfinald) {


        if (ship == null || localinitiald == null || localfinald == null || localinitiald.equals(localfinald)) return 0;


        Date initiald = java.sql.Timestamp.valueOf(localinitiald);
        Date finald = java.sql.Timestamp.valueOf(localfinald);

        double d = 0;
        List<Position> positionList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initiald);

        calendar.add(Calendar.SECOND, -1);
        initiald = calendar.getTime();

        Position sv1;
        Position sv2;

        Iterable<Position> posIterable = ship.getPosDate().getOrderList();
        Iterator<Position> posIterator = posIterable.iterator();


        while (!initiald.after(finald)) {


            while (posIterator.hasNext()) {

                Position pos = posIterator.next();
                Date posDate = java.sql.Timestamp.valueOf(pos.getDate());

                if (!posDate.before(initiald) && !posDate.after(initiald)) {
                    positionList.add(pos);
                }

            }

            posIterator = posIterable.iterator();

            calendar.add(Calendar.SECOND, 1);
            initiald = calendar.getTime();
        }
        //out of the loop

        int count = 0;
        Position[] posA = new Position[2];

        for (Position pos : positionList) {

            posA[count] = pos;
            count++;

            if (count == 2 && posA[0] != null && posA[1] != null) {

                d = d + distanceTo(posA[0], posA[1]);

                count = 0;
                posA[count] = pos;
                count++;
            }
        }
        return d;
    }

}
