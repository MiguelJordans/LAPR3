package lapr.project.data;

import lapr.project.model.Position;
import lapr.project.model.Ship;
import lapr.project.model.stores.ContainerStore;
import lapr.project.model.stores.PortStore;
import lapr.project.model.stores.ShipStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShipStoreData implements Persistable {

    /**
     * Constructor.
     */
    public ShipStoreData() {
        //Empty constructor
    }

    private final Set<Ship> listShip = new HashSet<>();
    private int i = 1;

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Connection connection = databaseConnection.getConnection();
        Ship ship = (Ship) object;

        updateVesselTypes(connection, ship);

        String sqlCommand;

        sqlCommand = "select * from ship where mmsi = " + ship.getMmsi();
        try (PreparedStatement getShipPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet addressesResultSet = getShipPreparedStatement.executeQuery()) {
                if (addressesResultSet.next()) {
                    sqlCommand = "UPDATE SHIP SET CALLSIGN = '" + ship.getCallSign() + "', VESSELTYPE = '" + ship.getVesselType() + "', IMO = '" + ship.getImo() + "', NAME = '" + ship.getName() + "', LENGTH = " + ship.getLength() + ", WIDTH = " + ship.getWidth() + ", CAPACITY = " + ship.getCargo() + ", DRAFT = " + ship.getDraft() + " where MMSI = " + ship.getMmsi();

                    try (PreparedStatement saveContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                        saveContainerPreparedStatement.executeUpdate();
                        return true;
                    }

                } else {
                    sqlCommand = "select MAX(VEHICLEID) as vehicleID from VEHICLE";

                    try (PreparedStatement saveContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                        try (ResultSet resultSet = saveContainerPreparedStatement.executeQuery()) {
                            resultSet.next();

                            int idTransportation = resultSet.getInt(1);

                            idTransportation += 1;

                            if (idTransportation == 10 || idTransportation > 10) {
                                idTransportation += i;
                                i++;
                            }

                            sqlCommand = "insert into vehicle (vehicleid) values (" + idTransportation + ")";
                            try (PreparedStatement saveContainerPreparedStatement1 = connection.prepareStatement(sqlCommand)) {
                                saveContainerPreparedStatement1.executeUpdate();
                            }

                            sqlCommand = "INSERT INTO SHIP(MMSI, VEHICLEID, VESSELTYPE, IMO, CALLSIGN, NAME, LENGTH, WIDTH, CAPACITY, DRAFT) values (" + ship.getMmsi() + "," + idTransportation + "," + ship.getVesselType() + ",'" + ship.getImo() + "','" + ship.getCallSign() + "','" + ship.getName() + "'," + ship.getLength() + ',' + ship.getWidth() + ',' + ship.getCargo() + ',' + ship.getDraft() + ")";
                            try (PreparedStatement saveContainerPreparedStatement1 = connection.prepareStatement(sqlCommand)) {
                                saveContainerPreparedStatement1.executeUpdate();
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    /**
     * Updates the vessel types.
     *
     * @param connection the database connection
     * @param ship       the ship
     * @return true if it succeeds, false if it doesn't
     */
    private boolean updateVesselTypes(Connection connection, Ship ship) {
        String sqlCommand = "select VESSELTYPEID from VESSELTYPE vt where vt.VESSELTYPEID = '" + ship.getVesselType() + "'";

        try (PreparedStatement getVesselTypePreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet vesselTypesResultSet = getVesselTypePreparedStatement.executeQuery()) {
                if (vesselTypesResultSet.next()) {
                    sqlCommand = "UPDATE VESSELTYPE SET VESSELTYPE = " + ship.getVesselType();

                    try (PreparedStatement saveContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                        saveContainerPreparedStatement.executeUpdate();
                        return true;
                    }

                } else {
                    sqlCommand = "INSERT INTO VESSELTYPE(VESSELTYPEID, VESSELTYPE) values ('" + ship.getVesselType() + "','" + ship.getVesselType() + "')";

                    try (PreparedStatement saveContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                        saveContainerPreparedStatement.executeUpdate();
                        return true;
                    }
                }
            }
        } catch (SQLException throwables) {
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Connection connection = databaseConnection.getConnection();
        Ship ship = (Ship) object;

        int i = getShipPositionDateSize(databaseConnection, ship.getMmsi());
        int j = 0;

        while (i != 0) {
            Position shipPos = getShipPositionWithoutDate(databaseConnection, ship.getMmsi(), j);
            if (shipPos != null) deleteShipPosition(databaseConnection, shipPos, ship.getMmsi());
            i--;
            j++;
        }

        try {
            String sqlCommand;
            sqlCommand = "delete from ship where mmsi = " + ship.getMmsi();
            try (PreparedStatement deleteShipPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteShipPreparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public Object getElement(DatabaseConnection databaseConnection, Object object) {
        Connection connection = databaseConnection.getConnection();
        Integer mmsiInteger = (Integer) object;

        int mmsi = mmsiInteger.intValue();

        String sqlCommand;
        sqlCommand = "SELECT * from SHIP where MMSI = '" + mmsi + "'";

        try (PreparedStatement getShipPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet shipResultSet = getShipPreparedStatement.executeQuery()) {
                if (shipResultSet.next()) {

                    String vesselType = shipResultSet.getString("VESSELTYPE");

                    String imo = shipResultSet.getString("IMO");
                    String callSign = shipResultSet.getString("CALLSIGN");
                    String shipName = shipResultSet.getString("NAME");

                    double length = shipResultSet.getDouble("LENGTH");
                    double width = shipResultSet.getDouble("WIDTH");
                    double capacity = shipResultSet.getDouble("CAPACITY");
                    double draft = shipResultSet.getDouble("DRAFT");


                    return new Ship(mmsi, shipName, imo, 1, 1, callSign, vesselType, length, width, capacity, draft);

                } else return null;

            }

        } catch (SQLException throwables) {
            Logger.getLogger(PortStore.class.getName()).log(Level.SEVERE, null, throwables);
            databaseConnection.registerError(throwables);
            return null;
        }
    }

    /**
     * Gets the transceiver.
     *
     * @param databaseConnection the database connection
     * @param transceiverID      the transceiver ID
     * @return the transceiver
     */
    public char getTransceiver(DatabaseConnection databaseConnection, int transceiverID) {
        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "SELECT * from TRANSCEIVER where MMSI = " + transceiverID;

        try (PreparedStatement getTransceiverPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet transceiverResultSet = getTransceiverPreparedStatement.executeQuery()) {
                if (transceiverResultSet.next()) {
                    String transceiverString = transceiverResultSet.getString("TRANSCEIVER");
                    char[] transceiverCharArray = transceiverString.toCharArray();
                    return transceiverCharArray[0];

                } else return '0';
            }
        } catch (SQLException throwables) {
            Logger.getLogger(PortStore.class.getName()).log(Level.SEVERE, null, throwables);
            databaseConnection.registerError(throwables);
            return '0';
        }
    }

    /**
     * Saves a ship position.
     *
     * @param databaseConnection the database connection
     * @param object             the object
     * @param ship               the ship
     * @return true if it succeeds, false if it doesn't
     */
    public boolean saveShipPosition(DatabaseConnection databaseConnection, Object object, Ship ship) {
        Connection connection = databaseConnection.getConnection();
        Position shipPosition = (Position) object;

        String date = getDate(shipPosition);

        String sqlCommand = "select * from POSITIONALMESSAGE where mmsi = " + ship.getMmsi() + " AND baseDateTime = '" + date + "'";

        try (PreparedStatement getContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet addressesResultSet = getContainerPreparedStatement.executeQuery()) {
                if (addressesResultSet.next()) {
                    sqlCommand = "update POSITIONALMESSAGE SET LONGITUDE = " + shipPosition.getLongitude() + ", LATITUDE = " + shipPosition.getLatitude() + ", SOG = " + shipPosition.getSog() + ",  COG = " + shipPosition.getCog() + ", HEADING = " + shipPosition.getHeading() + " where MMSI = " + ship.getMmsi() + " AND BASEDATETIME = '" + date + "'";

                    try (PreparedStatement updatePositionalMessagePreparedStatement = connection.prepareStatement(sqlCommand)) {
                        updatePositionalMessagePreparedStatement.executeUpdate();
                        return true;
                    }

                } else {
                    sqlCommand = "SELECT VEHICLEID FROM SHIP where MMSI = " + ship.getMmsi();

                    try (PreparedStatement getVehicleIDPreparedStatement = connection.prepareStatement(sqlCommand)) {
                        try (ResultSet vehicleSet = getVehicleIDPreparedStatement.executeQuery()) {
                            vehicleSet.next();

                            int vehicleID = vehicleSet.getInt(1);

                            sqlCommand = "SELECT TRANSCEIVERID FROM TRANSCEIVER where TRANSCEIVER = '" + ship.getTransceiverClass() + "'";

                            try (PreparedStatement getTransceiverPreparedStatement = connection.prepareStatement(sqlCommand)) {
                                try (ResultSet resultsetT = getTransceiverPreparedStatement.executeQuery()) {
                                    if (resultsetT.next()) {
                                        int transceiverID = resultsetT.getInt(1);

                                        sqlCommand = "INSERT INTO POSITIONALMESSAGE (BASEDATETIME, MMSI, VEHICLEID, LONGITUDE, LATITUDE, SOG, COG, HEADING, TRANSCEIVERID)  VALUES ('" + date + "'," + ship.getMmsi() + "," + vehicleID + "," + shipPosition.getLongitude() + "," + shipPosition.getLatitude() + "," + shipPosition.getSog() + "," + shipPosition.getCog() + "," + shipPosition.getHeading() + "," + transceiverID + ")";
                                        try (PreparedStatement insertPossitionalMessagePreparedStatement1 = connection.prepareStatement(sqlCommand)) {
                                            insertPossitionalMessagePreparedStatement1.executeUpdate();
                                        }
                                        return true;

                                    } else {
                                        sqlCommand = "select MAX(TRANSCEIVERID) as TRANSCEIVERID from TRANSCEIVER";

                                        try (PreparedStatement getTransceiverIDMaxPreparedStatement = connection.prepareStatement(sqlCommand)) {
                                            try (ResultSet resultsetTT = getTransceiverIDMaxPreparedStatement.executeQuery()) {
                                                resultsetTT.next();

                                                int transceiverID = resultsetTT.getInt(1) + 1;

                                                sqlCommand = "INSERT into TRANSCEIVER(TRANSCEIVERID, TRANSCEIVER) values (" + transceiverID + ",'" + ship.getTransceiverClass() + "')";

                                                try (PreparedStatement insertTransceiverMessagePreparedStatement1 = connection.prepareStatement(sqlCommand)) {
                                                    insertTransceiverMessagePreparedStatement1.executeUpdate();
                                                }

                                                sqlCommand = "INSERT INTO POSITIONALMESSAGE (BASEDATETIME, MMSI, VEHICLEID, LONGITUDE, LATITUDE, SOG, COG, HEADING, TRANSCEIVERID)  VALUES ('" + date + "'," + ship.getMmsi() + "," + vehicleID + "," + shipPosition.getLongitude() + "," + shipPosition.getLatitude() + "," + shipPosition.getSog() + "," + shipPosition.getCog() + "," + shipPosition.getHeading() + ",'" + ship.getTransceiverClass() + "'";
                                                try (PreparedStatement insertPossitionalMessagePreparedStatement1 = connection.prepareStatement(sqlCommand)) {
                                                    insertPossitionalMessagePreparedStatement1.executeUpdate();
                                                }
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    /**
     * Deletes a ship position.
     *
     * @param databaseConnection the database connection
     * @param object             the object
     * @param mmsi               the ship MMSI
     * @return true if it succeeds, false if it doesn't
     */
    public boolean deleteShipPosition(DatabaseConnection databaseConnection, Object object, int mmsi) {
        Connection connection = databaseConnection.getConnection();
        Position shipPosition = (Position) object;

        String dateTime = getDate(shipPosition);

        try {
            String sqlCommand;
            sqlCommand = "delete from POSITIONALMESSAGE where (MMSI = " + mmsi + "and BASEDATETIME = '" + dateTime + "')";
            try (PreparedStatement deleteContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteContainerPreparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    /**
     * Gets the ship position.
     *
     * @param databaseConnection the database connection
     * @param dateTime           the date time
     * @param mmsi               the ship MMSI
     * @return the ship position
     */
    public Position getShipPosition(DatabaseConnection databaseConnection, String dateTime, int mmsi) {
        Connection connection = databaseConnection.getConnection();

        dateTime = verifyString(dateTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String sqlCommand = "select * from POSITIONALMESSAGE where mmsi = " + mmsi + " AND baseDateTime = '" + dateTime + "'";

        try (PreparedStatement getShipPositionPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet shipPositionResultSet = getShipPositionPreparedStatement.executeQuery()) {
                if (shipPositionResultSet.next()) {
                    String positionalDate = shipPositionResultSet.getString("BASEDATETIME");
                    double longitude = shipPositionResultSet.getDouble("LONGITUDE");
                    double latitude = shipPositionResultSet.getDouble("LATITUDE");
                    double sog = shipPositionResultSet.getDouble("SOG");
                    double cog = shipPositionResultSet.getDouble("COG");
                    double heading = shipPositionResultSet.getDouble("HEADING");

                    LocalDateTime date = LocalDateTime.parse(positionalDate, formatter);

                    return new Position(latitude, longitude, heading, sog, cog, date);

                } else {
                    return null;
                }
            }
        } catch (SQLException throwables) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, throwables);
            databaseConnection.registerError(throwables);
            return null;
        }
    }

    /**
     * Gets the ship position without the date.
     *
     * @param databaseConnection the database connection
     * @param mmsi               the ship MMSI
     * @param i                  the count
     * @return the ship position without the date
     */
    public Position getShipPositionWithoutDate(DatabaseConnection databaseConnection, int mmsi, int i) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "select * from POSITIONALMESSAGE where mmsi = " + mmsi;

        try (PreparedStatement getShipPositionWithoutDatePreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet shipPositionResultSet = getShipPositionWithoutDatePreparedStatement.executeQuery()) {
                for (int j = 0; j < i; j++) {
                    shipPositionResultSet.next();
                }

                if (shipPositionResultSet.next()) {
                    String positionalDate = shipPositionResultSet.getString("BASEDATETIME");
                    double longitude = shipPositionResultSet.getDouble("LONGITUDE");
                    double latitude = shipPositionResultSet.getDouble("LATITUDE");
                    double sog = shipPositionResultSet.getDouble("SOG");
                    double cog = shipPositionResultSet.getDouble("COG");
                    double heading = shipPositionResultSet.getDouble("HEADING");

                    LocalDateTime date = LocalDateTime.parse(positionalDate, formatter);

                    return new Position(latitude, longitude, heading, sog, cog, date);

                } else {
                    return null;
                }
            }
        } catch (SQLException throwables) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, throwables);
            databaseConnection.registerError(throwables);
            return null;
        }
    }

    /**
     * Gets the number of positional messages of a ship.
     *
     * @param databaseConnection the database connection
     * @param mmsi               the ship's MMSI
     * @return the number of positional messages of a ship
     */
    public int getShipPositionDateSize(DatabaseConnection databaseConnection, int mmsi) {
        String sqlCommand = "SELECT COUNT(*) FROM PositionalMessage pm where pm.mmsi = '" + mmsi + "'";

        Connection connection = databaseConnection.getConnection();

        try (PreparedStatement getNumberPositions = connection.prepareStatement(sqlCommand)) {
            try (ResultSet getNumberPositionsResultSet = getNumberPositions.executeQuery()) {
                if (getNumberPositionsResultSet.next()) {

                    return getNumberPositionsResultSet.getInt(1);

                } else {
                    return 0;
                }
            }
        } catch (SQLException throwables) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, throwables);
            databaseConnection.registerError(throwables);
            return 0;
        }
    }

    /**
     * Gets the date of the ship position.
     *
     * @param shipPosition the ship position
     * @return the date of the ship position
     */
    public String getDate(Position shipPosition) {
        String localDateTime = shipPosition.getDate().toString();

        char[] temp = localDateTime.toCharArray();

        temp[10] = ' ';

        localDateTime = String.valueOf(temp);

        return localDateTime;
    }

    /**
     * Verifies the string.
     *
     * @param time the time
     * @return a String that contains the characters of the character array
     */
    public String verifyString(String time) {

        char[] stringIntoArrayChar = time.toCharArray();

        if (stringIntoArrayChar[10] == ' ') {
            stringIntoArrayChar[10] = ' ';
        }

        return String.valueOf(stringIntoArrayChar);
    }

    /**
     * Fills the ship list.
     *
     * @param databaseConnection the database connection
     */
    private void fillShipList(DatabaseConnection databaseConnection) {

        Connection connection = databaseConnection.getConnection();

        String sqlCommand;

        sqlCommand = "SELECT * from SHIP";

        try (PreparedStatement getPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet resultSet = getPreparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    String s = resultSet.getString("VESSELTYPE");
                    char[] transceiverArray = s.toCharArray();
                    char transceiver = transceiverArray[0];

                    int mmsi = resultSet.getInt("MMSI");
                    String name = resultSet.getString("NAME");
                    String imo = resultSet.getString("IMO");
                    String callSign = resultSet.getString("CALLSIGN");
                    double length = resultSet.getDouble("LENGTH");
                    double width = resultSet.getDouble("WIDTH");
                    double draft = resultSet.getDouble("DRAFT");
                    String cargo = resultSet.getString("CAPACITY");

                    String vesselTYPe = resultSet.getString("VESSELTYPE");

                    Ship ship = new Ship(mmsi, name, imo, callSign, vesselTYPe, length, width, draft, cargo, transceiver);

                    listShip.add(ship);
                }

            }
        } catch (SQLException e) {
            Logger.getLogger(ContainerStore.class.getName()).log(Level.SEVERE, null, e);
            databaseConnection.registerError(e);
        }

    }

    /**
     * Gets the ship list.
     *
     * @param databaseConnection the database connection
     * @return the ship list
     */
    public Set<Ship> getListShips(DatabaseConnection databaseConnection) {

        if (listShip.isEmpty()) fillShipList(databaseConnection);

        return listShip;
    }
}