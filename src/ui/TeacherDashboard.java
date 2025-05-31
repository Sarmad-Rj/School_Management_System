package ui;

import dao.TeacherDAO;
import models.ClassItem;
import models.Teacher;
import models.TeacherAssignment;
import theme.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeacherDashboard extends JFrame {

    public TeacherDashboard(Teacher teacher) {
        setTitle("Teacher Dashboard - " + teacher.getName());
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        UITheme.applyFrameDefaults(this);


        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(UITheme.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        JLabel titleLabel = UITheme.createTitleLabel("Welcome, " + teacher.getName());
        JPanel headerPanel = UITheme.createHeaderPanel(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // LEFT: Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 1, 10));
        infoPanel.setBackground(UITheme.WHITE);
        infoPanel.setPreferredSize(new Dimension(250, 180));
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UITheme.ORANGE, 2), "Teacher Info"));

        infoPanel.add(createLabel("Name: " + teacher.getName()));
        infoPanel.add(createLabel("Email: " + teacher.getEmail()));
        infoPanel.add(createLabel("Contact: " + teacher.getContact()));
        infoPanel.add(createLabel("CNIC: " + teacher.getCnic()));
        infoPanel.add(createLabel("Username: " + teacher.getUsername()));

        mainPanel.add(infoPanel, BorderLayout.WEST);

        // CENTER: Dynamic class+subject cards
        JPanel cardGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        cardGrid.setBackground(UITheme.LIGHT_GRAY);
        cardGrid.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UITheme.ORANGE, 2), "Assigned Classes & Subjects"));

        JScrollPane scrollPane = new JScrollPane(cardGrid);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // SOUTH: Logout Button
        JButton logoutBtn = new JButton("Logout");
        UITheme.stylePrimaryButton(logoutBtn);
        logoutBtn.setPreferredSize(new Dimension(120, 40));
        logoutBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.LIGHT_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        bottomPanel.add(logoutBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

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
        card.setBackground(UITheme.WHITE);
        card.setBorder(BorderFactory.createLineBorder(UITheme.ORANGE, 2));
        card.setPreferredSize(new Dimension(280, 140));

        JLabel classLabel = new JLabel("Class: " + ta.getClassName(), SwingConstants.CENTER);
        classLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel subjectLabel = new JLabel("Subject: " + ta.getSubjectName(), SwingConstants.CENTER);
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setBackground(UITheme.WHITE);
        JButton btnMarks = new JButton("Upload Marks");
        JButton btnAttendance = new JButton("Upload Attendance");

        UITheme.stylePrimaryButton(btnMarks);
        UITheme.stylePrimaryButton(btnAttendance);

        btnPanel.add(btnMarks);
        btnPanel.add(btnAttendance);

        btnMarks.addActionListener(e -> {
            String rawClass = ta.getClassName();
            String rawSubject = ta.getSubjectName();

            int classId = findClassIdSmartMatch(rawClass);

            String cleanedSubject = rawSubject.split("-")[0].trim();
            int subjectId = dao.SubjectDAO.getAllSubjects().stream()
                    .filter(s -> s.getName().equalsIgnoreCase(cleanedSubject))
                    .map(s -> s.getId())
                    .findFirst().orElse(-1);

            if (classId == -1 || subjectId == -1) {
                JOptionPane.showMessageDialog(this, "Class or Subject not found in database.");
                return;
            }

            JFrame frame = new JFrame("Upload Marks - " + rawClass + " - " + cleanedSubject);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new UploadMarksPanel(classId, subjectId));
            frame.setVisible(true);
        });

        btnAttendance.addActionListener(e -> {
            int classId = findClassIdSmartMatch(ta.getClassName());
            int subjectId = getSubjectIdByName(ta.getSubjectName());

            if (classId == -1) {
                JOptionPane.showMessageDialog(this, "Class not found.");
                return;
            }

            JFrame frame = new JFrame("Upload Attendance - " + ta.getClassName());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new UploadAttendancePanel(classId));
            frame.setVisible(true);
        });

        card.add(classLabel, BorderLayout.NORTH);
        card.add(subjectLabel, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.SOUTH);

        return card;
    }

    private int findClassIdSmartMatch(String classBaseName) {
        for (ClassItem c : dao.ClassDAO.getAllClasses()) {
            String fullName = c.getClassName().trim();
            if (fullName.equalsIgnoreCase(classBaseName.trim())) {
                return c.getId();
            }
        }
        return -1;
    }

    private int getSubjectIdByName(String subjectName) {
        return dao.SubjectDAO.getAllSubjects().stream()
                .filter(s -> s.getName().equalsIgnoreCase(subjectName))
                .map(s -> s.getId())
                .findFirst()
                .orElse(-1);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(UITheme.DARK_TEXT);
        return label;
    }
}
