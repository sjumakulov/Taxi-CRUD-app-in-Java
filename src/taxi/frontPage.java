package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import static taxi.backPage.cell;
import static taxi.readwrite.getData;

public class frontPage {

    public static JPanel frontPage(String monthIndex, String person_id, String putyovka_num) {
        JPanel frontPage = new JPanel(new GridLayout(1, 2, 10, 0));
        frontPage.setPreferredSize(new Dimension(813, 561));
        frontPage.setBackground(Color.white);
        frontPage.setName("Путёвка олд тараф");

        frontPage.add(left(monthIndex));
        frontPage.add(right(monthIndex, person_id, putyovka_num));

        return frontPage;
    }

    private static JPanel right(String monthIndex, String person_id, String putyovka_num) {
        Map personInfo = personInfo(person_id);

        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 0.5;

        gbc.insets = new Insets(0, 5, 28, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        right.add(borderlessLabel(dateText(monthIndex), 12), gbc);

        gbc.insets = new Insets(0, 0, 28, 0);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        right.add(borderlessLabel("Йўл Варақаси № " + putyovka_num, 12), gbc);

        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        right.add(borderlessLabel(personInfo.get("company_name").toString(), 22), gbc);

        gbc.insets = new Insets(0, 5, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 2;
        right.add(image(), gbc);

        // bottom table part:
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 3;
        right.add(borderLabel("Йўналиш номи", personInfo.get("route").toString(), 1, 1, 0, 1), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        right.add(borderLabel("Ҳайдовчи", personInfo.get("name").toString(), 1, 1, 0, 1), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        right.add(borderLabel("Автомобил русуми", personInfo.get("car_type").toString(), 1, 1, 0, 1), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        right.add(borderLabel("Давлат рақами", personInfo.get("car_num").toString(), 1, 1, 0, 1), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        right.add(borderLabel("Ҳайдовчилик гувоҳномаси", personInfo.get("license_num").toString(), 1, 1, 0, 1), gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        right.add(borderLabel("Тоифаси", personInfo.get("license_category").toString(), 1, 1, 0, 1), gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        right.add(borderLabel("Лицензия АТ", personInfo.get("permission_num").toString(), 1, 1, 0, 1), gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 1;
        gbc.gridy = 9;
        right.add(borderLabel("Муддати", personInfo.get("permission_expire_date").toString(), 1, 0, 0, 1), gbc);

        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        right.add(borderLabel("Ижара шартнома муддати", personInfo.get("contract_expire_date").toString(), 1, 1, 0, 1), gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        right.add(borderLabel("Полис муддати", personInfo.get("polis_expire_date").toString(), 1, 1, 0, 1), gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 1;
        gbc.gridy = 11;
        right.add(borderLabel("Газ акт", personInfo.get("gas_tank_expire_date").toString(), 1, 0, 0, 1), gbc);

        gbc.insets = new Insets(0, 5, 50, 0);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        right.add(borderLabel("Тех. Курик", personInfo.get("checkup_expire_date").toString(), 1, 1, 1, 1), gbc);

        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        right.add(borderlessLabel("Диспетчер: _____________________ М.П", 15), gbc);

        return right;
    }

    private static JPanel borderLabel(String labelText, String contentText, int top, int left, int bottom, int right) {

        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Calibri", Font.BOLD, 12));

        JLabel dataPoint = new JLabel(contentText);
        dataPoint.setFont(new Font("Calibri", Font.PLAIN, 12));

        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEADING));
        container.setOpaque(false);

        container.add(label);
        container.add(dataPoint);

        container.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.black));

        return container;
    }

    private static JLabel borderlessLabel(String text, int fontSize) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Calibri", Font.PLAIN, fontSize));

        return label;
    }

    private static JLabel image() {
        ImageIcon carPNG = new ImageIcon(Taxi.class.getResource("/car.PNG"));

        carPNG = new ImageIcon(carPNG.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH));

        JLabel label = new JLabel();
        label.setIcon(carPNG);

        return label;
    }

    private static JPanel left(String monthIndex) {
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        Calendar cal = Calendar.getInstance();
        int month = Integer.parseInt(monthIndex);

        cal.set(Calendar.MONTH, month);
        int numOfDays = cal.getActualMaximum(Calendar.DATE);
        SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        ArrayList dates = new ArrayList();

        for (int day = 25; day <= numOfDays; day++) {
            cal.set(Calendar.DATE, day);
            dates.add(dateFormater.format(cal.getTime()));
        }

        left.add(table(dates), BorderLayout.PAGE_START);
        left.add(bottom(), BorderLayout.PAGE_END);

        return left;
    }

    private static JPanel bottom() {
        Font font = new Font("Calibri", Font.PLAIN, 15);
        String warningText1 = "Ижара солиғини ўз вақтида тўлашни унутманг!";
        String warningText2 = "Алохида белгилар ва огоҳлантиришлар:";

        JLabel warningLabel1 = new JLabel(warningText1, SwingConstants.CENTER);
        JLabel warningLabel2 = new JLabel(warningText2, SwingConstants.CENTER);

        warningLabel1.setFont(font);
        warningLabel2.setFont(font);

        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setOpaque(false);
        bottom.setPreferredSize(new Dimension(100, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        bottom.add(warningLabel1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bottom.add(warningLabel2, gbc);

        gbc.weightx = 0.5;
        gbc.insets = new Insets(20, 0, 0, 0);
        for (int y = 2; y < 5; y++) {
            JLabel line = new JLabel();
            line.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.gray));
            line.setPreferredSize(new Dimension(100, 20));

            gbc.gridx = 0;
            gbc.gridy = y;
            bottom.add(line, gbc);
        }

        return bottom;
    }

    private static JPanel table(ArrayList dates) {
        int numOfRows = dates.size();

        JPanel table = new JPanel(new GridBagLayout());
        table.setBackground(Color.white);
        table.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints gbc = new GridBagConstraints();
        String[] headers = {"Сана", "Хайдовчи Ахволи", "Транспорт ҳолати", "Кунлик тўлов", "Чиқиш вақти", "Кириш вақти",};

        for (int row = 0; row <= numOfRows; row++) {
            for (int col = 0; col < 6; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                if (row == 0) {
                    table.add(cell(headers[col], "bold", col), gbc);
                } else if (row != numOfRows) {
                    if (col == 0) {
                        table.add(cell(dates.get(row - 1).toString(), "plain", col), gbc);
                    } else {
                        table.add(cell("", "plain", col), gbc);
                    }

                } else {
                    if (col == 0) {
                        table.add(cell(dates.get(row - 1).toString(), "lastRow", col), gbc);
                    } else {
                        table.add(cell("", "lastRow", col), gbc);
                    }

                }
            }
        }

        return table;
    }

    private static String dateText(String monthIndex) {
        int month = Integer.parseInt(monthIndex);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String[] monthNames = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

        String date = "«" + monthNames[month] + "» " + year + " йил";

        return date;
    }

    private static Map personInfo(String person_id) {
        Map personInfo = new HashMap();

        ArrayList persons = (ArrayList) getData("persons");
        for (int i = 0; i < persons.size(); i++) {
            Map person = (Map) persons.get(i);
            String currentID = person.get("person_id").toString();

            if (currentID.equals(person_id)) {
                personInfo = person;
                break;
            }
        }

        Map cars = (Map) getData("cars");
        Map car = (Map) cars.get(personInfo.get("car_num").toString());
        personInfo.put("car_type", car.get("car_type").toString());
        personInfo.put("putyovka_num", car.get("putyovka_num").toString());

        Map companies = (Map) getData("companies");
        Map company = (Map) companies.get(personInfo.get("company_id").toString());
        personInfo.put("company_name", company.get("name").toString());

        return personInfo;
    }
}
