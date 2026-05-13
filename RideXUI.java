import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RideXUI extends JFrame {
    private String loggedInPhone;
    private String selectedSeat = "";
    private JPanel seatPanel;
    private JLabel statusLabel;
    private JComboBox<String> routeBox;

    public RideXUI(String loginName, String loginPhone) {
        this.loggedInPhone = loginPhone;
        setTitle("RIDEX Dashboard | User: " + loginName);
        setSize(850, 750); // Increased height for the extra button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadExistingBookings();
    }

    private void initComponents() {
        // --- TOP PANEL ---
        JPanel top = new JPanel(new FlowLayout());
        top.setBackground(new Color(44, 62, 80));
        routeBox = new JComboBox<>(new String[]{"Delhi - Mumbai", "Pune - Bangalore", "Chennai - Hyderabad"});
        top.add(new JLabel("<html><font color='white'>Select Route: </font></html>"));
        top.add(routeBox);
        add(top, BorderLayout.NORTH);

        // --- CENTER PANEL (SEATS) ---
        seatPanel = new JPanel(new GridLayout(5, 4, 15, 15));
        seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        for (int i = 1; i <= 20; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setBackground(new Color(236, 240, 241));
            btn.setActionCommand(String.valueOf(i));
            btn.addActionListener(e -> {
                selectedSeat = btn.getActionCommand();
                if (!btn.getText().equals("X")) {
                    statusLabel.setText("Selected Seat: " + selectedSeat);
                    resetColors();
                    btn.setBackground(new Color(46, 204, 113));
                } else {
                    statusLabel.setText("Booked Seat: " + selectedSeat);
                    resetColors();
                }
            });
            seatPanel.add(btn);
        }
        add(new JScrollPane(seatPanel), BorderLayout.CENTER);

        // --- BOTTOM PANEL (ACTIONS) ---
        JPanel bot = new JPanel(new GridLayout(3, 2, 10, 10)); // Changed to 3 rows
        bot.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusLabel = new JLabel("Please select a seat", SwingConstants.CENTER);

        JButton bookBtn = new JButton("BOOK & PAY");
        bookBtn.setBackground(new Color(46, 204, 113));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.addActionListener(e -> handleBooking());

        JButton cancelBtn = new JButton("CANCEL BOOKING");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> handleCancellation());

        // NEW: Complete Ride Button (Resets Everything)
        JButton completeBtn = new JButton("COMPLETE RIDE (RESET)");
        completeBtn.setBackground(new Color(52, 152, 219));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.addActionListener(e -> resetAllSeats());

        bot.add(statusLabel);
        bot.add(new JLabel("")); 
        bot.add(bookBtn);
        bot.add(cancelBtn);
        bot.add(new JLabel("Admin Tools:")); // Label for clarity
        bot.add(completeBtn);
        add(bot, BorderLayout.SOUTH);
    }

    // --- NEW METHOD: RESET ALL SEATS ---
    private void resetAllSeats() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Ride reached destination? This will clear all bookings and reset seats.", 
            "Complete Ride", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE bookings SET status='Available', full_name=NULL, phone_number=NULL, age=NULL, gender=NULL";
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Ride Completed! All seats are now available.");
                loadExistingBookings(); // Refresh the UI
                selectedSeat = "";
                statusLabel.setText("Please select a seat");
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Reset Error: " + e.getMessage());
            }
        }
    }

    // --- EXISTING METHODS (KEEP THE REST SAME) ---
    private void resetColors() {
        for (Component c : seatPanel.getComponents()) {
            JButton b = (JButton) c;
            if (!b.getText().equals("X")) b.setBackground(new Color(236, 240, 241));
        }
    }

    private void loadExistingBookings() {
        for (Component c : seatPanel.getComponents()) {
            JButton b = (JButton) c;
            b.setText(b.getActionCommand());
            b.setBackground(new Color(236, 240, 241));
        }
        try (Connection conn = DBConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT seat_number FROM bookings WHERE status='Booked'")) {
            while (rs.next()) {
                String sNum = rs.getString("seat_number");
                for (Component c : seatPanel.getComponents()) {
                    JButton b = (JButton) c;
                    if (b.getActionCommand().equals(sNum)) {
                        b.setBackground(Color.RED);
                        b.setText("X");
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void handleBooking() {
        if (selectedSeat.isEmpty()) { JOptionPane.showMessageDialog(this, "Select a seat!"); return; }
        for (Component c : seatPanel.getComponents()) {
            JButton b = (JButton) c;
            if (b.getActionCommand().equals(selectedSeat) && b.getText().equals("X")) {
                JOptionPane.showMessageDialog(this, "Seat already booked!"); 
                return;
            }
        }
        String route = (String) routeBox.getSelectedItem();
        int price = getPrice(route);
        JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField nF = new JTextField(); JTextField aF = new JTextField();
        JComboBox<String> gB = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        p.add(new JLabel("Name:")); p.add(nF);
        p.add(new JLabel("Age:")); p.add(aF);
        p.add(new JLabel("Gender:")); p.add(gB);
        p.add(new JLabel("Fare:")); p.add(new JLabel("₹" + price));

        if (JOptionPane.showConfirmDialog(this, p, "Confirm Payment", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            finalizeBooking(nF.getText(), aF.getText(), (String)gB.getSelectedItem(), price, route);
        }
    }

    private int getPrice(String route) {
        switch (route) {
            case "Delhi - Mumbai": return 1500;
            case "Pune - Bangalore": return 1200;
            case "Chennai - Hyderabad": return 1000;
            default: return 0;
        }
    }

    private void finalizeBooking(String name, String age, String gender, int price, String route) {
        String sql = "UPDATE bookings SET status='Booked', full_name=?, phone_number=?, age=?, gender=? WHERE seat_number=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, loggedInPhone);
            ps.setString(3, age);
            ps.setString(4, gender);
            ps.setString(5, selectedSeat);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Booked Successfully!");
                new TicketWindow(name, selectedSeat, route, age, gender, String.valueOf(price)).setVisible(true);
                this.dispose();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void handleCancellation() {
        if (selectedSeat.isEmpty()) { JOptionPane.showMessageDialog(this, "Select a seat!"); return; }
        try (Connection conn = DBConnection.getConnection()) {
            String verifySql = "SELECT phone_number FROM bookings WHERE seat_number = ? AND status = 'Booked'";
            PreparedStatement vPs = conn.prepareStatement(verifySql);
            vPs.setString(1, selectedSeat);
            ResultSet rs = vPs.executeQuery();
            if (rs.next()) {
                if (rs.getString("phone_number").equals(loggedInPhone)) {
                    if (JOptionPane.showConfirmDialog(this, "Cancel seat " + selectedSeat + "?", "Confirm", 0) == 0) {
                        PreparedStatement cPs = conn.prepareStatement("UPDATE bookings SET status='Available', full_name=NULL, phone_number=NULL, age=NULL, gender=NULL WHERE seat_number=?");
                        cPs.setString(1, selectedSeat);
                        cPs.executeUpdate();
                        loadExistingBookings();
                        selectedSeat = "";
                        statusLabel.setText("Please select a seat");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error: You can only cancel seats booked by you!");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}