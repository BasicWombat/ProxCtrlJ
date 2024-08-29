import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("settings.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find settings.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getApiTokenID() {
        return properties.getProperty("apiTokenID");
    }

    public static String getApiSecret() {
        return properties.getProperty("apiSecret");
    }

    public static String getHost() {
        return properties.getProperty("host");
    }

    public static String getHostPort() {
        return properties.getProperty("hostport");
    }
    
    public static String getNode() {
        return properties.getProperty("node");
    }
}