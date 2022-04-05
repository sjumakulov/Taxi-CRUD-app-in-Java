package taxi;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import static taxi.functions.print;

public class excelIconMenu {
    
    
    public static void showMenuForExcelIcon(JComponent iconLabel, MouseEvent e){
        JPopupMenu menu = new JPopupMenu();
                        menu.removeAll();

                        JMenuItem item = new JMenuItem("Барчаси");
                        JMenuItem item2 = new JMenuItem("Асосий Шоферлар");
                        item.setBackground(Color.white);
                        item2.setBackground(Color.white);

                        item.addActionListener((ActionEvent e1) -> {
                            print("getting All");
//                            showPopup(FRAME);
                        });
                        item2.addActionListener((ActionEvent e1) -> {
                            print("getting Main Drivers");
//                            showPopup(FRAME);
                        });

                        menu.add(item);
                        menu.add(item2);
                        menu.show(iconLabel, e.getX() - 130, e.getY());
    }
    
}
