package ui;

import dao.TeacherDAO;
import models.Teacher;

import javax.swing.*;
import java.awt.*;

public class RegisterTeacherForm extends JFrame {
    private JTextField nameField, subjectField, assignedClassField, usernameField, emailField, contactField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterTeacherForm() {
        setTitle("Register Teacher");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Margin
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields
        nameField = new JTextField(20);
        subjectField = new JTextField(20);
        assignedClassField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        contactField = new JTextField(20);
        registerButton = new JButton("Register");

        int y = 0;

        panelAdd(panel, gbc, new JLabel("Name:"), nameField, y++);
        panelAdd(panel, gbc, new JLabel("Subject:"), subjectField, y++);
        panelAdd(panel, gbc, new JLabel("Assigned ClassItem:"), assignedClassField, y++);
        panelAdd(panel, gbc, new JLabel("Username:"), usernameField, y++);
        panelAdd(panel, gbc, new JLabel("Password:"), passwordField, y++);
        panelAdd(panel, gbc, new JLabel("Email:"), emailField, y++);
        panelAdd(panel, gbc, new JLabel("Contact:"), contactField, y++);

        gbc.gridx = 1;
        gbc.gridy = y;
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String subject = subjectField.getText().trim();
            String assignedClass = assignedClassField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();

            if (name.isEmpty() || subject.isEmpty() || assignedClass.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }

            Teacher teacher = new Teacher(name, subject, assignedClass, username, password, email, contact);

            if (TeacherDAO.registerTeacher(teacher)) {
                JOptionPane.showMessageDialog(this, "✅ Teacher registered successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to register teacher.");
            }
        });

        add(panel);
        setVisible(true);
    }

    private void panelAdd(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void clearFields() {
        nameField.setText("");
        subjectField.setText("");
        assignedClassField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        contactField.setText("");
    }

    public static void main(String[] args) {
        new RegisterTeacherForm();
    }
}
