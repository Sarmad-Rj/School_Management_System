// ui/LoginPage.java
package ui;

import dao.UserDAO;
import dao.TeacherDAO;
import db.DBConnection;
import email.EmailService;
import models.Teacher;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class LoginPage extends JFrame {
    private JComboBox<String> roleComboBox;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotButton;

    public LoginPage() {
        setTitle("School Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Login as:"), gbc);
        gbc.gridx = 1;
        roleComboBox = new JComboBox<>(new String[]{"Principal", "Vice Principal", "Teacher"});
        add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Login button
        gbc.gridx = 1; gbc.gridy = 3;
        loginButton = new JButton("Login");
        add(loginButton, gbc);

        // Forgot Password button
        gbc.gridy = 4;
        forgotButton = new JButton("Forgot Password?");
        forgotButton.setBorderPainted(false);
        forgotButton.setContentAreaFilled(false);
        forgotButton.setForeground(Color.BLUE.darker());
        forgotButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(forgotButton, gbc);

        // Event handlers
        loginButton.addActionListener(this::onLoginClick);
        forgotButton.addActionListener(e -> onForgotPassword());
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
                        JOptionPane.showMessageDialog(this, "Teacher details not found.");
                        return;
                    }
                }
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username, password, or role.");
        }
    }

    private void onForgotPassword() {
        String email = JOptionPane.showInputDialog(
                this,
                "Enter your registered email:",
                "Password Reset",
                JOptionPane.PLAIN_MESSAGE
        );
        if (email == null || email.trim().isEmpty()) return;

        String token = String.format("%06d", (int)(Math.random() * 1_000_000));
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO password_resets(email,token,expiry_time) VALUES (?,?,?)"
             )) {
            ps.setString(1, email);
            ps.setString(2, token);
            ps.setTimestamp(3, Timestamp.valueOf(expiry));
            ps.executeUpdate();

            EmailService.sendResetEmail(email, token);

            JOptionPane.showMessageDialog(
                    this,
                    "A reset 6-digit code has been sent to your email.\n" +
                            "Please check your inbox and then enter it in the next screen.",
                    "6-digit code Sent",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new ResetPasswordFrame().setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to send reset 6-digit code: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
