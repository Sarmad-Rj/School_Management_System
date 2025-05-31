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

public class LoginPage extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleComboBox;
    private final JButton loginButton;
    private final JButton forgotButton;

    // Theme colors
    private final Color ORANGE = new Color(255, 87, 34); // deep orange
    private final Color BLACK = new Color(25, 25, 25);
    private final Color WHITE = Color.WHITE;

    public LoginPage() {
        setTitle("Bright Future School - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // === Left Image Panel ===
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(300, 450));
        leftPanel.setLayout(new BorderLayout());

        try {
            ImageIcon icon = new ImageIcon("C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\logo Bright Future School.png"); // update path if needed
            Image scaled = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            leftPanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel fallback = new JLabel("Logo", SwingConstants.CENTER);
            fallback.setFont(new Font("Arial", Font.BOLD, 28));
            fallback.setForeground(Color.GRAY);
            leftPanel.add(fallback, BorderLayout.CENTER);
        }

        add(leftPanel, BorderLayout.WEST);

        // === Right Login Form Panel ===
        JPanel rightPanel = new JPanel(new GridBagLayout());
//        rightPanel.setBackground(new Color(25, 25, 25)); // if i want the BLACK
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 0, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Welcome to Bright Future School");
        title.setFont(new Font("Poppins", Font.BOLD, 20));
        title.setForeground(new Color(255, 87, 34));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        rightPanel.add(title, gbc);
        gbc.gridwidth = 1;

        // Role
        JLabel roleLabel = createLabel("Role:");
        gbc.gridx = 0; gbc.gridy = 1;
        rightPanel.add(roleLabel, gbc);

        roleComboBox = new JComboBox<>(new String[]{"Principal", "Vice Principal", "Teacher"});
        styleField(roleComboBox);
        gbc.gridx = 1;
        rightPanel.add(roleComboBox, gbc);

        roleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                c.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                c.setForeground(Color.BLACK);
                c.setBackground(isSelected ? new Color(255, 87, 34) : Color.WHITE); // Orange on hover, white otherwise
                return c;
            }
        });


        // Username
        JLabel userLabel = createLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 2;
        rightPanel.add(userLabel, gbc);

        usernameField = new JTextField(15);
        styleField(usernameField);
        gbc.gridx = 1;
        rightPanel.add(usernameField, gbc);

        // Password
        JLabel passLabel = createLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 3;
        rightPanel.add(passLabel, gbc);

        passwordField = new JPasswordField(15);
        styleField(passwordField);
        gbc.gridx = 1;
        rightPanel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        styleButton(loginButton);
        gbc.gridx = 1; gbc.gridy = 4;
        rightPanel.add(loginButton, gbc);

        // Forgot button
        forgotButton = new JButton("Forgot Password?");
        forgotButton.setForeground(new Color(255, 87, 34));
        forgotButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        forgotButton.setContentAreaFilled(false);
        forgotButton.setBorderPainted(false);
        forgotButton.setFocusPainted(false);
        forgotButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        rightPanel.add(forgotButton, gbc);

        add(rightPanel, BorderLayout.CENTER);

        loginButton.addActionListener(this::onLoginClick);
        forgotButton.addActionListener(e -> onForgotPassword());
    }

//    private JLabel createLabel(String text) {
//        JLabel label = new JLabel(text);
//        label.setForeground(WHITE);
//        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        return label;
//    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.DARK_GRAY); // instead of WHITE
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }

//    private void styleField(JComponent comp) {
//        comp.setBackground(Color.DARK_GRAY);
//        comp.setForeground(WHITE);
//        comp.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        comp.setBorder(BorderFactory.createLineBorder(ORANGE, 1));
//    }

    private void styleField(JComponent comp) {
        comp.setBackground(Color.WHITE);
        comp.setForeground(Color.BLACK);
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comp.setBorder(BorderFactory.createLineBorder(ORANGE, 1));
    }

    private void styleButton(JButton button) {
        button.setBackground(ORANGE);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

        String token = String.format("%06d", (int) (Math.random() * 1_000_000));
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO password_resets(email, token, expiry_time) VALUES (?, ?, ?)"
             )) {
            ps.setString(1, email);
            ps.setString(2, token);
            ps.setTimestamp(3, Timestamp.valueOf(expiry));
            ps.executeUpdate();

            EmailService.sendResetEmail(email, token);

            JOptionPane.showMessageDialog(
                    this,
                    "A 6-digit reset code has been sent to your email.\n" +
                            "Please enter it in the next screen to reset your password.",
                    "Reset Code Sent",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new ResetPasswordFrame().setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to send reset code: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
