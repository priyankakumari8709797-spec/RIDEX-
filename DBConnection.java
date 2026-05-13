import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        try {
            // Loading the driver manually
            Class.forName("org.sqlite.JDBC");

            // Connection string
            String url = "jdbc:sqlite:ridex.db";
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.out.println("DRIVER ERROR: SQLite JAR file not linked correctly!");
            return null;
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
            return null;
        }
    }
}