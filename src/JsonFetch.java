package src;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;

public class JsonFetch {

    private JsonObject jsonData;

    public JsonFetch(String jsonDataString) {
        this.jsonData = JsonParser.parseString(jsonDataString).getAsJsonObject();
    }

    
    /** 
     * @param textArea
     */
    public void displayData(JTextArea textArea) {
        String formattedData = formatJson(jsonData, 0);
        SwingUtilities.invokeLater(() -> textArea.setText(formattedData));
    }

    
    /** 
     * @param key
     * @return String
     */
    public String getValueByKey(String key) {
        if (jsonData != null && jsonData.has(key)) {
            return jsonData.get(key).toString();
        }
        return null;
    }

    
    /** 
     * @return JsonObject
     */
    public JsonObject getJsonData() {
        return this.jsonData;
    }
    

    /**
     * This method is used to get a nested value from the JSON object.
     * 
     * @param keys
     *            This is a variable argument parameter. The last element of
     *            this array is the key of the value to be fetched. All the
     *            elements before the last are keys of nested JSON objects.
     * @return The value of the key which is passed as the last element of the
     *         array. If any of the keys in the array don't exist in the JSON
     *         object, or if the JSON object is null, then this method will
     *         return null.
     */
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


    /**
     * Recursively formats a JsonElement into a string with proper indentation.
     * @param element The JsonElement to format
     * @param indent The amount of indentation to add to the formatted string
     * @return The formatted string
     */
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
