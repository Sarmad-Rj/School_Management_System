package ui;

import dao.ClassDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import dao.TeacherSalaryDAO;
import db.DBConnection;
import models.ClassItem;
import models.Student;
import models.TeacherAssignment;

import javax.swing.*;
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

        JTabbedPane tabbedPane = new JTabbedPane();
//        Color[] tabColors = {
//                new Color(255, 138, 101),   // light orange
//                new Color(255, 204, 128),   // orange/peach
//                new Color(255, 224, 178),   // cream-orange
//                new Color(255, 112, 67)     // deep orange
//        };

//        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
//            @Override
//            protected void installDefaults() {
//                super.installDefaults();
//                tabAreaInsets.left = 10;
//            }
//
//            @Override
//            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
//                                              int x, int y, int w, int h, boolean isSelected) {
//                Graphics2D g2 = (Graphics2D) g;
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//                g2.setColor(isSelected ? new Color(255, 87, 34) : tabColors[tabIndex % tabColors.length]);
//                g2.fillRoundRect(x + 2, y + 2, w - 4, h - 4, 15, 15);
//            }
//
//            @Override
//            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
//                // No content border
//            }
//
//            @Override
//            protected void paintFocusIndicator(Graphics g, int tabPlacement,
//                                               Rectangle[] rects, int tabIndex,
//                                               Rectangle iconRect, Rectangle textRect, boolean isSelected) {
//                // Remove default focus
//            }
//
//            @Override
//            protected void paintText(Graphics g, int tabPlacement, Font font,
//                                     FontMetrics metrics, int tabIndex,
//                                     String title, Rectangle textRect,
//                                     boolean isSelected) {
//                g.setFont(new Font("Segoe UI", Font.BOLD, 14));
//                g.setColor(isSelected ? Color.WHITE : Color.BLACK);
//                super.paintText(g, tabPlacement, g.getFont(), metrics, tabIndex, title, textRect, isSelected);
//            }
//        });

        tabbedPane.addTab("Teacher Details", new TeacherSalaryPanel());
        tabbedPane.addTab("Worker Details", new JLabel("[TODO] Worker salary management tab"));
        tabbedPane.addTab("Classes & Subjects", new JLabel("[TODO] Class-subject dropdown tab"));
        tabbedPane.addTab("Students In Class", new StudentsInClassPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    static class StudentsInClassPanel extends JPanel {
        private JTextField searchField;
        private JPanel classCardsPanel;

        public StudentsInClassPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Search bar at top
            JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
            searchField = new JTextField();
            JButton searchButton = new JButton("Search");
            searchPanel.add(new JLabel("Search Student by Name:"), BorderLayout.WEST);
            searchPanel.add(searchField, BorderLayout.CENTER);
            searchPanel.add(searchButton, BorderLayout.EAST);
            searchButton.addActionListener(e -> performSearch());

            add(searchPanel, BorderLayout.NORTH);

            // Class cards
            classCardsPanel = new JPanel(new GridLayout(0, 4, 15, 15)); // 4 per row
            JScrollPane scrollPane = new JScrollPane(classCardsPanel);
            add(scrollPane, BorderLayout.CENTER);

            loadClassCards();
        }

        private void performSearch() {
            String keyword = searchField.getText().trim().toLowerCase();
            if (keyword.isEmpty()) return;

            List<Student> all = StudentDAO.getAllStudents();
            List<Student> matched = all.stream()
                    .filter(s -> s.getName().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());

            showStudents(matched, "Search Results");
        }

        private void loadClassCards() {
            classCardsPanel.removeAll();
            List<ClassItem> classes = ClassDAO.getAllClasses();

            for (ClassItem c : classes) {
                JButton btn = new JButton(c.toString());
                btn.setPreferredSize(new Dimension(200, 40));
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
            f.setSize(300, 550);
            f.setLocationRelativeTo(this);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            for (Student s : students) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createTitledBorder(s.getName()));
                card.setBackground(Color.WHITE);
                card.setMaximumSize(new Dimension(700, 80));

                ClassItem classItem = ClassDAO.getClassById(s.getClassId());
                String className = (classItem != null) ? classItem.toString() : "Unknown";

                JPanel info = new JPanel(new GridLayout(2, 2));
                info.add(new JLabel("Father: " + s.getFatherName()));
                info.add(new JLabel("Age: " + s.getAge()));
                info.add(new JLabel("Class: " + className));
                info.add(new JLabel("Attendance: " + s.getAttendancePercentage() + "%"));

                JPanel feePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                if ("Yes".equalsIgnoreCase(s.getFeePaid())) {
                    feePanel.add(new JLabel("✅ Paid"));
                } else {
                    JCheckBox check = new JCheckBox("Mark Fee as Paid");
                    feePanel.add(check);

                    check.addActionListener(e -> {
                        // Update DB
                        try (Connection conn = DBConnection.getConnection();
                             PreparedStatement ps = conn.prepareStatement("UPDATE students SET fee_paid = 'Yes' WHERE id = ?")) {
                            ps.setInt(1, s.getId());
                            ps.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        // Update UI
                        feePanel.removeAll();
                        feePanel.add(new JLabel("✅ Paid"));
                        feePanel.revalidate();
                        feePanel.repaint();
                    });
                }

                card.add(info, BorderLayout.CENTER);
                card.add(feePanel, BorderLayout.SOUTH);
                panel.add(card);
            }


            JScrollPane scrollPane = new JScrollPane(panel);
            f.add(scrollPane);
            f.setVisible(true);
        }
    }

    class TeacherSalaryPanel extends JPanel {
        public TeacherSalaryPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            TeacherSalaryDAO.ensureCurrentMonthEntries();
            Map<Integer, String> salaryMap = TeacherSalaryDAO.getCurrentSalaryStatus();
            List<models.Teacher> teachers = dao.TeacherDAO.getAllTeachers();

            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

            for (models.Teacher t : teachers) {
                JPanel row = new JPanel();
                row.setLayout(new BorderLayout(10, 10));
                row.setBorder(BorderFactory.createTitledBorder(t.getName()));
                row.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));
                row.setBackground(Color.WHITE);

                // 🔁 Build assignment text (shortened)
                List<TeacherAssignment> assignments = TeacherDAO.getAssignmentsByUsername(t.getUsername());
                StringBuilder assignmentText = new StringBuilder();

                for (TeacherAssignment a : assignments) {
                    assignmentText.append("📘 ")
                            .append(a.getSubjectName())
                            .append(" (").append(a.getClassName()).append(")").append("<br>");
                }

                String displayHtml = assignmentText.isEmpty()
                        ? "<html>No Assignments</html>"
                        : "<html>" + assignmentText.toString() + "</html>";

                // 📦 Use vertical label area
                JPanel leftPanel = new JPanel();
                leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                leftPanel.setOpaque(false);
                leftPanel.add(new JLabel(displayHtml));
                leftPanel.add(new JLabel("📞 " + t.getContact()));
                leftPanel.add(new JLabel("💳 " + t.getCnic()));

                // ✅ Right: Paid status or checkbox
                JPanel rightPanel = new JPanel();
                String status = salaryMap.getOrDefault(t.getId(), "No");
                if ("Yes".equalsIgnoreCase(status)) {
                    rightPanel.add(new JLabel("✅ Salary Paid"));
                } else {
                    JCheckBox check = new JCheckBox("This Month's Salary: Mark as Paid");
                    rightPanel.add(check);

                    check.addActionListener(e -> {
                        TeacherSalaryDAO.markAsPaid(t.getId());
                        rightPanel.removeAll();
                        rightPanel.add(new JLabel("✅ Paid"));
                        rightPanel.revalidate();
                        rightPanel.repaint();
                    });
                }

                row.add(leftPanel, BorderLayout.CENTER);
                row.add(rightPanel, BorderLayout.EAST);
                container.add(row);
                container.add(Box.createVerticalStrut(10)); // spacing between rows
            }

            JScrollPane scrollPane = new JScrollPane(container);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("School Details Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new SchoolDetailsPanel());
            frame.setVisible(true);
        });
    }

}
