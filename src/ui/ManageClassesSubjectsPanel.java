package ui;

import dao.ClassDAO;
import models.ClassItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ManageClassesSubjectsPanel extends JPanel {
    private JTextField classNameField;
    private JTextField sectionField;
    private DefaultTableModel tableModel;
    private JTable classTable;
    private JComboBox<ClassItem> classComboBox;
    private JComboBox<String> subjectComboBox;

    public ManageClassesSubjectsPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage Classes and Subjects", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Class"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        classNameField = new JTextField(15);
        sectionField = new JTextField(5);
        JButton addClassButton = new JButton("Add Class");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Class Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(classNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Section:"), gbc);
        gbc.gridx = 1;
        formPanel.add(sectionField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(addClassButton, gbc);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("All Classes"));
        tableModel = new DefaultTableModel(new Object[]{"Class ID", "Class Name", "Section"}, 0);
        classTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(classTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Subject Assignment Panel
        JPanel assignPanel = new JPanel(new GridBagLayout());
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Subject to Class"));
        GridBagConstraints assignGbc = new GridBagConstraints();
        assignGbc.insets = new Insets(5, 5, 5, 5);
        assignGbc.fill = GridBagConstraints.HORIZONTAL;

        classComboBox = new JComboBox<>();
        subjectComboBox = new JComboBox<>();
        JButton assignButton = new JButton("Assign Subject");

        assignGbc.gridx = 0;
        assignGbc.gridy = 0;
        assignPanel.add(new JLabel("Select Class:"), assignGbc);
        assignGbc.gridx = 1;
        assignPanel.add(classComboBox, assignGbc);

        assignGbc.gridx = 0;
        assignGbc.gridy = 1;
        assignPanel.add(new JLabel("Select Subject:"), assignGbc);
        assignGbc.gridx = 1;
        assignPanel.add(subjectComboBox, assignGbc);

        assignGbc.gridx = 1;
        assignGbc.gridy = 2;
        assignPanel.add(assignButton, assignGbc);

        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.add(formPanel);
        verticalPanel.add(Box.createVerticalStrut(20));
        verticalPanel.add(tablePanel);
        verticalPanel.add(Box.createVerticalStrut(20));
        verticalPanel.add(assignPanel);

        JScrollPane scrollPane = new JScrollPane(verticalPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        addClassButton.addActionListener(e -> addClass());

        assignButton.addActionListener(e -> assignSubjectToClass());

        loadClasses();
        loadClassDropdown();
        loadSubjects();
    }

    private void loadClasses() {
        List<ClassItem> classes = ClassDAO.getAllClasses();
        tableModel.setRowCount(0);
        for (ClassItem c : classes) {
            tableModel.addRow(new Object[]{c.getId(), c.getClassName(), c.getSection()});
        }
    }

    private void loadClassDropdown() {
        List<ClassItem> classes = ClassDAO.getAllClasses();
        classComboBox.removeAllItems();
        for (ClassItem c : classes) {
            classComboBox.addItem(c);
        }
    }

    private void loadSubjects() {
        List<String> subjects = ClassDAO.getAllSubjectNames();
        subjectComboBox.removeAllItems();
        for (String subject : subjects) {
            subjectComboBox.addItem(subject);
        }
    }

    private void addClass() {
        String className = classNameField.getText().trim();
        String section = sectionField.getText().trim();

        if (className.isEmpty() || section.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both class name and section.");
            return;
        }

        boolean success = ClassDAO.addClass(className, section);
        if (success) {
            JOptionPane.showMessageDialog(this, "Class added successfully.");
            loadClasses();
            loadClassDropdown();
            classNameField.setText("");
            sectionField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add class.");
        }
    }

    private void assignSubjectToClass() {
        ClassItem selectedClass = (ClassItem) classComboBox.getSelectedItem();
        String selectedSubject = (String) subjectComboBox.getSelectedItem();

        if (selectedClass != null && selectedSubject != null) {
            boolean success = ClassDAO.assignSubjectToClass(selectedClass.getId(), selectedSubject);
            if (success) {
                JOptionPane.showMessageDialog(this, "Subject assigned successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Assignment failed or already exists.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select both a class and a subject.");
        }
    }
}
