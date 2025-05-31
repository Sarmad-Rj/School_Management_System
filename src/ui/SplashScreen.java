package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SplashScreen extends JFrame {

    public SplashScreen() {
        setUndecorated(true);
        setSize(700, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

        // Logo (Center)
        try {
            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\HP\\OneDrive\\Desktop\\School_Management_System\\images\\logo Bright Future School.png"));
            Image scaledImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(logoLabel, BorderLayout.CENTER);
        } catch (IOException e) {
            JLabel error = new JLabel("Logo not found", SwingConstants.CENTER);
            error.setForeground(Color.RED);
            add(error, BorderLayout.CENTER);
        }
    }

    public static void showSplashThenLogin() {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

//        Timer timer = new Timer(3000, e -> {
//            splash.dispose();
//            SwingUtilities.invokeLater(() -> {
//                LoginPage login = new LoginPage();
//                login.setVisible(true);
//            });
//        });
        Timer timer = new Timer(3000, e -> {
            splash.dispose();
            new LoginPage().setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::showSplashThenLogin);
    }
}
