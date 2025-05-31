package ui;
import javax.swing.*;
import java.awt.*;

public class GlobalButtonStyle {
    public static void applyRoundedStyle() {
        UIManager.put("Button.background", new Color(100, 149, 237)); // Background color
        UIManager.put("Button.foreground", Color.WHITE); // Text color
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 16));
        UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding for rounded effect
    }
}