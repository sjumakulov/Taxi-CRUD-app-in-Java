package taxi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static taxi.functions.print;

public class readwrite {

    public static void writeToExcel(File filePath, Map info) {
        new SwingWorker() {
            @Override
            protected Integer doInBackground() throws Exception {
                ArrayList persons = (ArrayList) getData("persons");
                int numOfPersons = persons.size();

                Map cars = (Map) getData("cars");
                Map companies = (Map) getData("companies");

                // Destructuring info MAP
                ArrayList headerKeys = (ArrayList) info.get("headerKeys");
                ArrayList headerVals = (ArrayList) info.get("headerVals");
                int numOfHeaders = headerKeys.size();

                ArrayList companyIDs = (ArrayList) info.get("companyIDs");
                boolean main_drivers = (boolean) info.get("main_drivers");
                boolean other_drivers = (boolean) info.get("other_drivers");

                // workbook object
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet;

                for (int compIndex = 0; compIndex < companyIDs.size(); compIndex++) {
                    String curCompanyID = (String) companyIDs.get(compIndex);
                    Map curCompany = (Map) companies.get(curCompanyID);

                    String company_name = (String) curCompany.get("name");

                    // spreadsheet object
                    spreadsheet = workbook.createSheet(company_name);

                    // writing the header rows to spreadSheet
                    XSSFRow headerRow;
                    headerRow = spreadsheet.createRow(0);
                    for (int headerIndex = 0; headerIndex < numOfHeaders; headerIndex++) {
                        Cell cell = headerRow.createCell(headerIndex);
                        cell.setCellValue((String) headerVals.get(headerIndex));
                    }

                    //writing the values to rows in spreadSheet
                    int rowIndex = 0;
                    for (int personIndex = 0; personIndex < numOfPersons; personIndex++) {
                        Map curPerson = (Map) persons.get(personIndex);
                        String curPersonID = curPerson.get("person_id").toString();
                        String curCarID = curPerson.get("car_num").toString();

                        boolean belongsToCurCompany = curPerson.get("company_id").toString()
                                .equals(curCompanyID);

                        boolean is_main_driver = ((Map) cars.get(curCarID)).get("main_driver").toString()
                                .equals(curPersonID);

                        if (belongsToCurCompany) {

                            if (main_drivers && other_drivers) {
                                // creating a row object
                                XSSFRow row;

                                // writing the data into the sheets...
                                row = spreadsheet.createRow(rowIndex + 1);

                                for (int headerIndex = 0; headerIndex < numOfHeaders; headerIndex++) {

                                    String val = (String) curPerson.get((String) headerKeys.get(headerIndex));
                                    if (headerKeys.get(headerIndex).toString().contains("expire")
                                            || headerKeys.get(headerIndex).toString().equals("pass_given_date")) {
                                        val = expireDate(val);
                                    } else if (headerKeys.get(headerIndex).toString().equals("is_main_driver")) {
                                        if (is_main_driver) {
                                            val = "Ҳа";
                                        }
                                    }

                                    Cell cell = row.createCell(headerIndex);
                                    cell.setCellValue(val);
                                }

                                rowIndex++;

                            } else if (main_drivers && is_main_driver) {
                                XSSFRow row;

                                // writing the data into the sheets...
                                row = spreadsheet.createRow(rowIndex + 1);

                                for (int headerIndex = 0; headerIndex < numOfHeaders; headerIndex++) {
                                    String val = (String) curPerson.get((String) headerKeys.get(headerIndex));

                                    if (headerKeys.get(headerIndex).toString().contains("expire")
                                            || headerKeys.get(headerIndex).toString().equals("pass_given_date")) {
                                        val = expireDate(val);
                                    } else if (headerKeys.get(headerIndex).toString().equals("is_main_driver")) {
                                        val = "Ҳа";
                                    }

                                    Cell cell = row.createCell(headerIndex);
                                    cell.setCellValue(val);
                                }

                                rowIndex++;

                            } else if (other_drivers && !is_main_driver) {
                                XSSFRow row;

                                // writing the data into the sheets...
                                row = spreadsheet.createRow(rowIndex + 1);

                                for (int headerIndex = 0; headerIndex < numOfHeaders; headerIndex++) {
                                    String val = (String) curPerson.get((String) headerKeys.get(headerIndex));
                                    if (headerKeys.get(headerIndex).toString().contains("expire")
                                            || headerKeys.get(headerIndex).toString().equals("pass_given_date")) {
                                        val = expireDate(val);
                                    }

                                    Cell cell = row.createCell(headerIndex);
                                    cell.setCellValue(val);
                                }

                                rowIndex++;
                            }
                        }
                    }
                }

                // writing the workbook into the file...
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(filePath);
                } catch (FileNotFoundException ex) {
                    print("error while writing to excel: " + ex.getMessage());
                }

                try {
                    workbook.write(out);
                } catch (IOException ex) {
                    print("error while writing to excel: " + ex.getMessage());
                }
                try {
                    out.close();
                } catch (IOException ex) {
                    print("error while writing to excel: " + ex.getMessage());
                }

                return 42;
            }

        }.execute();

    }

    public static void writeData(Object obj, String fileName) {
        File folder = new File("./data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            try ( FileOutputStream fileOut = new FileOutputStream(folder + "/" + fileName);  ObjectOutputStream objWriter = new ObjectOutputStream(fileOut)) {
                objWriter.writeObject(obj);
                objWriter.close();
                fileOut.close();
            }
        } catch (IOException e) {
            print("error while serializing " + fileName);
        }
    }

    public static Object getData(String fileName) {
        File file = new File("./data/" + fileName);

        Object obj = null;

        if (file.exists()) {
            try {
                try ( FileInputStream inFile = new FileInputStream(file);  ObjectInputStream objectReader = new ObjectInputStream(inFile)) {
                    obj = objectReader.readObject();
                    objectReader.close();
                    inFile.close();
                }
            } catch (IOException e) {
                print("error occured while deserializing " + fileName);

            } catch (ClassNotFoundException e) {
                print("ClassNotFoundException while deserializing");
            }
        } else {
            switch (fileName) {
                case "persons" -> {
                    obj = new ArrayList();
                    break;
                }
                case "cars", "companies" -> {
                    obj = new HashMap();
                    break;
                }
            }
        }

        return obj;
    }

    public static ArrayList getIDs() {
        ArrayList<String> IDs = new ArrayList();
        ArrayList persons = (ArrayList) getData("persons");
        for (int i = 0; i < persons.size(); i++) {
            IDs.add(((Map) persons.get(i)).get("person_id").toString());
        }

        Map companies = (Map) getData("companies");
        for (int i = 0; i < companies.size(); i++) {
            String compID = (companies.keySet().toArray()[i]).toString();
            IDs.add(compID);
        }

        return IDs;
    }

    public static void updateData() {
        File file = new File("./data/other");

        if (!file.exists()) {
            Calendar lastDayOfMonth = Calendar.getInstance();
            lastDayOfMonth.set(lastDayOfMonth.get(Calendar.YEAR), lastDayOfMonth.get(Calendar.MONTH), lastDayOfMonth.getActualMaximum(Calendar.DATE), 23, 59, 59);

            writeData(lastDayOfMonth, "other");
        } else {
            Calendar lastDayOfMonth = (Calendar) getData("other");
            Calendar now = Calendar.getInstance();
            long timeLeft = lastDayOfMonth.getTimeInMillis() - now.getTimeInMillis();

            if (timeLeft < 0) {
                Map cars = (Map) getData("cars");

                for (Object key : cars.keySet().toArray()) {

                    Map currentCar = (Map) cars.get(key);
                    String currentPutyovkaNum = currentCar.get("putyovka_num").toString();
                    if (!currentPutyovkaNum.equals("0")) {
                        currentCar.put("putyovka_num", "0");
                    }
                }

                writeData(cars, "cars");

                Calendar lastDayOfMonthNew = Calendar.getInstance();
                lastDayOfMonthNew.set(lastDayOfMonthNew.get(Calendar.YEAR), lastDayOfMonthNew.get(Calendar.MONTH), lastDayOfMonthNew.getActualMaximum(Calendar.DATE), 23, 59, 59);

                writeData(lastDayOfMonthNew, "other");
            }
        }
    }

    private static String expireDate(String expireDate) {

        if (expireDate.isBlank()) {
            return "";
        } else {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat dateFormater2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

            try {
                cal.setTime(dateFormater.parse(expireDate));
            } catch (ParseException ex) {
                Logger.getLogger(readwrite.class.getName()).log(Level.SEVERE, null, ex);
            }
            return dateFormater2.format(cal.getTime());
        }
    }
}
