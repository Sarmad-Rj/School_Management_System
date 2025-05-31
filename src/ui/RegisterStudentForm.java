package ui;

import dao.ClassDAO;
import dao.StudentDAO;
import models.ClassItem;
import models.Student;
import theme.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegisterStudentForm extends JFrame {
    private JTextField nameField, fatherNameField, ageField, prevSchoolField, guardianContactField, feeField, admissionDateField;
    private JComboBox<ClassItem> classComboBox;
    private JButton registerButton;

    public RegisterStudentForm() {
        setTitle("Register Student");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        UITheme.applyFrameDefaults(this);


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Father's Name:"), gbc);
        gbc.gridx = 1;
        fatherNameField = new JTextField(20);
        panel.add(fatherNameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(20);
        panel.add(ageField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Class:"), gbc);
        gbc.gridx = 1;
        classComboBox = new JComboBox<>();
        panel.add(classComboBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Previous School:"), gbc);
        gbc.gridx = 1;
        prevSchoolField = new JTextField(20);
        panel.add(prevSchoolField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Guardian Contact:"), gbc);
        gbc.gridx = 1;
        guardianContactField = new JTextField(20);
        panel.add(guardianContactField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Admission Fee:"), gbc);
        gbc.gridx = 1;
        feeField = new JTextField(20);
        panel.add(feeField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Admission Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        admissionDateField = new JTextField(20);
        panel.add(admissionDateField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        add(panel);

        loadClasses();

        registerButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String fatherName = fatherNameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                ClassItem selectedClass = (ClassItem) classComboBox.getSelectedItem();
                String prevSchool = prevSchoolField.getText().trim();
                String guardianContact = guardianContactField.getText().trim();
                double fee = Double.parseDouble(feeField.getText().trim());
                String admissionDate = admissionDateField.getText().trim();

                if (name.isEmpty() || fatherName.isEmpty() || selectedClass == null || admissionDate.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student student = new Student(name, fatherName, age, selectedClass.getId(), prevSchool, guardianContact, fee, admissionDate);

                boolean success = StudentDAO.registerStudent(student);
                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Student registered successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to register student.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for Age and Admission Fee.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    private void loadClasses() {
        List<ClassItem> classes = ClassDAO.getAllClasses();
        for (ClassItem c : classes) {
            classComboBox.addItem(c);
        }
    }

    private void clearFields() {
        nameField.setText("");
        fatherNameField.setText("");
        ageField.setText("");
        classComboBox.setSelectedIndex(0);
        prevSchoolField.setText("");
        guardianContactField.setText("");
        feeField.setText("");
        admissionDateField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterStudentForm::new);
    }
}
