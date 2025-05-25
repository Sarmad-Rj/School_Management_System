// ui/VPDashboard.java
package ui;

import javax.swing.*;
import java.awt.*;

public class VPDashboard extends JFrame {

    private final Color ORANGE = new Color(255, 87, 34);
    private final Color BLACK = new Color(25, 25, 25);
    private final Color WHITE = Color.WHITE;

    public VPDashboard() {
        setTitle("Vice Principal Dashboard");
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BLACK);

        JLabel header = new JLabel("Vice Principal Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(ORANGE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        cardPanel.setBackground(BLACK);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        cardPanel.add(createCard("Register", "Register students, teachers, workers"));
        cardPanel.add(createCard("Financial Record", "Manage income & expenses"));
        cardPanel.add(createCard("Staff Attendance", "Mark teacher & worker attendance"));
        cardPanel.add(createCard("Time Table Management", "Manage schedules & classroom assignments"));
        cardPanel.add(createCard("Notifications", "Send and receive messages"));
        cardPanel.add(createCard("Manage Classes & Subjects", "Add classes, sections & assign subjects"));
        cardPanel.add(createCard("School Details", "View all student/teacher/worker data"));
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false); // make it fully transparent
        cardPanel.add(emptyPanel);

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        bottomPanel.add(logoutButton);

        add(cardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(220, 130));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ORANGE, 2), title));

        JLabel descLabel = new JLabel("<html><div style='text-align:center;'>" + description + "</div></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton openButton = new JButton("Open");
        styleButton(openButton);

        card.add(descLabel, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);

        openButton.addActionListener(e -> handleCardClick(title));

        return card;
    }

    private void styleButton(JButton button) {
        button.setBackground(ORANGE);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void handleCardClick(String title) {
        switch (title) {
            case "Register":
                Object[] options = {"Student", "Teacher", "Worker"};
                String choice = (String) JOptionPane.showInputDialog(
                        this,
                        "Who do you want to register?",
                        "Select Registration Type",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice == null) return;

                switch (choice) {
                    case "Student" -> new RegisterStudentForm().setVisible(true);
                    case "Teacher" -> new RegisterTeacherForm().setVisible(true);
                    case "Worker" -> new RegisterWorkerForm().setVisible(true);
                }
                break;

            case "Manage Classes & Subjects":
                JFrame classFrame = new JFrame("Manage Classes & Subjects");
                classFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                classFrame.setSize(700, 600);
                classFrame.setLocationRelativeTo(this);
                classFrame.setContentPane(new ManageClassesSubjectsPanel());
                classFrame.setVisible(true);
                break;

            case "School Details":
                JOptionPane.showMessageDialog(this, "School Details functionality coming soon!");
                break;

            default:
                JOptionPane.showMessageDialog(this, title + " clicked. Functionality coming soon!");
                break;
        }
    }
}
