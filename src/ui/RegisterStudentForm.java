package ui;

import dao.StudentDAO;
import models.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterStudentForm extends JFrame {
    private JTextField nameField, fatherNameField, ageField, classField, prevSchoolField, guardianContactField, feeField, admissionDateField;
    private JButton registerButton;

    public RegisterStudentForm() {
        setTitle("Register Student");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new GridLayout(9, 2, 10, 10));

        nameField = new JTextField();
        fatherNameField = new JTextField();
        ageField = new JTextField();
        classField = new JTextField();
        prevSchoolField = new JTextField();
        guardianContactField = new JTextField();
        feeField = new JTextField();
        admissionDateField = new JTextField();

        registerButton = new JButton("Register");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Father's Name:"));
        add(fatherNameField);
        add(new JLabel("Age:"));
        add(ageField);
        add(new JLabel("Class:"));
        add(classField);
        add(new JLabel("Previous School:"));
        add(prevSchoolField);
        add(new JLabel("Guardian Contact:"));
        add(guardianContactField);
        add(new JLabel("Admission Fee:"));
        add(feeField);
        add(new JLabel("Admission Date (YYYY-MM-DD):"));
        add(admissionDateField);
        add(new JLabel(""));
        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get data from fields
                String name = nameField.getText();
                String fatherName = fatherNameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String studentClass = classField.getText();
                String prevSchool = prevSchoolField.getText();
                String guardianContact = guardianContactField.getText();
                double fee = Double.parseDouble(feeField.getText());
                String admissionDate = admissionDateField.getText();

                // Create student object
                Student student = new Student(name, fatherName, age, studentClass, prevSchool, guardianContact, fee, admissionDate);

                // Insert into DB
//                boolean success = StudentDAO.insertStudent(student);
//                if (success) {
//                    JOptionPane.showMessageDialog(null, "✅ Student registered successfully!");
//                } else {
//                    JOptionPane.showMessageDialog(null, "❌ Failed to register student.");
//                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new RegisterStudentForm();
    }
}
