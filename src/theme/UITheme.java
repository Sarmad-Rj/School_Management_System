package theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class UITheme {

    public static final Color ORANGE = new Color(255, 87, 34);
    public static final Color DARK_GRAY = new Color(25, 25, 25);
    public static final Color WHITE = Color.WHITE;
    public static final Color LIGHT_GRAY = new Color(245, 245, 245);
    public static final Color SUCCESS = new Color(76, 175, 80);
    public static final Color DANGER = new Color(244, 67, 54);
    public static final Color WARNING = new Color(255, 193, 7);
    public static final Color DARK_BG = new Color(30, 30, 47);
    public static final Color CARD_BG = new Color(42, 42, 64);
    public static final Color DARK_TEXT = new Color(50, 50, 50);
    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 16);

    private static final String DEFAULT_ICON_PATH = "C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\our_logo_with_BG.jpg";

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

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(WHITE);
        comboBox.setForeground(Color.BLACK);
        comboBox.setFont(DEFAULT_FONT);
        comboBox.setBorder(BorderFactory.createLineBorder(ORANGE, 1));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setFont(DEFAULT_FONT);
                c.setForeground(Color.BLACK);
                c.setBackground(isSelected ? new Color(255, 233, 218) : WHITE); // light orange highlight
                return c;
            }
        });
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ORANGE);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 74, 25)); // darker orange
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ORANGE);
            }
        });

        return button;
    }

    public static Border getRoundedOrangeBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(getRoundedOrangeBorder(), title);
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        border.setTitleColor(ORANGE);
        return border;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UITheme.DEFAULT_FONT);
        label.setForeground(UITheme.DARK_TEXT);
        return label;
    }

    public static void applyFrameDefaults(JFrame frame) {
        frame.setIconImage(new ImageIcon(DEFAULT_ICON_PATH).getImage());
        frame.getContentPane().setBackground(UITheme.LIGHT_GRAY);
    }

    public static void addIconToPanel(JPanel panel, int width, int height) {
        ImageIcon originalImage = new ImageIcon(DEFAULT_ICON_PATH);
        Image scaledImage = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedImage = new ImageIcon(scaledImage);

        JLabel iconLabel = new JLabel(resizedImage);
        panel.add(iconLabel);
    }

    public static JLabel createIconLabel(String text, String iconPath, int size) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new JLabel(text, new ImageIcon(scaled), JLabel.LEFT);
    }

}
