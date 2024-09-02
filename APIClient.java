import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.prefs.Preferences;



public class APIClient {
    private String host;
    private String hostPort;
    private String apiTokenID;
    private String apiSecret;
    private String node;


    public APIClient() {
        loadSettings();
    }

    // private void loadSettings() {
    //     Properties properties = new Properties();
    //     try (FileInputStream fis = new FileInputStream("settings.properties")) {
    //         properties.load(fis);
    //         host = properties.getProperty("host");
    //         hostPort = properties.getProperty("hostport");
    //         apiTokenID = properties.getProperty("apiTokenID");
    //         apiSecret = properties.getProperty("apiSecret");
    //         node = properties.getProperty("node");

    //         if (host == null || hostPort == null || apiTokenID == null || apiSecret == null || node == null) {
    //             showErrorDialog("Missing API settings in settings.properties. The program will continue running, but some features may not work correctly.");
    //         }
    //     } catch (IOException e) {
    //         showErrorDialog("Failed to load settings: " + e.getMessage());
    //     }
    // }

    private void loadSettings() {
        
        // Get the user preferences node for this class
        Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
        host = usrprefs.get("host", null);
        hostPort = usrprefs.get("hostport", null);
        apiTokenID = usrprefs.get("apiTokenID", null);
        apiSecret = usrprefs.get("apiSecret", null);
        node = usrprefs.get("node", null);
    }

    private void showErrorDialog(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog("Error");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    HttpURLConnection createConnection(String endpoint, String method) throws IOException {
        try {
            String urlString = "https://" + host + ":" + hostPort + endpoint;
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Authorization", "PVEAPIToken=" + apiTokenID + "=" + apiSecret);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            return connection;
        } catch (URISyntaxException e) {
            showErrorDialog("Invalid URL syntax: " + e.getMessage());
            throw new IOException("Invalid URL syntax: " + e.getMessage(), e);
        }
    }

    public String readData(String endpoint) {
        if (host == null || hostPort == null || apiTokenID == null || apiSecret == null || node == null) {
            showErrorDialog("API settings are missing. Cannot perform the request.");
            return null;
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = createConnection(endpoint, "GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the input stream
                System.out.println("Reading Data...");
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    System.out.println(line);
                }
                return response.toString(); // Return the JSON response
            } else {
                showErrorDialog("Failed to read data: HTTP error code " + responseCode);
            }
        } catch (IOException e) {
            showErrorDialog("Failed to connect to Proxmox API: " + e.getMessage());
        } finally {
            // Ensure the reader and connection are closed properly
            try {
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                showErrorDialog("Failed to close resources: " + e.getMessage());
            }
        }
        return null;
    }

    public void writeData(String endpoint, String jsonPayload) {
        if (host == null || hostPort == null || apiTokenID == null || apiSecret == null || node == null) {
            showErrorDialog("API settings are missing. Cannot perform the request.");
            return;
        }

        try {
            HttpURLConnection connection = createConnection(endpoint, "POST");
            connection.getOutputStream().write(jsonPayload.getBytes());
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Code to handle successful write
            } else {
                showErrorDialog("Failed to write data: HTTP error code " + responseCode);
            }
        } catch (IOException e) {
            showErrorDialog("Failed to connect to Proxmox API: " + e.getMessage());
        }
    }

    public void disconnect() {
        // Clear the stored credentials or invalidate the session
        apiTokenID = null;
        apiSecret = null;
        host = null;
        hostPort = null;
        node = null;
        showErrorDialog("Disconnected from Proxmox API.");
    }
}
