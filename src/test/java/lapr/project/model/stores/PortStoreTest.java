package lapr.project.model.stores;

import lapr.project.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class PortStoreTest {

    @Test
    void addPortTest() {
        Country co1 = new Country("United Kingdom", "UK".toCharArray(), "UNK".toCharArray(), 25, Continent.EUROPE);

        PortStore portStore = new PortStore();
        Assertions.assertTrue(portStore.add(new Port("1sda", "Leixões", "Europe", co1, new FacilityLocation(66, 87), 0)));
    }

    @Test
    void addPortTestException() {
        PortStore portStore = new PortStore();
        Assertions.assertFalse(portStore.add(null));
    }

    @Test
    void fillTreeEmpty() {
        PortStore portStore = new PortStore();
        Assertions.assertFalse(portStore.fillTree());
    }

    @Test
    void fillTreeNotEmpty() {
        Country co1 = new Country("United Kingdom", "UK".toCharArray(), "UNK".toCharArray(), 25, Continent.EUROPE);

        PortStore portStore = new PortStore();
        portStore.add(new Port("1sda", "Leixões", "Europe", co1, new FacilityLocation(66, 87), 0));
        Assertions.assertTrue(portStore.fillTree());
    }

    @Test
    void fillTreeGetTree() {
        PortStore portStore = new PortStore();
        Country co1 = new Country("United Kingdom", "UK".toCharArray(), "UNK".toCharArray(), 25, Continent.EUROPE);

        portStore.add(new Port("1sda", "Leixões", "Europe", co1, new FacilityLocation(66, 87), 0));
        portStore.fillTree();
        Assertions.assertNotNull(portStore.getPortList());
    }

    @Test
    void getNearestNeighbour() {

        String sdate = "31/11/2020 23:16";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime date = LocalDateTime.from(formatter.parse(sdate));

        Position posgeral = new Position(1, 0, 0, 1, 1, date);

        Ship shipgeral = new Ship(111111111, "name", "IMO1111111", 1, 1, "A", "A", 1, 1, 1, 1);


        shipgeral.getPosDate().addPosition(posgeral);

        LocalDateTime localDateTime = posgeral.getDate();
        Country co1 = new Country("United Kingdom", "UK".toCharArray(), "UNK".toCharArray(), 25, Continent.EUROPE);

        PortStore portStore = new PortStore();
        portStore.add(new Port("1sda", "Leixões", "Europe", co1, new FacilityLocation(66, 87), 0));
        portStore.fillTree();


        Port nearest = portStore.getNearestNeighbourByTime(shipgeral, localDateTime);

        if (nearest == null) Assertions.fail();

        Assertions.assertNotNull(nearest);


    }

}