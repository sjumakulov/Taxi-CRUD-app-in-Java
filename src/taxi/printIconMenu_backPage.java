package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import static taxi.printPopup.showPrintPopup;

public class printIconMenu_backPage {

    public static void showMenuPrintCalendarForBackPage(MouseEvent e) {
        JComponent iconLabel = (JComponent) e.getSource();

        String[] months = {"Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ной", "Дек"};
        Calendar cal = new GregorianCalendar();
        int currentMonth = cal.get(Calendar.MONTH);

        JPopupMenu menu = new JPopupMenu();
        menu.removeAll();
        menu.setLayout(new GridBagLayout());
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menu.setBorder(BorderFactory.createLineBorder(Color.gray));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);

        int monthsCount = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                JLabel monthName = new JLabel(months[monthsCount], SwingConstants.CENTER);

                JMenuItem item = new JMenuItem();
                item.setPreferredSize(new Dimension(30, 30));
                item.setLayout(new BorderLayout());
                item.setName(String.valueOf(monthsCount));
                item.setBackground(Color.white);
                item.setOpaque(true);
                item.add(monthName, BorderLayout.CENTER);

                if (monthsCount == currentMonth) {
                    monthName.setBorder(BorderFactory.createLineBorder(Color.decode("#4285F4")));
                }

                gbc.gridx = x;
                gbc.gridy = y;
                menu.add(item, gbc);
                monthsCount++;

                item.addActionListener((ActionEvent calendarEvent) -> {
                    JMenuItem monthItem = (JMenuItem) calendarEvent.getSource();
                    showPrintPopup(e, monthItem.getName());

                });

            }
        }

        menu.show(iconLabel, e.getX() - 130, e.getY());
    }

}
