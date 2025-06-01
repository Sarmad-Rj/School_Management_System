package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import theme.UITheme;

public class SidebarMenuPanel extends JPanel {

    public SidebarMenuPanel(ActionListener listener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new UITheme().ORANGE);


        add(Box.createVerticalStrut(30)); // Padding at top
        add(createSidebarCard("Income", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Income.png", listener));
        add(Box.createVerticalStrut(10));
        add(createSidebarCard("Expenses", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Expenses.png", listener));
        add(Box.createVerticalStrut(10));
        add(createSidebarCard("Back", "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\Back.png", listener));

    }

    private JPanel createSidebarCard(String text, String iconPath, ActionListener listener) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setMaximumSize(new Dimension(180, 35));
        panel.setBackground(new Color(244, 81, 30)); // button orange
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Icon
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            System.out.println("⚠️ Icon not found: " + iconPath);
        }

        // Text
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        textLabel.setForeground(Color.WHITE);

        // Click behavior
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
                listener.actionPerformed(actionEvent);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(255, 102, 51)); // hover effect
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(244, 81, 30));
            }
        });

        panel.add(iconLabel);
        panel.add(textLabel);
        return panel;
    }

}
