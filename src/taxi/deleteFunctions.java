package taxi;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import static taxi.companyStatistics.companiesTabel;
import static taxi.readwrite.getData;
import static taxi.readwrite.writeData;

public class deleteFunctions {

    public static void deletePerson(MouseEvent e) {
        JComponent iconLabel = (JComponent) e.getSource();
        JPanel personPanel = (JPanel) iconLabel.getParent().getParent().getParent().getParent();
        JPanel personsPanel = (JPanel) personPanel.getParent();

        String person_id = personPanel.getName();
        ArrayList personsArray = (ArrayList) getData("persons");

        for (Object p : personsArray) {
            Map personMap = (Map) p;

            String currentID = personMap.get("person_id").toString();
            if (person_id.equals(currentID)) {
                String carID = personMap.get("car_num").toString();
                Map cars = (Map) getData("cars");
                Map car = (Map) cars.get(carID);
                if (car.get("main_driver").toString().equals(person_id)) {
                    car.put("main_driver", "");
                } else {
                    ArrayList other_drivers = (ArrayList) car.get("other_drivers");
                    other_drivers.remove(person_id);
                }
                if (car.get("main_driver").toString().isBlank()) {
                    ArrayList other_drivers = (ArrayList) car.get("other_drivers");
                    if (other_drivers.isEmpty()) {
                        cars.remove(carID);
                    }
                }

                personsArray.remove(personMap);

                writeData(personsArray, "persons");
                writeData(cars, "cars");
                break;
            }
        }

        personsPanel.remove(personPanel);
        personsPanel.revalidate();
        editCompanyStatistics(personsPanel);
    }

    private static void editCompanyStatistics(JPanel personsPanel) {
        JFrame FRAME = (JFrame) SwingUtilities.getRoot(personsPanel);
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);
        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(1);

        scrollPane.getViewport().remove(0);
        scrollPane.getViewport().add(companiesTabel());
    }
}
