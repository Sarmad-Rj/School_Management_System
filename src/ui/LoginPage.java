package ui;

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
        roleComboBox.addActionListener(this::onRoleChange);
        loginButton.addActionListener(this::onLoginClick);

        onRoleChange(null); // Set initial visibility based on selected role
    }

    private void onRoleChange(ActionEvent e) {
        String role = (String) roleComboBox.getSelectedItem();
        usernameField.setEnabled(role.equals("Teacher"));
    }

    private void onLoginClick(ActionEvent e) {
        String role = (String) roleComboBox.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        switch (role) {
            case "Principal":
                if ("PClouds".equals(password)) {
                    JOptionPane.showMessageDialog(this, "Principal login successful!");
                    // open principal dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid password for Principal.");
                }
                break;
            case "Vice Principal":
                if ("VPClouds".equals(password)) {
                    JOptionPane.showMessageDialog(this, "VP login successful!");
                    new VPDashboard().setVisible(true);
                    this.dispose(); // close login page
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid password for VP.");
                }
                break;
            case "Teacher":
                // TODO: validate from DB later
                if (!username.isEmpty() && !password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Teacher login successful!");
                    // open teacher dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials for Teacher.");
                }
                break;
        }
    }
}
