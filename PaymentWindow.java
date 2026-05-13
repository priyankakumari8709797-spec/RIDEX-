import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A secure modal dialog that handles the transaction phase of the booking.
 * It ensures the user confirms the fare before the seat is officially reserved.
 */
public class PaymentWindow extends JDialog {

    private boolean transactionAuthorized = false;
    private final Color SUCCESS_GREEN = new Color(40, 167, 69);
    private final Color DARK_BG = new Color(33, 37, 41);

    public PaymentWindow(JFrame parent, int fareAmount) {
        // 'true' makes this a modal, meaning the user must finish payment
        // before they can go back to the main app.
        super(parent, "RideX | Secure Checkout", true);

        initWindow();
        setupContent(fareAmount);
    }

    private void initWindow() {
        setSize(350, 250);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getOwner());
        getContentPane().setBackground(DARK_BG);
    }

    private void setupContent(int amount) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(DARK_BG);
        container.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Confirm Payment");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fare Display
        JLabel amountLabel = new JLabel("Total Fare: ₹" + amount);
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        amountLabel.setForeground(new Color(135, 206, 235)); // Sky Blue
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        JButton payNowBtn = new JButton("Authorize Payment");
        JButton cancelBtn = new JButton("Cancel");

        stylePrimaryButton(payNowBtn);
        styleSecondaryButton(cancelBtn);

        // Button Actions
        payNowBtn.addActionListener(e -> processTransaction());
        cancelBtn.addActionListener(e -> dispose());

        // Assembly
        container.add(headerLabel);
        container.add(Box.createVerticalStrut(20));
        container.add(amountLabel);
        container.add(Box.createVerticalStrut(30));
        container.add(payNowBtn);
        container.add(Box.createVerticalStrut(10));
        container.add(cancelBtn);

        add(container, BorderLayout.CENTER);
    }

    private void processTransaction() {
        // In a real app, this is where you'd connect to a Bank API.
        // For our RideX app, we simulate a successful authorization.
        this.transactionAuthorized = true;

        JOptionPane.showMessageDialog(this,
                "Payment Authorized Successfully!\nYour ticket is being generated.",
                "RideX Payment",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    public boolean isPaymentSuccessful() {
        return transactionAuthorized;
    }

    // Helper methods for "Humanized" styling
    private void stylePrimaryButton(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBackground(SUCCESS_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.LIGHT_GRAY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}