package taxi;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.SwingUtilities;
import static taxi.Person.progressBar;
import static taxi.companyStatistics.companiesTabel;
import static taxi.functions.print;
import static taxi.readwrite.getData;
import static taxi.readwrite.writeData;

public class editFunctions {

    public static void saveEditedCompany(JDialog jd, JPanel companyNameContainer) {
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

            String company_id = companyNameContainer.getName();
            companies.put(company_id, company);

            jd.dispose();
            writeData(companies, "companies");
            editCompanyInstancesInGUI(companyNameContainer, companyName, hex);

        }
    }

    private static void editCompanyInstancesInGUI(JPanel companyNameContainer, String companyName, String hex) {
        // edit company instance in companiesTable:
        JLabel oldNameLabel = (JLabel) companyNameContainer.getComponent(0);
        oldNameLabel.setText(companyName);

        JPanel oldColorPanel = (JPanel) companyNameContainer.getComponent(1);
        oldColorPanel.setBackground(Color.decode(hex));

        // edit company instances in persons:
        String company_id = companyNameContainer.getName();

        JFrame FRAME = (JFrame) SwingUtilities.getRoot(companyNameContainer);
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);
        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(0);
        JPanel panelForScrollPane = (JPanel) scrollPane.getViewport().getComponent(0);
        JPanel PERSONS = (JPanel) panelForScrollPane.getComponent(0);

        for (int i = 0; i < PERSONS.getComponentCount(); i++) {
            JComponent component = (JComponent) PERSONS.getComponent(i);
            if (component instanceof JPanel) {
                String currentCompanyID = ((JPanel) component.getComponent(1)).getName();
                if (currentCompanyID.equals(company_id)) {
                    JPanel head = (JPanel) component.getComponent(0);
                    JPanel companyColorPanel = (JPanel) head.getComponent(0);
                    companyColorPanel.setBackground(Color.decode(hex));

                    JPanel body = (JPanel) component.getComponent(1);
                    JPanel companyPanel = (JPanel) body.getComponent(3);
                    JLabel oldLabel = (JLabel) companyPanel.getComponent(1);
                    oldLabel.setText(companyName);
                    oldLabel.revalidate();
                    oldLabel.repaint();
                }
            }
        }

    }

    public static void checkForCarWhenEditing(JTextField textField, String person_id) {

        Map cars = (Map) getData("cars");

        textField.addKeyListener(new KeyListener() {
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
                    autofillCarTexFieldWhenEditing(dataEntryContainer, car);

                    String main_driver = car.get("main_driver").toString();
                    if (!main_driver.isBlank()) {
                        if (!main_driver.equals(person_id)) { // and this person isn't the main_driver:
                            isMainDriverCheckBox.setSelected(false);
                            isMainDriverCheckBox.setEnabled(false);
                        }

                    } else {
                        isMainDriverCheckBox.setEnabled(true);
                    }
                } else {
                    autofillCarTexFieldWhenEditing(dataEntryContainer, car);
                    isMainDriverCheckBox.setEnabled(true);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
    }

    private static void autofillCarTexFieldWhenEditing(JPanel dataEntryContainer, Map car) {
        JPanel dataEntryPanel = (JPanel) dataEntryContainer.getComponent(7);
        JTextField textField = (JTextField) dataEntryPanel.getComponent(1);
        if (car != null) {
            String car_type = car.get("car_type").toString();
            if (!car_type.isBlank()) {
                textField.setText(car_type);
            }
        } else {
            textField.setText("");
        }
    }

    public static void saveEditedPerson(JButton button, JDialog jd, Map oldPerson, MouseEvent eve) {
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
            editPersonPanel(eve, dataEntryContainer, oldPerson);
            editCompanyStatistics(eve, dataEntryContainer, oldPerson);
        }

    }

    private static void editCompanyStatistics(MouseEvent eve, JPanel dataEntryContainer, Map oldPerson) {
        String new_company_id = inputValue(dataEntryContainer, 5, "comboBox");
        String old_company_id = oldPerson.get("company_id").toString();

        JFrame FRAME = (JFrame) SwingUtilities.getRoot(eve.getComponent());
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);
        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(1);

        if (!old_company_id.equals(new_company_id)) {
            scrollPane.getViewport().remove(0);
            scrollPane.getViewport().add(companiesTabel());
            scrollPane.revalidate();
            scrollPane.repaint();
        }
    }

    private static Map makePerson(JPanel dataEntryContainer, Map oldPerson) {
        String person_id = oldPerson.get("person_id").toString();
        Map<String, String> newPerson = new HashMap();

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

        newPerson.put("name", name);
        newPerson.put("car_num", car_num);
        newPerson.put("permission_num", permission_num);
        newPerson.put("permission_expire_date", permission_expire_date);
        newPerson.put("company_id", company_id);
        newPerson.put("contract_expire_date", contract_expire_date);
        newPerson.put("route", route);
        newPerson.put("polis_expire_date", polis_expire_date);
        newPerson.put("gas_tank_expire_date", gas_tank_expire_date);
        newPerson.put("license_num", license_num);
        newPerson.put("checkup_expire_date", checkup_expire_date);
        newPerson.put("license_category", license_category);
        newPerson.put("pass_num", pass_num);
        newPerson.put("med_expire_date", med_expire_date);
        newPerson.put("pass_given_date", pass_given_date);
        newPerson.put("work_contract_expire_date", work_contract_expire_date);
        newPerson.put("pass_giving_auth", pass_giving_auth);
        newPerson.put("address", address);
        newPerson.put("phone", phone);
        newPerson.put("person_id", person_id);

        String[] expireDateKeys = {"med_expire_date", "polis_expire_date", "contract_expire_date", "permission_expire_date", "gas_tank_expire_date", "checkup_expire_date", "work_contract_expire_date"};

        for (String expireDateKey : expireDateKeys) {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat startDateFormater = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss a", Locale.ENGLISH);
            String timeNow = startDateFormater.format(now.getTime());

            String oldExpireDate = oldPerson.get(expireDateKey).toString();
            String newExpireDate = newPerson.get(expireDateKey);

            String startDateKey = expireDateKey.replace("expire", "start");
            String oldStartDate = oldPerson.get(startDateKey).toString();

            if (oldExpireDate.equals(newExpireDate)) {
                newPerson.put(startDateKey, oldStartDate);
            } else {
                newPerson.put(startDateKey, timeNow);
            }
        }

        return newPerson;
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

    public static void editPersonPanel(MouseEvent e, JPanel dataEntryContainer, Map oldPerson) {
        Map companies = (Map) getData("companies");
        String new_company_id = inputValue(dataEntryContainer, 5, "comboBox");

        Map company = (Map) companies.get(new_company_id);
        String company_name = company.get("name").toString();
        String company_color = company.get("color").toString();

        Map newPerson = makePerson(dataEntryContainer, oldPerson);
        String person_id = newPerson.get("person_id").toString();
        String car_num = newPerson.get("car_num").toString();

        Map cars = editCars(dataEntryContainer, oldPerson);
        Map car = (Map) cars.get(car_num);
        String is_main_driver = String.valueOf(car.get("main_driver").toString().equals(person_id));

        JPanel companyColorPanel = (JPanel) getDataCell(e, "companyColorPanel", 0);
        companyColorPanel.setBackground(Color.decode(company_color));

        editJLabel(e, "head", 0, newPerson.get("name").toString());
        editJLabel(e, "head", 1, newPerson.get("license_num").toString());
        editJLabel(e, "head", 2, newPerson.get("license_category").toString());
        editJLabel(e, "head", 3, newPerson.get("car_num").toString());
        editJLabel(e, "head", 4, is_main_driver);

        String putyovka_num = car.get("putyovka_num").toString();
        if (putyovka_num.equals("0")) {
            putyovka_num = "";
        }

        editJLabel(e, "head", 5, putyovka_num);

        editJLabel(e, "body", 0, newPerson.get("permission_num").toString());
        editCarLabels(e, car);

        editJLabel(e, "body", 2, newPerson.get("route").toString());
        editJLabel(e, "body", 3, company_name);
        editJLabel(e, "body", 4, newPerson.get("pass_num").toString());
        editJLabel(e, "body", 5, newPerson.get("pass_giving_auth").toString());
        editJLabel(e, "body", 6, newPerson.get("pass_given_date").toString());
        editJLabel(e, "body", 7, newPerson.get("address").toString());
        editJLabel(e, "body", 8, newPerson.get("phone").toString());

        editProgressBarPanel(e, 9, newPerson.get("permission_expire_date").toString(), newPerson.get("permission_start_date").toString());
        editProgressBarPanel(e, 10, newPerson.get("checkup_expire_date").toString(), newPerson.get("checkup_start_date").toString());
        editProgressBarPanel(e, 11, newPerson.get("contract_expire_date").toString(), newPerson.get("contract_start_date").toString());
        editProgressBarPanel(e, 12, newPerson.get("polis_expire_date").toString(), newPerson.get("polis_start_date").toString());
        editProgressBarPanel(e, 13, newPerson.get("gas_tank_expire_date").toString(), newPerson.get("gas_tank_start_date").toString());
        editProgressBarPanel(e, 14, newPerson.get("med_expire_date").toString(), newPerson.get("med_start_date").toString());
        editProgressBarPanel(e, 15, newPerson.get("work_contract_expire_date").toString(), newPerson.get("work_contract_start_date").toString());

        ArrayList persons = (ArrayList) getData("persons");
        for (int i = 0; i < persons.size(); i++) {
            Map currentPerson = (Map) persons.get(i);
            boolean personFound = currentPerson.get("person_id").toString().equals(person_id);
            if (personFound) {
                persons.remove(i);
                persons.add(i, newPerson);
            }

        }

        writeData(cars, "cars");
        writeData(persons, "persons");
    }

    private static void editCarLabels(MouseEvent e, Map car) {
        JComponent iconLabel = (JComponent) e.getSource();
        JPanel personsPanel = (JPanel) iconLabel.getParent().getParent().getParent().getParent().getParent();

        ArrayList person_ids = new ArrayList();

        String main_driver = car.get("main_driver").toString();
        if (!main_driver.isBlank()) {
            person_ids.add(main_driver);
        }
        ArrayList other_drivers = (ArrayList) car.get("other_drivers");
        for (Object person_id : other_drivers) {
            person_ids.add(person_id);
        }

        String carType = car.get("car_type").toString();

        for (int x = 0; x < person_ids.size(); x++) {
            String person_id = person_ids.get(x).toString();
            for (int i = 0; i < personsPanel.getComponentCount(); i++) {
                JComponent comp = (JComponent) personsPanel.getComponent(i);

                if (comp.getName() != null && comp.getName().equals(person_id)) {

                    JPanel body = (JPanel) comp.getComponent(1);
                    JPanel carPanel = (JPanel) body.getComponent(1);
                    JLabel carLabel = (JLabel) carPanel.getComponent(1);
                    carLabel.setText(carType);
                    carLabel.revalidate();
                    carLabel.repaint();
                    break;
                }
            }
        }

    }

    private static void editJLabel(MouseEvent e, String where, int index, String newText) {
        JLabel oldLabel = (JLabel) getDataCell(e, where, index);
        if (where.equals("head") && index == 4) {
            if (newText.equals("true")) {
                ImageIcon icon = new ImageIcon(new ImageIcon(Taxi.class.getResource("/check2.png")).getImage().getScaledInstance(15, -1, Image.SCALE_SMOOTH));
                oldLabel.setIcon(icon);
            } else {
                oldLabel.setIcon(null);
            }
        } else {
            oldLabel.setText(newText);
        }
        oldLabel.revalidate();
        oldLabel.repaint();
    }

    private static JComponent getDataCell(MouseEvent e, String where, int index) {
        JComponent iconLabel = (JComponent) e.getSource();
        JPanel personPanel = (JPanel) iconLabel.getParent().getParent().getParent().getParent();
        JPanel head = (JPanel) personPanel.getComponent(0);
        JPanel headDataPanel = (JPanel) head.getComponent(1);
        JPanel body = (JPanel) personPanel.getComponent(1);

        if (where.equals("head")) {
            JPanel container = (JPanel) headDataPanel.getComponent(index);
            return (JLabel) container.getComponent(1);
        } else if (where.equals("body")) {
            if (index < 9) {
                JPanel container = (JPanel) body.getComponent(index);
                return (JLabel) container.getComponent(1);
            } else {
                return (JPanel) body.getComponent(index);
            }
        } else {
            return (JPanel) head.getComponent(0);
        }
    }

    private static void editProgressBarPanel(MouseEvent e, int index, String expireDate, String startDate) {
        JPanel progressBarPanel = (JPanel) getDataCell(e, "body", index);
        JLabel dateLabel = (JLabel) progressBarPanel.getComponent(1);

        Calendar expireTime = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();
        SimpleDateFormat expireDateFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat startDateFormater = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss a", Locale.ENGLISH);

        SimpleDateFormat expireDateFormater2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String expireDateString = "";

        if (!expireDate.isBlank()) {
            try {
                expireTime.setTime(expireDateFormater.parse(expireDate));
                expireDateString = expireDateFormater2.format(expireTime.getTime());

                startTime.setTime(startDateFormater.parse(startDate));
            } catch (ParseException ex) {
                print("error when parsing expire dates");
            }

            dateLabel.setText(expireDateString);
            if (progressBarPanel.getComponentCount() == 2) {
                progressBarPanel.add(progressBar(expireTime, startTime), BorderLayout.PAGE_END);
            } else {
                progressBarPanel.remove(2);
                progressBarPanel.add(progressBar(expireTime, startTime), BorderLayout.PAGE_END);
            }
        }
        progressBarPanel.revalidate();
        progressBarPanel.repaint();
    }

    private static Map editCars(JPanel dataEntryContainer, Map oldPerson) {
        Map cars = (Map) getData("cars");
        Map newCar;

        String car_type = inputValue(dataEntryContainer, 7, "textField");

        String person_id = oldPerson.get("person_id").toString();
        String old_car_num = oldPerson.get("car_num").toString();
        Map oldCar = (Map) cars.get(old_car_num);
        ArrayList OldCar_other_drivers = (ArrayList) oldCar.get("other_drivers");
        boolean was_main_driver = oldCar.get("main_driver").toString().equals(person_id);

        String new_car_num = inputValue(dataEntryContainer, 2, "textField");
        String is_main_driver_now = inputValue(dataEntryContainer, 14, "checkBox");

        if (new_car_num.equals(old_car_num)) {
            if (was_main_driver) {
                if (!is_main_driver_now.equals("true")) {
                    oldCar.put("main_driver", "");

                    OldCar_other_drivers.add(person_id);
                }
            } else if (!was_main_driver) {
                if (is_main_driver_now.equals("true")) {
                    oldCar.put("main_driver", person_id);
                    OldCar_other_drivers.remove(person_id);
                }
            }

        } else if (!new_car_num.equals(old_car_num)) {
            if (cars.containsKey(new_car_num)) { // car exists
                Map car = (Map) cars.get(new_car_num);
                ArrayList existingCar_other_drivers = (ArrayList) car.get("other_drivers");
                if (is_main_driver_now.equals("true")) {
                    if (was_main_driver) {
                        car.put("main_driver", person_id);
                        oldCar.put("main_driver", "");
                    } else {
                        car.put("main_driver", person_id);
                        OldCar_other_drivers.remove(person_id);
                    }

                } else if (!is_main_driver_now.equals("true")) {
                    if (was_main_driver) {
                        existingCar_other_drivers.add(person_id);
                        oldCar.put("main_driver", "");
                    } else {
                        existingCar_other_drivers.add(person_id);
                        OldCar_other_drivers.remove(person_id);
                    }
                }

            } else if (!cars.containsKey(new_car_num)) {

                newCar = new HashMap();
                ArrayList newCar_other_drivers = new ArrayList();

                newCar.put("putyovka_num", 0);
                newCar.put("car_type", car_type);
                newCar.put("other_drivers", newCar_other_drivers);
                newCar.put("main_driver", "");

                if (was_main_driver) {
                    if (is_main_driver_now.equals("true")) {
                        oldCar.put("main_driver", "");
                        newCar.put("main_driver", person_id);
                    } else {
                        oldCar.put("main_driver", "");
                        newCar_other_drivers.add(person_id);
//                        newCar.put("other_drivers", newCar_other_drivers);
                    }

                } else {
                    if (is_main_driver_now.equals("true")) {
                        OldCar_other_drivers.remove(person_id);
                        newCar.put("main_driver", person_id);
                    } else {
                        OldCar_other_drivers.remove(person_id);

                        newCar_other_drivers.add(person_id);
                        newCar.put("other_drivers", newCar_other_drivers);
                    }

                }
                cars.put(new_car_num, newCar);
            }
        }

        Map car = (Map) cars.get(new_car_num);
        car.put("car_type", car_type);

        if (!carHasDriver(oldCar)) {
            cars.remove(old_car_num);
        }

        return cars;
    }

    private static boolean carHasDriver(Map car) {
        return !(((ArrayList) car.get("other_drivers")).isEmpty() && car.get("main_driver").toString().isBlank());
    }

    public static String getPutyovkaNumber(String car_num) {
        Map cars = (Map) getData("cars");

        Map car = (Map) cars.get(car_num);

        String putyovka_num = car.get("putyovka_num").toString();

        if (putyovka_num.equals("0")) {
            ArrayList persons = (ArrayList) getData("persons");

            String companyID = "";
            for (int i = 0; i < persons.size(); i++) {
                Map currentPerson = (Map) persons.get(i);
                String currentCarNum = currentPerson.get("car_num").toString();
                if (car_num.equals(currentCarNum)) {
                    companyID = currentPerson.get("company_id").toString();
                    break;
                }
            }

            ArrayList carNumbers = new ArrayList();
//            int putyovkaNum = 1;
            ArrayList putyovkaNumbers = new ArrayList();

            for (int i = 0; i < persons.size(); i++) {
                Map currentPerson = (Map) persons.get(i);
                String currentCompanyID = currentPerson.get("company_id").toString();
                if (companyID.equals(currentCompanyID)) {
                    String currentCarNum = currentPerson.get("car_num").toString();
                    if (!car_num.equals(currentCarNum) && !carNumbers.contains(currentCarNum)) {
                        Map currentCar = (Map) cars.get(currentCarNum);
                        String currentPutyovkaNum = currentCar.get("putyovka_num").toString();
                        if (!currentPutyovkaNum.equals("0")) {
//                            putyovkaNum++;
                            putyovkaNumbers.add(Integer.parseInt(currentPutyovkaNum));
                        }
                        carNumbers.add(currentCarNum);
                    }
                }
            }

            int putyovkaNum = 0;

            if (putyovkaNumbers.isEmpty()) {
                putyovkaNum = 1;
            } else {
                int max = Integer.parseInt(Collections.max(putyovkaNumbers).toString());

                for (int putNum = 1; putNum <= max; putNum++) {
                    if (!putyovkaNumbers.contains(putNum)) {
                        putyovkaNum = putNum;
                        break;
                    } else {
                        putyovkaNum = max + 1;
                        break;
                    }
                }
            }

            return String.valueOf(putyovkaNum);

        } else {
            return putyovka_num;
        }

    }
}
