package taxi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class pcUUID {

    //Get Windows Machine UUID
    public static String getWindowsDeviceUUID() {
        try {
            String command = "wmic csproduct get UUID";
            StringBuilder output = new StringBuilder();

            Process SerNumProcess = Runtime.getRuntime().exec(command);
            BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

            String line;
            while ((line = sNumReader.readLine()) != null) {
                output.append(line).append("\n");
            }
            String uuid = output.toString().substring(output.indexOf("\n"), output.length()).trim();
            return uuid;
        } catch (IOException ex) {
            System.out.println("OutPut Error " + ex.getMessage());
        }
        return null;
    }

    //Get Mac Machine UUID
    public static String getMacUUID() {
        try {
            String command = "system_profiler SPHardwareDataType | awk '/UUID/ { print $3; }'";

            StringBuilder output = new StringBuilder();

            Process SerNumProcess = Runtime.getRuntime().exec(command);

            String uuid;
            try ( BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()))) {
                String line;
                while ((line = sNumReader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                uuid = output.toString().substring(output.indexOf("UUID: "), output.length()).replace("UUID: ", "");
                SerNumProcess.waitFor();
            }

            return uuid;
        } catch (IOException | InterruptedException ex) {
        }

        return null;
    }

    public static String getOSUUID() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {

            return getWindowsDeviceUUID();

        } else if (OS.contains("mac")) {
            return getMacUUID();
        }

        return null;
    }
}
