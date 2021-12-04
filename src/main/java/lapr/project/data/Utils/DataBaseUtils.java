package lapr.project.data.Utils;

import lapr.project.data.CargoManifestStoreData;
import lapr.project.data.DatabaseConnection;
import lapr.project.model.FacilityLocation;
import lapr.project.model.Port;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseUtils {

    public static Port getPort(String facilityID, DatabaseConnection databaseConnection) {

        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "SELECT * FROM FACILITY WHERE FACILITYID = '" + facilityID +"'";

        try (PreparedStatement getPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet resultSet = getPreparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    String continentID = getContinentID(resultSet.getString("COUNTRYID"),databaseConnection);

                    String continent = getContinent(continentID,databaseConnection);

                    String identification = resultSet.getString("FACILITYID");
                    String name = resultSet.getString("NAME");
                    String country = resultSet.getString("COUNTRYID");

                    double longitude = resultSet.getDouble("LONGITUDE");
                    double latitude = resultSet.getDouble("LATITUDE");

                    if(latitude<-90) latitude +=90;
                    if(longitude<-180) longitude +=180;

                    return new Port(identification,name ,continent,country,new FacilityLocation(longitude,latitude));

                } else return null;


            }
        } catch (SQLException e) {
            Logger.getLogger(CargoManifestStoreData.class.getName()).log(Level.SEVERE, null, e);
            databaseConnection.registerError(e);
            return null;
        }

    }


    public static String getContinentID(String countryID,DatabaseConnection databaseConnection) {

        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "SELECT * FROM COUNTRY WHERE COUNTRYID = '" + countryID + "'";


        try (PreparedStatement getPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet resultSet = getPreparedStatement.executeQuery()) {


                if (resultSet.next()) {

                    return resultSet.getString("CONTINENTID");

                } else return null;


            }
        } catch (SQLException e) {
            Logger.getLogger(CargoManifestStoreData.class.getName()).log(Level.SEVERE, null, e);
            databaseConnection.registerError(e);
            return null;
        }

    }

    public static String getContinent(String continentID,DatabaseConnection databaseConnection){

        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "SELECT * FROM CONTINENT WHERE CONTINENTID = '" + continentID + "'";

        try (PreparedStatement getPreparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet resultSet = getPreparedStatement.executeQuery()) {


                if (resultSet.next()) {

                    return resultSet.getString("NAME");

                } else return null;


            }
        } catch (SQLException e) {
            Logger.getLogger(CargoManifestStoreData.class.getName()).log(Level.SEVERE, null, e);
            databaseConnection.registerError(e);
            return null;
        }

    }


}
