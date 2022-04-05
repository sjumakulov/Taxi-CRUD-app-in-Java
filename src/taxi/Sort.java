package taxi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

public class Sort {

    public static void showSortMenu(JFrame FRAME, MouseEvent e, JComponent iconLabel) {
        JPopupMenu menu = new JPopupMenu();
        menu.removeAll();
        menu.setBorder(BorderFactory.createLineBorder(Color.gray));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        menu.add(item("Лицензия муддати", 9, FRAME));
        menu.add(item("Тех. курик муддат", 10, FRAME));
        menu.add(item("Ижара шартнома муддати", 11, FRAME));
        menu.add(item("Полис муддати", 12, FRAME));
        menu.add(item("Газ балон муддат", 13, FRAME));
        menu.add(item("Тиббий кўрик муддати", 14, FRAME));
        menu.add(item("Меҳнат шартнома муддати", 15, FRAME));

        menu.show(iconLabel, e.getX() - 205, e.getY());
    }

    private static JMenuItem item(String val, int index, JFrame FRAME) {
        JMenuItem item = new JMenuItem(val);
        item.setBackground(Color.white);
        item.setOpaque(true);

        item.addActionListener((ActionEvent e1) -> {
            sort(FRAME, index);
        });

        return item;
    }

    private static void sort(JFrame FRAME, int index) {
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);

        JScrollPane personsScrollPane = (JScrollPane) BODY.getComponent(0);
        JPanel panelForScrollPanel = (JPanel) personsScrollPane.getViewport().getComponent(0);
        JPanel persons = (JPanel) panelForScrollPanel.getComponent(0);

        Comparator comparator = (Comparator<JPanel>) (JPanel p1, JPanel p2) -> {
            JPanel body = (JPanel) p1.getComponent(1);
            String date1 = ((JLabel) ((JPanel) body.getComponent(index)).getComponent(1)).getText();

            JPanel body2 = (JPanel) p2.getComponent(1);
            String date2 = ((JLabel) ((JPanel) body2.getComponent(index)).getComponent(1)).getText();

            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            try {

                if (!date1.isBlank()) {
                    cal1.setTime(f.parse(date1));
                }

                if (!date2.isBlank()) {
                    cal2.setTime(f.parse(date2));
                }

            } catch (ParseException ex) {
                Logger.getLogger(Sort.class.getName()).log(Level.SEVERE, null, ex);
            }
            return cal1.compareTo(cal2);
        };

        ArrayList<Component> personComponents = new ArrayList<>();
        personComponents.addAll(Arrays.asList(persons.getComponents()));

        Collections.sort(personComponents, comparator);

        persons.removeAll();
        for (Object person : personComponents) {
            persons.add((JPanel) person);
        }
        persons.revalidate();
        persons.repaint();
    }
}
