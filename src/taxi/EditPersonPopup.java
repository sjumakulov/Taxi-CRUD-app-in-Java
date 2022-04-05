package taxi;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static taxi.MyScrollBarUI.myScrollBarUI;
import static taxi.PersonPopup.addToGridBag;
import static taxi.editFunctions.checkForCarWhenEditing;
import static taxi.editFunctions.saveEditedPerson;
import static taxi.printPopup.comboBoxRenderer;
import static taxi.readwrite.getData;

public class EditPersonPopup {

    public static void showEditPersonPopup(JFrame FRAME, MouseEvent mouseEv) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        personDialogPopup(FRAME, mouseEv);
        glassPane.setVisible(false);
    }

    private static JDialog personDialogPopup(JFrame FRAME, MouseEvent mouseEv) {
        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new BorderLayout());
        jd.setSize(800, 600);
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);

        jd.add(ScrollPane(jd, mouseEv), BorderLayout.CENTER);
        jd.setUndecorated(true);
        jd.setVisible(true);

        return jd;
    }

    public static JScrollPane ScrollPane(JDialog jd, MouseEvent mouseEv) {
        JPanel panelForScrollPane = new JPanel();
        panelForScrollPane.add(dataEntryContainer(jd, mouseEv));
        panelForScrollPane.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(panelForScrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());

//        scrollPane.setName("scrollPane");
        return scrollPane;
    }

    public static JPanel dataEntryContainer(JDialog jd, MouseEvent mouseEv) {
        JPanel dataEntryContainer = new JPanel(new GridBagLayout());
        dataEntryContainer.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 30, 5, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Map person = (Map) getPerson(mouseEv);
        String car_num = person.get("car_num").toString();

        Map cars = (Map) getData("cars");
        Map car = (Map) cars.get(car_num);
        String car_type = car.get("car_type").toString();
        String person_id = person.get("person_id").toString();

        addLabelToGridBag("Маълумотларни ўзгартириш", dataEntryContainer, gbc, 0, 0);

        addToGridBag(dataEntryContainer, stringDataEntry("Хайдовчи ФИО", person.get("name").toString(), null), gbc, 0, 1);
        addToGridBag(dataEntryContainer, stringDataEntry("Давлат рақами", car_num, person_id), gbc, 1, 1);

        addToGridBag(dataEntryContainer, stringDataEntry("Лицензия Т №", person.get("permission_num").toString(), null), gbc, 0, 2);
        addToGridBag(dataEntryContainer, dateDataEntry("Лицензия муддати", person.get("permission_expire_date").toString(), false), gbc, 1, 2);

        addToGridBag(dataEntryContainer, companyChooser("Фирма номи", person.get("company_id").toString()), gbc, 0, 3);
        addToGridBag(dataEntryContainer, dateDataEntry("Ижара шартнома муддати", person.get("contract_expire_date").toString(), false), gbc, 1, 3);

        addToGridBag(dataEntryContainer, stringDataEntry("Автомобил русуми", car_type, null), gbc, 0, 4);
        addToGridBag(dataEntryContainer, dateDataEntry("Полис муддати", person.get("polis_expire_date").toString(), false), gbc, 1, 4);

        addToGridBag(dataEntryContainer, stringDataEntry("Йўналиш номи", person.get("route").toString(), null), gbc, 0, 5);
        addToGridBag(dataEntryContainer, dateDataEntry("Газ балон муддати", person.get("gas_tank_expire_date").toString(), false), gbc, 1, 5);

        addToGridBag(dataEntryContainer, stringDataEntry("Ҳайдовчилик гувоҳномаси", person.get("license_num").toString(), null), gbc, 0, 6);
        addToGridBag(dataEntryContainer, dateDataEntry("Тех. Курик муддати", person.get("checkup_expire_date").toString(), false), gbc, 1, 6);

        addToGridBag(dataEntryContainer, stringDataEntry("Тоифаси", person.get("license_category").toString(), null), gbc, 0, 7);
        addToGridBag(dataEntryContainer, mainDriverCheckBox("Асосий Шофер?", car, person_id), gbc, 1, 7);

        gbc.insets = new Insets(30, 30, 5, 30);
        addLabelToGridBag("Қўшимча маълумотлар", dataEntryContainer, gbc, 0, 8);
        gbc.insets = new Insets(5, 30, 5, 30);

        addToGridBag(dataEntryContainer, stringDataEntry("Паспорт сериаси & рақами", person.get("pass_num").toString(), null), gbc, 0, 9);
        addToGridBag(dataEntryContainer, dateDataEntry("Тиббий кўрик муддати", person.get("med_expire_date").toString(), false), gbc, 1, 9);

        addToGridBag(dataEntryContainer, dateDataEntry("Паспорт берилган сана", person.get("pass_given_date").toString(), true), gbc, 0, 10);
        addToGridBag(dataEntryContainer, dateDataEntry("Меҳнат шартнома муддати:", person.get("work_contract_expire_date").toString(), false), gbc, 1, 10);

        addToGridBag(dataEntryContainer, stringDataEntry("Паспорт берувчи маъмурият", person.get("pass_giving_auth").toString(), null), gbc, 0, 11);
        addToGridBag(dataEntryContainer, stringDataEntry("Манзил", person.get("address").toString(), null), gbc, 1, 11);

        addToGridBag(dataEntryContainer, stringDataEntry("Тел рақам", person.get("phone").toString(), null), gbc, 0, 12);

        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridx = 0;
        gbc.gridy = 13;
        dataEntryContainer.add(button("Бекор қилиш", "cancel", jd, null, null), gbc);
        gbc.gridx = 1;
        gbc.gridy = 13;
        dataEntryContainer.add(button("Сақлаш", "save", jd, person, mouseEv), gbc);

        return dataEntryContainer;
    }

    private static void addLabelToGridBag(String text, JPanel dataEntryContainer, GridBagConstraints gbc, int X, int Y) {
        gbc.gridx = X;
        gbc.gridy = Y;
        gbc.gridwidth = 2;
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font(null, Font.BOLD, 25));
        dataEntryContainer.add(label, gbc);
        gbc.gridwidth = 1;
    }

    public static JPanel stringDataEntry(String dataLabel, String dataValue, String person_id) {

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(320, 30));
        textField.setBorder(null);
        textField.setBackground(Color.decode("#e6e6e6"));
        textField.setFont(new Font(null, Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        textField.setText(dataValue);

        container.add(label, BorderLayout.PAGE_START);
        container.add(textField, BorderLayout.CENTER);

        if (dataLabel.equals("Давлат рақами")) {
            checkForCarWhenEditing(textField, person_id);
//            "edit this part"
        }

        return container;
    }

    private static JPanel dateDataEntry(String dataLabel, String expire_date, boolean passDate) {
        Calendar expireCal = Calendar.getInstance();
        SimpleDateFormat expireDateFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if (!expire_date.isBlank()) {
            try {
                expireCal.setTime(expireDateFormater.parse(expire_date));
            } catch (ParseException ex) {
                System.out.println("error parsing expire_date in EditPersonPopup");
            }
        }

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JDateChooser chooser = null;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            chooser = new JDateChooser();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
        }

        chooser.setPreferredSize(new Dimension(320, 30));
        chooser.setBorder(null);
        chooser.setFont(new Font(null, Font.PLAIN, 15));

        if (!expire_date.isBlank()) {
            chooser.setDate(expireCal.getTime());
        }

        if (passDate) {
            chooser.setMaxSelectableDate(new GregorianCalendar().getTime());

        } else {
            chooser.setMinSelectableDate(new GregorianCalendar().getTime());
        }

        chooser.setDateFormatString("dd-MM-yyy");
        JTextFieldDateEditor editor = (JTextFieldDateEditor) chooser.getDateEditor();
        editor.setEditable(false);

        container.add(label, BorderLayout.PAGE_START);
        container.add(chooser, BorderLayout.CENTER);

        return container;
    }

    private static JPanel companyChooser(String dataLabel, String company_id) {
        Map companies = (Map) getData("companies");
        ArrayList<Item> model = new ArrayList();
        Item companyItemSelected = null;

        for (int i = 0; i < companies.size(); i++) {
            String compID = (companies.keySet().toArray()[i]).toString();
            String compName = ((Map) companies.get(compID)).get("name").toString();
            Item companyItem = new Item(compID, compName);
            model.add(companyItem);
            if (compID.equals(company_id)) {
                companyItemSelected = companyItem;
            }
        }

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JComboBox comboBox = new JComboBox(model.toArray());
        comboBox.setSelectedItem(companyItemSelected);
        comboBox.setRenderer(comboBoxRenderer());
        comboBox.setBackground(Color.white);
        comboBox.getComponent(0).setBackground(Color.white);
        comboBox.setFont(new Font(null, Font.PLAIN, 14));

        container.add(label, BorderLayout.PAGE_START);
        container.add(comboBox, BorderLayout.CENTER);

        return container;
    }

    private static JPanel mainDriverCheckBox(String dataLabel, Map car, String person_id) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.white);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        container.add(label);
        container.add(checkBox);

        String main_driver = car.get("main_driver").toString();

        if (!main_driver.isBlank()) {
            if (main_driver.equals(person_id)) {
                checkBox.setSelected(true);
            } else {
                checkBox.setEnabled(false);
            }
        }

        return container;
    }

    private static JButton button(String label, String name, JDialog jd, Map oldPerson, MouseEvent eve) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setPreferredSize(new Dimension(320, 35));
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
                    new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            saveEditedPerson(button, jd, oldPerson, eve);
                            return 42;
                        }
                    }.execute();

                }
            }

        });

        return button;
    }

    private static Map getPerson(MouseEvent eve) {
        JComponent iconLabel = (JComponent) eve.getSource();
        JPanel personPanel = (JPanel) iconLabel.getParent().getParent().getParent().getParent();

        String person_id = personPanel.getName();
        ArrayList persons = (ArrayList) getData("persons");

        for (Object p : persons) {
            Map person = (Map) p;
            String currentPersonID = person.get("person_id").toString();
            if (currentPersonID.equals(person_id)) {
                return person;
            }
        }
        return null;
    }

}
