package taxi;

import javax.print.PrintService;

public class PrintServiceItem {

    private final PrintService printService;
    private final String name;

    public PrintServiceItem(PrintService printService, String name) {
        this.printService = printService;
        this.name = name;
    }

    public PrintService getPrintService() {
        return printService;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
