package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.filechooser.FileNameExtensionFilter;
import static taxi.MyScrollBarUI.myScrollBarUI;
import static taxi.readwrite.getData;
import static taxi.readwrite.writeToExcel;

public class ExcelPopup {

    public static void showExcelPopup(MouseEvent e) {
        JComponent excelIcon = (JComponent) e.getComponent();
        JFrame FRAME = (JFrame) SwingUtilities.getRoot(excelIcon);

        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        jDialog(FRAME);
        glassPane.setVisible(false);

    }

    public static JDialog jDialog(JFrame FRAME) {
        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new BorderLayout());
        jd.getContentPane().setBackground(Color.white);
        jd.setMinimumSize(new Dimension(700, 580));
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);
        jd.setUndecorated(true);

        JScrollPane controller = controller();
        jd.getContentPane().add(controller, BorderLayout.CENTER);
        jd.getContentPane().add(buttons(jd, controller), BorderLayout.PAGE_END);
        jd.setVisible(true);

        return jd;
    }

    private static JScrollPane controller() {
        Map companies = (Map) getData("companies");

        JPanel controller = new JPanel(new GridBagLayout());
        controller.setOpaque(false);
        controller.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.decode("#f0f0f0")));

        GridBagConstraints gbc = new GridBagConstraints();

        // Company Title:
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(20, 0, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        controller.add(title("Фирма(лар)ни танланг"), gbc);

        // company labels with checkboxes:
        gbc.gridwidth = 1;
        int row = 1;
        for (Object k : companies.keySet()) {
            Map curComp = (Map) companies.get(k);

            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel label_compName = label(curComp.get("name").toString());
            label_compName.setName(k.toString());
            controller.add(label_compName, gbc);

            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.insets = new Insets(0, 15, 0, 0);
            gbc.gridx = 1;
            gbc.gridy = row;
            controller.add(checkBox(label_compName), gbc);

            row++;
        }

        // Main / Other drivers:
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(20, 0, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row + 1;
        controller.add(title("Асосий / Асосий бўлмаган ҳайдовчилар"), gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = row + 2;
        JLabel label_mainDrivers = label("Асосий ҳайдовчилар");
        controller.add(label_mainDrivers, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 15, 0, 0);
        gbc.gridx = 1;
        gbc.gridy = row + 2;
        controller.add(checkBox(label_mainDrivers), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = row + 3;
        JLabel label_otherDrivers = label("Асосий бўлмаган ҳайдовчилар");
        controller.add(label_otherDrivers, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 15, 0, 0);
        gbc.gridx = 1;
        gbc.gridy = row + 3;
        controller.add(checkBox(label_otherDrivers), gbc);

        // Headers:
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(20, 0, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row + 4;
        controller.add(title("Маълумотларни танланг"), gbc);

        String[] headers = {"Ф.И.О", "Ҳайдовчи гувоҳномаси",
            "Тоифаси", "Автомобил рақами",
            "Асосий Шофер", "Лицензия АТ №",
            "Автомобил русуми", "Йўналиш номи", "Паспорт сериаси ва рақами",
            "Паспорт берилган жой", "Паспорт берилган сана",
            "Лицензия муддати", "Тех. курик муддати",
            "Ижара шартнома муддати", "Полис муддати",
            "Газ балон муддати", "Тиббий кўрик муддати",
            "Меҳнат шартнома муддати", "Манзил",
            "Телефон"};

        String[] headerKeys = {"name", "license_num",
            "license_category", "car_num",
            "is_main_driver", "permission_num",
            "car_type", "route", "pass_num",
            "pass_giving_auth", "pass_given_date",
            "permission_expire_date", "checkup_expire_date",
            "contract_expire_date", "polis_expire_date",
            "gas_tank_expire_date", "med_expire_date",
            "work_contract_expire_date", "address",
            "phone"};

        for (int i = 1; i <= headers.length; i++) {
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.gridx = 0;
            gbc.gridy = row + 4 + i;
            JLabel label = label(headers[i - 1]);
            label.setName(headerKeys[i - 1]);
            controller.add(label, gbc);

            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.insets = new Insets(0, 15, 0, 0);
            gbc.gridx = 1;
            gbc.gridy = row + 4 + i;
            controller.add(checkBox(label), gbc);
        }

        JScrollPane scrollPane = new JScrollPane(controller);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setBorder(BorderFactory.createMatteBorder(20, 0, 10, 0, Color.white));

        return scrollPane;
    }

    private static JCheckBox checkBox(JLabel l) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        checkBox.setPreferredSize(new Dimension(20, 20));
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        checkBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                l.setForeground(Color.red);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                l.setForeground(Color.gray);
            }
        });

        return checkBox;
    }

    private static JPanel buttons(JDialog jd, JScrollPane controller) {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        container.add(lineSeperator(), gbc);

        // save and cancel buttons:
        gbc.insets = new Insets(30, 0, 30, 10);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        container.add(button("Юклаш", jd, controller), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;

        container.add(button("Бекор қилиш", jd, controller), gbc);

        return container;
    }

    private static JLabel title(String t) {
        JLabel title = new JLabel(t);
        title.setFont(new Font("Calibri", Font.BOLD, 22));
        title.setForeground(Color.black);

        return title;
    }

    private static JLabel label(String t) {
        JLabel label = new JLabel(t);
        label.setFont(new Font("Calibri", Font.PLAIN, 18));
        label.setForeground(Color.gray);

        return label;

    }

    private static JLabel lineSeperator() {
        JLabel line = new JLabel();
        line.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#ebebeb")));

        return line;
    }

    private static JPanel button(String type, JDialog jd, JScrollPane controller) {
        JButton button = new JButton(type);
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.CENTER_BASELINE, 12));

        JPanel bcon = new JPanel(new BorderLayout());
        bcon.setPreferredSize(new Dimension(190, 30));
        bcon.add(button);

        if (type.equals("Юклаш")) {
            button.setBackground(Color.decode("#4285F4"));
            button.setForeground(Color.white);
        } else {
            button.setForeground(Color.decode("#4285F4"));
            button.setBackground(Color.white);
            bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (type.equals("Юклаш")) {
                    button.setBackground(Color.decode("#4f8ef5"));
                } else {
                    button.setBackground(Color.decode("#f7faff"));
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#d6e5ff")));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (type.equals("Юклаш")) {
                    button.setBackground(Color.decode("#4285F4"));
                } else {
                    button.setBackground(Color.white);
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel controllerPanel = (JPanel) controller.getViewport().getComponent(0);

                if (type.equals("Юклаш")) {
                    Map companies = (Map) getData("companies");
                    int numOfCompanies = companies.size();

                    Map info = new HashMap();
                    ArrayList companyIDs = new ArrayList();

                    for (int i = 1; i < numOfCompanies * 2; i += 2) {
                        JLabel compName = (JLabel) controllerPanel.getComponent(i);
                        JCheckBox checkBox = (JCheckBox) controllerPanel.getComponent(i + 1);

                        if (checkBox.isSelected()) {
                            String compID = compName.getName();
                            companyIDs.add(compID);
                        }
                    }

                    JCheckBox mainDriversCheck = (JCheckBox) controllerPanel.getComponent(numOfCompanies * 2 + 3);
                    JCheckBox otherDriversCheck = (JCheckBox) controllerPanel.getComponent(numOfCompanies * 2 + 5);

                    info.put("main_drivers", mainDriversCheck.isSelected());
                    info.put("other_drivers", otherDriversCheck.isSelected());

                    ArrayList selectedHeaderKeys = new ArrayList();
                    ArrayList selectedHeaderVals = new ArrayList();

                    for (int i = numOfCompanies * 2 + 7; i < (numOfCompanies * 2 + 47); i += 2) {
                        JLabel label = (JLabel) controllerPanel.getComponent(i);
                        JCheckBox checkBox = (JCheckBox) controllerPanel.getComponent(i + 1);

                        if (checkBox.isSelected()) {
                            selectedHeaderVals.add(label.getText());
                            selectedHeaderKeys.add(label.getName());
                        }
                    }

                    info.put("companyIDs", companyIDs);
                    info.put("headerKeys", selectedHeaderKeys);
                    info.put("headerVals", selectedHeaderVals);

                    Timer timer = new Timer();
                    JScrollPane scrollPane = (JScrollPane) jd.getContentPane().getComponent(0);

                    if (companyIDs.isEmpty() || (!mainDriversCheck.isSelected() && !otherDriversCheck.isSelected()) || selectedHeaderKeys.isEmpty()) {
                        if (companyIDs.isEmpty()) {
                            setForgroundRed(scrollPane, controllerPanel, timer, 0);
                        }
                        if (!mainDriversCheck.isSelected() && !otherDriversCheck.isSelected()) {
                            setForgroundRed(scrollPane, controllerPanel, timer, numOfCompanies * 2 + 1);
                        }
                        if (selectedHeaderKeys.isEmpty()) {
                            setForgroundRed(scrollPane, controllerPanel, timer, numOfCompanies * 2 + 6);
                        }
                    } else {
                        button.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        showFilePathChooser(jd, info, button);
                    }
                } else {
                    jd.dispose();
                }
            }
        });

        return bcon;
    }

    private static void setForgroundRed(JScrollPane scrollPane, JPanel controllerPanel, Timer timer, int index) {
        scrollPane.getViewport().setViewPosition(new Point(0, 0));
        JLabel label = (JLabel) controllerPanel.getComponent(index);

        if (label.getForeground().getRGB() == Color.black.getRGB()) {
            label.setForeground(Color.red);
            scrollPane.revalidate();
            scrollPane.repaint();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    label.setForeground(Color.black);
                }
            }, 1000);

        }

    }

    private static void showFilePathChooser(JDialog jd, Map info, JButton button) {
        JFrame FRAME = (JFrame) SwingUtilities.getRoot(jd.getParent());

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads"));

        fileChooser.setDialogTitle("Сақлаш учун манзилни танланг");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", "xlsx", "xlsx");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showSaveDialog(FRAME);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            jd.dispose();

            File selectedFile = fileChooser.getSelectedFile();

            if (!selectedFile.toString().endsWith(".xlsx")) {
                selectedFile = new File(selectedFile.toString() + ".xlsx");
            }

            writeToExcel(selectedFile, info);
        }else{
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

}
