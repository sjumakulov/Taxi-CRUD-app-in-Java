package taxi;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import static java.lang.Float.parseFloat;
import java.util.Locale;
import java.util.Map;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class printFunction {

    public static void printPage(Map printInfo) {
        PrintService ps = (PrintService) printInfo.get("ps");
        int numOfCompies = Integer.parseInt(printInfo.get("numOfCopies").toString());
        float scale = parseFloat(printInfo.get("scale").toString());
        JPanel panelToPrint = (JPanel) printInfo.get("panelToPrint");
        String jobName = panelToPrint.getName();

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        try {
            printerJob.setPrintService(ps);
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(panelToPrint, "Print Error: " + ex.getMessage());
        }

        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(OrientationRequested.LANDSCAPE);
        attributes.add(MediaSizeName.ISO_A4);
        attributes.add(PrintQuality.HIGH);
        attributes.add(new Copies(numOfCompies));
        attributes.add(new JobName(jobName, new Locale("Uz")));

        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                double width = pageFormat.getWidth();
                double height = pageFormat.getHeight();

                Graphics2D g2 = (Graphics2D) graphics;
                g2.translate(pageFormat.getImageableX() + (width * (1 - scale))/2, pageFormat.getImageableY() + (height * (1 - scale))/2);
                g2.scale(scale, scale);

                panelToPrint.paint(g2);

                return Printable.PAGE_EXISTS;
            }

        });

        try {
            //now call print method inside printerJob to print:
            printerJob.print(attributes);
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(panelToPrint, "Print Error: " + ex.getMessage());
        }

    }

}
