package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import static taxi.functions.mouseAdapterForPerson;
import static taxi.functions.print;

public class Person {

    public static JPanel PERSON(Map personMap, Map company, Map car, String companyID) {

        JPanel head = head(personMap, company.get("color").toString(), car);
        head.setName("head");

        JPanel body = body(personMap, company.get("name").toString(), car);
        body.setName(companyID);

        JPanel person = new JPanel();
        person.setLayout(new BorderLayout());
        person.add(head, BorderLayout.PAGE_START);
        person.add(body, BorderLayout.CENTER);
        person.setOpaque(false);
        person.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));

        person.addMouseListener(mouseAdapterForPerson());
        head.addMouseListener(mouseAdapterForPerson());

        return person;
    }

    public static JPanel head(Map personMap, String companyColor, Map car) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.90);

        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(Color.white);

        head.setPreferredSize(new Dimension(width, 50));
        head.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#b0b0b0")));

        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        dataPanel.setBackground(Color.white);
        dataPanel.add(dataCell("Ф.И.О", personMap.get("name").toString(), 265));
        dataPanel.add(dataCell("Ҳайдовчи гувоҳномаси", personMap.get("license_num").toString(), 210));
        dataPanel.add(dataCell("Тоифаси", personMap.get("license_category").toString(), 110));
        dataPanel.add(dataCell("Автомобил рақами", personMap.get("car_num").toString(), 180));
        dataPanel.add(dataCell("Асосий Шофер", is_main_driver(personMap, car), 145));
        dataPanel.add(dataCell("Путёвка Рақами", putyovkaGiven(car), 145));

        head.add(companyColor(companyColor), BorderLayout.LINE_START);
        head.add(dataPanel, BorderLayout.CENTER);
        head.add(toolsPanel(), BorderLayout.LINE_END);

        return head;
    }

    public static JPanel body(Map personMap, String companyName, Map car) {

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.white);
        body.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.decode("#b0b0b0")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 10, 0);
        gbc.weightx = 0.5;

        addToGridBag(0, 0, gbc, body, "Лицензия АТ №", personMap.get("permission_num").toString(), "");
        addToGridBag(0, 1, gbc, body, "Автомобил русуми", car.get("car_type").toString(), "");
        addToGridBag(0, 2, gbc, body, "Йўналиш номи", personMap.get("route").toString(), "");
        addToGridBag(0, 4, gbc, body, "Фирма номи", companyName, "");

        addToGridBag(1, 0, gbc, body, "Паспорт сериаси ва рақами", personMap.get("pass_num").toString(), "");
        addToGridBag(1, 1, gbc, body, "Паспорт берилган жой", personMap.get("pass_giving_auth").toString(), "");
        addToGridBag(1, 2, gbc, body, "Паспорт берилган сана", personMap.get("pass_given_date").toString(), "");

        gbc.insets = new Insets(5, 5, 10, 10);
        addToGridBag(2, 0, gbc, body, "Манзил", personMap.get("address").toString(), "");
        addToGridBag(2, 1, gbc, body, "Телефон", personMap.get("phone").toString(), "");

        addToGridBag(2, 2, gbc, body, "Лицензия муддати", personMap.get("permission_expire_date").toString(), personMap.get("permission_start_date").toString());

        addToGridBag(3, 0, gbc, body, "Тех. курик муддат", personMap.get("checkup_expire_date").toString(), personMap.get("checkup_start_date").toString());
        addToGridBag(3, 1, gbc, body, "Ижара шартнома муддати", personMap.get("contract_expire_date").toString(), personMap.get("contract_start_date").toString());
        addToGridBag(3, 2, gbc, body, "Полис муддати", personMap.get("polis_expire_date").toString(), personMap.get("polis_start_date").toString());

        addToGridBag(4, 0, gbc, body, "Газ балон муддат", personMap.get("gas_tank_expire_date").toString(), personMap.get("gas_tank_start_date").toString());
        addToGridBag(4, 1, gbc, body, "Тиббий кўрик муддати", personMap.get("med_expire_date").toString(), personMap.get("med_start_date").toString());
        addToGridBag(4, 2, gbc, body, "Меҳнат шартнома муддати", personMap.get("work_contract_expire_date").toString(), personMap.get("work_contract_start_date").toString());

        body.setVisible(false);

        return body;
    }

    public static JPanel companyColor(String color) {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(Color.decode(color));
        colorPanel.setPreferredSize(new Dimension(4, 50));

        return colorPanel;
    }

    public static JPanel toolsPanel() {
        JPanel toolsPanel = new JPanel(new GridLayout(1, 3, 15, 0));

        toolsPanel.add(icon("print-icon6.png", "Печатлаш", "icon_print", mouseAdapterForPerson()));
        toolsPanel.add(icon("pencil-icon3.png", "Ўзгартириш", "icon_edit", mouseAdapterForPerson()));
        toolsPanel.add(icon("bin8.png", "Ўчириш", "icon_delete", mouseAdapterForPerson()));
        toolsPanel.setBackground(Color.white);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(100, 50));
        mainPanel.add(toolsPanel);

        toolsPanel.setVisible(false);

        return mainPanel;
    }

    public static JLabel icon(String pathToIcon, String tooltiptext, String name, MouseAdapter mAdapter) {
        Icon icon = new ImageIcon(new ImageIcon(Taxi.class.getResource("/" + pathToIcon)).getImage().getScaledInstance(20, -1, Image.SCALE_SMOOTH));
        
        JLabel iconLabel = new JLabel();
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconLabel.setIcon(icon);

        iconLabel.setToolTipText(tooltiptext);
        iconLabel.setName(name);
        iconLabel.addMouseListener(mAdapter);

        return iconLabel;
    }

    public static JPanel dataCell(String label, String content, int width) {
        JPanel dataCell = new JPanel(new GridLayout(2, 1));
        dataCell.setPreferredSize(new Dimension(width, 40));
        dataCell.setBackground(Color.white);

        JLabel dataLabel = new JLabel(label);
        JLabel dataContent = new JLabel();

        dataLabel.setForeground(Color.gray);
        dataLabel.setFont(new Font(null, Font.BOLD, 15));
        dataContent.setFont(new Font(null, Font.BOLD, 15));

        dataCell.add(dataLabel);
        dataCell.add(dataContent);

        if (content.equals("true")) {
            ImageIcon icon = new ImageIcon(new ImageIcon(Taxi.class.getResource("/check2.png")).getImage().getScaledInstance(15, -1, Image.SCALE_SMOOTH));
            dataContent.setIcon(icon);
        } else if (!content.equals("false")) {
            dataContent.setText(content);
        }

        return dataCell;
    }

    public static JPanel dataCellProgress(String label, String expireDate, String startDate) {
        Calendar expireTime = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();
        SimpleDateFormat expireDateFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat startDateFormater = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss a", Locale.ENGLISH);

        SimpleDateFormat expireDateFormater2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String expireDateString = "";

        try {
            if (!expireDate.isBlank()) {
                expireTime.setTime(expireDateFormater.parse(expireDate));
                expireDateString = expireDateFormater2.format(expireTime.getTime());
            }

            startTime.setTime(startDateFormater.parse(startDate));
        } catch (ParseException e) {
            print("error while parsing date in Person.java");
        }

        JPanel dataCell = new JPanel(new BorderLayout());
        dataCell.setBackground(Color.white);

        JLabel dataLabel = new JLabel(label);
        dataLabel.setForeground(Color.gray);
        dataLabel.setFont(new Font(null, Font.BOLD, 14));

        JLabel dataContent = new JLabel(expireDateString);
        dataContent.setFont(new Font(null, Font.BOLD, 15));

        dataCell.add(dataLabel, BorderLayout.PAGE_START);
        dataCell.add(dataContent, BorderLayout.CENTER);

        if (!expireDate.isBlank()) {
            dataCell.add(progressBar(expireTime, startTime), BorderLayout.PAGE_END);
        }

        return dataCell;
    }

    private static JProgressBar bar(Calendar expireTime, Calendar startTime) {
        Calendar now = Calendar.getInstance();

        long totalTime = expireTime.getTimeInMillis() - startTime.getTimeInMillis();
        long timeLeft = expireTime.getTimeInMillis() - now.getTimeInMillis();
        int timeLeftInPercentage = (int) (timeLeft * 100 / totalTime);

        JProgressBar bar = new JProgressBar();

        bar.setValue(timeLeftInPercentage);
        bar.setPreferredSize(new Dimension(100, 2));
        bar.setBackground(Color.decode("#ebebeb"));
        bar.setForeground(Color.decode("#44cf71"));
        bar.setUI((ProgressBarUI) BasicProgressBarUI.createUI(bar));
        bar.setBorder(null);

        Timer timer = new Timer((int) (totalTime / 100), null);

        ActionListener actionListener = (ActionEvent e) -> {
            if (e.getSource() == timer) {
                bar.setValue(bar.getValue() - 1);
            }
        };

        timer.addActionListener(actionListener);
        timer.start();

        return bar;
    }

    public static JProgressBar progressBar(Calendar expireTime, Calendar startTime) {
        JProgressBar progressBar = null;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            progressBar = bar(expireTime, startTime);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
        }

        return progressBar;
    }
    

    public static String is_main_driver(Map personMap, Map car) {
        String mainDriverID = car.get("main_driver").toString();
        String personID = personMap.get("person_id").toString();
        if (mainDriverID.equals(personID)) {
            return "true";
        } else {
            return "false";
        }
    }

    private static String putyovkaGiven(Map car) {
        if (car.get("putyovka_num").toString().equals("0")) {
            return "";
        } else {
            return car.get("putyovka_num").toString();
        }
    }

    public static void addToGridBag(int gridX, int gridY, GridBagConstraints gbc, JPanel body, String label, String content, String startDate) {
        gbc.gridx = gridX;
        gbc.gridy = gridY;

        if (!startDate.isEmpty()) {
            body.add(dataCellProgress(label, content, startDate), gbc);
        } else {
            body.add(dataCell(label, content, 200), gbc);
        }
    }

}
