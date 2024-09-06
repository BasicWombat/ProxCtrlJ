import java.io.*;
import java.util.Properties;
import java.util.prefs.Preferences;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


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

    static String nodeStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName +"/status");
        
        // Create an instance of ProxmoxDataFetcher with the JSON response
        if (response == null) {
            System.err.println("Node Not Found.");
            return "Node Not Found.";
        } else {
            JsonFetch dataFetcher = new JsonFetch(response);

            String pveRelease = dataFetcher.getNestedValueByKey("data", "current-kernel", "release");
            String cpuModel = dataFetcher.getNestedValueByKey("data", "cpuinfo", "model");
            String cpuSockets = dataFetcher.getNestedValueByKey("data", "cpuinfo", "sockets");
            String cpuCores = dataFetcher.getNestedValueByKey("data", "cpuinfo", "cores");
            String bootMode = dataFetcher.getNestedValueByKey("data", "boot-info", "mode");
            String uptime = dataFetcher.getNestedValueByKey("data","uptime");

            // Convert uptime to days, hours, and minutes
            long uptimeSeconds = Long.parseLong(uptime);
            long days = uptimeSeconds / 86400; // 86400 seconds in a day
            long hours = (uptimeSeconds % 86400) / 3600; // 3600 seconds in an hour
            long minutes = (uptimeSeconds % 3600) / 60; // 60 seconds in a minute

            StringBuilder result = new StringBuilder();
            result.append("---------------\n");
            result.append("PVE Release: ").append(pveRelease).append("\n");
            result.append("---------------\n");
            result.append("CPU Model: ").append(cpuModel).append("\n");
            result.append("CPU Sockets: ").append(cpuSockets).append("\n");
            result.append("CPU Cores: ").append(cpuCores).append("\n");
            result.append("---------------\n");
            result.append("Boot Mode: ").append(bootMode).append("\n");
            result.append("Uptime: ").append(days).append(" days, ")
            .append(hours).append(" hours, ")
            .append(minutes).append(" minutes\n");
            result.append("---------------\n");

            return result.toString();
        }
        
    }

        // Method to convert bytes to megabytes
    public static double bytesToMegabytes(long bytes) {
        return bytes / 1_048_576.0; // 1 MB = 1,048,576 bytes
    }
    static String ramUsagedata(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName +"/status");
        
        if (response == null) {
            System.err.println("Node Not Found.");
            return "Node Not Found.";
        } else {

        // Create an instance of ProxmoxDataFetcher with the JSON response
        JsonFetch dataFetcher = new JsonFetch(response);
        String ramTotal = dataFetcher.getNestedValueByKey("data", "memory", "total");
        String ramUsed = dataFetcher.getNestedValueByKey("data", "memory", "used");

            // Convert memory values from bytes to megabytes
        long ramTotalBytes = Long.parseLong(ramTotal);
        long ramUsedBytes = Long.parseLong(ramUsed);
        
        double ramTotalMB = bytesToMegabytes(ramTotalBytes);
        double ramUsedMB = bytesToMegabytes(ramUsedBytes);

        // Create a formatted string with these values
        StringBuilder result = new StringBuilder();
        result.append("RAM Total: ").append(String.format("%.2f MB", ramTotalMB)).append("\n");
        result.append("RAM Used: ").append(String.format("%.2f MB", ramUsedMB)).append("\n");

        return result.toString();
        }
    }

    static String diskStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/disks/list");

        if (response == null) {
            System.err.println("Node Not Found.");
            return "Node Not Found.";
        } else {

        JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
        JsonArray disks = jsonData.getAsJsonArray("data");
        StringBuilder result = new StringBuilder();
        
        // Iterate through each disk entry
        for (JsonElement diskElement : disks) {
            JsonObject disk = diskElement.getAsJsonObject();
            
            // Extract relevant disk details
            String diskVendor = disk.has("vendor") ? disk.get("vendor").getAsString() : "N/A";
            String diskModel = disk.has("model") ? disk.get("model").getAsString() : "N/A";
            String diskName = disk.has("disk") ? disk.get("disk").getAsString() : "N/A";
            String diskSize = disk.has("size") ? disk.get("size").getAsString() : "N/A";
            String diskType = disk.has("type") ? disk.get("type").getAsString() : "N/A";
            String diskHealth = disk.has("health") ? disk.get("health").getAsString() : "N/A";

            // Append the disk details to the result
            result.append("Vendor: ").append(diskVendor).append("\n");
            result.append("Model: ").append(diskModel).append("\n");
            result.append("Disk Name: ").append(diskName).append("\n");
            result.append("Size: ").append(diskSize).append("\n");
            result.append("Type: ").append(diskType).append("\n");
            result.append("Health: ").append(diskHealth).append("\n");
            result.append("---------------\n");
        }
        
        return result.toString();
        }
    }

    static String networkStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/network");

        if (response == null) {
            System.err.println("Node Not Found.");
            return "Node Not Found.";
        } else {

        JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
        JsonArray net = jsonData.getAsJsonArray("data");
        StringBuilder result = new StringBuilder();
        
        // Iterate through each disk entry
        for (JsonElement netElement : net) {
            JsonObject network = netElement.getAsJsonObject();
            
            // Extract relevant disk details
            String netType = network.has("type") ? network.get("type").getAsString() : "N/A";
            String netActive = network.has("active") ? network.get("active").getAsString() : "N/A";
            String netAddr = network.has("address") ? network.get("address").getAsString() : "N/A";
            String netIface = network.has("iface") ? network.get("iface").getAsString() : "N/A";
            String netVlan = network.has("vlan-id") ? network.get("vlan-id").getAsString() : "N/A";
            String netVlanraw = network.has("vlan-raw-device") ? network.get("vlan-raw-device").getAsString() : "N/A";
            String netComments = network.has("comments") ? network.get("comments").getAsString() : "N/A";

            // Append the disk details to the result
            result.append("Type: ").append(netType).append("\n");
            result.append("Active: ").append(netActive).append("\n");
            result.append("Address: ").append(netAddr).append("\n");
            result.append("Interface: ").append(netIface).append("\n");
            result.append("VLAN ID: ").append(netVlan).append("\n");
            result.append("VLAN Raw Device: ").append(netVlanraw).append("\n");
            result.append("Comments: ").append(netComments).append("\n");
            result.append("---------------\n");
        }
        
        return result.toString();
    }
    }
    static String storageStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/storage");
        if (response == null) {
            System.err.println("Node Not Found.");
            return "Node Not Found.";
        } else {
        JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
        JsonArray storage = jsonData.getAsJsonArray("data");
        StringBuilder result = new StringBuilder();
        
        // Iterate through each disk entry
        for (JsonElement storElement : storage) {
            JsonObject stor = storElement.getAsJsonObject();
            
            // Extract relevant disk details
            String storName = stor.has("storage") ? stor.get("storage").getAsString() : "N/A";
            String storType = stor.has("type") ? stor.get("type").getAsString() : "N/A";
            String storActive = stor.has("active") ? stor.get("active").getAsString() : "N/A";
            String storEnabled = stor.has("enabled") ? stor.get("enabled").getAsString() : "N/A";
            String storContent = stor.has("content") ? stor.get("content").getAsString() : "N/A";
            String storUsed = stor.has("used_fraction") ? stor.get("used_fraction").getAsString() : "N/A";
            double usedFraction = Double.parseDouble(storUsed);
            double usedPct = usedFraction * 100;
            String formattedPct = String.format("%.2f%%", usedPct);

            // Append the disk details to the result
            result.append("Name: ").append(storName).append("\n");
            result.append("Type: ").append(storType).append("\n");
            result.append("Active: ").append(storActive).append("\n");
            result.append("Enabled: ").append(storEnabled).append("\n");
            result.append("Content: ").append(storContent).append("\n");
            result.append("Used: ").append(formattedPct).append("\n");
            result.append("---------------\n");
        }
        
        return result.toString();
    }
    }

    static String updateStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/apt/update");
        if (response == null) {
            System.err.println("Node Not Found.");
            return "Node Not Found.";
        } else {
        JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
        JsonArray update = jsonData.getAsJsonArray("data");
        StringBuilder result = new StringBuilder();
        
        // Iterate through each disk entry
        for (JsonElement updElement : update) {
            JsonObject updt = updElement.getAsJsonObject();
            
            // Extract relevant disk details
            String updTitle = updt.has("Title") ? updt.get("Title").getAsString() : "N/A";
            String updPriority = updt.has("Priority") ? updt.get("Priority").getAsString() : "N/A";
            String updOrigin = updt.has("Origin") ? updt.get("Origin").getAsString() : "N/A";
            String updVer = updt.has("Version") ? updt.get("Version").getAsString() : "N/A";
            String updVerold = updt.has("OldVersion") ? updt.get("OldVersion").getAsString() : "N/A";
            String updDesc = updt.has("Description") ? updt.get("Description").getAsString() : "N/A";

            // Append the disk details to the result
            result.append("Title: ").append(updTitle).append("\n");
            result.append("Priority: ").append(updPriority).append("\n");
            result.append("Origin: ").append(updOrigin).append("\n");
            result.append("Version: ").append(updVer).append("\n");
            result.append("Old Version: ").append(updVerold).append("\n");
            result.append("Description: ").append(updDesc).append("\n");
            result.append("---------------\n");
        }
        
        return result.toString();
    }
    }

    static int updateCount(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/apt/update");
        if (response == null) {
            System.err.println("Node Not Found.");
            return 0;
        } else {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data"); // Assuming the JSON contains an array named "data"
        int itemCount = jsonArray.size();
        return itemCount;
        }
    }
    

    public static void main(String[] args) {
        new mainFrame();
        
    }

}
