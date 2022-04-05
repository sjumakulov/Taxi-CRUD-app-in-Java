package taxi;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import static taxi.CompanyPopup.showCompanyPopup;
import static taxi.PersonPopup.showPersonPopup;
import static taxi.readwrite.getData;

public class addIconMenu {

    public static void showAddMenu(JFrame FRAME, JComponent iconLabel, MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();
        menu.removeAll();
        menu.setBorder(BorderFactory.createLineBorder(Color.gray));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JMenuItem personMenuItem = new JMenuItem("+ Хайдовчи");
        JMenuItem companyMenuItem = new JMenuItem("+ Фирма");
        personMenuItem.setBackground(Color.white);
        companyMenuItem.setBackground(Color.white);
        personMenuItem.setOpaque(true);
        companyMenuItem.setOpaque(true);
                
                
        personMenuItem.addActionListener((ActionEvent e1) -> {
            showPersonPopup(FRAME);
        });
        companyMenuItem.addActionListener((ActionEvent e1) -> {
            showCompanyPopup(FRAME);
        });

        Map companies = (Map) getData("companies");
        if (!companies.isEmpty()) {
            menu.add(personMenuItem);
        }

        menu.add(companyMenuItem);
        menu.show(iconLabel, e.getX() - 120, e.getY());

    }
}
