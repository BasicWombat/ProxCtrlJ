package src;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.io.*;
import java.net.*;
import java.util.prefs.Preferences;

public class APIClient {
    private static String host;
    private static String hostPort;
    private static String apiTokenID;
    private static String apiSecret;
    private static String node;

    public APIClient() {
        loadSettings();
    }

    private void loadSettings() {
        // Get the user preferences node for this class
        Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
        host = usrprefs.get("host", null);
        hostPort = usrprefs.get("hostport", null);
        apiTokenID = usrprefs.get("apiTokenID", null);
        apiSecret = usrprefs.get("apiSecret", null);
        node = usrprefs.get("node", null);
    }

    /** 
     * @param message
     */
    private void showErrorDialog(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog("Error");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    /** 
     * @param endpoint
     * @param method
     * @return HttpURLConnection
     * @throws IOException
     */
    private HttpURLConnection createConnection(String endpoint, String method) throws IOException {
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
        } catch (URISyntaxException | IOException e) {
            System.err.println("Failed to create connection: " + e.getMessage());
            return null;
        }
    }

    /** 
     * @param endpoint
     * @return String
     */
    public String readData(String endpoint) {
        if (host == null || hostPort == null || apiTokenID == null || apiSecret == null || node == null) {
            System.err.println("API settings are missing. Cannot perform the request.");
            return null;
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = createConnection(endpoint, "GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the input stream
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString(); // Return the JSON response
            } else {
                System.err.println("Failed to read data: HTTP error code " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("Failed to connect to Proxmox API: " + e.getMessage());
        } finally {
            // Ensure the reader and connection are closed properly
            try {
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
        return null;
    }

    /** 
     * @param endpoint
     * @param jsonPayload
     * @return String
     */
    public String postData(String endpoint, String jsonPayload) {
        if (host == null || hostPort == null || apiTokenID == null || apiSecret == null || node == null) {
            System.err.println("API settings are missing. Cannot perform the request.");
            return null;
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = createConnection(endpoint, "POST");
            // Send JSON payload in the POST request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the input stream
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString(); // Return the JSON response
            } else {
                System.err.println("Failed to post data: HTTP error code " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("Failed to connect to Proxmox API: " + e.getMessage());
        } finally {
            // Ensure the reader and connection are closed properly
            try {
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
        return null;
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

    /** 
     * @param vmid
     * @return JsonFetch
     * @throws Exception
     */
    // Method to get VNC ticket and port
    public static JsonFetch getVncTicketAndPort(String vmid) throws Exception {
        String urlString = "https://" + host + ":" + hostPort + "/api2/json/nodes/" + node + "/qemu/" + vmid + "/vncproxy";
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "PVEAPIToken=" + apiTokenID + "=" + apiSecret);
        connection.setRequestMethod("POST");  // POST request to get ticket and port

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        // Parse JSON response and return it
        return new JsonFetch(response.toString());
    }

    // Method to get VNC WebSocket URL using the ticket and port
    public static String getVNCWebSocketUrl(String vmid, String vmName) throws Exception {
        // First, retrieve the VNC ticket and port
        JsonFetch vncResponse = getVncTicketAndPort(vmid);
        String vncticket = vncResponse.getNestedValueByKey("data", "ticket");
        String port = vncResponse.getNestedValueByKey("data", "port");
        // Add null checks for ticket and port
        if (vncticket == null) {
            throw new Exception("VNC ticket is missing from the response");
        }

        if (port == null) {
            throw new Exception("VNC port is missing from the response");
        }

        String noVncUrl = "https://" + host + ":" + hostPort + "/?console=kvm&novnc=1&vmid=" + vmid + "&vmname=" + URLEncoder.encode(vmName, "UTF-8");
        System.out.println(noVncUrl);
        return noVncUrl;
    }

    public boolean triggerUpdateCheck() {
    if (node == null) {
        System.err.println("Node not set in preferences.");
        return false;
    }

    String endpoint = "/api2/json/nodes/" + node + "/apt/update";
    String payload = "{}"; // Empty JSON payload required by the API

    String response = postData(endpoint, payload);
    if (response != null) {
        System.out.println("Successfully triggered update check: " + response);
        return true;
    } else {
        System.err.println("Failed to trigger update check.");
        return false;
    }
}

    


}
