package lapr.project.ui;

import lapr.project.controller.LoadedShipsController;

public class LoadedShipsUI implements Runnable {

    LoadedShipsController ctrl = new LoadedShipsController();

    public void run() {
        int op;

        do {
            try {
                op = Utils.readIntegerFromConsole("Please enter the ship's MMSI:");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid MMSI!");
                op = 0;
            }
        } while (op == 0);

        boolean bool = ctrl.loadedShips(op);

        if (bool) {
            System.out.println("Operation was a success!");
        } else {
            System.out.println("Operation failed! Please, try again.");
        }
    }
}
