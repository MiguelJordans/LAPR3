package lapr.project.model.stores;

import lapr.project.model.*;
import lapr.project.shared.DistanceCalculation;
import lapr.project.shared.PairOfShips;
import lapr.project.shared.tree.AVL;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ShipStore {

    public AVL<ShipByMmsi> shipByMmsiAVL;
    public AVL<ShipByIMO> shipByIMOAVL;
    public AVL<ShipByCallSign> shipByCallSignAVL;
    public AVL<PairOfShips> pairsOfShipsSearchTree = new AVL<>();

    /**
     * Constructor.
     */
    public ShipStore() {
        this.shipByMmsiAVL = new AVL<>();
        this.shipByIMOAVL = new AVL<>();
        this.shipByCallSignAVL = new AVL<>();
    }

    /**
     * Creates a new ship.
     *
     * @param mmsi             the ship's MMSI
     * @param name             the ship's name
     * @param imo              the ship's IMO
     * @param callSign         the ship's call sign
     * @param vesselType       the ship's vessel type
     * @param length           the ship's length
     * @param width            the ship's width
     * @param draft            the ship's draft
     * @param cargo            the ship's cargo
     * @param transceiverClass the ship's transceiver class
     * @return the ship created
     */
    public Ship createShip(int mmsi, String name, String imo, String callSign, String vesselType, double length, double width, double draft, String cargo, char transceiverClass) {
        return new Ship(mmsi, name, imo, callSign, vesselType, length, width, draft, cargo, transceiverClass);
    }

    /**
     * Adds a ship in the BST.
     *
     * @param ship the ship to be added
     * @return true if it adds, false if it doesn't
     */
    public boolean addShip(Ship ship) {
        try {
            insertIntoMmsiAVL(ship);
            insertIntoImoAVL(ship);
            insertIntoCallSign(ship);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void insertIntoMmsiAVL(Ship ship) {
        ShipByMmsi shipToInsert = new ShipByMmsi(ship.getMmsi(), ship.getName(), ship.getImo(), ship.getCallSign(), ship.getVesselType(), ship.getLength(), ship.getWidth(), ship.getDraft(), ship.getCargo(), ship.getTransceiverClass());
        shipToInsert.setPosDate(ship.getPosDate());
        shipByMmsiAVL.insert(shipToInsert);
    }

    public void insertIntoImoAVL(Ship ship) {
        ShipByIMO shipToInsert = new ShipByIMO(ship.getMmsi(), ship.getName(), ship.getImo(), ship.getCallSign(), ship.getVesselType(), ship.getLength(), ship.getWidth(), ship.getDraft(), ship.getCargo(), ship.getTransceiverClass());
        shipToInsert.setPosDate(ship.getPosDate());
        shipByIMOAVL.insert(shipToInsert);
    }

    public void insertIntoCallSign(Ship ship) {
        ShipByCallSign shipToInsert = new ShipByCallSign(ship.getMmsi(), ship.getName(), ship.getImo(), ship.getCallSign(), ship.getVesselType(), ship.getLength(), ship.getWidth(), ship.getDraft(), ship.getCargo(), ship.getTransceiverClass());
        shipToInsert.setPosDate(ship.getPosDate());
        shipByCallSignAVL.insert(shipToInsert);
    }

    /**
     * Checks if a ship's MMSI already exists.
     *
     * @param mmsi the ship's MMSI
     * @return true if it founds the ship, false if it doesn't
     */
    public boolean existsShip(int mmsi) {
        try {
            getShipByMmsi(mmsi);
            return true;
        } catch (Exception e) {
            System.out.println(mmsi);
            return false;
        }
    }

    /**
     * Gets ship by MMSI from BST.
     *
     * @param mmsi the ship's MMSI
     * @return the ship by MMSI
     */
    public Ship getShipByMmsi(int mmsi) {
        ShipByMmsi ship = new ShipByMmsi(mmsi);
        return shipByMmsiAVL.find(ship);
    }

    public Ship getShipByIMO(String imo) {
        ShipByIMO ship = new ShipByIMO(imo);
        return shipByIMOAVL.find(ship);
    }

    public Ship getShipByCallSign(String callSign) {
        ShipByCallSign ship = new ShipByCallSign(callSign);
        return shipByCallSignAVL.find(ship);
    }

    /**
     * Transforms BST in a list.
     *
     * @return BST as a list
     */
   /* public List<Ship> transformBSTintoList() {
        Iterable<Ship> ls = shipBinarySearchTree.inOrder();
        List<Ship> lShip = new ArrayList<>();
        ls.iterator().forEachRemaining(lShip::add);

        return lShip;
    }*/

    /**
     * Writes all ships from BST in order.
     *
     * @return false if BST is empty, true if it isn't
     */
    public boolean writeAllShips() {
        if (shipByIMOAVL.isEmpty() || shipByCallSignAVL.isEmpty() || shipByMmsiAVL.isEmpty()) {
            return false;
        }

        for (Ship s : shipByMmsiAVL.inOrder()) {
            System.out.println(s); //Tiago passa isto para a UI
        }

        return true;
    }

    /**
     * Gets the pair of ships list.
     *
     * @return the pair of ships list
     */
    public List<PairOfShips> getPairsOfShipsSearchTree() {
        return transformBSTintoListPairsOfShip();
    }

    /**
     * Transforms BST in a pair of ships list.
     *
     * @return BST in a pair of ships list
     */
    public List<PairOfShips> transformBSTintoListPairsOfShip() {
        Iterable<PairOfShips> ls = pairsOfShipsSearchTree.inOrder();
        List<PairOfShips> pairsShip = new ArrayList<>();
        ls.iterator().forEachRemaining(pairsShip::add);

        return pairsShip;
    }

    /**
     * Gets the top-N ships in a period of time.
     *
     * @param n          the n value of ships
     * @param vesselType the ship's vessel type
     * @param dt         the initial date time
     * @param dt2        the final date time
     * @return the top-N ships in a period of time
     */
    public List<Ship> getTopN(int n, String vesselType, LocalDateTime dt, LocalDateTime dt2) {

        DistanceCalculation distance = new DistanceCalculation();
        int count = 0;
        List<Ship> shipsByVessel = new ArrayList<>();

        if (shipByMmsiAVL.isEmpty() || shipByCallSignAVL.isEmpty() || shipByIMOAVL.isEmpty()) {
            throw new IllegalArgumentException("Store is empty!");
        }

        List<Ship> shipList = transformAVLintoList();

        for (Ship s : shipList) {
            if (s.getVesselType().equals(vesselType)) {
                shipsByVessel.add(s);
            }
        }

        if (shipsByVessel.size() < n) {
            throw new IllegalArgumentException("There is not enough ships to do this operation!");
        } else {
            double max = 0;
            Ship maxShip = null;
            List<Ship> topNShips = new ArrayList<>();

            while (count < n) {
                for (Ship s : shipsByVessel) {
                    if (max < distance.traveledDistanceBaseDateTime(s, dt, dt2)) {
                        max = distance.traveledDistanceBaseDateTime(s, dt, dt2);
                        maxShip = s;
                    }
                }
                topNShips.add(maxShip);
                shipsByVessel.remove(maxShip);
                max = 0;
                count++;
            }

            Set<Ship> set = new HashSet<>(topNShips);

            if (set.size() < topNShips.size()) {
                throw new IllegalArgumentException("Not enough ships for that period of time!");
            }
            return topNShips;
        }
    }

    /**
     * Gets ship summary by MMSI.
     *
     * @param mmsi the ship's MMSI
     * @return the ship's summary by MMSI
     */
    public String getShipSummaryByMMSI(int mmsi) {

        String returnString = null;
        StringBuilder sb = new StringBuilder();

        Ship s = getShipByMmsi(mmsi);

        if (s != null) {

            sb
                    .append("MMSI : " + s.getMmsi() + "\n")
                    .append(getShipSummaryStructure(s));

            returnString = sb.toString();

        }
        if (returnString == null || returnString.isEmpty()) {
            throw new IllegalArgumentException("Invalid Ship, please enter another one");
        } else {
            return returnString;
        }
    }

    /**
     * Gets ship summary by IMO.
     *
     * @param imo the ship's IMO
     * @return the ship's summary by IMO
     */
    public String getShipSummaryByIMO(String imo) {
        String returnString = null;
        StringBuilder sb = new StringBuilder();

        Ship s = getShipByIMO(imo);

        if (s != null) {

            sb
                    .append("IMO : " + s.getImo() + "\n")
                    .append(getShipSummaryStructure(s));

            returnString = sb.toString();

        }
        if (returnString == null || returnString.isEmpty()) {
            throw new IllegalArgumentException("Invalid Ship, please enter another one");
        } else {
            return returnString;
        }
    }

    /**
     * Gets ship summary by call sign.
     *
     * @param callSign the ship's call sign
     * @return the ship's summary by call sign
     */
    public String getShipSummaryByCallSign(String callSign) {

        String returnString = null;
        StringBuilder sb = new StringBuilder();

        Ship s = getShipByCallSign(callSign);

        if (s != null) {

            sb
                    .append("Call Sign : " + s.getCallSign() + "\n")
                    .append(getShipSummaryStructure(s));

            returnString = sb.toString();

        }

        if (returnString == null || returnString.isEmpty()) {
            throw new IllegalArgumentException("Invalid Ship, please enter another one");
        } else {
            return returnString;
        }
    }

    /**
     * Gets the ship summary structure.
     *
     * @param s the ship
     * @return the ship summary structure
     */
    public String getShipSummaryStructure(Ship s) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Vessel name: " + s.getVesselType() + "\n")
                .append("Start Base date Time: " + getFirstDate(s) + "\n")
                .append("End base date time : " + getLastDate(s) + "\n")
                .append("Total movement time: " + differenceBetweenDates(getFirstDate(s), getLastDate(s)) + " minutes" + "\n")
                .append("Total number of movements : " + s.getTotalNumberOfMovements() + "\n")
                .append("Max SOG : " + getMaxSOG(s) + "\n")
                .append("Mean SOG : " + getMeanSOG(s) + "\n")
                .append("Max COG : " + getMaxCOG(s) + "\n")
                .append("Mean COG : " + getMeanCOG(s) + "\n")
                .append("Departure Latitude : " + getDepartureLatitude(s) + "\n")
                .append("Departure Longitude : " + getDepartureLongitude(s) + "\n")
                .append("Arrival Latitude : " + getArrivalLatitude(s) + "\n")
                .append("Arrival Longitude : " + getArrivalLongitude(s) + "\n")
                .append("Travelled Distance : " + s.getTravelledDistance() + "\n")
                .append("Delta Distance : " + s.getDeltaDistance());

        return sb.toString();
    }

    /**
     * Gets the first position date of a ship.
     *
     * @param s the ship
     * @return the first position date of a ship
     */
    public LocalDateTime getFirstDate(Ship s) {
        try {
            return s.getPosDate().getSmallestPosition().getDate();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Gets the last position date of a ship.
     *
     * @param s the ship
     * @return the last position date of a ship
     */
    public LocalDateTime getLastDate(Ship s) {
        try {
            return s.getPosDate().getBiggestPosition().getDate();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Gets the difference between two dates.
     *
     * @param first  the first date
     * @param second the second date
     * @return the difference between the dates
     */
    public long differenceBetweenDates(LocalDateTime first, LocalDateTime second) {
        try {
            Date firstDate = java.util.Date.from(first.atZone(ZoneId.systemDefault()).toInstant());
            Date secondDate = java.util.Date.from(second.atZone(ZoneId.systemDefault()).toInstant());
            long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());
            return (TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * Gets the maximum SOG of a ship.
     *
     * @param s the ship
     * @return the maximum SOG of a ship
     */
    public double getMaxSOG(Ship s) {
        double maxSog = 0;

        for (Position ps1 : s.getPosDate().getOrderList()) {
            if (ps1.getSog() > maxSog) {
                maxSog = ps1.getSog();
            }
        }
        return maxSog;
    }

    /**
     * Gets the mean SOG of a ship.
     *
     * @param s the ship
     * @return the mean SOG of a ship
     */
    public double getMeanSOG(Ship s) {
        try {
            double meanSOG = 0;
            int count = 0;

            for (Position ps1 : s.getPosDate().getOrderList()) {
                meanSOG += ps1.getSog();
                count++;
            }

            if (count == 0) {
                return 0;
            }
            return (meanSOG / count);

        } catch (ArithmeticException | NullPointerException e) {
            return 0;
        }
    }

    /**
     * Gets the maximum COG of a ship.
     *
     * @param s the ship
     * @return the maximum COG of a ship
     */
    public double getMaxCOG(Ship s) {
        double maxCog = 0;

        for (Position ps1 : s.getPosDate().getOrderList()) {
            if (ps1.getCog() > maxCog) {
                maxCog = ps1.getCog();
            }
        }
        return maxCog;
    }

    /**
     * Gets the mean COG of a ship.
     *
     * @param s the ship
     * @return the mean COG of a ship
     */
    public double getMeanCOG(Ship s) {
        try {
            double meanCOG = 0;
            int count = 0;

            for (Position ps1 : s.getPosDate().getOrderList()) {
                meanCOG += ps1.getCog();
                count++;
            }

            if (count == 0) {
                return 0;
            }
            return (meanCOG / count);

        } catch (ArithmeticException | NullPointerException e) {
            return 0;
        }
    }

    public AVL<ShipByMmsi> getShipByMMSIBinarySearchTree() {
        return shipByMmsiAVL;
    }

    public AVL<ShipByIMO> getShipByIMOBinarySearchTree() {
        return shipByIMOAVL;
    }

    public AVL<ShipByCallSign> getShipByCallSignBinarySearchTree() {
        return shipByCallSignAVL;
    }

    /**
     * Gets the departure latitude of a ship.
     *
     * @param s the ship
     * @return the departure latitude of a ship
     */
    public double getDepartureLatitude(Ship s) {
        try {
            return (s.getPosDate().getSmallestPosition().getLatitude());
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

    /**
     * Gets the departure longitude of a ship.
     *
     * @param s the ship
     * @return the departure longitude of a ship
     */
    public double getDepartureLongitude(Ship s) {
        try {
            return (s.getPosDate().getSmallestPosition().getLongitude());
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

    /**
     * Gets the arrival latitude of a ship.
     *
     * @param s the ship
     * @return the arrival latitude of a ship
     */
    public double getArrivalLatitude(Ship s) {
        try {
            return (s.getPosDate().getBiggestPosition().getLatitude());
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

    /**
     * Gets the arrival longitude of a ship.
     *
     * @param s the ship
     * @return the arrival longitude of a ship
     */
    public double getArrivalLongitude(Ship s) {
        try {
            return (s.getPosDate().getBiggestPosition().getLongitude());
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

    /**
     * Sorts the ship list by travelled distance (descending) and total number of movements (ascending).
     *
     * @return the ship list ordered by travelled distance (descending) and total number of movements (ascending)
     */
    public List<Ship> sortedList() {
        List<Ship> shipList = transformAVLintoList();
        Comparator<Ship> comparator1 = (o1, o2) -> {

            double x1 = o1.getTravelledDistance();
            double x2 = o2.getTravelledDistance();

            double z1 = o1.getPosDate().getSize();
            double z2 = o2.getPosDate().getSize();

            double result1 = x2 - x1;
            double result2 = z2 - z1;

            if (result1 > 0) {
                return 1;
            } else if (result1 < 0) {
                return -1;
            } else {
                if (result2 > 0) {
                    return -1;
                } else if (result2 < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        shipList.sort(comparator1);

        return shipList;
    }


    /**
     * Transforms the AVL in a list.
     *
     * @return the AVL in a list
     */
    public List<Ship> transformAVLintoList() {
        List<Ship> slist = new ArrayList<>();
        Iterable<ShipByMmsi> iterator = shipByMmsiAVL.inOrder();
        iterator.forEach(slist::add);

        return slist;
    }

    /**
     * Gets pairs of ships in the BST.
     */
    public void getPairOfShipsInsideBST() {
        List<Ship> lShip = transformAVLintoList();

        for (int i = 0; i < lShip.size(); i++) {
            Ship s1 = lShip.get(i);
            for (int j = 1; j < lShip.size(); j++) {
                Ship s2 = lShip.get(j);
                if (!s1.equals(s2)) {
                    if (DistanceCalculation.distanceTo(s1.getPosDate().getSmallestPosition(), s2.getPosDate().getSmallestPosition()) < 5000) {
                        if (DistanceCalculation.distanceTo(s1.getPosDate().getBiggestPosition(), s2.getPosDate().getBiggestPosition()) < 5000) {
                            if (s1.getTravelledDistance() != s2.getTravelledDistance()) {
                                if (s1.getTravelledDistance() >= 10000 && s2.getTravelledDistance() >= 10000) {
                                    PairOfShips pairOfShips = new PairOfShips(s1, s2);
                                    pairsOfShipsSearchTree.insert(pairOfShips);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the pair of ships information in a string.
     *
     * @return the pair of ships info in a string
     */
    public String getPairsOfShipsString() {
        getPairOfShipsInsideBST();
        StringBuilder sb = new StringBuilder();

        sb.append("|   Ship 1 MMSI   \t | \t     Ship 2 MMSI  \t  | \t   DistOrig  \t  | \t  DistDest  \t  |      \t  Movs  \t       |       \t   TravelDist  \t        |  \t       Movs  \t       |           \t TravelDist           \t |\n");

        for (PairOfShips pairOfShips : getPairsOfShipsSearchTree()) {
            sb.append("     " + pairOfShips.getLeft().getMmsi() + "\t\t\t     " + pairOfShips.getRight().getMmsi() + "         \t\t\t " + DistanceCalculation.distanceTo(pairOfShips.getLeft().getPosDate().getSmallestPosition(), pairOfShips.getLeft().getPosDate().getBiggestPosition()) + "     \t\t\t\t" + DistanceCalculation.distanceTo(pairOfShips.getLeft().getPosDate().getSmallestPosition(), pairOfShips.getLeft().getPosDate().getBiggestPosition()) + "        \t\t\t" + pairOfShips.getLeft().getTotalNumberOfMovements() + "                 \t\t" + pairOfShips.getLeft().getTravelledDistance() + "            \t\t" + pairOfShips.getRight().getTotalNumberOfMovements() + "                \t\t" + pairOfShips.getRight().getTravelledDistance() + "\n");
        }
        return sb.toString();
    }

}

