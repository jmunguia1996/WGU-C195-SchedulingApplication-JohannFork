package devTools;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class JDBTools {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String MYSQLJDBToolsDriver = "com.mysql.cj.jdbc.Driver"; //referencing the driver
    private static final String jdbToolsURL = protocol + vendorName + ipAddress + databaseName + "?connectionTimeZone=SERVER";

    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!"; // Password
    public static Connection connection = null;

    /**
     * Initiates the Data Base connection.
     * @return connection
     */
    public static Connection openConnection() {
        try {
            Class.forName(MYSQLJDBToolsDriver);
            connection = DriverManager.getConnection(jdbToolsURL, username, password);
            DevToolC.println("Connection successful.");
        } catch (Exception e) {
            DevToolC.println("Connection to Data Base failed.");
        }
        return connection;
        //TODO [] how does this open and close connections as needed?
    }

    /**
     * Method to return the current connection.
     * @return connection.
     */
    public static Connection getConnection() {

        return connection;
    }

    /**
     * Method to close connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
            DevToolC.println("Connection Closed");
        } catch (SQLException exception) {
            DevToolC.println("Error: " + exception.getMessage());
            //TODO [] check the difference between these two option ^v
            exception.printStackTrace();
        }
    }

    //for preparing statements
    private static PreparedStatement preparedStatement;

    public static void prepareStatement(Connection conn, String sqlStatement) throws SQLException {
        preparedStatement = conn.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPreparedStatement() {

        return preparedStatement;
    }

}
