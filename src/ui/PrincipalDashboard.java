package ui;

import dao.ClassDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import dao.WorkerDAO;
import theme.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



public class PrincipalDashboard extends JFrame {
    private final Map<String, String> iconMap = new HashMap<>() {{
        put("Notifications", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Notification.png");
        put("Approvals", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Approvals.png");
        put("Financial Record", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Financial Record (2).png");
    }};

    public PrincipalDashboard() {
        setTitle("Principal Dashboard");
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.LIGHT_GRAY);
        UITheme.applyFrameDefaults(this);

        JLabel header = UITheme.createTitleLabel("Principal Dashboard");
        JPanel headerPanel = UITheme.createHeaderPanel(header);
        add(headerPanel, BorderLayout.NORTH);

        JPanel statPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        statPanel.setBackground(UITheme.LIGHT_GRAY);
        statPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30));

        statPanel.add(createStatCard("Total Students", StudentDAO.getAllStudents().size()));
        statPanel.add(createStatCard("Total Teachers", TeacherDAO.getAllTeachers().size()));
        statPanel.add(createStatCard("Total Classes", ClassDAO.getAllClasses().size()));
        statPanel.add(createStatCard("Total Workers", getWorkerCount()));
        statPanel.add(createClickableCard("Notifications", "Send or View important messages and alerts"));
        statPanel.add(createClickableCard("Approvals", "Approve leaves, new entries or fee waivers"));
        statPanel.add(createClickableCard("Financial Record", "Manage income, expenses, and fee records"));

        add(statPanel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        UITheme.stylePrimaryButton(logoutButton);
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.LIGHT_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String title, int count) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(UITheme.getRoundedOrangeBorder());
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setPreferredSize(new Dimension(220, 140));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(66, 66, 66));

        JLabel countLabel = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        countLabel.setForeground(new Color(255, 87, 34));

        JLabel infoLabel = new JLabel("For more details", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);

        JButton openButton = new JButton("Open");
        UITheme.stylePrimaryButton(openButton);
        openButton.setPreferredSize(new Dimension(90, 30));
        openButton.addActionListener(e -> handleCardClick(title));

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.add(infoLabel);
        bottomPanel.add(openButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(countLabel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(255, 243, 233));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(UITheme.WHITE);
            }
        });

        return panel;
    }

    private JPanel createClickableCard(String title, String description) {
        JLabel iconLabel = new JLabel();
        String iconPath = iconMap.get(title);
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledIcon));
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descLabel = new JLabel("<html><div style='text-align:center;'><b>" + title + "</b><br>" + description + "</div></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton openButton = new JButton("Open");
        UITheme.stylePrimaryButton(openButton);
        openButton.setPreferredSize(new Dimension(90, 30));
        openButton.addActionListener(e -> handleCardClick(title));

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(220, 160));
        card.setBackground(UITheme.WHITE);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(UITheme.getRoundedOrangeBorder());

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(255, 243, 233));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(UITheme.WHITE);
            }
        });
        return card;
    }

    private void handleCardClick(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(this);

        switch (title) {
            case "Notifications" -> JOptionPane.showMessageDialog(this, "Notifications clicked!");
            case "Approvals" -> JOptionPane.showMessageDialog(this, "Approvals clicked!");
            case "Financial Record" -> JOptionPane.showMessageDialog(this, "Financial Record clicked!");
            case "Total Students" -> {
                SchoolDetailsPanel panel = new SchoolDetailsPanel();
                panel.setSelectedTab("Students");
                frame.setContentPane(panel);
                frame.setVisible(true);
            }
            case "Total Teachers" -> {
                SchoolDetailsPanel panel = new SchoolDetailsPanel();
                panel.setSelectedTab("Teachers");
                frame.setContentPane(panel);
                frame.setVisible(true);
            }
            case "Total Workers" -> {
                SchoolDetailsPanel panel = new SchoolDetailsPanel();
                panel.setSelectedTab("Workers");
                frame.setContentPane(panel);
                frame.setVisible(true);
            }
            case "Total Classes" -> {
                SchoolDetailsPanel panel = new SchoolDetailsPanel();
                panel.setSelectedTab("Classes");
                frame.setContentPane(panel);
                frame.setVisible(true);
            }
            default -> JOptionPane.showMessageDialog(this, title + " clicked!");
        }
    }

    private int getWorkerCount() {
        // Replace with actual worker count logic whenever I will make
        return 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrincipalDashboard().setVisible(true));
    }
}
