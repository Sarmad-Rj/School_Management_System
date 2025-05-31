package ui;

import theme.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class VPDashboard extends JFrame {

    private final Map<String, String> iconMap = new HashMap<>() {{
        put("Register", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Register.png");
        put("Financial Record", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Financial Record.png");
        put("Staff Attendance", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Staff Attendance.png");
        put("Time Table Management", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Time Table Management.png");
        put("Notifications", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Notifications.png");
        put("Manage Classes & Subjects", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Manage Classes & Subjects.png");
        put("School Details", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\School Details.png");
    }};

    public VPDashboard() {
        setTitle("Vice Principal Dashboard");
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.LIGHT_GRAY);
        UITheme.applyFrameDefaults(this);

        // Header
        JLabel header = UITheme.createTitleLabel("Vice Principal Dashboard");
        JPanel headerPanel = UITheme.createHeaderPanel(header);
        add(headerPanel, BorderLayout.NORTH);

        // Card Grid
        JPanel cardPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        cardPanel.setBackground(UITheme.LIGHT_GRAY);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        cardPanel.add(createCard("Register", "Register students, teachers, workers"));
        cardPanel.add(createCard("Financial Record", "Manage income & expenses"));
        cardPanel.add(createCard("Staff Attendance", "Mark teacher & worker attendance"));
        cardPanel.add(createCard("Time Table Management", "Manage schedules & classroom assignments"));
        cardPanel.add(createCard("Notifications", "Send and receive messages"));
        cardPanel.add(createCard("Manage Classes & Subjects", "Add classes, sections & assign subjects"));
        cardPanel.add(createCard("School Details", "View all student/teacher/worker data"));
        cardPanel.add(new JPanel());

        add(cardPanel, BorderLayout.CENTER);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        UITheme.stylePrimaryButton(logoutButton);
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.LIGHT_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String description) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(220, 160));
        card.setBackground(UITheme.WHITE);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(UITheme.getRoundedOrangeBorder());

        JLabel iconLabel = new JLabel();
        String iconPath = iconMap.get(title);
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledIcon));
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descLabel = new JLabel("<html><div style='text-align:center;'><b>" + title + "</b><br>" + description + "</div></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton openButton = new JButton("Open");
        UITheme.stylePrimaryButton(openButton);
        openButton.setPreferredSize(new Dimension(90, 30));

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(255, 243, 233));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(UITheme.WHITE);
            }
        });

        openButton.addActionListener(e -> handleCardClick(title));
        return card;
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
                JFrame schoolFrame = new JFrame("School Details");
                schoolFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                schoolFrame.setSize(1000, 700);
                schoolFrame.setLocationRelativeTo(this);
                schoolFrame.setContentPane(new SchoolDetailsPanel());
                schoolFrame.setVisible(true);
                break;

            default:
                JOptionPane.showMessageDialog(this, title + " clicked. Functionality coming soon!");
                break;
        }
    }
}
