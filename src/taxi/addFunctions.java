package taxi;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import static taxi.Person.PERSON;
import static taxi.companyStatistics.companiesTabel;
import static taxi.readwrite.getData;
import static taxi.readwrite.getIDs;
import static taxi.readwrite.writeData;

public class addFunctions {

    public static void saveCompany(JDialog jd, JFrame FRAME) {
        JPanel companyNamePanel = (JPanel) jd.getContentPane().getComponent(0);
        JPanel textColorPanel = (JPanel) companyNamePanel.getComponent(1);

        JTextField textField = (JTextField) textColorPanel.getComponent(0);
        String companyName = textField.getText();

        if (companyName.isBlank()) {
            textField.setBorder(BorderFactory.createLineBorder(Color.red));
        } else {
            JPanel colorPanel = (JPanel) textColorPanel.getComponent(1);
            String hex = String.format("#%02x%02x%02x", colorPanel.getBackground().getRed(), colorPanel.getBackground().getGreen(), colorPanel.getBackground().getBlue());

            Map companies = (Map) getData("companies");
            Map company = new HashMap();
            company.put("name", companyName);
            company.put("color", hex);

            companies.put(genrateUID(), company);

            jd.dispose();
            writeData(companies, "companies");
            putCompanyInGUI(FRAME);
        }
    }

