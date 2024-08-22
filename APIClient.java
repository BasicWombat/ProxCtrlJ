import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class APIClient {
    private static final String PROXMOX_API_URL = "https://your-proxmox-server:8006/api2/json/";
    private String csrfToken;
    private String authTicket;

    public void authenticate(String username, String password) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(PROXMOX_API_URL + "access/ticket");

            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);

            StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(credentials));
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(response.getEntity().getContent());

                // Extract CSRF Token and Auth Ticket
                this.csrfToken = responseJson.path("data").path("CSRFPreventionToken").asText();
                this.authTicket = responseJson.path("data").path("ticket").asText();
            }
        }
    }

    public void getVirtualMachines() throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(PROXMOX_API_URL + "nodes/your-node-name/qemu");

            httpGet.setHeader("CSRFPreventionToken", csrfToken);
            httpGet.setHeader("Cookie", "PVEAuthCookie=" + authTicket);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(response.getEntity().getContent());
                System.out.println(responseJson.toPrettyString());
            }
        }
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public String getAuthTicket() {
        return authTicket;
    }
}
