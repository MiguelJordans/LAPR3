package lapr.project.controller;

import lapr.project.model.*;
import lapr.project.shared.Constants;
import lapr.project.utils.auth.AuthFacade;
import lapr.project.utils.auth.UserSession;
import lapr.project.utils.auth.domain.OrgRole;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

public class App {

    private static App singleton = null;
    private final Company company;
    private final AuthFacade authFacade;


    public App() {


        Properties props = getProperties();
        this.company = new Company(props.getProperty(Constants.PARAMS_COMPANY_DESIGNATION));
        this.authFacade = this.company.getAuthFacade();

        bootstrap();
    }


    public Company getCompany() {
        return this.company;
    }


    public Properties getProperties() {
        Properties props = new Properties();

        // Add default properties and values.
        props.setProperty(Constants.PARAMS_COMPANY_DESIGNATION, "CargoShipping");


        // Read configured values.
        try {
            InputStream in = new FileInputStream(Constants.PARAMS_FILENAME);
            props.load(in);
            in.close();
        } catch (IOException ex) {

        }
        return props;
    }

    public static App getInstance() {
        if (singleton == null) {
            synchronized (App.class) {
                singleton = new App();
            }
        }
        return singleton;
    }

    public UserSession getCurrentUserSession() {
        return this.authFacade.getCurrentUserSession();
    }


    public boolean doLogin(String email, String pwd) {
        return this.authFacade.doLogin(email, pwd).isLoggedIn();
    }

    public void doLogout() {
        this.authFacade.doLogout();
    }

    public boolean bootstrap() {

        this.authFacade.addUserRole(Constants.ROLE_CLIENT, Constants.ROLE_CLIENT);
        this.authFacade.addUserRole(Constants.ROLE_TRAFFIC_MANAGER, Constants.ROLE_TRAFFIC_MANAGER);
        this.authFacade.addUserRole(Constants.ROLE_SHIP_CAPTAIN, Constants.ROLE_SHIP_CAPTAIN);

        this.company.getOrgRoleStore().addOrgRole(new OrgRole(Constants.CLIENT, Constants.MODEL_CLASS_PATH + "" + Constants.CLIENT));
        this.company.getOrgRoleStore().addOrgRole(new OrgRole(Constants.TRAFFIC_MANAGER, Constants.MODEL_CLASS_PATH + "" + Constants.TRAFFIC_MANAGER));
        this.company.getOrgRoleStore().addOrgRole(new OrgRole(Constants.SHIP_CAPTAIN, Constants.MODEL_CLASS_PATH + "" + Constants.SHIP_CAPTAIN));

        //email: R00001@lei.pt pass: 123
        Client c1 = new Client(this.company.getOrgRoleStore().getRoleById(Constants.CLIENT), "R00001", "Receptionist1");
        this.authFacade.addUserWithRole(c1.getName(), c1.getEmail(), "123", Constants.ROLE_CLIENT);

        //email: TM00001@lei.pt pass: 495
        TrafficManager tm1 = new TrafficManager(this.company.getOrgRoleStore().getRoleById(Constants.TRAFFIC_MANAGER), "TM00001", "Traffic Manager");
        this.authFacade.addUserWithRole(tm1.getName(), tm1.getEmail(), "495", Constants.ROLE_TRAFFIC_MANAGER);


        //email: SC00001@lei.pt pass: 123
        ShipCaptain sc1 = new ShipCaptain(this.company.getOrgRoleStore().getRoleById(Constants.SHIP_CAPTAIN), "SC00001", "Ship Captain");
        this.authFacade.addUserWithRole(sc1.getName(), sc1.getEmail(), "123", Constants.ROLE_SHIP_CAPTAIN);


/*

        //ContainerPosition ContainerPosition
        ContainerPosition containerPosition1 = new ContainerPosition(1,1,1);
        ContainerPosition containerPosition2 = new ContainerPosition(3,3,3);
        //Container
        Container container1 = new Container("11",1,1,1,"11",containerPosition1, 1,1);
        company.getContainerStore().addContainer(container1);

        Container container2 = new Container("22",1,1,1,"22",containerPosition2, 1,1);
        company.getContainerStore().addContainer(container2);

        //Facility Location Location
        FacilityLocation facilityLocation1 = new FacilityLocation(2,2);
        FacilityLocation facilityLocation2 = new FacilityLocation(4,4);
        //Port
        Port port1 = new Port("Ilha Das Cores2","Europa", "11","ola",facilityLocation1);
        company.getPortStore().add(port1);
        Port port2 = new Port("Ilha Das Cores","Asia", "11","ola",facilityLocation2);
        company.getPortStore().add(port2);

        //CargoManifest
        CargoManifest cargoManifest1 = new CargoManifest("11",port1);
        company.getCargoManifestStore().add(cargoManifest1);

        CargoManifest cargoManifest2 = new CargoManifest("69",port2);
        company.getCargoManifestStore().add(cargoManifest2);



        //Ship Ship
        Ship ship = new Ship(222222222, "name", "IMO1111111", 1, 1, "A", "A", 1, 1, 1, 1);
        ship.getCargoManifestAVL().insert(cargoManifest1);
        ship.getCargoManifestAVL().insert(cargoManifest2);
        company.getShipStore().addShip(ship);


        //position position
        String sdate = "31/11/2020 23:16";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime date = LocalDateTime.from(formatter.parse(sdate));
        Position posgeral = new Position(1, 0, 0, 1, 1, date);

        ship.insertPosition(posgeral);




        ship.addLoadedContainer(container1,port1);
        ship.addOffLoadedContainer(container2,port2);
*/
        return true;
    }

    public AuthFacade getAuthFacade() {
        return authFacade;
    }
}
