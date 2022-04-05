package taxi;

import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;
import static taxi.Person.PERSON;
import static taxi.readwrite.getData;

public class myPersonAddingThread extends Thread {

    ArrayList personsArray = (ArrayList) getData("persons");
    Map cars = (Map) getData("cars");
    Map companies = (Map) getData("companies");

    int maxIteration = personsArray.size();
    
    JPanel persons;

    public myPersonAddingThread(JPanel persons) {
        this.persons = persons;
    }

    @Override
    public void run() {
//        if (maxIteration> 20) {
//            maxIteration = 20;
//        }

        for (int i = 0; i < 100; i++) {
            Map person = (Map) personsArray.get(i);

            String carID = person.get("car_num").toString();
            Map car = (Map) cars.get(carID);

            String companyID = person.get("company_id").toString();
            Map company = (Map) companies.get(companyID);

            JPanel PERSON = PERSON(person, company, car, companyID);
            PERSON.setName(person.get("person_id").toString());

            persons.add(PERSON);
            persons.revalidate();
        }
        System.out.println("done adding thread");
        
    }
    
  

}
