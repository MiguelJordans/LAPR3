package lapr.project.data.DataBaseScripts;

import lapr.project.data.DatabaseConnection;
import lapr.project.shared.Constants;
import lapr.project.shared.exceptions.MaterialTypeNullException;
import lapr.project.shared.exceptions.NoMaterialsForThatTemperatureException;
import lapr.project.shared.exceptions.NoMaterialsFoundException;
import lapr.project.shared.exceptions.ProportionalityConstantNullException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class GetMaterialsScript {

    private final DatabaseConnection databaseConnection;
    private static final String TEMP = "ºC :\n";

    public GetMaterialsScript(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }


    public String getThermalResistance(int containerID) throws ProportionalityConstantNullException {

        StringBuilder stringBuilder = new StringBuilder();
        String[] walls = {"Outer Wall", "Inner Wall", "Intermediate Material"};
        DecimalFormat df = new DecimalFormat("#.#######");

        for (String wall : walls) {
            stringBuilder.append(wall).append(":\n");
            stringBuilder.append(df.format(Constants.GROSS / (getKConstant(containerID, wall) * Constants.AREA))).append(" W/(m·K)\n");
        }

        return stringBuilder.toString();
    }

    public String materialScript(int temperature) throws MaterialTypeNullException, NoMaterialsFoundException, NoMaterialsForThatTemperatureException {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Outer Walls for ").append(temperature).append(TEMP);

        int k = countMaterialsByTemperature(temperature);
        int count2 = 0;

        if (k == 0) {
            throw new NoMaterialsForThatTemperatureException();
        }

        if (temperature == 7) {
            while (k != 0) {

                if (count2 == 3)
                    stringBuilder.append("\nIntermediate Material for ").append(temperature).append(TEMP);
                else if (count2 == 7)
                    stringBuilder.append("\nInner Walls Material for ").append(temperature).append(TEMP);
                stringBuilder.append(getMaterialByTemperature(temperature, count2));
                stringBuilder.append("\n");
                count2++;
                k--;

            }
        } else {
            while (k != 0) {

                if (count2 == 1)
                    stringBuilder.append("\nIntermediate Material for ").append(temperature).append(TEMP);
                else if (count2 == 2)
                    stringBuilder.append("\nInner Walls Material for ").append(temperature).append(TEMP);
                stringBuilder.append(getMaterialByTemperature(temperature, count2));
                stringBuilder.append("\n");
                count2++;
                k--;
            }
        }


        return stringBuilder.toString();
    }

    public double getKConstant(int containerId, String wallType) throws ProportionalityConstantNullException {

        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "SELECT m.PROPORTIONALITYCONSTANT from MATERIAL m\n" +
                "where m.ID = (Select  cm.MATERIALID from CONTAINERMATERIAL cm\n" +
                "where cm.REFRIGERATEDCONTAINERID = " + containerId + "\n" +
                "and cm.WALLTYPE = '" + wallType + "')";

        try (PreparedStatement getPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet resultSet = getPreparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getDouble("PROPORTIONALITYCONSTANT");
                } else {
                    return 0;
                }
            }
        } catch (SQLException exception) {
            throw new ProportionalityConstantNullException();
        }
    }

    public String getMaterialByTemperature(int temperature, int j) throws MaterialTypeNullException {

        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "SELECT mt.TYPE from MATERIALTYPE  mt\n" +
                "inner join MATERIAL m\n" +
                "on mt.ID = m.ID\n" +
                "where m.TEMPERATURE =" + temperature;

        try (PreparedStatement getPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet resultSet = getPreparedStatement.executeQuery()) {


                for (int i = 0; i < j; i++) {
                    resultSet.next();
                }

                if (resultSet.next()) {

                    return resultSet.getString("TYPE");
                }

            }
        } catch (SQLException exception) {
            throw new MaterialTypeNullException();
        }
        return null;
    }

    public int countMaterialsByTemperature(int temperature) throws NoMaterialsFoundException {

        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "SELECT count(mt.TYPE) COUNTMATERIALS from MATERIALTYPE  mt\n" +
                "inner join MATERIAL m\n" +
                "on mt.ID = m.ID\n" +
                "where m.TEMPERATURE = " + temperature;

        try (PreparedStatement getPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet resultSet = getPreparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("COUNTMATERIALS");
                } else {
                    return 0;
                }
            }
        } catch (SQLException exception) {
            throw new NoMaterialsFoundException();
        }

    }
}