package ui;

import models.Teacher;

import javax.swing.*;
import java.awt.*;

public class TeacherDashboard extends JFrame {

    private Teacher teacher;

    public TeacherDashboard(Teacher teacher) {
        this.teacher = teacher;

        setTitle("Teacher Dashboard - " + teacher.getName());
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        add(mainPanel);

        // Title label
        JLabel titleLabel = new JLabel("Welcome, " + teacher.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(titleLabel, BorderLayout.PAGE_START);

        // Info panel (teacher details)
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Teacher Information"));
        infoPanel.add(new JLabel("Name: " + teacher.getName()));
        infoPanel.add(new JLabel("Subject: " + teacher.getSubject()));
        infoPanel.add(new JLabel("Assigned ClassItem: " + teacher.getAssignedClass()));
        infoPanel.add(new JLabel("Email: " + teacher.getEmail()));
        infoPanel.add(new JLabel("Contact: " + teacher.getContact()));

        mainPanel.add(infoPanel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnViewStudents = new JButton("View ClassItem Students");
        JButton btnUploadMarks = new JButton("Upload Marks");
        JButton btnUploadAttendance = new JButton("Upload Attendance");
        JButton btnViewTimetable = new JButton("View Timetable");
        JButton btnLogout = new JButton("Logout");

        // Set preferred sizes for uniform button look (optional)
        Dimension btnSize = new Dimension(150, 40);
        for (JButton btn : new JButton[]{btnViewStudents, btnUploadMarks, btnUploadAttendance, btnViewTimetable, btnLogout}) {
            btn.setPreferredSize(btnSize);
        }

        buttonsPanel.add(btnViewStudents);
        buttonsPanel.add(btnUploadMarks);
        buttonsPanel.add(btnUploadAttendance);
        buttonsPanel.add(btnViewTimetable);
        buttonsPanel.add(btnLogout);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Action listeners (placeholders)
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

        btnLogout.addActionListener(e -> {
            this.dispose();
            // Optional: redirect to login page if needed
        });

        setVisible(true);
    }

    // Test method with dummy teacher data
    public static void main(String[] args) {
        Teacher dummy = new Teacher("Alice Johnson", "Math", "ClassItem 10", "alicej", "pass123", "alice@example.com", "1234567890");
        new TeacherDashboard(dummy);
    }
}
