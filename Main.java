import java.io.*;
import java.util.Properties;
import java.util.prefs.Preferences;
import javax.swing.UIManager;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;

public class Main {
    
    private Properties properties;
    static Preferences usrprefs = Preferences.userNodeForPackage(Main.class);

    public Main(String propertiesFilePath) {
        // Load properties
        properties = new Properties();
        try (InputStream input = new FileInputStream(propertiesFilePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public String getProperty(String key) {
        return properties.getProperty(key, "Not Found");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatArcOrangeIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        new mainFrame();
        
    }

}
