package lapr.project.controller;

import lapr.project.model.PhysicsCalculation;

public class AuxiliaryPowerNeededForVoyageController {

    /**
     * Constructor.
     */
    public AuxiliaryPowerNeededForVoyageController() {
        // Empty constructor
    }

    /**
     * Gets the result values of the auxiliary power equipment needed for voyage for containers of -5ºC.
     *
     * @param numberOfContainers the number of containers
     * @param temperature        the container temperature
     * @param journeyTime        the journey time
     * @return a string with all the result values
     */
    public String calculateSupplyNeededForMinus5(int numberOfContainers, double temperature, int journeyTime) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Energy Needed: " + PhysicsCalculation.calculateTotalEnergySuppliedMinus5(numberOfContainers, temperature, journeyTime));
        stringBuilder.append("\nThe amount of auxiliary power equipment needed for the trip is " + PhysicsCalculation.calculateSuppliesNeededForMinus5(numberOfContainers, temperature, journeyTime) + " generator(s).");

        return "The amount of auxiliary power equipment needed for the trip is " +
                PhysicsCalculation.calculateSuppliesNeededForMinus5(numberOfContainers, temperature, journeyTime) +
                " generator(s).";
    }

    /**
     * Gets the result values of the auxiliary power equipment needed for voyage for containers of 7ºC.
     *
     * @param numberOfContainers the number of containers
     * @param temperature        the container temperature
     * @param journeyTime        the journey time
     * @return a string with all the result values
     */
    public String calculateSupplyNeededFor7(int numberOfContainers, double temperature, int journeyTime) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Energy Needed: " + PhysicsCalculation.calculateTotalEnergySupplied7(numberOfContainers, temperature, journeyTime));
        stringBuilder.append("\nThe amount of auxiliary power equipment needed for the trip is " +
                PhysicsCalculation.calculateSuppliesNeededFor7(numberOfContainers, temperature, journeyTime) +
                " generator(s).");
        return stringBuilder.toString();
    }
}
