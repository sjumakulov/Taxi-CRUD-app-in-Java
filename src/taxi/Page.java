package taxi;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import static taxi.functions.allow;

public class Page {

    public static void page(javax.swing.JFrame loadingFrame) {
        new SwingWorker() {
            @Override
            protected Integer doInBackground() throws Exception {

                if (allow()) {
                    taxi.readwrite.updateData();

                    javax.swing.ImageIcon icon = new javax.swing.ImageIcon(Taxi.class.getResource("/taxi-icon.png"));

                    javax.swing.JFrame FRAME = new javax.swing.JFrame("");
                    FRAME.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                    FRAME.setIconImage(icon.getImage());

                    JPanel BODY = taxi.Body.BODY();
                    JPanel HEADER = taxi.Header.HEADER(BODY);

                    FRAME.getContentPane().add(HEADER, BorderLayout.PAGE_START);
                    FRAME.getContentPane().add(BODY, BorderLayout.CENTER);
                    FRAME.pack();

                    Thread.sleep(2000);
                    loadingFrame.dispose();
                    FRAME.setVisible(true);
                    FRAME.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

                } else {
                    Thread.sleep(3000);
                    loadingFrame.dispose();
                    taxi.BuyLisenceFrame.buyLisenceFrame();
                }

                return 42;
            }

        }.execute();

    }

}
