package lapr.project.model.stores;

import lapr.project.model.BinarySearchTree;
import lapr.project.model.Position;
import lapr.project.model.Ship;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipStoreTest {

    Ship shipgeral = new Ship(111111111, "name", "IMO1111111", 1, 1, "A", "A", 1, 1, 1, 1);
    Ship shipgeral2 = new Ship(222222222, "name", "IMO2222222", 1, 1, "A", "A", 1, 1, 1, 1);

    ShipStore shipstore = new ShipStore();

    String sdate = "31-12-2020 23:16";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    LocalDateTime date = LocalDateTime.parse(sdate, formatter);

    String sdate2 = "31-12-2020 23:50";
    LocalDateTime date2 = LocalDateTime.parse(sdate2, formatter);

    String sdate3 = "31-12-2020 23:55";
    LocalDateTime date3 = LocalDateTime.parse(sdate3, formatter);

    String sdate4 = "31-12-2020 23:58";
    LocalDateTime date4 = LocalDateTime.parse(sdate4, formatter);

    Position posgeral = new Position(0, 0, 0, 0, 1, date);
    Position posgeral2 = new Position(10, 20, 30, 20, 10, date2);
    Position posgeral3 = new Position(5, 10, 15, 10, 5, date3);
    Position posgeral4 = new Position(10, 20, 30, 20, 10, date4);

    @Test
    void existsShip() {
        shipstore.addShip(shipgeral);
        assertNotNull(shipstore.findShip(shipgeral.getMmsi()));

        Ship ship1 = new Ship(123456789, "ya", "IMO1111211", "L", "123", 15.6, 45, 54, "NA", 'b');
        assertFalse((shipstore.existsShip(123456789)));
    }

    @Test
    void findShip() {
        shipstore.addShip(shipgeral);
        assertTrue(shipstore.existsShip((shipgeral.getMmsi())));
    }

    @Test
    void getlShipTest() {

        //Arrange
        List expected = shipstore.getlShip();
        //Act
        //Assert
        assertEquals(expected, shipstore.getlShip());
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
        String expected = null;
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
        shipstore.addShip(shipgeral);
        String expected = null;
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
        List<Ship> expected = new ArrayList<>();
        expected.add(shipgeral);
        expected.add(shipgeral2);
        List<Ship> actual = shipstore.sortedList();

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
        List<Ship> expected = new ArrayList<>();
        expected.add(shipgeral);
        expected.add(shipgeral2);
        List<Ship> actual = shipstore.sortedList();

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
        List<Ship> expected = new ArrayList<>();
        expected.add(shipgeral);
        expected.add(shipgeral2);
        List<Ship> actual = shipstore.sortedList();

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
        String expected = null;
        //Act
        String actual = shipstore.getShipSummaryByCallSign("F");
        //Assert
        assertEquals(expected, actual);

    }


    @Test
    void getBinarySearchTree() {

        //Arrange + Act
        shipstore.addShip(shipgeral);
        BinarySearchTree<Ship> binarySearchTree = shipstore.getShipBinarySearchTree();
        //Assert
        assertNotNull(binarySearchTree);
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
        Ship expected = shipgeral;
        //Act
        Ship actual = shipstore.getShipByMMSI(111111111);
        //Assert
        assertEquals(expected, actual);
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


    /*
    @Test
    void getTopN(){
        //Arrange
        List<Ship> expectedtestShip = new ArrayList<>();
        expectedtestShip.add(shipgeral);
        shipstore.getShipBinarySearchTree().insert(shipgeral);
        ShipStore shipStoreTest = new ShipStore();
        //Act
        List<Ship> actualtestShip1 = shipstore.getTopN(1,"A",date,date2);
        List<Ship> actualtestShip2 = shipstore.getTopN(10,"A",date,date2);


        //Assert
        assertEquals(null,shipStoreTest.getTopN(1,"A",date,date2));
        assertEquals(expectedtestShip.size(),actualtestShip1.size());
        assertEquals(expectedtestShip.indexOf(shipgeral),actualtestShip1.indexOf(shipgeral));
        assertEquals(null,actualtestShip2);

    }

}*/

    @Test
    void getMeanSOG() {

        //Arrange
        double expected = 0;
        //Act
        double actual = shipstore.getMeanCOG(null);
        //Assert
        assertEquals(expected, actual);

    }

}