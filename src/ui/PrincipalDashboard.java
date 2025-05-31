package ui;

import theme.UITheme;

import javax.swing.*;

public class PrincipalDashboard extends JFrame {
    public PrincipalDashboard() {
        setTitle("Principal Dashboard");
        UITheme.applyFrameDefaults(this);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

}
