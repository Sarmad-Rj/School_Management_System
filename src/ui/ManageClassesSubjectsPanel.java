package ui;

import dao.ClassDAO;
import dao.SubjectDAO;
import models.ClassItem;
import models.Subject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ManageClassesSubjectsPanel extends JPanel {
    private JTextField classNameField;
    private JTextField sectionField;
    private JTextField subjectNameField;
    private JComboBox<ClassItem> classComboBox;
    private JPanel subjectCheckboxPanel;
    private DefaultTableModel classTableModel;

    public ManageClassesSubjectsPanel() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel titleLabel = new JLabel("Manage Classes and Subjects", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panels Container
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.add(createClassFormPanel());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createSubjectFormPanel());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createAssignSubjectsPanel());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createClassTablePanel());

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        loadClasses();
        loadSubjects();
    }

    private JPanel createClassFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Class"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        classNameField = new JTextField(15);
        sectionField = new JTextField(10);
        JButton addButton = new JButton("Add Class");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Class Name:"), gbc);
        gbc.gridx = 1;
        panel.add(classNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Section:"), gbc);
        gbc.gridx = 1;
        panel.add(sectionField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(addButton, gbc);

        addButton.addActionListener(e -> {
            String name = classNameField.getText().trim();
            String section = sectionField.getText().trim();
            if (!name.isEmpty() && !section.isEmpty()) {
                boolean success = ClassDAO.addClass(name, section);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Class added.");
                    classNameField.setText("");
                    sectionField.setText("");
                    loadClasses();
                }
            }
        });

        return panel;
    }

    private JPanel createSubjectFormPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Add New Subject"));

        subjectNameField = new JTextField(20);
        JButton addSubjectButton = new JButton("Add Subject");

        panel.add(new JLabel("Subject Name:"));
        panel.add(subjectNameField);
        panel.add(addSubjectButton);

        addSubjectButton.addActionListener(e -> {
            String subjectName = subjectNameField.getText().trim();
            if (!subjectName.isEmpty()) {
                boolean added = new SubjectDAO().addSubject(subjectName);
                if (added) {
                    JOptionPane.showMessageDialog(this, "Subject added.");
                    subjectNameField.setText("");
                    loadSubjects();
                }
            }
        });

        return panel;
    }

    private JPanel createAssignSubjectsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Assign Subjects to Class"));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        classComboBox = new JComboBox<>();
        topRow.add(new JLabel("Select Class:"));
        topRow.add(classComboBox);

        subjectCheckboxPanel = new JPanel();
        subjectCheckboxPanel.setLayout(new BoxLayout(subjectCheckboxPanel, BoxLayout.Y_AXIS));

        JButton assignButton = new JButton("Assign Selected Subjects");

        panel.add(topRow);
        panel.add(subjectCheckboxPanel);
        panel.add(assignButton);

        assignButton.addActionListener((ActionEvent e) -> {
            ClassItem selectedClass = (ClassItem) classComboBox.getSelectedItem();
            if (selectedClass == null) return;

            List<Integer> selectedSubjectIds = subjectCheckboxPanel.getComponents().length > 0 ?
                    SubjectDAO.getSelectedSubjectIds(subjectCheckboxPanel) : null;

            if (selectedSubjectIds != null && !selectedSubjectIds.isEmpty()) {
                boolean success = SubjectDAO.assignSubjectsToClass(selectedClass.getId(), selectedSubjectIds);
                JOptionPane.showMessageDialog(this, success ? "Subjects assigned." : "Failed to assign.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select at least one subject.");
            }
        });

        return panel;
    }

    private JPanel createClassTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Existing Classes"));

        classTableModel = new DefaultTableModel(new Object[]{"ID", "Class", "Section"}, 0);
        JTable table = new JTable(classTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(400, 100));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private void loadClasses() {
        List<ClassItem> classes = ClassDAO.getAllClasses();
        classComboBox.removeAllItems();
        classTableModel.setRowCount(0);
        for (ClassItem c : classes) {
            classComboBox.addItem(c);
            classTableModel.addRow(new Object[]{c.getId(), c.getClassName(), c.getSection()});
        }
    }

    private void loadSubjects() {
        List<Subject> subjects = SubjectDAO.getAllSubjects();
        subjectCheckboxPanel.removeAll();
        for (Subject s : subjects) {
            JCheckBox checkbox = new JCheckBox(s.getName());
            checkbox.setActionCommand(String.valueOf(s.getId()));
            subjectCheckboxPanel.add(checkbox);
        }
        subjectCheckboxPanel.revalidate();
        subjectCheckboxPanel.repaint();
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test ManageClassesSubjectsPanel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ManageClassesSubjectsPanel());
            frame.setSize(800, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
