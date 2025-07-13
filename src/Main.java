package src;
/** 
* ProxCtrlJ
* Desktop Application Client for Proxmox VE
*
* @author: https://github.com/BasicWombat
* @since: 2023-08-15
* @license: MIT
*/

import java.io.*;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;
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

    
    /** 
     * @param key
     * @return String
     */
    public String getProperty(String key) {
      
      /** 
       * @param key
       * @param args
       */
        return properties.getProperty(key, "Not Found");
    }

    public static void main(String[] args) {
        /** 
        * Makes the application look and feel FlatArcOrangeIJTheme
        */

        /** 
        * Sets the main application theme to FlatArcOrangeIJTheme
        */
        try {
            UIManager.setLookAndFeel( new FlatArcOrangeIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        // Show splash screen first
        SplashScreen splash = new SplashScreen();
        splash.showSplash(2000); // Show for 2 seconds

        /** 
        * Runs the application
        */
        SwingUtilities.invokeLater(() -> {
            new mainFrame(); // Initialize the main frame
        });
        
    }

    public boolean waitForUpdateToComplete(APIClient apiclient, int timeoutMillis) {
    String endpoint = "/api2/json/nodes/" + Preferences.userNodeForPackage(Main.class).get("node", null) + "/apt/update";
    int elapsed = 0;
    int interval = 1000; // 1 second

    while (elapsed < timeoutMillis) {
        String response = apiclient.readData(endpoint);
        if (response != null) {
            JsonFetch checker = new JsonFetch(response);
            if (checker.isUpdateCheckDone()) {
                return true;
            }
        }

        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            break;
        }
        elapsed += interval;
    }

    return false;
}


}
