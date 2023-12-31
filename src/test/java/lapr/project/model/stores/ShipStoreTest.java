package lapr.project.model.stores;

import lapr.project.data.CargoManifest;
import lapr.project.model.*;
import lapr.project.shared.PairOfShips;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipStoreTest {

    Ship shipgeral = new Ship(111111111, "name", "IMO1111111", 1, 1, "A", "A", 1, 1, 1, 1);
    Ship shipgeral2 = new Ship(222222222, "name", "IMO2222222", 1, 1, "A", "A", 1, 1, 1, 1);
    Ship shipgeral3 = new Ship(111114111, "name", "IMO1111111", 1, 1, "F", "A", 1, 1, 1, 1);
    Ship shipgeral4 = new Ship(111114111, "name", "IMO9999999", 1, 1, "F", "A", 1, 1, 1, 1);
    Ship shipgeral5 = new Ship(999999999, "name", "IMO9999999", 1, 1, "F", "A", 1, 1, 1, 1);

    ShipStore shipstore = new ShipStore();

    String sdate = "31-12-2020 23:16";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    LocalDateTime date = LocalDateTime.parse(sdate, formatter);
    Position posgeral = new Position(0, 0, 0, 0, 1, date);
    String sdate2 = "31-12-2020 23:50";
    LocalDateTime date2 = LocalDateTime.parse(sdate2, formatter);
    Position posgeral2 = new Position(10, 20, 30, 20, 10, date2);
    String sdate3 = "31-12-2020 23:55";
    LocalDateTime date3 = LocalDateTime.parse(sdate3, formatter);
    Position posgeral3 = new Position(5, 10, 15, 10, 5, date3);
    String sdate4 = "31-12-2020 23:58";
    LocalDateTime date4 = LocalDateTime.parse(sdate4, formatter);
    Position posgeral4 = new Position(10, 20, 30, 20, 10, date4);

    @Test
    void addShipMutant() {

        String dt = "2020-01-01";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;

        try {
            date = sdf.parse(dt);
        } catch (ParseException e) {

        }

        Container containerReal = new Container("20BD", 1000, 1000, 100, "20RF", false, false);
        Country co1 = new Country("United Kingdom", "UK".toCharArray(), "UNK".toCharArray(), 25, Continent.EUROPE);

        Port port = new Port("29002", "Liverpool", "Europe", co1, new FacilityLocation(53.46666667, -3.033333333), 0);


        CargoManifest cargoManifest = new CargoManifest("1Ab", port, date);

        shipgeral.getCargoManifestAVL().insert(cargoManifest);

        shipgeral.addLoadedContainer(containerReal, port);
        shipgeral.addOffLoadedContainer(containerReal, port);


        shipstore.insertIntoMmsiAVL(shipgeral);

        Ship s = shipstore.getShipByMmsi(shipgeral.getMmsi());

        if (s.getCargoManifestAVL().isEmpty()) fail();
        if (s.getCurrentCapacity() == 0) fail();

        assertNotNull(s);


    }

    @Test
    void getCargoMutant() {

        if (shipstore.writeAllShips()) fail();


    }

    @Test
    void getCargoMutant2() {


        shipstore.insertIntoMmsiAVL(shipgeral);

        if (shipstore.writeAllShips()) fail();


    }

    @Test
    void getCargoMutant3() {

        shipstore.insertIntoMmsiAVL(shipgeral);
        shipstore.insertIntoCallSign(shipgeral);

        if (shipstore.writeAllShips()) fail();


    }

    @Test
    void getCargoMutant4() {

        shipstore.insertIntoCallSign(shipgeral);

        if (shipstore.writeAllShips()) fail();


    }


    @Test
    void getCargoMutant5() {

        shipstore.insertIntoImoAVL(shipgeral);

        if (shipstore.writeAllShips()) fail();


    }


    @Test
    void getCargoMutant6() {

        shipstore.insertIntoImoAVL(shipgeral);
        shipstore.insertIntoCallSign(shipgeral);

        if (shipstore.writeAllShips()) fail();


    }

    @Test
    void getCargoMutant7() {

        shipstore.insertIntoImoAVL(shipgeral);
        shipstore.insertIntoMmsiAVL(shipgeral);

        if (shipstore.writeAllShips()) fail();


    }

    @Test
    void getMMSIAVl() {

        shipstore.insertIntoMmsiAVL(shipgeral);


        if (shipstore.getShipByMmsiAVL().isEmpty()) fail();


    }

    @Test
    void existsShip() {
        shipstore.addShip(shipgeral);
        assertNotNull(shipstore.getShipByMmsi(shipgeral.getMmsi()));

        Ship ship1 = new Ship(123456789, "ya", "IMO1111211", "L", "123", 15.6, 45, 54, "NA", 'b');
        assertFalse((shipstore.existsShip(123456789)));
    }

    @Test
    void findShip() {
        shipstore.addShip(shipgeral);
        assertTrue(shipstore.existsShip((shipgeral.getMmsi())));
    }

    @Test
    void getShipSummaryByMMSI1Position() {

        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipstore.addShip(shipgeral);
        String expected = "MMSI : 111111111\n" +
                "Vessel name: A\n" +
                "Start Base date Time: 2020-12-31T23:16\n" +
                "End base date time : 2020-12-31T23:16\n" +
                "Total movement time: 0 minutes\n" +
                "Total number of movements : 1\n" +
                "Max SOG : 0.0\n" +
                "Mean SOG : 0.0\n" +
                "Max COG : 1.0\n" +
                "Mean COG : 1.0\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 0.0\n" +
                "Arrival Longitude : 0.0\n" +
                "Travelled Distance : 0.0\n" +
                "Delta Distance : 0.0";
        //Act
        String actual = shipstore.getShipSummaryByMMSI(shipgeral.getMmsi());
        //Assert
        assertEquals(expected, actual);

    }


    @Test
    void getShipSummaryByMMSIInvalid() {

        //Arrange
        shipstore.addShip(shipgeral);
        String expected = "MMSI : 111111111\n" +
                "Vessel name: A\n" +
                "Start Base date Time: null\n" +
                "End base date time : null\n" +
                "Total movement time: 0 minutes\n" +
                "Total number of movements : 0\n" +
                "Max SOG : 0.0\n" +
                "Mean SOG : 0.0\n" +
                "Max COG : 0.0\n" +
                "Mean COG : 0.0\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 0.0\n" +
                "Arrival Longitude : 0.0\n" +
                "Travelled Distance : 0.0\n" +
                "Delta Distance : 0.0";
        //Act
        String actual = shipstore.getShipSummaryByMMSI(shipgeral.getMmsi());
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void getShipSummaryByMMSINull() {

        //Arrange
        shipstore.addShip(shipgeral);
        shipstore.addShip(shipgeral5);
        String expected = "MMSI : 999999999\n" +
                "Vessel name: A\n" +
                "Start Base date Time: null\n" +
                "End base date time : null\n" +
                "Total movement time: 0 minutes\n" +
                "Total number of movements : 0\n" +
                "Max SOG : 0.0\n" +
                "Mean SOG : 0.0\n" +
                "Max COG : 0.0\n" +
                "Mean COG : 0.0\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 0.0\n" +
                "Arrival Longitude : 0.0\n" +
                "Travelled Distance : 0.0\n" +
                "Delta Distance : 0.0";
        //Act
        String actual = shipstore.getShipSummaryByMMSI(999999999);
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void getShipSummaryByMMSI2Positions() {

        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipgeral.getPosDate().addPosition(posgeral2);
        shipstore.addShip(shipgeral);
        shipstore.calculateTravelledDistanceOfAllShips();
        String expected = "MMSI : 111111111\n" +
                "Vessel name: A\n" +
                "Start Base date Time: 2020-12-31T23:16\n" +
                "End base date time : 2020-12-31T23:50\n" +
                "Total movement time: 34 minutes\n" +
                "Total number of movements : 2\n" +
                "Max SOG : 20.0\n" +
                "Mean SOG : 10.0\n" +
                "Max COG : 10.0\n" +
                "Mean COG : 5.5\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 10.0\n" +
                "Arrival Longitude : 20.0\n" +
                "Travelled Distance : 2491535.47\n" +
                "Delta Distance : 2491535.47";
        //Act
        String actual = shipstore.getShipSummaryByMMSI(shipgeral.getMmsi());
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void getShipSummaryByIMO1Position() {

        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipstore.addShip(shipgeral);
        String expected = "IMO : IMO1111111\n" +
                "Vessel name: A\n" +
                "Start Base date Time: 2020-12-31T23:16\n" +
                "End base date time : 2020-12-31T23:16\n" +
                "Total movement time: 0 minutes\n" +
                "Total number of movements : 1\n" +
                "Max SOG : 0.0\n" +
                "Mean SOG : 0.0\n" +
                "Max COG : 1.0\n" +
                "Mean COG : 1.0\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 0.0\n" +
                "Arrival Longitude : 0.0\n" +
                "Travelled Distance : 0.0\n" +
                "Delta Distance : 0.0";
        //Act
        String actual = shipstore.getShipSummaryByIMO(shipgeral.getImo());
        //Assert
        assertEquals(expected, actual);

    }


    @Test
    void getShipSummaryByIMOInvalid() {

        //Arrange
        shipstore.addShip(shipgeral);
        String expected = "MMSI : 111111111\n" +
                "Vessel name: A\n" +
                "Start Base date Time: null\n" +
                "End base date time : null\n" +
                "Total movement time: 0 minutes\n" +
                "Total number of movements : 0\n" +
                "Max SOG : 0.0\n" +
                "Mean SOG : 0.0\n" +
                "Max COG : 0.0\n" +
                "Mean COG : 0.0\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 0.0\n" +
                "Arrival Longitude : 0.0\n" +
                "Travelled Distance : 0.0\n" +
                "Delta Distance : 0.0";
        //Act
        String actual = shipstore.getShipSummaryByMMSI(shipgeral.getMmsi());
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void getShipSummaryByIMO2Positions() {

        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipgeral.getPosDate().addPosition(posgeral2);
        shipstore.addShip(shipgeral);
        shipstore.calculateTravelledDistanceOfAllShips();
        String expected = "IMO : IMO1111111\n" +
                "Vessel name: A\n" +
                "Start Base date Time: 2020-12-31T23:16\n" +
                "End base date time : 2020-12-31T23:50\n" +
                "Total movement time: 34 minutes\n" +
                "Total number of movements : 2\n" +
                "Max SOG : 20.0\n" +
                "Mean SOG : 10.0\n" +
                "Max COG : 10.0\n" +
                "Mean COG : 5.5\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 10.0\n" +
                "Arrival Longitude : 20.0\n" +
                "Travelled Distance : 2491535.47\n" +
                "Delta Distance : 2491535.47";
        //Act
        String actual = shipstore.getShipSummaryByIMO(shipgeral.getImo());
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void getShipSummaryByIMONull() {

        //Arrange
        shipstore.addShip(shipgeral4);
        String expected = "IMO : IMO9999999\n" +
                "Vessel name: A\n" +
                "Start Base date Time: null\n" +
                "End base date time : null\n" +
                "Total movement time: 0 minutes\n" +
                "Total number of movements : 0\n" +
                "Max SOG : 0.0\n" +
                "Mean SOG : 0.0\n" +
                "Max COG : 0.0\n" +
                "Mean COG : 0.0\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 0.0\n" +
                "Arrival Longitude : 0.0\n" +
                "Travelled Distance : 0.0\n" +
                "Delta Distance : 0.0";
        //Act
        String actual = shipstore.getShipSummaryByIMO("IMO9999999");
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void getShipSummaryByCallSign() {

        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipstore.addShip(shipgeral);
        String expected = "Call Sign : A\n" +
                "Vessel name: A\n" +
                "Start Base date Time: 2020-12-31T23:16\n" +
                "End base date time : 2020-12-31T23:16\n" +
                "Total movement time: 0 minutes\n" +
                "Total number of movements : 1\n" +
                "Max SOG : 0.0\n" +
                "Mean SOG : 0.0\n" +
                "Max COG : 1.0\n" +
                "Mean COG : 1.0\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 0.0\n" +
                "Arrival Longitude : 0.0\n" +
                "Travelled Distance : 0.0\n" +
                "Delta Distance : 0.0";
        //Act
        String actual = shipstore.getShipSummaryByCallSign(shipgeral.getCallSign());
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void sortedListTDDiffNumMovEqual() {
        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipgeral.getPosDate().addPosition(posgeral2);
        shipgeral2.getPosDate().addPosition(posgeral3);
        shipgeral2.getPosDate().addPosition(posgeral4);
        shipstore.addShip(shipgeral);
        shipstore.addShip(shipgeral2);

        //Act
        String expected = "[Ship{cargo='null', MMSI=111111111, name='name', IMO='IMO1111111', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}, Ship{cargo='null', MMSI=222222222, name='name', IMO='IMO2222222', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}]";
        String actual = shipstore.sortedList().toString();

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void sortedListTDEqualNumMovEqual() {
        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipgeral.getPosDate().addPosition(posgeral2);
        shipgeral2.getPosDate().addPosition(posgeral);
        shipgeral2.getPosDate().addPosition(posgeral2);
        shipstore.addShip(shipgeral);
        shipstore.addShip(shipgeral2);

        //Act
        String expected = "[Ship{cargo='null', MMSI=111111111, name='name', IMO='IMO1111111', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}, Ship{cargo='null', MMSI=222222222, name='name', IMO='IMO2222222', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}]";
        String actual = shipstore.sortedList().toString();

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void sortedListTDDiffNumMovDiff() {
        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipgeral.getPosDate().addPosition(posgeral2);
        shipgeral2.getPosDate().addPosition(posgeral2);
        shipgeral2.getPosDate().addPosition(posgeral3);
        shipgeral2.getPosDate().addPosition(posgeral4);
        shipstore.addShip(shipgeral);
        shipstore.addShip(shipgeral2);

        //Act
        String expected = "[Ship{cargo='null', MMSI=111111111, name='name', IMO='IMO1111111', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}, Ship{cargo='null', MMSI=222222222, name='name', IMO='IMO2222222', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}]";
        String actual = shipstore.sortedList().toString();

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void sortedListTDDiffNumMovDiffMutant() {
        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipgeral.getPosDate().addPosition(posgeral2);
        shipgeral2.getPosDate().addPosition(posgeral2);
        shipgeral2.getPosDate().addPosition(posgeral3);
        shipgeral2.getPosDate().addPosition(posgeral4);
        shipstore.addShip(shipgeral);
        shipstore.addShip(shipgeral2);

        //Act
        String expected = "[Ship{cargo='null', MMSI=111111111, name='name', IMO='IMO1111111', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}, Ship{cargo='null', MMSI=222222222, name='name', IMO='IMO2222222', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}]";
        String actual = shipstore.sortedList().toString();

        //if (!actual.toString().equals("[Ship{cargo='null', MMSI=111111111, name='name', IMO='IMO1111111', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=0.0, draft=1.0}, Ship{cargo='null', MMSI=222222222, name='name', IMO='IMO2222222', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=0.0, draft=1.0}]"))
        //    fail();

        //Assert
        assertEquals(expected, actual);
    }

    /*
    @Test
    void sortedListTDEqualNumMovDiff() {
        //Arrange
        shipgeral.getPosDate().addPosition(posgeral);
        shipgeral.getPosDate().addPosition(posgeral2);
        shipgeral2.getPosDate().addPosition(posgeral);
        shipgeral2.getPosDate().addPosition(posgeral3);
        shipgeral2.getPosDate().addPosition(posgeral4);
        shipstore.addShip(shipgeral);
        shipstore.addShip(shipgeral2);

        //Act
        List<Ship> expected = new ArrayList<>();
        expected.add(shipgeral2);
        expected.add(shipgeral);
        List<Ship> actual = shipstore.sortedList();

        System.out.println(shipgeral.getTravelledDistance());
        System.out.println(shipgeral2.getTravelledDistance());

        //Assert
        assertEquals(expected, actual);
    }
     */

    @Test
    void getShipSummaryByCallSignNull() {

        //Arrange
        shipstore.addShip(shipgeral);
        shipstore.addShip(shipgeral3);
        String expected = "Call Sign : F\n" +
                "Vessel name: A\n" +
                "Start Base date Time: null\n" +
                "End base date time : null\n" +
                "Total movement time: 0 minutes\n" +
                "Total number of movements : 0\n" +
                "Max SOG : 0.0\n" +
                "Mean SOG : 0.0\n" +
                "Max COG : 0.0\n" +
                "Mean COG : 0.0\n" +
                "Departure Latitude : 0.0\n" +
                "Departure Longitude : 0.0\n" +
                "Arrival Latitude : 0.0\n" +
                "Arrival Longitude : 0.0\n" +
                "Travelled Distance : 0.0\n" +
                "Delta Distance : 0.0";
        //Act
        String actual = shipstore.getShipSummaryByCallSign("F");
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void createShipStore() {

        //Arrange + Act
        ShipStore shipStore1 = new ShipStore();

        //Assert
        assertNotNull(shipStore1);
    }

    @Test
    void writeAllShips() {

        //Arrange
        shipstore.addShip(shipgeral);
        boolean expected = true;
        //Act
        boolean actual = shipstore.writeAllShips();
        //Assert
        assertEquals(expected, actual);
    }


    @Test
    void writeAllShipsEmpty() {

        //Arrange
        boolean expected = false;
        //Act
        boolean actual = shipstore.writeAllShips();
        //Assert
        assertEquals(expected, actual);
    }


    @Test
    void getShipByMMSI() {

        //Arrange
        shipstore.addShip(shipgeral);
        String expected = "Ship{cargo='null', MMSI=111111111, name='name', IMO='IMO1111111', numGen=0, genPowerOutput=0, callSign='A', vesselType='A', length=1.0, width=1.0, capacity=1.0, draft=1.0}";
        //Act
        String actual = shipstore.getShipByMmsi(111111111).toString();
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void getMaxCOGMutant() {

        //Arrange
        Position position_cog = new Position(1, 1, 1, 1, -10, LocalDateTime.now());
        shipgeral.insertPosition(position_cog);

        //Act + Arrange
        assertNotNull(shipstore.getMeanCOG(shipgeral));


    }

    @Test
    void getMaxSOGMutant() {

        //Arrange
        Position position_cog = new Position(1, 1, 1, 1, -10, LocalDateTime.now());
        shipgeral.insertPosition(position_cog);

        //Act + Arrange
        assertNotNull(shipstore.getMeanSOG(shipgeral));


    }

    /*
    @Test
    void getMeanSOGDividedByZero() {

        //Arrange
        double expected = 0;
        Ship ship = new Ship(111111111);
        ship.addNewPosMessage(null);
        //Act
        double actual = shipstore.getMeanSOG(ship);
        //Assert
        assertEquals(expected,actual);


    }

    @Test
    void getMeanCOGDividedByZero() {

        //Arrange
        double expected = 0;
        Ship ship = new Ship(111111111);
        ship.addNewPosMessage(null);
        //Act
        double actual = shipstore.getMeanCOG(ship);
        //Assert
        assertEquals(expected,actual);


    }*/

    /*
    @Test
    void sortedList() {
        //Arrange
        List<Ship> shipList = shipstore.getlShip();
        //Act
        List<Ship> expected =

        //Assert
        assertEquals(expected, shipList);
    }
     */


    @Test
    void getTopN() {
        //Arrange
        List<Ship> expectedtestShip = new ArrayList<>();
        expectedtestShip.add(shipgeral);
        shipstore.addShip(shipgeral);
        ShipStore shipStoreTest = new ShipStore();

        //Act + Assert
        List<Ship> actualtestShip1 = shipstore.getTopN(1, "A", date, date2);

        try {
            List<Ship> actualtestShip2 = shipstore.getTopN(10, "A", date, date2);
        } catch (IllegalArgumentException ex) {
            assertEquals("There is not enough ships to do this operation!", ex.getMessage());
        }

        try {
            shipStoreTest.getTopN(1, "A", date, date2);
        } catch (IllegalArgumentException ex) {
            assertEquals("Store is empty!", ex.getMessage());
        }

        assertEquals(expectedtestShip.size(), actualtestShip1.size());


    }

    @Test
    void getMeanSOG() {

        //Arrange
        double expected = 0;
        //Act
        double actual = shipstore.getMeanCOG(null);
        //Assert
        assertEquals(expected, actual);

    }

    @Test
    void getPairOfShipsInsideBST() {
        ShipStore shipStore = new ShipStore();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String dt = "2020-01-01";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;

        try {
            date = sdf.parse(dt);
        } catch (ParseException e) {

        }
        Country co1 = new Country("United Kingdom", "UK".toCharArray(), "UNK".toCharArray(), 25, Continent.EUROPE);

        Port port = new Port("29002", "Liverpool", "Europe", co1, new FacilityLocation(53.46666667, -3.033333333), 0);

        CargoManifest cargoManifest = new CargoManifest("1Ab", port, date);

        Ship ship1 = new Ship(210950000, "VARAMO", "IMO9395044", "C4SQ2", "70", 166, 25, 9.5, "NA", 'B', cargoManifest);
        Ship ship2 = new Ship(228339600, "CMA CGM ALMAVIVA", "IMO9450648", "FLSU", "70", 334, 42, 15, "79", 'B', cargoManifest);
        shipstore.addShip(ship1);
        shipstore.addShip(ship2);
        ship1.insertPosition(ship1.createPosition(LocalDateTime.parse("31-12-2020 23:16", formatter), 0, 0, 0, 0, 1));
        ship1.insertPosition(ship1.createPosition(LocalDateTime.parse("01-01-2021 00:16", formatter), 25, 0, 0, 0, 1));

        ship2.insertPosition(ship2.createPosition(LocalDateTime.parse("02-01-2021 00:16", formatter), 0, 0, 0, 0, 1));
        ship2.insertPosition(ship2.createPosition(LocalDateTime.parse("02-01-2021 01:16", formatter), 15, 0, 0, 0, 1));
        ship2.insertPosition(ship2.createPosition(LocalDateTime.parse("02-01-2021 01:16", formatter), 25.0001, 0, 0, 0, 1));

        Ship ship3 = new Ship(256888000, "CMA CGM MELISANDE", "IMO9473028", "9HA2954", "70", 334, 42, 14.7, "70", 'B', cargoManifest);
        Ship ship4 = new Ship(257881000, "SPAR ARIES", "IMO9701920", "LATO7", "70", 199, 32, 13.3, "NA", 'B', cargoManifest);

        ship3.insertPosition(ship3.createPosition(LocalDateTime.parse("03-01-2021 01:16", formatter), 50, 0, 0, 0, 1));
        ship3.insertPosition(ship3.createPosition(LocalDateTime.parse("04-01-2021 01:16", formatter), 15.645, 0, 0, 0, 1));

        ship4.insertPosition(ship4.createPosition(LocalDateTime.parse("02-01-2021 01:16", formatter), 50, 0, 0, 0, 1));
        ship4.insertPosition(ship4.createPosition(LocalDateTime.parse("02-01-2021 01:26", formatter), 15.6456, 0, 0, 0, 1));

        Ship ship5 = new Ship(256888000, "CMA CGM MELISANDE", "IMO9473028", "9HA2954", "70", 334, 42, 14.7, "70", 'B', cargoManifest);
        Ship ship6 = new Ship(257881000, "SPAR ARIES", "IMO9701920", "LATO7", "70", 199, 32, 13.3, "NA", 'B', cargoManifest);

        ship5.insertPosition(ship5.createPosition(LocalDateTime.parse("03-01-2021 01:16", formatter), 0, 0, 0, 0, 1));
        ship5.insertPosition(ship5.createPosition(LocalDateTime.parse("04-01-2021 01:16", formatter), 15, 0, 0, 0, 1));

        ship6.insertPosition(ship6.createPosition(LocalDateTime.parse("02-01-2021 01:16", formatter), 0, 0, 0, 0, 1));
        ship6.insertPosition(ship6.createPosition(LocalDateTime.parse("02-01-2021 01:26", formatter), 15, 0, 0, 0, 1));


        PairOfShips pairOfShips = new PairOfShips(ship1, ship2);
        PairOfShips pairOfShips1 = new PairOfShips(ship3, ship4);
        PairOfShips[] arrayPairs = {pairOfShips, pairOfShips1};
        shipstore.addShip(ship3);
        shipstore.addShip(ship4);
        shipstore.addShip(ship5);
        shipstore.addShip(ship6);
        shipstore.getPairOfShipsInsideBST();
        int i = 0;
        for (PairOfShips ships : shipstore.getPairsOfShipsSearchTree()) {
            assertTrue(arrayPairs[i].equals(ships));
            i++;
        }
    }

    @Test
    void getPairsOfShipsString() {
        ShipStore shipStore = new ShipStore();

        String dt = "2020-01-01";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;

        try {
            date = sdf.parse(dt);
        } catch (ParseException e) {

        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Country co1 = new Country("United Kingdom", "UK".toCharArray(), "UNK".toCharArray(), 25, Continent.EUROPE);

        Port port = new Port("29002", "Liverpool", "Europe", co1, new FacilityLocation(53.46666667, -3.033333333), 0);

        CargoManifest cargoManifest = new CargoManifest("1Ab", port, date);

        Ship ship1 = new Ship(210950000, "VARAMO", "IMO9395044", "C4SQ2", "70", 166, 25, 9.5, "NA", 'B', cargoManifest);
        Ship ship2 = new Ship(228339600, "CMA CGM ALMAVIVA", "IMO9450648", "FLSU", "70", 334, 42, 15, "79", 'B', cargoManifest);
        ship1.insertPosition(ship1.createPosition(LocalDateTime.parse("31-12-2020 23:16", formatter), 0, 0, 0, 0, 1));
        ship1.insertPosition(ship1.createPosition(LocalDateTime.parse("01-01-2021 00:16", formatter), 25, 0, 0, 0, 1));

        ship2.insertPosition(ship2.createPosition(LocalDateTime.parse("02-01-2021 00:16", formatter), 0, 0, 0, 0, 1));
        ship2.insertPosition(ship2.createPosition(LocalDateTime.parse("02-01-2021 01:16", formatter), 15, 0, 0, 0, 1));
        ship2.insertPosition(ship2.createPosition(LocalDateTime.parse("02-01-2021 01:16", formatter), 25.0001, 0, 0, 0, 1));

        shipstore.addShip(ship1);
        shipstore.addShip(ship2);

        Ship ship3 = new Ship(256888000, "CMA CGM MELISANDE", "IMO9473028", "9HA2954", "70", 334, 42, 14.7, "70", 'B', cargoManifest);
        Ship ship4 = new Ship(257881000, "SPAR ARIES", "IMO9701920", "LATO7", "70", 199, 32, 13.3, "NA", 'B', cargoManifest);

        ship3.insertPosition(ship3.createPosition(LocalDateTime.parse("03-01-2021 01:16", formatter), 50, 0, 0, 0, 1));
        ship3.insertPosition(ship3.createPosition(LocalDateTime.parse("04-01-2021 01:16", formatter), 15.645, 0, 0, 0, 1));

        ship4.insertPosition(ship4.createPosition(LocalDateTime.parse("02-01-2021 01:16", formatter), 50, 0, 0, 0, 1));
        ship4.insertPosition(ship4.createPosition(LocalDateTime.parse("02-01-2021 01:26", formatter), 15.6456, 0, 0, 0, 1));

        shipstore.calculateTravelledDistanceOfAllShips();

        String expected = "|   Ship 1 MMSI   \t | \t     Ship 2 MMSI  \t  | \t   DistOrig     \t  |    \t  DistDest     \t  |      \t  Movs  \t       |         \t   TravelDist    \t        |  \t       Movs  \t       |           \t TravelDist           \t |\n" +
                "     228339600\t\t\t     210950000         \t\t\t 2779890.79     \t\t\t\t2779890.79          \t\t\t2                     \t\t2779890.79            \t\t2                    \t\t2779879.67\n";
        assertEquals(expected, shipstore.getPairsOfShipsString());

    }

    @Test
    void createNullShipFalse() {

        Ship ship = shipstore.createShip(210950000, "VARAMO", "IMO9395044", "C4SQ2", "70", 166, 25, 9.5, "NA", 'B');

        if (ship.equals(null) || ship == null) fail();

    }

    @Test
    void insertShipIntoBST() {

        Ship ship = shipstore.createShip(210950000, "VARAMO", "IMO9395044", "C4SQ2", "70", 166, 25, 9.5, "NA", 'B');

        boolean actual = shipstore.addShip(ship);

        if (!actual) fail();

    }

    @Test
    void getMaxSog() {

        //Arrange
        Position position_cog = new Position(1, 1, 1, -10, 0, LocalDateTime.now());
        shipgeral.insertPosition(position_cog);

        //Act + Arrange

        assertNotEquals(0.0, shipstore.getMeanSOG(shipgeral));


    }

    @Test
    void getShipInvalid() {

        String expected1 = null;
        String expected2 = null;
        String expected3 = null;

        try {

            expected1 = shipstore.getShipSummaryByCallSign("zzzz");
            expected2 = shipstore.getShipSummaryByIMO("IMO9990111");
            expected3 = shipstore.getShipSummaryByMMSI(999999345);

        } catch (NullPointerException e) {

            assertEquals(expected1, null);
            assertEquals(expected2, null);
            assertEquals(expected3, null);

        }

    }

    @Test
    void calculateTravelledDistance() {

        shipgeral.insertPosition(posgeral);
        shipgeral.insertPosition(posgeral2);

        shipstore.addShip(shipgeral);
        shipstore.calculateTravelledDistanceOfAllShips();

        for (Ship s : shipstore.transformAVLintoListMMSI()) {
            if (s.getSmallestPosition() == null) fail();
            else if (s.getBiggestPosition() == null) fail();
            else if (s.getPosDateSize() == 0) fail();
            else if (s.getShipsTravelledDistance() == 0) fail();
        }

        for (Ship s : shipstore.transformAVLintoListIMO()) {
            if (s.getSmallestPosition() == null) fail();
            else if (s.getBiggestPosition() == null) fail();
            else if (s.getPosDateSize() == 0) fail();
            else if (s.getShipsTravelledDistance() == 0) fail();
        }

        for (Ship s : shipstore.transformAVLintoListCallSign()) {
            if (s.getSmallestPosition() == null) fail();
            else if (s.getBiggestPosition() == null) fail();
            else if (s.getPosDateSize() == 0) fail();
            else if (s.getShipsTravelledDistance() == 0) fail();
        }

    }

}