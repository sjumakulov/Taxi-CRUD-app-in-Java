package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import static taxi.deleteFunctions.deletePerson;

public class DeletePopup {

    public static void showDeletePopup(JFrame FRAME, MouseEvent e) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        deleteDialogPopup(FRAME, e);
        glassPane.setVisible(false);
    }

    public static JDialog deleteDialogPopup(JFrame FRAME, MouseEvent eve) {
        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new BorderLayout());
        jd.setMinimumSize(new Dimension(370, 140));
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);
        jd.setUndecorated(true);
        jd.getContentPane().setBackground(Color.white);
        jd.add(warningText(), BorderLayout.PAGE_START);
        jd.add(buttonContainer(jd, eve), BorderLayout.CENTER);
        jd.setVisible(true);

        return jd;
    }

    private static JPanel warningText() {
        JLabel label1 = new JLabel("Ростдан ҳам бу маълумотни ўчиришни", JLabel.CENTER);
        JLabel label2 = new JLabel("ҳохлайсизми?", JLabel.CENTER);

        label1.setFont(new Font(null, Font.BOLD, 16));
        label1.setForeground(Color.decode("#696969"));
        label2.setFont(new Font(null, Font.BOLD, 16));
        label2.setForeground(Color.decode("#696969"));

        JPanel container = new JPanel(new GridLayout(2, 1));
        container.setBackground(Color.white);
        container.setBorder(BorderFactory.createEmptyBorder(15, 5, 25, 5));

        container.add(label1);
        container.add(label2);

        return container;
    }

    private static JPanel buttonContainer(JDialog jd, MouseEvent eve) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        container.setBackground(Color.white);

        container.add(button("Йўқ", "no", jd, eve));
        container.add(button("Ха", "yes", jd, eve));

        return container;
    }

    private static JPanel button(String label, String name, JDialog jd, MouseEvent eve) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.BOLD, 16));

        JPanel bcon = new JPanel(new BorderLayout());
        bcon.setPreferredSize(new Dimension(135, 35));
        bcon.add(button);
        
        if (name.equals("no")) {
            button.setBackground(Color.decode("#DB4437"));
            button.setForeground(Color.white);
        } else {
            button.setBackground(Color.white);
            button.setForeground(Color.decode("#4285F4"));
            bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (name.equals("no")) {
                    button.setBackground(Color.decode("#df5549"));

                } else {
                    button.setBackground(Color.decode("#f7faff"));
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#d6e5ff")));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (name.equals("no")) {
                    button.setBackground(Color.decode("#DB4437"));

                } else {
                    button.setBackground(Color.white);
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (name.equals("no")) {
                    jd.dispose();
                } else {
                    jd.dispose();

                    new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            deletePerson(eve);
                            return 42;
                        }

                    }.execute();

                }
            }
        });

        return bcon;
    }
}
