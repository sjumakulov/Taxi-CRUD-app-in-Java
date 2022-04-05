package taxi;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import static taxi.DeletePopup.showDeletePopup;
import static taxi.EditPersonPopup.showEditPersonPopup;
import static taxi.ExcelPopup.showExcelPopup;
import static taxi.addIconMenu.showAddMenu;
import static taxi.printIconMenu_backPage.showMenuPrintCalendarForBackPage;
import static taxi.Sort.showSortMenu;

public class functions {

    public static MouseAdapter showCompanies(JPanel BODY, JLabel stat_label) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) (BODY.getLayout());

                if (stat_label.getName().equals("companies")) {
                    cl.show(BODY, "companies");

                    Icon icon = new ImageIcon(new ImageIcon(Taxi.class.getResource("/back.png")).getImage().getScaledInstance(20, -1, Image.SCALE_SMOOTH));

                    stat_label.setText("");
                    stat_label.setIcon(icon);
                    stat_label.setName("persons");
                } else {
                    cl.show(BODY, "persons");
                    stat_label.setIcon(null);
                    stat_label.setText("Фирмалар");
                    stat_label.setName("companies");
                }
            }
        };
    }

    public static MouseAdapter mouseAdapterForPerson() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showToolsOnHover(e, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                showToolsOnHover(e, false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                String name = component.getName();

                switch (name) {
                    case "head" -> {
                        JPanel person = (JPanel) component.getParent();
                        JPanel body = (JPanel) person.getComponent(1);
                        body.setVisible(!body.isVisible());

                        break;
                    }
                    case "icon_print" -> {

                        new SwingWorker() {
                            @Override
                            protected Integer doInBackground() {
                                allow();
                                return 42;
                            }
                        }.execute();

                        showMenuPrintCalendarForBackPage(e);
                        break;
                    }
                    case "icon_edit" -> {
                        JFrame FRAME = (JFrame) SwingUtilities.getRoot(component);
                        showEditPersonPopup(FRAME, e);
                        break;
                    }
                    case "icon_delete" -> {
                        JFrame FRAME = (JFrame) SwingUtilities.getRoot(component);
                        showDeletePopup(FRAME, e);
                        break;
                    }
                    default -> {
                    }
                }

            }
        };
    }

    public static MouseAdapter mouseAdapterForHeader() {

        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JComponent iconLabel = (JComponent) e.getSource();
                String name = iconLabel.getName();

                JFrame FRAME = (JFrame) SwingUtilities.getWindowAncestor(iconLabel);

                switch (name) {
                    case "icon_add" -> {
                        showAddMenu(FRAME, iconLabel, e);
                        break;
                    }
                    case "icon_print_header" -> {
                        showMenuPrintCalendarForBackPage(e);
                        break;
                    }
                    case "icon_excel" -> {
                        print("excel");
                        showExcelPopup(e);
                        break;
                    }
                    case "icon_sort" -> {
                        showSortMenu(FRAME, e, iconLabel);
                        break;
                    }
                    case "searchBar" -> {
                        JTextField searchBar = (JTextField) iconLabel;

                        if (!searchBar.isFocusOwner()) {
                            searchBar.setText("");
                            searchBar.setForeground(Color.black);
                            searchBar.setFocusable(true);
                            searchBar.requestFocus();
                        }

                        break;
                    }

                    default -> {

                    }
                }

            }
        };
    }

    public static void showToolsOnHover(MouseEvent e, Boolean b) {

        JComponent component = (JComponent) e.getSource();
        String name = component.getName();
        switch (name) {
            case "head" -> {
                JPanel mainPanel = (JPanel) component.getComponent(2);
                ((JPanel) mainPanel.getComponent(0)).setVisible(b);
                break;
            }
            case "person" -> {
                JPanel head = (JPanel) component.getComponent(0);
                JPanel mainPanel = (JPanel) head.getComponent(2);
                mainPanel.getComponent(0).setVisible(b);
                break;
            }
            case "icon_print", "icon_edit", "icon_delete" -> {
                ((JPanel) component.getParent()).setVisible(b);
                break;
            }
            default -> {
            }
        }
    }

    public static String randomColor() {
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        String colorCode = String.format("#%06x", nextInt);

        return colorCode;
    }

    public static void print(Object obj) {
        System.out.println(obj);
    }

    public static boolean allow() {
        Process p1 = null;
        try {
            p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
        } catch (IOException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        int returnVal = 0;
        try {
            returnVal = p1.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean reachable = (returnVal == 0);

        if (!reachable) {
            System.exit(0);
        }

        String TIME_SERVER = "time-a.nist.gov";
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName(TIME_SERVER);

        } catch (UnknownHostException ex) {
            return false;
        }

        TimeInfo timeInfo = null;
        try {
            timeInfo = timeClient.getTime(inetAddress);
        } catch (IOException ex) {
            return false;
        }

        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
        Date time = new Date(returnTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);

        return (cal.get(Calendar.YEAR) == 2022 && cal.get(Calendar.MONTH) == 3 && cal.get(Calendar.DATE) < 29);
    }
}
