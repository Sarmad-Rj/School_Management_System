package ui;

import models.Teacher;

import javax.swing.*;
import java.awt.*;

public class TeacherDashboard extends JFrame {

    private final Color ORANGE = new Color(255, 87, 34);
    private final Color BLACK = new Color(25, 25, 25);
    private final Color WHITE = Color.WHITE;
    private final Color CARD_BG = new Color(245, 245, 245); // light white-gray

    private final Teacher teacher;

    public TeacherDashboard(Teacher teacher) {
        this.teacher = teacher;

        setTitle("Teacher Dashboard - " + teacher.getName());
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // TITLE
        JLabel titleLabel = new JLabel("Welcome, " + teacher.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(ORANGE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // LEFT: INFO PANEL
        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // changed from 5 to 6 rows
        infoPanel.setBackground(WHITE);
        infoPanel.setPreferredSize(new Dimension(300, 220));
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ORANGE, 2), "Teacher Information"));

        infoPanel.add(createWhiteLabel("Name: " + teacher.getName()));
        infoPanel.add(createWhiteLabel("Subject: " + teacher.getSubject()));
        infoPanel.add(createWhiteLabel("Class: " + teacher.getAssignedClass()));
        infoPanel.add(createWhiteLabel("Email: " + teacher.getEmail()));
        infoPanel.add(createWhiteLabel("Contact: " + teacher.getContact()));
        infoPanel.add(createWhiteLabel("CNIC: " + teacher.getCnic())); // 🟢 New line

        mainPanel.add(infoPanel, BorderLayout.WEST);

        // CENTER: BUTTON PANEL
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        buttonsPanel.setBackground(WHITE);
        buttonsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ORANGE, 2), "Quick Actions"));

        JButton btnViewStudents = createButton("View Students");
        JButton btnUploadMarks = createButton("Upload Marks");
        JButton btnUploadAttendance = createButton("Upload Attendance");
        JButton btnViewTimetable = createButton("View Timetable");
        JButton btnLogout = createButton("Logout");

        buttonsPanel.add(btnViewStudents);
        buttonsPanel.add(btnUploadMarks);
        buttonsPanel.add(btnUploadAttendance);
        buttonsPanel.add(btnViewTimetable);
        buttonsPanel.add(btnLogout);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        // ACTIONS
        btnViewStudents.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Feature to view class students coming soon!")
        );
        btnUploadMarks.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Feature to upload marks coming soon!")
        );
        btnUploadAttendance.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Feature to upload attendance coming soon!")
        );
        btnViewTimetable.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Feature to view timetable coming soon!")
        );
        btnLogout.addActionListener(e -> dispose());

        setVisible(true);
    }

    private JLabel createWhiteLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(BLACK);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ORANGE);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    public static void main(String[] args) {
        Teacher dummy = new Teacher("Alice Johnson", "Math", "Class 10", "alicej", "pass123", "alice@example.com", "1234567890", "35201-1234567-1");
        new TeacherDashboard(dummy);
    }

}
