import java.sql.*;

/**
 * Bookingmanager: Handles all database transactions for RideX.
 * Fixed to handle SQL exceptions properly and prevent connection leaks.
 */
public class Bookingmanager {

    // 1. Fetches seat status for the UI grid
    public static ResultSet getSeats(String source, String dest) throws Exception {
        // Note: We don't use try-with-resources here because the UI needs the ResultSet
        // alive
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT status FROM bookings WHERE source = ? AND destination = ? ORDER BY seat_id ASC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, source);
        pstmt.setString(2, dest);
        return pstmt.executeQuery();
    }

    // 2. Counts how many seats are taken for the footer status
    public static int getTotalBooked(String source, String dest) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE source = ? AND destination = ? AND status = 'Booked'";

        // try-with-resources: This automatically closes the connection to prevent
        // memory leaks
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, source);
            pstmt.setString(2, dest);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("Error calculating occupancy: " + e.getMessage());
        }
        return 0;
    }

    // 3. Updates a seat back to 'Available'
    public static boolean cancelSeat(int seatId, String source, String dest) {
        String sql = "UPDATE bookings SET status = 'Available', name = NULL, age = NULL, gender = NULL " +
                "WHERE seat_id = ? AND source = ? AND destination = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, seatId);
            pstmt.setString(2, source);
            pstmt.setString(3, dest);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Books a seat with passenger details
    public static boolean bookSeat(int seatId, String source, String dest, String name, int age, String gender) {
        String sql = "UPDATE bookings SET status = 'Booked', name = ?, age = ?, gender = ? " +
                "WHERE seat_id = ? AND source = ? AND destination = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.setInt(4, seatId);
            pstmt.setString(5, source);
            pstmt.setString(6, dest);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}