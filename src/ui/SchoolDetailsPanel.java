package ui;

import dao.ClassDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import dao.TeacherSalaryDAO;
import db.DBConnection;
import models.ClassItem;
import models.Student;
import models.TeacherAssignment;
import ui.ManageClassesSubjectsPanel;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SchoolDetailsPanel extends JPanel {
    private final Color DARK_BG = new Color(30, 30, 47);
    private final Color CARD_BG = new Color(42, 42, 64);
    private final Color ORANGE = new Color(255, 111, 0);
    private final Color WHITE = Color.WHITE;
    private final Color GREEN = new Color(76, 175, 80);
    private final Color RED = new Color(244, 67, 54);

    public SchoolDetailsPanel() {
//        GlobalButtonStyle.applyRoundedStyle();
        setLayout(new BorderLayout());
        setBackground(DARK_BG);

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
            setBackground(DARK_BG);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            // 🔍 Top Search Panel
            JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
            searchPanel.setBackground(DARK_BG);
            searchField = new JTextField();
            JButton searchButton = styleButton("Search");
            JLabel searchLabel = new JLabel("🔍 Search Student:");
            searchLabel.setForeground(WHITE);
            searchPanel.add(searchLabel, BorderLayout.WEST);
            searchPanel.add(searchField, BorderLayout.CENTER);
            searchPanel.add(searchButton, BorderLayout.EAST);
            searchButton.addActionListener(e -> performSearch());

            add(searchPanel, BorderLayout.NORTH);

            // 👨‍🎓 Total Student Count Label
            totalStudentsLabel = new JLabel(); // initialize
            totalStudentsLabel.setForeground(WHITE);
            totalStudentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            totalStudentsLabel.setHorizontalAlignment(SwingConstants.LEFT);
            add(totalStudentsLabel, BorderLayout.SOUTH); // add to bottom of top section

            // 📚 Class Card Grid
            classCardsPanel = new JPanel(new GridLayout(0, 4, 15, 15));
            classCardsPanel.setBackground(DARK_BG);
            JScrollPane scrollPane = new JScrollPane(classCardsPanel);
            add(scrollPane, BorderLayout.CENTER);

            updateStudentCount(); // 👈 Initial load
            loadClassCards();
        }

        private void updateStudentCount() {
            int total = StudentDAO.getAllStudents().size();
            totalStudentsLabel.setText("Total Students in School: " + total);
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
                JButton btn = styleButton(c.toString());
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
            panel.setBackground(DARK_BG);

            for (Student s : students) {
                JPanel card = new JPanel(new BorderLayout(10, 10));
                card.setBorder(BorderFactory.createTitledBorder(
                        new LineBorder(ORANGE, 1), s.getName()));
                card.setBackground(CARD_BG);
                card.setMaximumSize(new Dimension(700, 100));

                ClassItem classItem = ClassDAO.getClassById(s.getClassId());
                String className = (classItem != null) ? classItem.toString() : "Unknown";

                JPanel info = new JPanel(new GridLayout(3, 2));
                info.setBackground(CARD_BG);
                info.setForeground(WHITE);
                info.add(new JLabel("Father: " + s.getFatherName()));
                info.add(new JLabel("Age: " + s.getAge()));
                info.add(new JLabel("Class: " + className));

                // ✅ Progress bar for attendance
                JProgressBar attendanceBar = new JProgressBar(0, 100);
                attendanceBar.setValue((int) s.getAttendancePercentage());
                attendanceBar.setStringPainted(true);
                if (s.getAttendancePercentage() >= 75)
                    attendanceBar.setForeground(GREEN);
                else if (s.getAttendancePercentage() >= 50)
                    attendanceBar.setForeground(Color.YELLOW);
                else
                    attendanceBar.setForeground(RED);
                info.add(new JLabel("Attendance:"));
                info.add(attendanceBar);

                JPanel feePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                feePanel.setBackground(CARD_BG);
                if ("Yes".equalsIgnoreCase(s.getFeePaid())) {
                    JLabel paid = new JLabel("✅ Paid");
                    paid.setForeground(GREEN);
                    feePanel.add(paid);
                } else {
                    JCheckBox check = new JCheckBox("Mark Fee as Paid");
                    check.setBackground(CARD_BG);
                    check.setForeground(WHITE);
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
                        paid.setForeground(GREEN);
                        feePanel.add(paid);
                        feePanel.revalidate();
                        feePanel.repaint();
                    });
                }

                card.add(info, BorderLayout.CENTER);
                card.add(feePanel, BorderLayout.SOUTH);
                applyLabelColors(card, WHITE);
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
            setBackground(DARK_BG);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            TeacherSalaryDAO.ensureCurrentMonthEntries();
            Map<Integer, String> salaryMap = TeacherSalaryDAO.getCurrentSalaryStatus();
            List<models.Teacher> teachers = TeacherDAO.getAllTeachers();

            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(DARK_BG);

            for (models.Teacher t : teachers) {
                JPanel row = new JPanel(new BorderLayout(10, 10));
                TitledBorder border = BorderFactory.createTitledBorder(new LineBorder(ORANGE), t.getName());
                border.setTitleColor(WHITE);
                row.setBorder(border);
                row.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));
                row.setBackground(CARD_BG);

                List<TeacherAssignment> assignments = TeacherDAO.getAssignmentsByUsername(t.getUsername());
                StringBuilder assignmentText = new StringBuilder();
                for (TeacherAssignment a : assignments) {
                    assignmentText.append("📘 ")
                            .append(a.getSubjectName())
                            .append(" (").append(a.getClassName()).append(")").append("<br>");
                }

                String displayHtml = assignmentText.isEmpty()
                        ? "<html>No Assignments</html>"
                        : "<html>" + assignmentText + "</html>";

                JPanel leftPanel = new JPanel();
                leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                leftPanel.setOpaque(false);
                leftPanel.add(new JLabel(displayHtml));
                leftPanel.add(new JLabel("📞 " + t.getContact()));
                leftPanel.add(new JLabel("💳 " + t.getCnic()));

                for (Component c : leftPanel.getComponents()) {
                    c.setForeground(WHITE);
                }

                JPanel rightPanel = new JPanel();
                rightPanel.setBackground(CARD_BG);
                String status = salaryMap.getOrDefault(t.getId(), "No");

                if ("Yes".equalsIgnoreCase(status)) {
                    JLabel paid = new JLabel("✅ Salary Paid");
                    paid.setForeground(GREEN);
                    rightPanel.add(paid);
                } else {
                    JCheckBox check = new JCheckBox("Mark as Paid");
                    check.setForeground(WHITE);
                    check.setBackground(CARD_BG);
                    rightPanel.add(check);
                    check.addActionListener(e -> {
                        TeacherSalaryDAO.markAsPaid(t.getId());
                        rightPanel.removeAll();
                        JLabel paid = new JLabel("✅ Paid");
                        paid.setForeground(GREEN);
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
            setBackground(DARK_BG);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel header = new JLabel("📘 Classes and Their Assigned Subjects", SwingConstants.CENTER);
            header.setFont(new Font("Segoe UI", Font.BOLD, 20));
            header.setForeground(WHITE);
            header.setBorder(new EmptyBorder(10, 10, 20, 10));
            add(header, BorderLayout.NORTH);

            // ✅ Use GridLayout: 0 rows, 4 columns, auto-wrap
            JPanel cardGrid = new JPanel(new GridLayout(0, 4, 15, 15));
            cardGrid.setBackground(DARK_BG);

            // ✅ Fill cards
            Map<String, List<String>> classToSubjects = TeacherDAO.getAllDetailedAssignments().stream()
                    .collect(Collectors.groupingBy(
                            TeacherAssignment::getClassName,
                            Collectors.mapping(TeacherAssignment::getSubjectName, Collectors.toList())
                    ));

            for (Map.Entry<String, List<String>> entry : classToSubjects.entrySet()) {
                String className = entry.getKey();
                List<String> subjects = entry.getValue();

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(CARD_BG);
                card.setPreferredSize(new Dimension(220, 190));

                TitledBorder border = BorderFactory.createTitledBorder(new LineBorder(ORANGE), className);
                border.setTitleColor(WHITE);
                card.setBorder(border);

                for (String subject : subjects) {
                    JLabel label = new JLabel("➤ " + subject);
                    label.setForeground(WHITE);
                    card.add(label);
                }

                cardGrid.add(card);
            }

            // ✅ Scrollable wrapper
            JScrollPane scrollPane = new JScrollPane(cardGrid);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setBorder(null);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            add(scrollPane, BorderLayout.CENTER);
        }
    }

    private JButton styleButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ORANGE);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private void applyLabelColors(JPanel panel, Color color) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel label) {
                label.setForeground(color);
            } else if (comp instanceof JPanel subPanel) {
                applyLabelColors(subPanel, color);
            }
        }
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

