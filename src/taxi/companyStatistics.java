package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static taxi.EditCompanyPopup.showEditCompanyPopup;
import static taxi.readwrite.getData;

public class companyStatistics {

    public static JPanel companiesTabel() {
        JPanel container = new JPanel();
        container.setBackground(Color.white);

        JPanel companiesTabel = new JPanel(new GridBagLayout());
        companiesTabel.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        companiesTabel.add(labelForCompanyTable("Фирма", false), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        companiesTabel.add(labelForCompanyTable("Машиналар Сони", false), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        companiesTabel.add(labelForCompanyTable("Ҳайдовчилар Сони", false), gbc);

        addLine(companiesTabel, gbc, 1);

        ArrayList companies = getCompanyStatistics();
        int length = companies.size();
        for (int i = 0; i < length; i++) {
            Map map = (Map) companies.get(i);

            if (i != length - 1) {
                addRowToTable(map, companiesTabel, gbc, i + 2);
            } else {
                addLine(companiesTabel, gbc, i + 2);
                String totalNumOfCars = map.get("totalNumOfCars").toString();
                String totalNumOfDrivers = map.get("totalNumOfDrivers").toString();

                gbc.gridx = 0;
                gbc.gridy = i + 3;
                companiesTabel.add(labelForCompanyTable("Жами", false), gbc);

                gbc.gridx = 1;
                companiesTabel.add(labelForCompanyTable(totalNumOfCars, true), gbc);

                gbc.gridx = 2;
                companiesTabel.add(labelForCompanyTable(totalNumOfDrivers, true), gbc);
            }
        }

        container.add(companiesTabel);

        return container;
    }

    private static void addLine(JPanel companiesTabel, GridBagConstraints gbc, int ROW) {
        JLabel line = new JLabel();
        line.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#b0b0b0")));
        gbc.gridx = 0;
        gbc.gridy = ROW;
        gbc.gridwidth = 3;

        companiesTabel.add(line, gbc);
        gbc.gridwidth = 1;
    }

    private static JPanel companyNamePanel(Map company, JLabel numOfCarsLabel, JLabel numOfDriversLabel) {
        String name = company.get("name").toString();
        String color = company.get("color").toString();
        String company_id = company.get("company_id").toString();

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(null, Font.BOLD, 15));

        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(Color.decode(color));
        colorPanel.setPreferredSize(new Dimension(20, 20));

        JPanel container = new JPanel(new BorderLayout(20, 0));
        container.add(nameLabel, BorderLayout.CENTER);
        container.add(colorPanel, BorderLayout.LINE_END);
        container.setBackground(Color.white);
        container.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));
        container.setName(company_id);

        container.setCursor(new Cursor(Cursor.HAND_CURSOR));
        container.addMouseListener(mouseAdapterForCompany(container, numOfCarsLabel, numOfDriversLabel));

        container.setToolTipText("Ўзгартириш учун 2 марта босинг");

        return container;
    }

    private static JLabel labelForCompanyTable(String l, boolean isNumber) {
        JLabel label;

        if (!isNumber) {
            label = new JLabel(l);
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 10));
            label.setForeground(Color.gray);
        } else {
            label = new JLabel(l, SwingUtilities.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));
            label.setOpaque(true);
            label.setBackground(Color.white);
        }

        label.setFont(new Font(null, Font.BOLD, 15));
        return label;
    }

    private static void addRowToTable(Map company, JPanel companiesTabel, GridBagConstraints gbc, int ROW) {
        String numOfCars = company.get("numOfCars").toString();
        String numOfDrivers = company.get("numOfDrivers").toString();

        JLabel numOfCarsLabel = labelForCompanyTable(numOfCars, true);
        JLabel numOfDriversLabel = labelForCompanyTable(numOfDrivers, true);

        gbc.gridy = ROW;
        gbc.gridx = 0;
        companiesTabel.add(companyNamePanel(company, numOfCarsLabel, numOfDriversLabel), gbc);

        gbc.gridy = ROW;
        gbc.gridx = 1;
        companiesTabel.add(numOfCarsLabel, gbc);

        gbc.gridy = ROW;
        gbc.gridx = 2;
        companiesTabel.add(numOfDriversLabel, gbc);
    }

    private static ArrayList getCompanyStatistics() {
        ArrayList companiesStatistics = new ArrayList();

        Map companies = (Map) getData("companies");
        ArrayList<Map> persons = (ArrayList) getData("persons");

        int totalNumOfCars = 0;
        int totalNumOfDrivers = 0;

        for (Object id : companies.keySet()) {

            String company_id = id.toString();
            Map companyStatistics = new HashMap(); // company name/color     numOfCars / numOfDrivers
            int numOfDrivers = 0;
            ArrayList tempCars = new ArrayList();

            for (int x = 0; x < persons.size(); x++) {
                Map currentPerson = (Map) persons.get(x);
                String compID_from_currentPerson = currentPerson.get("company_id").toString();

                if (company_id.equals(compID_from_currentPerson)) {
                    numOfDrivers++;
                    String car_num = currentPerson.get("car_num").toString();
                    if (!tempCars.contains(car_num)) {
                        tempCars.add(car_num);
                    }
                }
            }

            Map currentCompany = (Map) companies.get(company_id);
            String companyName = currentCompany.get("name").toString();
            String companycolor = currentCompany.get("color").toString();

            companyStatistics.put("name", companyName);
            companyStatistics.put("color", companycolor);
            companyStatistics.put("numOfCars", tempCars.size());
            companyStatistics.put("numOfDrivers", numOfDrivers);
            companyStatistics.put("company_id", company_id);

            totalNumOfCars += tempCars.size();
            totalNumOfDrivers += numOfDrivers;

            companiesStatistics.add(companyStatistics);
        }

        Map total = new HashMap();
        total.put("totalNumOfCars", totalNumOfCars);
        total.put("totalNumOfDrivers", totalNumOfDrivers);

        companiesStatistics.add(total);

        return companiesStatistics;
    }

    private static MouseAdapter mouseAdapterForCompany(JPanel container, JLabel numOfCarsLabel, JLabel numOfDriversLabel) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                container.setBackground(Color.decode("#f7f7f7"));
                numOfCarsLabel.setBackground(Color.decode("#f7f7f7"));
                numOfDriversLabel.setBackground(Color.decode("#f7f7f7"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                container.setBackground(Color.white);
                numOfCarsLabel.setBackground(Color.white);
                numOfDriversLabel.setBackground(Color.white);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    JFrame FRAME = (JFrame) SwingUtilities.getRoot(e.getComponent().getParent());
                    showEditCompanyPopup(FRAME, container);
                }
            }
        };
    }

}
