package ui;

import javax.swing.*;
import java.awt.*;

public class VPDashboard extends JFrame {

    public VPDashboard() {
        setTitle("Vice Principal Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // GridLayout with 2 rows, 4 columns for 7 cards + 1 empty space
        JPanel cardPanel = new JPanel(new GridLayout(2, 4, 15, 15));

        cardPanel.add(createCard("Register", "Register students, teachers, workers"));
        cardPanel.add(createCard("Financial Record", "Manage income & expenses"));
        cardPanel.add(createCard("Staff Attendance", "Mark teacher & worker attendance"));
        cardPanel.add(createCard("Time Table Management", "Manage schedules & classroom assignments"));
        cardPanel.add(createCard("Notifications", "Send and receive messages"));
        cardPanel.add(createCard("Manage Classes & Subjects", "Add classes, sections & assign subjects"));
        cardPanel.add(createCard("School Details", "View all student/teacher/worker data"));
        cardPanel.add(new JPanel()); // empty placeholder for symmetry

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        add(cardPanel, BorderLayout.CENTER);
        add(logoutButton, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String description) {
        JPanel card = new JPanel();
        card.setBorder(BorderFactory.createTitledBorder(title));
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(200, 100));

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
        JButton openButton = new JButton("Open");

        card.add(descLabel, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);

        openButton.addActionListener(e -> {
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
                        case "Student":
                            new RegisterStudentForm().setVisible(true);
                            break;
                        case "Teacher":
                            new RegisterTeacherForm().setVisible(true);
                            break;
                        case "Worker":
                            new RegisterWorkerForm().setVisible(true);
                            break;
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
                    JOptionPane.showMessageDialog(this, title + " card clicked. Functionality coming soon!");
                    break;
            }
        });

        return card;
    }
}
