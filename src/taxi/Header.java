package taxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import static taxi.Person.icon;
import static taxi.functions.mouseAdapterForHeader;
import static taxi.functions.showCompanies;

public class Header {

    public static JPanel HEADER(JPanel BODY) {
        Color header_color = Color.decode("#3b3b3b");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.90);

        JPanel header = new JPanel();
        header.setBackground(header_color);

        JPanel toolsPanel = new JPanel(new BorderLayout());
        toolsPanel.setPreferredSize(new Dimension(width, 50));
        toolsPanel.setBackground(header_color);

        JLabel companies_label = new JLabel("Фирмалар");
        companies_label.setForeground(Color.white);
        companies_label.setFont(new Font(null, Font.PLAIN, 20));
        companies_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        companies_label.setName("companies");
        companies_label.addMouseListener(showCompanies(BODY, companies_label));

        toolsPanel.add(companies_label, BorderLayout.LINE_START);
        toolsPanel.add(rightSidePanel(BODY), BorderLayout.LINE_END);
        header.add(toolsPanel);

        return header;
    }

    public static JPanel rightSidePanel(JPanel BODY) {
        JPanel rightSidePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.insets = new Insets(0, 30, 0, 0);
        gbc.weightx = 0.5;

        rightSidePanel.setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        rightSidePanel.add(icon("add-icon.png", "Маълумот қўшиш", "icon_add", mouseAdapterForHeader()), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        rightSidePanel.add(icon("print-icon6.png", "Печатлаш", "icon_print_header", mouseAdapterForHeader()), gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        rightSidePanel.add(icon("excel.png", "'Excel'да олиш", "icon_excel", mouseAdapterForHeader()), gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 0;
        rightSidePanel.add(icon("sort.png", "Тартиблаш", "icon_sort", mouseAdapterForHeader()), gbc);
        
        gbc.gridx = 4;
        gbc.gridy = 0;
        rightSidePanel.add(searchBar(BODY), gbc);

        return rightSidePanel;
    }

    public static JTextField searchBar(JPanel BODY) {
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(150, 23));
        searchBar.setBorder(BorderFactory.createEmptyBorder());
        searchBar.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        searchBar.setText("Излаш...");
        searchBar.setForeground(Color.gray);
        searchBar.setFocusable(false);
        searchBar.setName("searchBar");
        searchBar.setFont(new Font(null, Font.PLAIN, 15));
        searchBar.addMouseListener(mouseAdapterForHeader());

        searchBar.addKeyListener(searchFunction(BODY, searchBar));

        return searchBar;
    }

    public static KeyListener searchFunction(JPanel BODY, JTextField searchBar) {
        return new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                JScrollPane scrollPane = (JScrollPane) BODY.getComponent(0);
                JPanel panelForScrollPane = (JPanel) scrollPane.getViewport().getComponent(0);
                JPanel personsPanel = (JPanel) panelForScrollPane.getComponent(0);
                int numOfComponents = personsPanel.getComponentCount();

                String searchText = searchBar.getText();

                if (!searchText.isBlank()) {
                    String upperCaseSearchText = searchText.toUpperCase();

                    search(personsPanel, upperCaseSearchText);

                } else {
                    for (int x = 0; x < numOfComponents; x++) {
                        personsPanel.getComponent(x).setVisible(true);
                    }
                }

            }
        };
    }

    private static void search(JPanel personsPanel, String upperCaseSearchText) {
        for (Object p : personsPanel.getComponents()) {
            JPanel person = (JPanel) p;

            JPanel head = (JPanel) person.getComponent(0);

            JPanel dataContainer = (JPanel) head.getComponent(1);
            boolean visible = true;
            for (int i = 0; i < 4; i++) {

                JPanel dp = (JPanel) dataContainer.getComponent(i);
                String data = ((JLabel) (dp.getComponent(1))).getText().toUpperCase();

                if (data.contains(upperCaseSearchText)) {
                    visible = true;
                    break;
                } else {
                    visible = false;
                }
            }

            if (visible) {
                person.setVisible(visible);
            } else {
                JPanel body = (JPanel) person.getComponent(1);
                for (int i = 0; i < 9; i++) {
                    String data = ((JLabel) ((JPanel) body.getComponent(i)).getComponent(1)).getText().toUpperCase();
                    if (data.contains(upperCaseSearchText)) {
                        visible = true;
                        break;
                    } else {
                        visible = false;
                    }
                }
            }
            person.setVisible(visible);
        }
    }
}
