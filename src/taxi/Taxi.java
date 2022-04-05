package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Taxi {

    public static void main(String[] args) {

        Runnable GUITask = () -> {
            window();
        };
        SwingUtilities.invokeLater(GUITask);
    }

    public static void window() {

        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(Taxi.class.getResource("/taxi-icon.png"));

        JLabel contactInfo = new JLabel("Telegram: @sjumakulov | Instagram: @sjumakuloff");
        contactInfo.setForeground(Color.white);
        contactInfo.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        javax.swing.JFrame loadingFrame = new javax.swing.JFrame();
        loadingFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        loadingFrame.setPreferredSize(new Dimension(400, 100));
        loadingFrame.setUndecorated(true);
        loadingFrame.setIconImage(icon.getImage());
        loadingFrame.getContentPane().setLayout(new BorderLayout(5, 5));
        loadingFrame.getContentPane().setBackground(Color.decode("#f0bc00"));
        loadingFrame.getContentPane().add(contactInfo, BorderLayout.PAGE_END);

        loadingFrame.pack();
        loadingFrame.setVisible(true);
        loadingFrame.setLocationRelativeTo(null);

        taxi.Page.page(loadingFrame);

    }
}