    private static void putCompanyInGUI(JFrame FRAME) {
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);
        JScrollPane COMPANIES = (JScrollPane) BODY.getComponent(1);
        COMPANIES.getViewport().remove(0);
        COMPANIES.getViewport().add(companiesTabel());
        COMPANIES.revalidate();
        COMPANIES.repaint();
    }

    public static void checkForCarWhenAdding(JTextField textField) {

        Map cars = (Map) getData("cars");
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String carID = textField.getText().toUpperCase().replaceAll(" ", "");
                textField.setText(carID);

                Map car = null;
                JPanel dataEntryContainer = (JPanel) textField.getParent().getParent();
                JPanel isMainDriverPanel = (JPanel) dataEntryContainer.getComponent(14);

                JCheckBox isMainDriverCheckBox = (JCheckBox) isMainDriverPanel.getComponent(1);

                if (cars.containsKey(carID)) {
                    car = (Map) cars.get(carID);
                    autofillCarTexField(dataEntryContainer, car);

                    if (!car.get("main_driver").toString().isBlank()) {
                        isMainDriverCheckBox.setSelected(false);
                        isMainDriverCheckBox.setEnabled(false);
                    } else {
                        isMainDriverCheckBox.setEnabled(true);
                    }
                } else {
                    autofillCarTexField(dataEntryContainer, car);
                    isMainDriverCheckBox.setEnabled(true);
                }
            }
        });
    }

    private static void autofillCarTexField(JPanel dataEntryContainer, Map car) {
        JPanel dataEntryPanel = (JPanel) dataEntryContainer.getComponent(7);
        JTextField textField = (JTextField) dataEntryPanel.getComponent(1);
        if (car != null) {
            String car_type = car.get("car_type").toString();
            if (!car_type.isBlank()) {
                textField.setText(car_type);
                textField.setEditable(false);
            }
        } else {
            textField.setText("");
            textField.setEditable(true);
        }
    }

    public static void savePerson(JButton button, JDialog jd) {
        JPanel dataEntryContainer = (JPanel) button.getParent();

        JPanel car_num_panel = (JPanel) dataEntryContainer.getComponent(2);
        JPanel car_type_panel = (JPanel) dataEntryContainer.getComponent(7);

        JTextField car_num_textfield = (JTextField) car_num_panel.getComponent(1);
        JTextField car_type_textfield = (JTextField) car_type_panel.getComponent(1);

        String car_type = car_type_textfield.getText();
        String car_num = car_num_textfield.getText();

        if (car_num.isBlank() && car_type.isBlank()) {
            JScrollPane scrollPane = (JScrollPane) dataEntryContainer.getParent().getParent().getParent();
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            car_num_textfield.setBorder(BorderFactory.createLineBorder(Color.red));
            car_type_textfield.setBorder(BorderFactory.createLineBorder(Color.red));

            scrollPane.revalidate();
            scrollPane.repaint();

        } else if (!car_num.isBlank() && car_type.isBlank()) {
            JScrollPane scrollPane = (JScrollPane) dataEntryContainer.getParent().getParent().getParent();
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            car_type_textfield.setBorder(BorderFactory.createLineBorder(Color.red));
            car_type_textfield.requestFocus();
            car_num_textfield.setBorder(null);
            scrollPane.revalidate();
            scrollPane.repaint();

        } else if (car_num.isBlank() && !car_type.isBlank()) {
            JScrollPane scrollPane = (JScrollPane) dataEntryContainer.getParent().getParent().getParent();
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            car_num_textfield.setBorder(BorderFactory.createLineBorder(Color.red));
            car_num_textfield.requestFocus();
            car_type_textfield.setBorder(null);
            scrollPane.revalidate();
            scrollPane.repaint();
        } else {
            jd.dispose();
            Map person = makePerson(dataEntryContainer);
            ArrayList persons = (ArrayList) getData("persons");
            persons.add(person);

            writeData(persons, "persons");

            String person_id = person.get("person_id").toString();
            String is_main_driver = inputValue(dataEntryContainer, 14, "checkBox");

            saveCar(car_num_textfield, person_id, is_main_driver, car_type);

            addToPersonsPanel(jd, person);
            editCompanyStatistics(jd);
        }

    }

    private static void editCompanyStatistics(JDialog jd) {
        JFrame FRAME = (JFrame) jd.getParent();
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);
        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(1);

        scrollPane.getViewport().remove(0);
        scrollPane.getViewport().add(companiesTabel());
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public static void saveCar(JTextField car_num_textField, String person_id, String is_main_driver, String car_type) {
        Map cars = (Map) getData("cars");
        Map car;
        String carID = car_num_textField.getText();
        if (cars.containsKey(carID)) {
            car = (Map) cars.get(carID);

            if (!car.get("main_driver").toString().isBlank()) { //"car exists AND has main driver"
                ArrayList other_drivers = (ArrayList) car.get("other_drivers");
                other_drivers.add(person_id);

            } else { //"car exists BUT doesn't have main driver"
                if (is_main_driver.equals("true")) {
                    car.put("main_driver", person_id);
                } else {
                    ArrayList other_drivers = (ArrayList) car.get("other_drivers");
                    other_drivers.add(person_id);
                }
            }
        } else { //"car doesn't exist"
            Map newCar = new HashMap();
            newCar.put("main_driver", "");
            newCar.put("other_drivers", new ArrayList());
            newCar.put("car_type", car_type);
            newCar.put("putyovka_num", 0);  // 0 means putyovka not given
            if (is_main_driver.equals("true")) {
                newCar.put("main_driver", person_id);
            } else {
                ArrayList other_drivers = (ArrayList) newCar.get("other_drivers");
                other_drivers.add(person_id);
            }

            cars.put(carID, newCar);
        }
        writeData(cars, "cars");
    }

    private static void addToPersonsPanel(JDialog jd, Map person) {
        String company_id = person.get("company_id").toString();
        String carID = person.get("car_num").toString();
        String person_id = person.get("person_id").toString();

        Map cars = (Map) getData("cars");
        Map car = (Map) cars.get(carID);

        Map companies = (Map) getData("companies");
        Map company = (Map) companies.get(company_id);

        JFrame FRAME = (JFrame) jd.getParent();
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);
        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(0);
        JPanel panelForScrollPane = (JPanel) scrollPane.getViewport().getComponent(0);
        JPanel persons = (JPanel) panelForScrollPane.getComponent(0);

        JPanel PERSON = PERSON(person, company, car, company_id);
        PERSON.setName(person_id);
        persons.add(PERSON);
        persons.revalidate();
        persons.repaint();
    }

    private static Map makePerson(JPanel dataEntryContainer) {
        Map<String, String> person = new HashMap();

        String name = inputValue(dataEntryContainer, 1, "textField");
        String car_num = inputValue(dataEntryContainer, 2, "textField");
        String permission_num = inputValue(dataEntryContainer, 3, "textField");
        String permission_expire_date = inputValue(dataEntryContainer, 4, "dateChooser");
        String company_id = inputValue(dataEntryContainer, 5, "comboBox");
        String contract_expire_date = inputValue(dataEntryContainer, 6, "dateChooser");
        String polis_expire_date = inputValue(dataEntryContainer, 8, "dateChooser");
        String route = inputValue(dataEntryContainer, 9, "textField");
        String gas_tank_expire_date = inputValue(dataEntryContainer, 10, "dateChooser");
        String license_num = inputValue(dataEntryContainer, 11, "textField");
        String checkup_expire_date = inputValue(dataEntryContainer, 12, "dateChooser");
        String license_category = inputValue(dataEntryContainer, 13, "textField");
        String pass_num = inputValue(dataEntryContainer, 16, "textField");
        String med_expire_date = inputValue(dataEntryContainer, 17, "dateChooser");
        String pass_given_date = inputValue(dataEntryContainer, 18, "dateChooser");
        String work_contract_expire_date = inputValue(dataEntryContainer, 19, "dateChooser");
        String pass_giving_auth = inputValue(dataEntryContainer, 20, "textField");
        String address = inputValue(dataEntryContainer, 21, "textField");
        String phone = inputValue(dataEntryContainer, 22, "textField");

        person.put("name", name);
        person.put("car_num", car_num);
        person.put("permission_num", permission_num);
        person.put("permission_expire_date", permission_expire_date);
        person.put("company_id", company_id);
        person.put("contract_expire_date", contract_expire_date);
        person.put("route", route);
        person.put("polis_expire_date", polis_expire_date);
        person.put("gas_tank_expire_date", gas_tank_expire_date);
        person.put("license_num", license_num);
        person.put("checkup_expire_date", checkup_expire_date);
        person.put("license_category", license_category);
        person.put("pass_num", pass_num);
        person.put("med_expire_date", med_expire_date);
        person.put("pass_given_date", pass_given_date);
        person.put("work_contract_expire_date", work_contract_expire_date);
        person.put("pass_giving_auth", pass_giving_auth);
        person.put("address", address);
        person.put("phone", phone);
        person.put("person_id", genrateUID());

        String[] startDates = {"med_start_date", "polis_start_date", "contract_start_date", "permission_start_date", "gas_tank_start_date", "checkup_start_date", "work_contract_start_date"};

        for (String startDate : startDates) {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat startDateFormater = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss a", Locale.ENGLISH);
            String timeNow = startDateFormater.format(now.getTime());
            person.put(startDate, timeNow);
        }

        return person;
    }

    private static String inputValue(JPanel dataEntryContainer, int index, String type) {
        JPanel dataEntryPanel = (JPanel) dataEntryContainer.getComponent(index);

        switch (type) {
            case "comboBox" -> {
                JComboBox comboBox = (JComboBox) dataEntryPanel.getComponent(1);
                Item selectedItem = (Item) comboBox.getSelectedItem();
                return selectedItem.getId();
            }
            case "textField" -> {
                JTextField textField = (JTextField) dataEntryPanel.getComponent(1);
                return textField.getText();
            }
            case "dateChooser" -> {
                JDateChooser dateChooser = (JDateChooser) dataEntryPanel.getComponent(1);

                SimpleDateFormat expireDateFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                if (dateChooser.getDate() != null) {
                    return expireDateFormater.format(dateChooser.getDate().getTime());
                } else {
                    return "";
                }
            }
            case "checkBox" -> {
                JCheckBox checkBox = (JCheckBox) dataEntryPanel.getComponent(1);
                return String.valueOf(checkBox.isSelected());
            }
            default -> {
                return "";
            }
        }
    }

    public static String genrateUID() {
        ArrayList IDs = (ArrayList) getIDs();
        String ID = UUID.randomUUID().toString();

        while (IDs.contains(ID)) {
            ID = UUID.randomUUID().toString();
        }

        return ID;
    }
}
