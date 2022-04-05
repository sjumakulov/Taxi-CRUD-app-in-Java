package taxi;

import java.awt.CardLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static taxi.MyScrollBarUI.myScrollBarUI;
import static taxi.Person.PERSON;
import static taxi.companyStatistics.companiesTabel;
import static taxi.readwrite.getData;

public class Body {

    public static JScrollPane PERSONS() {

        JPanel persons = new JPanel();
        persons.setLayout(new BoxLayout(persons, BoxLayout.Y_AXIS));
        persons.setBackground(Color.white);
        persons.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        JPanel panelForScrollPane = new JPanel();
        panelForScrollPane.add(persons);
        panelForScrollPane.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(panelForScrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);

        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(myScrollBarUI());

        ArrayList personsArray = (ArrayList) getData("persons");
        Map cars = (Map) getData("cars");
        Map companies = (Map) getData("companies");

        addPersonsInBackground(persons, 1, personsArray, cars, companies);
        addPersonsInBackground(persons, 2, personsArray, cars, companies);
        addPersonsInBackground(persons, 3, personsArray, cars, companies);
        addPersonsInBackground(persons, 4, personsArray, cars, companies);
        addPersonsInBackground(persons, 5, personsArray, cars, companies);

        return scrollPane;
    }

    public static JScrollPane COMPANIES() {

        JScrollPane scrollPane = new JScrollPane(companiesTabel());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);
        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(myScrollBarUI());

        return scrollPane;
    }

    public static JPanel BODY() {
        JPanel cards = new JPanel(new CardLayout());

        cards.add(PERSONS(), "persons");
        cards.add(COMPANIES(), "companies");

        return cards;
    }

    private static void addPersonsInBackground(JPanel persons, int partNum, ArrayList personsArray, Map cars, Map companies) {

        new SwingWorker() {

            @Override
            protected Integer doInBackground() throws Exception {
                int personsArraySize = personsArray.size();
                int numOfParts = 5;
                int numOfPersonsPerLoop = personsArraySize / numOfParts;

                int min = 0;
                int max = 0;

                switch (partNum) {
                    case 1 -> {
                        max = numOfPersonsPerLoop;
                        break;
                    }
                    case 2 -> {
                        min = numOfPersonsPerLoop;
                        max = 2 * numOfPersonsPerLoop;
                        break;
                    }
                    case 3 -> {
                        min = 2 * numOfPersonsPerLoop;
                        max = 3 * numOfPersonsPerLoop;
                        break;
                    }
                    case 4 -> {
                        min = 3 * numOfPersonsPerLoop;
                        max = 4 * numOfPersonsPerLoop;
                        break;
                    }
                    case 5 -> {
                        if (personsArraySize % numOfParts == 0) {
                            min = 4 * numOfPersonsPerLoop;
                            max = 5 * numOfPersonsPerLoop;
                            break;
                        } else {
                            min = 4 * numOfPersonsPerLoop;
                            max = (personsArraySize - numOfParts * numOfPersonsPerLoop) + 5 * numOfPersonsPerLoop;
                            break;
                        }
                    }
                    default -> {

                    }
                }

                for (int i = min; i < max; i++) {
                   
                    Map person = (Map) personsArray.get(i);

                    String carID = person.get("car_num").toString();
                    Map car = (Map) cars.get(carID);

                    String companyID = person.get("company_id").toString();
                    Map company = (Map) companies.get(companyID);

                    JPanel PERSON = PERSON(person, company, car, companyID);
                    PERSON.setName(person.get("person_id").toString());

                    persons.add(PERSON);
                    persons.revalidate();
                    persons.repaint();
                }

                return 42;
            }

            @Override
            protected void done() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
            }

        }.execute();

    }

}
