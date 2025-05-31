package ui;

import dao.ClassDAO;
import dao.SubjectDAO;
import models.ClassItem;
import models.Subject;
import theme.UITheme;

import javax.swing.*;
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
        setBackground(UITheme.LIGHT_GRAY);

        JLabel titleLabel = UITheme.createTitleLabel("Manage Classes and Subjects");
        JPanel headerPanel = UITheme.createHeaderPanel(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UITheme.LIGHT_GRAY);

        contentPanel.add(createClassFormPanel());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createSubjectFormPanel());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createAssignSubjectsPanel());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createClassTablePanel());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        loadClasses();
        loadSubjects();
    }

    private JPanel createClassFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Class"));
        panel.setBackground(UITheme.WHITE);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        inputPanel.setBackground(UITheme.WHITE);

        classNameField = new JTextField(10);
        sectionField = new JTextField(5);

        inputPanel.add(new JLabel("Class Name:"));
        inputPanel.add(classNameField);
        inputPanel.add(new JLabel("Section:"));
        inputPanel.add(sectionField);

        JButton addButton = new JButton("Add Class");
        UITheme.stylePrimaryButton(addButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.add(addButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Subject"));
        panel.setBackground(UITheme.WHITE);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBackground(UITheme.WHITE);

        subjectNameField = new JTextField(20);
        inputPanel.add(new JLabel("Subject Name:"));
        inputPanel.add(subjectNameField);

        JButton addSubjectButton = new JButton("Add Subject");
        UITheme.stylePrimaryButton(addSubjectButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.add(addSubjectButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

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
        panel.setBackground(UITheme.WHITE);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.setBackground(UITheme.WHITE);

        classComboBox = new JComboBox<>();
        UITheme.styleComboBox(classComboBox);

        topRow.add(new JLabel("Select Class:"));
        topRow.add(classComboBox);

        subjectCheckboxPanel = new JPanel(new GridLayout(0, 4, 10, 5));
        subjectCheckboxPanel.setBackground(UITheme.WHITE);

        JScrollPane scrollPane = new JScrollPane(subjectCheckboxPanel);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        JButton assignButton = new JButton("Assign Selected Subjects");
        UITheme.stylePrimaryButton(assignButton);

        panel.add(topRow);
        panel.add(scrollPane);
        panel.add(assignButton);

        assignButton.addActionListener((ActionEvent e) -> {
            ClassItem selectedClass = (ClassItem) classComboBox.getSelectedItem();
            if (selectedClass == null) return;

            List<Integer> selectedSubjectIds = SubjectDAO.getSelectedSubjectIds(subjectCheckboxPanel);
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
        panel.setBackground(UITheme.WHITE);

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
