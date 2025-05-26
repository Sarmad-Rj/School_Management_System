package ui;

import dao.TeacherDAO;
import models.Teacher;
import models.TeacherAssignment;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeacherDashboard extends JFrame {

    private final Color ORANGE = new Color(255, 87, 34);
    private final Color BLACK = new Color(25, 25, 25);
    private final Color WHITE = Color.WHITE;
    private final Color CARD_BG = new Color(245, 245, 245);

    public TeacherDashboard(Teacher teacher) {
        setTitle("Teacher Dashboard - " + teacher.getName());
        setSize(1050, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("Welcome, " + teacher.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(ORANGE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // LEFT: Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 1, 10));
        infoPanel.setBackground(WHITE);
        infoPanel.setPreferredSize(new Dimension(300, 180));
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ORANGE, 2), "Teacher Info"));

        infoPanel.add(createLabel("Name: " + teacher.getName()));
        infoPanel.add(createLabel("Email: " + teacher.getEmail()));
        infoPanel.add(createLabel("Contact: " + teacher.getContact()));
        infoPanel.add(createLabel("CNIC: " + teacher.getCnic()));
        infoPanel.add(createLabel("Username: " + teacher.getUsername()));

        mainPanel.add(infoPanel, BorderLayout.WEST);

        // CENTER: Dynamic class+subject cards
        JPanel cardGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        cardGrid.setBackground(WHITE);
        cardGrid.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ORANGE, 2), "Assigned Classes & Subjects"));

        JScrollPane scrollPane = new JScrollPane(cardGrid);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // SOUTH: Logout Button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(ORANGE);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        logoutBtn.addActionListener(e -> {
            dispose(); // Close current dashboard
            SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true)); // Open login page again
        });

        mainPanel.add(logoutBtn, BorderLayout.SOUTH);

        List<TeacherAssignment> assignments = TeacherDAO.getAssignmentsByUsername(teacher.getUsername());

        if (assignments.isEmpty()) {
            cardGrid.add(new JLabel("No class/subject assigned.", SwingConstants.CENTER));
        } else {
            for (TeacherAssignment ta : assignments) {
                cardGrid.add(createAssignmentCard(ta));
            }
        }

        setVisible(true);
    }

    private JPanel createAssignmentCard(TeacherAssignment ta) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(ORANGE, 2));

        JLabel classLabel = new JLabel("Class: " + ta.getClassName(), SwingConstants.CENTER);
        classLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel subjectLabel = new JLabel("Subject: " + ta.getSubjectName(), SwingConstants.CENTER);
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton btnMarks = new JButton("Upload Marks");
        JButton btnAttendance = new JButton("Upload Attendance");

        btnMarks.setBackground(ORANGE);
        btnMarks.setForeground(Color.WHITE);
        btnAttendance.setBackground(ORANGE);
        btnAttendance.setForeground(Color.WHITE);

        btnPanel.add(btnMarks);
        btnPanel.add(btnAttendance);

        card.add(classLabel, BorderLayout.NORTH);
        card.add(subjectLabel, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.SOUTH);

        return card;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(BLACK);
        return label;
    }

//    public static void main(String[] args) {
//        Teacher t = new Teacher("Alice", "alice@example.com", "03111222333", "35201-1234567-1", "alicej", "pass123", "assASs", "as");
//        new TeacherDashboard(t);
//    }
}
