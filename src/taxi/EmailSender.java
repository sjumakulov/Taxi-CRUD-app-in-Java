package taxi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String telegramAddress) {

        Properties props = new Properties();

        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        String myEmail = "wwyyuuoohh@gmail.com";
        String passWord = "sherzod0799594";

        javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(myEmail, passWord);
            }
        });

        javax.mail.Message message = prepareMessage(session, myEmail, telegramAddress);
        try {
            javax.mail.Transport.send(message);
        } catch (javax.mail.MessagingException ex) {
            java.util.logging.Logger.getLogger(Taxi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private static javax.mail.Message prepareMessage(javax.mail.Session session, String myEmail, String telegramAddress) {

        javax.mail.Message message = new javax.mail.internet.MimeMessage(session);

        try {
            message.setFrom(new javax.mail.internet.InternetAddress(myEmail));
            message.setRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress("ninjaenglishuz@gmail.com"));
            message.setSubject(telegramAddress);
            message.setText(getOSUUID());
            return message;

        } catch (javax.mail.MessagingException ex) {
            java.util.logging.Logger.getLogger(Taxi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        return null;
    }

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

    public static String OS() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {

        } else if (OS.contains("mac")) {

        } else if (OS.contains("sunos")) {

        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {

        }

        return OS;
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
