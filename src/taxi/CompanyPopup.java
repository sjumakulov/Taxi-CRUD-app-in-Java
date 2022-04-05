package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static taxi.addFunctions.saveCompany;
import static taxi.functions.randomColor;

public class CompanyPopup {

    public static void showCompanyPopup(JFrame FRAME) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        companyDialogPopup(FRAME);
        glassPane.setVisible(false);
    }

    public static JDialog companyDialogPopup(JFrame FRAME) {
        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new GridBagLayout());
        jd.setMinimumSize(new Dimension(450, 180));
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);

        jd.setUndecorated(true);
        jd.getContentPane().setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        jd.add(stringDataEntry("Фирма номи:"), gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        jd.add(button("Бекор қилиш", "cancel", jd, null), gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        jd.add(button("Сақлаш", "save", jd, FRAME), gbc);

        jd.setVisible(true);

        return jd;
    }

    public static JPanel stringDataEntry(String dataLabel) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setBorder(null);
        textField.setBackground(Color.decode("#e6e6e6"));
        textField.setFont(new Font(null, Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(Color.decode(randomColor()));
        colorPanel.setPreferredSize(new Dimension(30, 30));
        colorPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        colorPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                colorPanel.setBackground(Color.decode(randomColor()));
            }
        });

        JPanel textFieldColorContainer = new JPanel(new BorderLayout());
        textFieldColorContainer.setPreferredSize(new Dimension(230, 30));
        textFieldColorContainer.add(textField, BorderLayout.CENTER);
        textFieldColorContainer.add(colorPanel, BorderLayout.LINE_END);

        container.add(label, BorderLayout.PAGE_START);
        container.add(textFieldColorContainer, BorderLayout.CENTER);

        return container;
    }

    private static JButton button(String label, String name, JDialog jd, JFrame FRAME) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setPreferredSize(new Dimension(190, 35));
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.BOLD, 16));

        if (name.equals("cancel")) {
            button.setBackground(Color.decode("#DB4437"));
            button.setForeground(Color.white);
        } else {
            button.setBackground(Color.decode("#4285F4"));
            button.setForeground(Color.white);
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (name.equals("cancel")) {
                    button.setBackground(Color.decode("#df5549"));
                } else {
                    button.setBackground(Color.decode("#4f8ef5"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (name.equals("cancel")) {
                    button.setBackground(Color.decode("#DB4437"));
                } else {
                    button.setBackground(Color.decode("#4285F4"));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (name.equals("cancel")) {
                    jd.dispose();
                } else {
                    saveCompany(jd, FRAME);
                }
            }
        });

        return button;
    }

}
