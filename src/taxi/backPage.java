package taxi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class backPage {

    public static JPanel backPage(String monthIndex) {
        JPanel backPage = new JPanel(new GridLayout(1, 2, 8, 0));
        backPage.setPreferredSize(new Dimension(813, 561));
        backPage.setBackground(Color.white);

        backPage.setName("Путёвка орқа тараф");

        Calendar cal = Calendar.getInstance();
        int month = Integer.parseInt(monthIndex) + 1;
        String formattedMonth = "";

        if (month < 10) {
            formattedMonth = "0" + String.valueOf(month);
        } else {
            formattedMonth = String.valueOf(month);
        }

        int year = cal.get(Calendar.YEAR);

        String[] left = new String[12];
        for (int day = 1; day < 13; day++) {
            String forattedDay = "";
            if (day < 10) {
                forattedDay = "0" + day;
            } else {
                forattedDay = String.valueOf(day);
            }

            left[day - 1] = forattedDay + "." + formattedMonth + "." + year;
        }

        String[] right = new String[12];
        for (int day = 13; day < 25; day++) {
            String forattedDay = "";
            if (day < 10) {
                forattedDay = "0" + day;
            } else {
                forattedDay = String.valueOf(day);
            }
            right[day - 13] = forattedDay + "." + formattedMonth + "." + year;
        }

        backPage.add(table(left));
        backPage.add(table(right));

        return backPage;
    }

    private static JPanel table(String[] dates) {
        JPanel table = new JPanel(new GridBagLayout());
        table.setOpaque(false);

        table.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints gbc = new GridBagConstraints();

        String[] headers = {"Сана", "Хайдовчи Ахволи", "Транспорт ҳолати", "Кунлик тўлов", "Чиқиш вақти", "Кириш вақти",};

        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < 6; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                if (row == 0) {
                    table.add(cell(headers[col], "bold", col), gbc);
                } else if (row != 12) {
                    if (col == 0) {
                        table.add(cell(dates[row - 1], "plain", col), gbc);
                    } else {
                        table.add(cell("", "plain", col), gbc);
                    }

                } else {
                    if (col == 0) {
                        table.add(cell(dates[row - 1], "lastRow", col), gbc);
                    } else {
                        table.add(cell("", "lastRow", col), gbc);
                    }

                }
            }
        }

        return table;
    }

    public static JLabel cell(String label, String type, int colNumber) {
        JLabel cell = new JLabel(String.format("<html><body style=\"text-align: center;  text-justify: inter-word;\">%s</body></html>", label), SwingConstants.CENTER);

        Font font;
        if (type.equals("bold")) {
            font = new Font("Calibri", Font.BOLD, 12);

        } else {
            font = new Font("Calibri", Font.PLAIN, 12);
        }

        if (type.equals("lastRow")) {
            if (colNumber != 5) {
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
            }
        } else {
            if (colNumber == 5) {
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            } else {
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
            }
        }

        cell.setFont(font);

        int width = 0;
        int height = 43;

        switch (colNumber) {
            case 0 -> {
                width = 55;
                break;
            }
            case 1, 2, 3 -> {
                width = 88;
                break;
            }
            case 4, 5 -> {
                width = 40;
                break;
            }
        }

        cell.setPreferredSize(new Dimension(width, height));

        return cell;
    }
}
