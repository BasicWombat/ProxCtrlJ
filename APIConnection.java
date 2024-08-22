import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.TimeValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class APIConnection {

    private static final String BASE_URL = "https://proxmox.example.com:8006/api2/json/";
    private final CloseableHttpClient httpClient;
    private final String authToken;

    public APIConnection(String authToken) {
        this.authToken = authToken;

        this.httpClient = HttpClients.custom()
            .setConnectionManager(
                PoolingHttpClientConnectionManagerBuilder.create()
                    .setConnectionTimeToLive(TimeValue.ofSeconds(30)) // Time to live setting
                    .build())
            .build();
    }

    /**
     * Makes a GET request to the Proxmox API and returns the response as a JsonNode.
     *
     * @param endpoint The API endpoint to call (relative to BASE_URL).
     * @return The API response as a JsonNode.
     * @throws IOException If an I/O error occurs.
     * @throws ParseException 
     */
    public static JsonNode makeGetRequest(String endpoint) throws IOException, ParseException {
        String url = BASE_URL + endpoint;
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "PVEAPIToken=" + authToken);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(jsonResponse);
            } else {
                throw new IOException("Received non-OK response: " + statusCode);
            }
        }
    }

    /**
     * Closes the HTTP client and releases any resources associated with it.
     */
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParseException {
        // Example usage of the ProxmoxAPIConnection class
        String apiToken = "user@pam:12345-myapitoken-67890"; // Replace with your actual API token
        APIConnection proxmoxAPIConnection = new APIConnection(apiToken);

        try {
            // Example: Get a list of VMs on a specific node
            JsonNode response = APIConnection.makeGetRequest("nodes/your-node-name/qemu");
            System.out.println(response.toPrettyString());

            // Perform additional API requests as needed...

        } catch (IOException e) {
            System.err.println("Error making API request: " + e.getMessage());
        } finally {
            // Ensure the connection is properly closed
            proxmoxAPIConnection.close();
        }
    }
}
