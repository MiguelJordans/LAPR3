package lapr.project.ui;

import lapr.project.controller.OccupancyRateGivenCargoManifestIDController;
import lapr.project.shared.exceptions.*;

public class OccupancyRateGivenCargoManifestIDUI implements Runnable {

    private final OccupancyRateGivenCargoManifestIDController occupancyRateGivenCargoManifestIDController;

    public OccupancyRateGivenCargoManifestIDUI(){
        this.occupancyRateGivenCargoManifestIDController = new OccupancyRateGivenCargoManifestIDController();
    }

    @Override
    public void run() {

        String cargoManifestID = null;
        int shipMmsi = 0;

        do {
            try {
                shipMmsi = Utils.readIntegerFromConsole("Please enter the desired MMSI: ");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid MMSI!");
                shipMmsi = 0;
            }
        } while (shipMmsi == 0);

        do {
            try {
                cargoManifestID = Utils.readLineFromConsole("Please enter the desired cargo manifest ID: ");
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid CargoManifest!");
                cargoManifestID = null;
            }
        } while (cargoManifestID == null);

        try {
            occupancyRateGivenCargoManifestIDController.getOccupancyRate(shipMmsi,cargoManifestID);
            System.out.printf("For the given information, the occupancy rate its : %.2f%%\n",occupancyRateGivenCargoManifestIDController.getOccupancyRate(shipMmsi,cargoManifestID));
        } catch (ShipCargoCapacityException | ContainerGrossException | ContainersInsideCargoManifestListSizeException | CargoManifestDoesntBelongToThatShipException | VehicleIDNotValidException | IllegalArgumentException ex1) {
            System.out.println(ex1.getMessage());
        }

    }
}

