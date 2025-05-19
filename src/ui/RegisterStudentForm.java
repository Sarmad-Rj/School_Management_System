package ui;

import dao.StudentDAO;
import models.Student;

import javax.swing.*;
import java.awt.*;

public class RegisterStudentForm extends JFrame {
    private JTextField nameField, fatherNameField, ageField, classField, prevSchoolField, guardianContactField, feeField, admissionDateField;
    private JButton registerButton;

    public RegisterStudentForm() {
        setTitle("Register Student");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use JPanel with GridBagLayout and padding
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Outer margin

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0 - Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Row 1 - Father's Name
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        panel.add(new JLabel("Father's Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        fatherNameField = new JTextField(20);
        panel.add(fatherNameField, gbc);

        // Row 2 - Age
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ageField = new JTextField(20);
        panel.add(ageField, gbc);

        // Row 3 - ClassItem
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        panel.add(new JLabel("ClassItem:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        classField = new JTextField(20);
        panel.add(classField, gbc);

        // Row 4 - Previous School
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        panel.add(new JLabel("Previous School:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        prevSchoolField = new JTextField(20);
        panel.add(prevSchoolField, gbc);

        // Row 5 - Guardian Contact
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        panel.add(new JLabel("Guardian Contact:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        guardianContactField = new JTextField(20);
        panel.add(guardianContactField, gbc);

        // Row 6 - Admission Fee
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        panel.add(new JLabel("Admission Fee:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        feeField = new JTextField(20);
        panel.add(feeField, gbc);

        // Row 7 - Admission Date
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        panel.add(new JLabel("Admission Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        admissionDateField = new JTextField(20);
        panel.add(admissionDateField, gbc);

        // Row 8 - Register button (span 2 columns)
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        add(panel);

        registerButton.addActionListener(e -> {
            try {
                // Get data from fields
                String name = nameField.getText().trim();
                String fatherName = fatherNameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String studentClass = classField.getText().trim();
                String prevSchool = prevSchoolField.getText().trim();
                String guardianContact = guardianContactField.getText().trim();
                double fee = Double.parseDouble(feeField.getText().trim());
                String admissionDate = admissionDateField.getText().trim();

                // Basic validation
                if (name.isEmpty() || fatherName.isEmpty() || studentClass.isEmpty() || admissionDate.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create student object
                Student student = new Student(name, fatherName, age, studentClass, prevSchool, guardianContact, fee, admissionDate);

                // Insert into DB
                boolean success = StudentDAO.registerStudent(student);
                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Student registered successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to register student. Check console for errors.");
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

    private void clearFields() {
        nameField.setText("");
        fatherNameField.setText("");
        ageField.setText("");
        classField.setText("");
        prevSchoolField.setText("");
        guardianContactField.setText("");
        feeField.setText("");
        admissionDateField.setText("");
    }

    public static void main(String[] args) {
        new RegisterStudentForm();
    }
}
