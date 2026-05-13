import javax.swing.*;
import java.awt.*;

public class TicketWindow extends JFrame {

    public TicketWindow(String name, String seat, String route, String age, String gender, String amount) {
        // Window Setup
        setTitle("RIDEX | Official Booking Receipt");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window

        // Main Container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Ticket Header & Details using HTML for styling
        String ticketContent = "<html>"
                + "<div style='text-align: center; font-family: Arial;'>"
                + "<h1 style='color: #2C3E50; margin-bottom: 0;'>RIDEX TICKETING</h1>"
                + "<p style='color: #7F8C8D;'>Travel with Comfort</p>"
                + "<hr style='border: 1px dashed #BDC3C7;'>"
                + "<br>"
                + "<table style='width: 100%; font-size: 14px; text-align: left;'>"
                + "<tr><td><b>Passenger:</b></td><td>" + name + "</td></tr>"
                + "<tr><td><b>Age / Gender:</b></td><td>" + age + " / " + gender + "</td></tr>"
                + "<tr><td><b>Seat Number:</b></td><td><span style='color: #E67E22; font-size: 18px;'>" + seat
                + "</span></td></tr>"
                + "<tr><td><b>Route:</b></td><td>" + route + "</td></tr>"
                + "</table>"
                + "<br><br>"
                + "<div style='background-color: #F4F6F7; padding: 15px; border-radius: 10px;'>"
                + "<h2 style='margin: 0; color: #27AE60;'>PAID: ₹" + amount + "</h2>"
                + "<p style='margin: 5px 0 0 0; color: #7F8C8D; font-size: 10px;'>Payment Mode: Digital / Confirmed</p>"
                + "</div>"
                + "<br><hr style='border: 1px dashed #BDC3C7;'>"
                + "<p style='font-size: 11px; color: #95A5A6;'>Please carry a valid ID during travel.<br>Thank you for choosing RideX!</p>"
                + "</div></html>";

        JLabel contentLabel = new JLabel(ticketContent);
        contentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(contentLabel);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        JButton closeBtn = new JButton("CLOSE RECEIPT");
        closeBtn.setBackground(new Color(52, 73, 94));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> this.dispose());

        buttonPanel.add(closeBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space before button
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }
}