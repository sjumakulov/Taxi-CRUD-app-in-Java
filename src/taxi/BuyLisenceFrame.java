package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BuyLisenceFrame {
    
    public static void buyLisenceFrame() {
        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(Taxi.class.getResource("/taxi-icon.png"));
        
        javax.swing.JTextField tField = new javax.swing.JTextField();
        tField.setPreferredSize(new Dimension(250, 30));
        tField.setBorder(BorderFactory.createLineBorder(Color.decode("#dbdbdb")));
        tField.setFont(new Font("Verdana", Font.PLAIN, 17));
        
        JLabel label = new JLabel("Litsenziya sotib olish uchun tele-");
        label.setFont(new Font("Verdana", Font.PLAIN, 14));
        label.setForeground(Color.gray);
        label.setPreferredSize(new Dimension(250, 20));
        
        JLabel label2 = new JLabel("gram manzilingizni kiriting:");
        label2.setFont(new Font("Verdana", Font.PLAIN, 14));
        label2.setForeground(Color.gray);
        label2.setPreferredSize(new Dimension(250, 20));
        
        javax.swing.JFrame FRAME = new javax.swing.JFrame();
        FRAME.setIconImage(icon.getImage());
        FRAME.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        FRAME.setUndecorated(true);
        FRAME.setPreferredSize(new Dimension(350, 140));
        
        FRAME.getContentPane().setBackground(Color.white);
        
        JButton button = new JButton("Yuborish");
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.BOLD, 16));
        button.setBackground(Color.decode("#4285F4"));
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(250, 35));
        button.addMouseListener(ML(button, tField, FRAME));
        
        FRAME.getContentPane().setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(0, 0, 0, 12);
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        FRAME.getContentPane().add(x(FRAME), gbc);
        
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;
        FRAME.getContentPane().add(label, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        FRAME.getContentPane().add(label2, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        FRAME.getContentPane().add(tField, gbc);
        
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 4;
        FRAME.getContentPane().add(button, gbc);
        
        FRAME.pack();
        FRAME.setVisible(true);
        FRAME.setLocationRelativeTo(null);
    }
    
    private static java.awt.event.MouseAdapter ML(JButton button, javax.swing.JTextField tField, javax.swing.JFrame FRAME) {
        return new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.decode("#4f8ef5"));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.decode("#4285F4"));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                String telegramAddress = tField.getText();
                telegramAddress = telegramAddress.trim();
                if (telegramAddress.isBlank()) {
                    tField.setBorder(BorderFactory.createLineBorder(Color.red));
                } else {
                    if (telegramAddress.length() > 25) {
                        telegramAddress = telegramAddress.substring(0, 25);
                    }
                    
                    FRAME.dispose();
                    taxi.EmailSender.sendEmail(telegramAddress);
                }
                
            }
        };
    }
    
    private static JPanel x(javax.swing.JFrame FRAME) {
        JButton button = new JButton("x");
        button.setFont(new Font("Verdana", Font.PLAIN, 16));
        button.setForeground(Color.decode("#d4d4d4"));
        button.setBackground(Color.white);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.setBorder(null);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.red);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.decode("#d4d4d4"));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                FRAME.dispose();
            }
        });
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(button, BorderLayout.CENTER);
        
        return container;
    }
}
