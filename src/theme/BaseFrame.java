package theme;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {
    public BaseFrame(String title) {
        setTitle(title);
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set the application icon
        ImageIcon icon = new ImageIcon("C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\logo Bright Future School.png");
        setIconImage(icon.getImage());

        // Apply theme background
        getContentPane().setBackground(UITheme.LIGHT_GRAY);
    }
}