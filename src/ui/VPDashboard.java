package ui;

import javax.swing.*;
import java.awt.*;

public class VPDashboard extends JFrame {

    public VPDashboard() {
        setTitle("Vice Principal Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 3, 15, 15)); // 6 cards in 2 rows

        add(createCard("Register", "Register students, teachers, workers"));
        add(createCard("Financial Record", "Manage income & expenses"));
        add(createCard("Staff Attendance", "Mark teacher & worker attendance"));
        add(createCard("Time Table Management", "Manage schedules & classroom assignments"));
        add(createCard("Notifications", "Send and receive messages"));
        add(createCard("School Details", "View all student/teacher/worker data"));
    }

    private JPanel createCard(String title, String description) {
        JPanel card = new JPanel();
        card.setBorder(BorderFactory.createTitledBorder(title));
        card.setLayout(new BorderLayout());

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
        JButton openButton = new JButton("Open");

        card.add(descLabel, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);

        openButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, title + " card clicked. Functionality coming soon!");
            // TODO: Open respective UI
        });

        return card;
    }
}
