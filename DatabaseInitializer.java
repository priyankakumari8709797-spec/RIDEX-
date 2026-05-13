import java.sql.*;

public class DatabaseInitializer {
    public static void initialize() {
        // This creates a new database file if one doesn't exist
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // Create Users Table for Login
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "full_name TEXT, " +
                    "phone_number TEXT)");

            // Create Bookings Table with all necessary columns
            // MUST include age and gender here
            stmt.execute("CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "seat_number TEXT UNIQUE, " +
                    "full_name TEXT, " +
                    "phone_number TEXT, " +
                    "age TEXT, " +
                    "gender TEXT, " +
                    "status TEXT DEFAULT 'Available')");

            // Pre-fill seats 1 to 20
            for (int i = 1; i <= 20; i++) {
                stmt.execute("INSERT OR IGNORE INTO bookings (seat_number, status) VALUES ('" + i + "', 'Available')");
            }

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database Initialization Error: " + e.getMessage());
        }
    }
}