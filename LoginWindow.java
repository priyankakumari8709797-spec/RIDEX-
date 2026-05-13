import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginWindow extends JFrame {
    private JTextField nameField;
    private JTextField phoneField;
    private JButton loginBtn, createAccountBtn;

    public LoginWindow() {
        // Initialize database structure first
        DatabaseInitializer.initialize();

        setTitle("RIDEX | Travel Securely");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("RIDEX", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));

        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Full Name"));

        phoneField = new JTextField();
        phoneField.setBorder(BorderFactory.createTitledBorder("Phone Number"));

        loginBtn = new JButton("LOGIN");
        createAccountBtn = new JButton("CREATE ACCOUNT");

        loginBtn.addActionListener(e -> performLogin());
        createAccountBtn.addActionListener(e -> handleRegistration());

        panel.add(title);
        panel.add(nameField);
        panel.add(phoneField);
        panel.add(loginBtn);
        panel.add(createAccountBtn);
        add(panel);
    }

    private void performLogin() {
        String nameInput = nameField.getText().trim();
        String phoneInput = phoneField.getText().trim();

        if (nameInput.isEmpty() || phoneInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your details.");
            return;
        }

        String query = "SELECT * FROM users WHERE full_name = ? AND phone_number = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nameInput);
            pstmt.setString(2, phoneInput);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("full_name");
                String phone = rs.getString("phone_number");

                JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + name);

                SwingUtilities.invokeLater(() -> {
                    // This passes the user data to the RideXUI dashboard
                    new RideXUI(name, phone).setVisible(true);
                    this.dispose();
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please create an account.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    } // Method properly closed now

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter name and phone first!");
            return;
        }

        String sql = "INSERT INTO users (full_name, phone_number) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account Created! Now click Login.");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow());
    }
}