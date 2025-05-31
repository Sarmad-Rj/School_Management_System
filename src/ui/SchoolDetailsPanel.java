package ui;

import dao.ClassDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import dao.TeacherSalaryDAO;
import db.DBConnection;
import models.ClassItem;
import models.Student;
import models.TeacherAssignment;
import theme.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SchoolDetailsPanel extends JPanel {

    public SchoolDetailsPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.LIGHT_GRAY);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabbedPane.addTab("Teacher Details", new TeacherSalaryPanel());
        tabbedPane.addTab("Worker Details", new JLabel("[TODO] Worker salary tab"));
        tabbedPane.addTab("Classes & Subjects", new ClassSubjectViewerPanel());
        tabbedPane.addTab("Students In Class", new StudentsInClassPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    class StudentsInClassPanel extends JPanel {
        private JTextField searchField;
        private JPanel classCardsPanel;
        private JLabel totalStudentsLabel;

        public StudentsInClassPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(UITheme.LIGHT_GRAY);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
            searchPanel.setBackground(UITheme.LIGHT_GRAY);
            searchField = new JTextField();
            JButton searchButton = createButton("Search");
            JLabel searchLabel = new JLabel("🔍 Search Student:");
            searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            searchPanel.add(searchLabel, BorderLayout.WEST);
            searchPanel.add(searchField, BorderLayout.CENTER);
            searchPanel.add(searchButton, BorderLayout.EAST);
            searchButton.addActionListener(e -> performSearch());

            add(searchPanel, BorderLayout.NORTH);

            totalStudentsLabel = new JLabel();
            totalStudentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            add(totalStudentsLabel, BorderLayout.SOUTH);

            classCardsPanel = new JPanel(new GridLayout(0, 4, 15, 15));
            classCardsPanel.setBackground(UITheme.LIGHT_GRAY);
            JScrollPane scrollPane = new JScrollPane(classCardsPanel);
            scrollPane.setBorder(null);
            add(scrollPane, BorderLayout.CENTER);

            updateStudentCount();
            loadClassCards();
        }

        private void updateStudentCount() {
            int total = StudentDAO.getAllStudents().size();
            totalStudentsLabel.setText("Total Students: " + total);
        }

        private void performSearch() {
            String keyword = searchField.getText().trim().toLowerCase();
            if (keyword.isEmpty()) return;

            List<Student> matched = StudentDAO.getAllStudents().stream()
                    .filter(s -> s.getName().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());

            showStudents(matched, "Search Results");
        }

        private void loadClassCards() {
            classCardsPanel.removeAll();
            List<ClassItem> classes = ClassDAO.getAllClasses();

            for (ClassItem c : classes) {
                JButton btn = createButton(c.toString());
                btn.addActionListener(e -> {
                    List<Student> list = StudentDAO.getStudentsByClassId(c.getId());
                    showStudents(list, "Students in " + c);
                });
                classCardsPanel.add(btn);
            }

            classCardsPanel.revalidate();
            classCardsPanel.repaint();
        }

        private void showStudents(List<Student> students, String title) {
            JFrame f = new JFrame(title);
            f.setSize(600, 600);
            f.setLocationRelativeTo(this);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(UITheme.LIGHT_GRAY);

            for (Student s : students) {
                JPanel card = new JPanel(new BorderLayout(10, 10));
                card.setBackground(UITheme.WHITE);
                card.setBorder(UITheme.getRoundedOrangeBorder(s.getName()));
                card.setMaximumSize(new Dimension(700, 150));

                ClassItem classItem = ClassDAO.getClassById(s.getClassId());
                String className = (classItem != null) ? classItem.toString() : "Unknown";

                JPanel info = new JPanel(new GridLayout(3, 2));
                info.setBackground(UITheme.WHITE);
                info.add(new JLabel("Father: " + s.getFatherName()));
                info.add(new JLabel("Age: " + s.getAge()));
                info.add(new JLabel("Class: " + className));

                JProgressBar attendanceBar = new JProgressBar(0, 100);
                attendanceBar.setValue((int) s.getAttendancePercentage());
                attendanceBar.setStringPainted(true);
                attendanceBar.setForeground(s.getAttendancePercentage() >= 75 ? new Color(76, 175, 80)
                        : s.getAttendancePercentage() >= 50 ? Color.ORANGE : Color.RED);

                info.add(new JLabel("Attendance:"));
                info.add(attendanceBar);

                JPanel feePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                feePanel.setBackground(UITheme.WHITE);
                if ("Yes".equalsIgnoreCase(s.getFeePaid())) {
                    JLabel paid = new JLabel("✅ Paid");
                    paid.setForeground(new Color(76, 175, 80));
                    feePanel.add(paid);
                } else {
                    JCheckBox check = new JCheckBox("Mark Fee as Paid");
                    check.setBackground(UITheme.WHITE);
                    feePanel.add(check);
                    check.addActionListener(e -> {
                        try (Connection conn = DBConnection.getConnection();
                             PreparedStatement ps = conn.prepareStatement("UPDATE students SET fee_paid = 'Yes' WHERE id = ?")) {
                            ps.setInt(1, s.getId());
                            ps.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        feePanel.removeAll();
                        JLabel paid = new JLabel("✅ Paid");
                        paid.setForeground(new Color(76, 175, 80));
                        feePanel.add(paid);
                        feePanel.revalidate();
                        feePanel.repaint();
                    });
                }

                card.add(info, BorderLayout.CENTER);
                card.add(feePanel, BorderLayout.SOUTH);
                panel.add(card);
                panel.add(Box.createVerticalStrut(10));
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            f.add(scrollPane);
            f.setVisible(true);
        }
    }

    class TeacherSalaryPanel extends JPanel {
        public TeacherSalaryPanel() {
            setLayout(new BorderLayout());
            setBackground(UITheme.LIGHT_GRAY);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            // Ensure DB entries exist for this month
            TeacherSalaryDAO.ensureCurrentMonthEntries();

            // Get status and date maps
            Map<Integer, String> statusMap = TeacherSalaryDAO.getCurrentSalaryStatus();
            Map<Integer, String> dateMap = TeacherSalaryDAO.getPaidDates();

            List<models.Teacher> teachers = TeacherDAO.getAllTeachers();

            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(UITheme.LIGHT_GRAY);

            for (models.Teacher t : teachers) {
                JPanel row = new JPanel(new BorderLayout(10, 10));
                row.setBackground(UITheme.WHITE);
                row.setBorder(UITheme.getRoundedOrangeBorder(t.getName()));
                row.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));

                // LEFT: Info & Assignments
                JPanel leftPanel = new JPanel();
                leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                leftPanel.setOpaque(false);
                leftPanel.add(new JLabel("📞 " + t.getContact()));
                leftPanel.add(new JLabel("💳 " + t.getCnic()));

                List<TeacherAssignment> assignments = TeacherDAO.getAssignmentsByUsername(t.getUsername());
                for (TeacherAssignment a : assignments) {
                    leftPanel.add(new JLabel("📘 " + a.getSubjectName() + " (" + a.getClassName() + ")"));
                }

                // RIGHT: Salary Status UI
                JPanel rightPanel = new JPanel();
                rightPanel.setBackground(UITheme.WHITE);

                String status = statusMap.getOrDefault(t.getId(), "No");
                String paidDate = dateMap.get(t.getId());

                if ("Yes".equalsIgnoreCase(status) && paidDate != null) {
                    JLabel paid = new JLabel("✅ Paid on " + paidDate);
                    paid.setForeground(UITheme.SUCCESS);
                    rightPanel.add(paid);
                } else {
                    JCheckBox check = new JCheckBox("Mark as Paid");
                    check.setBackground(UITheme.WHITE);
                    rightPanel.add(check);

                    check.addActionListener(e -> {
                        TeacherSalaryDAO.markAsPaid(t.getId());

                        // Refresh only this row’s right panel
                        rightPanel.removeAll();
                        JLabel paid = new JLabel("✅ Paid on Today");
                        paid.setForeground(UITheme.SUCCESS);
                        rightPanel.add(paid);
                        rightPanel.revalidate();
                        rightPanel.repaint();
                    });
                }

                row.add(leftPanel, BorderLayout.CENTER);
                row.add(rightPanel, BorderLayout.EAST);
                container.add(row);
                container.add(Box.createVerticalStrut(10));
            }

            JScrollPane scrollPane = new JScrollPane(container);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    class ClassSubjectViewerPanel extends JPanel {
        public ClassSubjectViewerPanel() {
            setLayout(new BorderLayout());
            setBackground(UITheme.LIGHT_GRAY);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel header = UITheme.createTitleLabel("Classes and Their Assigned Subjects");
            add(header, BorderLayout.NORTH);

            JPanel cardGrid = new JPanel(new GridLayout(0, 4, 15, 15));
            cardGrid.setBackground(UITheme.LIGHT_GRAY);

            Map<String, List<String>> classToSubjects = TeacherDAO.getAllDetailedAssignments().stream()
                    .collect(Collectors.groupingBy(
                            TeacherAssignment::getClassName,
                            Collectors.mapping(TeacherAssignment::getSubjectName, Collectors.toList())
                    ));

            for (Map.Entry<String, List<String>> entry : classToSubjects.entrySet()) {
                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(UITheme.WHITE);
                card.setPreferredSize(new Dimension(220, 190));
                card.setBorder(UITheme.getRoundedOrangeBorder(entry.getKey()));

                for (String subject : entry.getValue()) {
                    JLabel label = new JLabel("➤ " + subject);
                    card.add(label);
                }

                cardGrid.add(card);
            }

            JScrollPane scrollPane = new JScrollPane(cardGrid);
            scrollPane.setBorder(null);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        UITheme.stylePrimaryButton(btn);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("School Details Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new SchoolDetailsPanel());
            frame.setVisible(true);
        });
    }
}
