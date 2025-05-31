// ui/ResetPasswordFrame.java
package ui;

import dao.PasswordResetDAO;
import dao.TeacherDAO;
import dao.UserDAO;
import db.DBConnection;
import theme.UITheme;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class ResetPasswordFrame extends JFrame {
    private JTextField emailField;
    private JTextField tokenField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton resetButton;

    public ResetPasswordFrame() {
        setTitle("Reset Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        initComponents();
        UITheme.applyFrameDefaults(this);
    }

    private void initComponents() {
        emailField = new JTextField(20);
        tokenField = new JTextField(36);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        resetButton = new JButton("Reset Password");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Email:"));             panel.add(emailField);
        panel.add(new JLabel("6-Digit Code:"));       panel.add(tokenField);
        panel.add(new JLabel("New Password:"));      panel.add(newPasswordField);
        panel.add(new JLabel("Confirm Password:"));  panel.add(confirmPasswordField);
        panel.add(new JLabel());                     panel.add(resetButton);

        add(panel);
        resetButton.addActionListener(e -> handleReset());
    }

    private void handleReset() {
        String email = emailField.getText().trim();
        String token = tokenField.getText().trim();
        String newPass = new String(newPasswordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (email.isEmpty() || token.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            PasswordResetDAO resetDAO = new PasswordResetDAO(conn);

            if (!resetDAO.isTokenValid(email, token)) {
                JOptionPane.showMessageDialog(this, "Invalid or expired 6-digit code.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Token valid → update password & delete token
            String username = TeacherDAO.getUsernameByEmail(email);
            if (username == null) {
                JOptionPane.showMessageDialog(this, "No user found with that email.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            UserDAO.updatePassword(username, newPass);

            resetDAO.deleteToken(email, token);

            JOptionPane.showMessageDialog(this,
                    "Password has been reset successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error resetting password: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResetPasswordFrame().setVisible(true));
    }
}
