package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UITheme {

    public static final Color ORANGE = new Color(255, 87, 34);
    public static final Color DARK_GRAY = new Color(25, 25, 25);
    public static final Color WHITE = Color.WHITE;
    public static final Color LIGHT_GRAY = new Color(245, 245, 245);
    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 16);

    public static void stylePrimaryButton(JButton button) {
        button.setBackground(ORANGE);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(ORANGE);
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        return label;
    }

    public static JPanel createHeaderPanel(JComponent centerComponent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        panel.add(centerComponent, BorderLayout.CENTER);
        return panel;
    }

    public static Border getRoundedOrangeBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ORANGE, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
}
