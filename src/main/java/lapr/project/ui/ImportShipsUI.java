package lapr.project.ui;

import lapr.project.controller.ImportShipsController;
import lapr.project.shared.exceptions.InvalidLineException;

import java.io.FileNotFoundException;

public class ImportShipsUI implements Runnable {

    public void run() {

        boolean readOptionBoolean = false;
        String fileName;
        ImportShipsController controller = new ImportShipsController();

        do {
            try {
                String readOption = Utils.readLineFromConsole("Do you wish to import a file (Y - yes/ N - no)");
                readOption = readOption.toUpperCase();
                if (readOption.equals("Y")) {
                    readOptionBoolean = true;
                    fileName = Utils.readLineFromConsole("Please enter the file path");
                    System.out.println("This operation may take a while!");
                    controller.importShips(fileName);
                } else if (readOption.equals("N")) {
                    readOptionBoolean = true;
                } else if (!readOption.equals("Y") || !readOption.equals("N")) throw new IllegalArgumentException();

            } catch (IllegalArgumentException ex1) {
                System.out.println("Please enter a valid option!");
            } catch (InvalidLineException | FileNotFoundException ex2) {
                System.out.println("Please enter a valid file! (See the file names before entering one)");
            }
        } while (readOptionBoolean == false);
    }
}
