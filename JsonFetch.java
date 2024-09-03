import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonFetch extends JFrame {

    private String apiUrl;
    private JsonObject jsonData;

    public JsonFetch(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void fetchAndDisplayData(JTextArea textArea) {
        new Thread(() -> {
            try {
                String jsonDataString = fetchDataFromApi();
                jsonData = JsonParser.parseString(jsonDataString).getAsJsonObject();
                String formattedData = formatJson(jsonData, 0);

                SwingUtilities.invokeLater(() -> textArea.setText(formattedData));
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> textArea.setText("Error fetching data"));
            }
        }).start();
    }

    public String getValueByKey(String key) {
        if (jsonData != null && jsonData.has(key)) {
            return jsonData.get(key).toString();
        }
        return null;
    }

    public String getNestedValueByKey(String... keys) {
        JsonObject temp = jsonData;
        for (int i = 0; i < keys.length - 1; i++) {
            if (temp != null && temp.has(keys[i])) {
                temp = temp.getAsJsonObject(keys[i]);
            } else {
                return null;
            }
        }
        return temp != null && temp.has(keys[keys.length - 1]) ? temp.get(keys[keys.length - 1]).toString() : null;
    }

    private String fetchDataFromApi() throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    private String formatJson(JsonElement element, int indent) {
        StringBuilder sb = new StringBuilder();
        String indentStr = " ".repeat(indent);

        if (element.isJsonObject()) {
            sb.append("{\n");
            JsonObject obj = element.getAsJsonObject();
            for (String key : obj.keySet()) {
                sb.append(indentStr).append("  \"").append(key).append("\": ");
                sb.append(formatJson(obj.get(key), indent + 2)).append(",\n");
            }
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 2); // Remove last comma
            }
            sb.append("\n").append(indentStr).append("}");
        } else if (element.isJsonArray()) {
            sb.append("[\n");
            for (JsonElement el : element.getAsJsonArray()) {
                sb.append(formatJson(el, indent + 2)).append(",\n");
            }
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 2); // Remove last comma
            }
            sb.append("\n").append(indentStr).append("]");
        } else if (element.isJsonPrimitive()) {
            sb.append(element.getAsJsonPrimitive().toString());
        }

        return sb.toString();
    }
}
