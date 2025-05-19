package ui;

import dao.TeacherDAO;
import dao.UserDAO;
import models.Teacher;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPage extends JFrame {
    private JComboBox<String> roleComboBox;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        setTitle("School Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel roleLabel = new JLabel("Login as:");
        roleComboBox = new JComboBox<>(new String[]{"Principal", "Vice Principal", "Teacher"});

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        loginButton = new JButton("Login");

        // Layout
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(roleLabel, gbc);

        gbc.gridx = 1;
        add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(loginButton, gbc);

        // Event Handling
        loginButton.addActionListener(this::onLoginClick);
    }

    private void onLoginClick(ActionEvent e) {
        String role = (String) roleComboBox.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        User user = UserDAO.validateLogin(username, password, role);

        if (user != null) {
            switch (role) {
                case "Principal" -> {
                    JOptionPane.showMessageDialog(this, "Principal login successful!");
                    new PrincipalDashboard().setVisible(true);
                }
                case "Vice Principal" -> {
                    JOptionPane.showMessageDialog(this, "Vice Principal login successful!");
                    new VPDashboard().setVisible(true);
                }
                case "Teacher" -> {
                    Teacher teacher = TeacherDAO.getTeacherByUsername(username);
                    if (teacher != null) {
                        JOptionPane.showMessageDialog(this, "Teacher login successful!");
                        new TeacherDashboard(teacher).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Teacher details not found in teachers table.");
                        return;
                    }
                }
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username, password, or role.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
