import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;

public class JsonFetch {

    private JsonObject jsonData;

    public JsonFetch(String jsonDataString) {
        this.jsonData = JsonParser.parseString(jsonDataString).getAsJsonObject();
    }

    public void displayData(JTextArea textArea) {
        String formattedData = formatJson(jsonData, 0);
        SwingUtilities.invokeLater(() -> textArea.setText(formattedData));
    }

    public String getValueByKey(String key) {
        if (jsonData != null && jsonData.has(key)) {
            return jsonData.get(key).toString();
        }
        return null;
    }

    public JsonObject getJsonData() {
        return this.jsonData;
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

    String formatJson(JsonElement element, int indent) {
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
