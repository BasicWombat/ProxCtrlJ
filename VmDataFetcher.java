import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VmDataFetcher {
    private String response;

    public VmDataFetcher(String response) {
        this.response = response;
    }

    public String getVmId(String vmName) {
        try {
            JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
            JsonArray vmArray = jsonData.getAsJsonArray("data");
            System.out.println("Geting ID for Node: " + vmName);

            // Iterate through the VM array
            for (JsonElement vmElement : vmArray) {
                JsonObject vmObject = vmElement.getAsJsonObject();

                String vmNodeName = vmObject.has("name") ? vmObject.get("name").getAsString() : "N/A";
                System.out.println("VM Name: " + vmNodeName);
                
                // Check if the node name matches
                if (vmNodeName.equals(vmName)) {
                    String vmId = vmObject.has("name") ? vmObject.get("vmid").getAsString() : "N/A"; // Adjust field name as per actual JSON structure
                    System.out.println("VM ID: " + vmId);
                    return vmId; // Return the VM ID when found
                }
            }
            // Return a message or null if the VM ID was not found
            return "VM ID not found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing JSON";
        }
    }

    public String getVmstatus(String vmName) {
        try {
            JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
            JsonArray vmArray = jsonData.getAsJsonArray("data");
            System.out.println("Geting status for Node: " + vmName);

            // Iterate through the VM array
            for (JsonElement vmElement : vmArray) {
                JsonObject vmObject = vmElement.getAsJsonObject();

                String vmNodeName = vmObject.has("name") ? vmObject.get("name").getAsString() : "N/A";
                System.out.println("VM Name: " + vmNodeName);
                
                // Check if the node name matches
                if (vmNodeName.equals(vmName)) {
                    String vmStaus = vmObject.has("name") ? vmObject.get("status").getAsString() : "N/A";
                    System.out.println("VM Status: " + vmStaus);
                    return vmStaus; // Return the VM ID when found
                }
            }
            // Return a message or null if the VM was not found
            return "VM Status not found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing JSON";
        }
    }

    public String getVmuptime(String vmName) {
        try {
            JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
            JsonArray vmArray = jsonData.getAsJsonArray("data");
            System.out.println("Geting status for Node: " + vmName);

            // Iterate through the VM array
            for (JsonElement vmElement : vmArray) {
                JsonObject vmObject = vmElement.getAsJsonObject();

                String vmNodeName = vmObject.has("name") ? vmObject.get("name").getAsString() : "N/A";
                System.out.println("VM Name: " + vmNodeName);
                
                // Check if the node name matches
                if (vmNodeName.equals(vmName)) {
                    String vmUptime = vmObject.has("name") ? vmObject.get("uptime").getAsString() : "N/A";
                    System.out.println("VM Uptime: " + vmUptime);
                    return vmUptime;
                }
            }
            // Return a message or null if the VM  was not found
            return "VM Uptime not found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing JSON";
        }
    }
}
