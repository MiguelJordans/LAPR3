package lapr.project.ui;

import lapr.project.controller.AuthController;
import lapr.project.shared.Constants;
import lapr.project.utils.auth.mappers.dto.UserRoleDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AuthUI implements Runnable {

    private final AuthController ctrl;

    public AuthUI() {
        ctrl = new AuthController();
    }

    public void run() {
        boolean success = doLogin();

        if (success) {

            List<UserRoleDTO> roles = this.ctrl.getUserRoles();
            if ((roles == null) || (roles.isEmpty())) {
                System.out.println("User has not any role assigned.");
            } else {
                UserRoleDTO role = selectsRole(roles);
                if (!Objects.isNull(role) ) {
                    List<MenuItem> rolesUI = getMenuItemForRoles();
                    this.redirectToRoleUI(rolesUI, role);
                } else {
                    System.out.println("User did not select a role.");
                }
            }
        }
        this.logout();
    }

    private boolean doLogin() {
        System.out.println("\nLogin UI:");

        int maxAttempts = 3;
        boolean success = false;
        do {
            maxAttempts--;
            String id = Utils.readLineFromConsole("Enter UserId/Email: ");
            String pwd = Utils.readLineFromConsole("Enter Password: ");
            success = ctrl.doLogin(id, pwd);
            if (!success) {
                System.out.println("Invalid UserId and/or Password. \n You have  " + maxAttempts + " more attempt(s).");
            }

        } while (!success && maxAttempts > 0);
        return success;
    }

    private List<MenuItem> getMenuItemForRoles() {
        List<MenuItem> rolesUI = new ArrayList<>();

        rolesUI.add(new MenuItem(Constants.ROLE_CLIENT, new ClientUI()));
        rolesUI.add(new MenuItem(Constants.ROLE_TRAFFIC_MANAGER, new TrafficManagerUI()));
        rolesUI.add(new MenuItem(Constants.ROLE_SHIP_CAPTAIN, new ShipCaptainUI()));
        rolesUI.add(new MenuItem(Constants.ROLE_PORT_MANAGER, new PortManagerUI()));
        rolesUI.add(new MenuItem(Constants.ROLE_PORT_STAFF, new PortStaffUI()));
        rolesUI.add(new MenuItem(Constants.ROLE_CHIEF_ELECTRICAL_ENGINEER,new ShipChiefElectricalEngineerUI()));
        rolesUI.add(new MenuItem(Constants.ROLE_FLEET_MANAGER, new FleetManagerUI()));

        return rolesUI;
    }

    private void redirectToRoleUI(List<MenuItem> rolesUI, UserRoleDTO role) {
        boolean found = false;
        Iterator<MenuItem> it = rolesUI.iterator();
        while (it.hasNext() && !found) {
            MenuItem item = it.next();
            found = item.hasDescription(role.getDescription());

            if (found)
                item.run();
        }
        if (!found)
            System.out.println("There is no UI for users with role '" + role.getDescription() + "'");
    }

    private void logout() {
        ctrl.doLogout();
    }

    private UserRoleDTO selectsRole(List<UserRoleDTO> roles) {
        if (roles.size() == 1)
            return roles.get(0);
        else
            return (UserRoleDTO) Utils.showAndSelectOne(roles, "Select the role you want to adopt in this session:");
    }
}