import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Status {
    private Properties properties;
    static Preferences usrprefs = Preferences.userNodeForPackage(Main.class);

    public Status(String propertiesFilePath) {
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

    public class NodeStatusPanel extends JPanel {

        public NodeStatusPanel() {
            setLayout(new GridLayout(0, 2, 10, 10));  // 2 columns layout for label-value pairs
        }

        public void displayNodeStatus() {
            APIClient apiclient = new APIClient();
            String nodeName = usrprefs.get("node", null);
            String response = apiclient.readData("/api2/json/nodes/" + nodeName + "/status");

            if (response == null) {
                JOptionPane.showMessageDialog(this, "Node Not Found", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JsonFetch dataFetcher = new JsonFetch(response);

                String pveRelease = dataFetcher.getNestedValueByKey("data", "current-kernel", "release");
                String cpuModel = dataFetcher.getNestedValueByKey("data", "cpuinfo", "model");
                String cpuSockets = dataFetcher.getNestedValueByKey("data", "cpuinfo", "sockets");
                String cpuCores = dataFetcher.getNestedValueByKey("data", "cpuinfo", "cores");
                String bootMode = dataFetcher.getNestedValueByKey("data", "boot-info", "mode");
                String uptime = dataFetcher.getNestedValueByKey("data", "uptime");

                // Convert uptime to days, hours, and minutes
                long uptimeSeconds = Long.parseLong(uptime);
                long days = uptimeSeconds / 86400; // 86400 seconds in a day
                long hours = (uptimeSeconds % 86400) / 3600; // 3600 seconds in an hour
                long minutes = (uptimeSeconds % 3600) / 60; // 60 seconds in a minute

                // Clear previous data
                this.removeAll();

                // Add labels to the panel
                addLabel("PVE Release:", pveRelease);
                addLabel("CPU Model:", cpuModel);
                addLabel("CPU Sockets:", cpuSockets);
                addLabel("CPU Cores:", cpuCores);
                addLabel("Boot Mode:", bootMode);
                addLabel("Uptime:", days + " days, " + hours + " hours, " + minutes + " minutes");

                // Refresh the panel to display the new data
                revalidate();
                repaint();
            }
    }

    // Helper method to add a label with a value
    private void addLabel(String labelText, String value) {
        JLabel label = new JLabel(labelText);
        JLabel valueLabel = new JLabel(value);
        add(label);
        add(valueLabel);
    }
}

    static String nodeStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName +"/status");
        
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
            int updateCnt = updateCount();
            String updateCntStr = Integer.toString(updateCnt);
            
            // Convert uptime to days, hours, and minutes
            long uptimeSeconds = Long.parseLong(uptime);
            long days = uptimeSeconds / 86400; // 86400 seconds in a day
            long hours = (uptimeSeconds % 86400) / 3600; // 3600 seconds in an hour
            long minutes = (uptimeSeconds % 3600) / 60; // 60 seconds in a minute

            StringBuilder result = new StringBuilder();
            result.append("PVE Release: ").append(pveRelease).append("\n");
            result.append("---------------\n");
            result.append("CPU Model: ").append(cpuModel).append("\n");
            result.append("CPU Sockets: ").append(cpuSockets).append("\n");
            result.append("CPU Cores: ").append(cpuCores).append("\n");
            result.append("---------------\n");
            result.append("Boot Mode: ").append(bootMode).append("\n");
            result.append("Uptime: ").append(days).append(" days, ").append(hours).append(" hours, ").append(minutes).append(" minutes\n");
            result.append("Update Count: " ).append(updateCntStr).append("\n");
            result.append("---------------\n");

            return result.toString();
        }
        
    }

    // Method to convert bytes to megabytes
    public static double bytesToMegabytes(long bytes) {
        return bytes / 1_048_576.0; // 1 MB = 1,048,576 bytes
    }



    static String ramUsagedata(){
        // RAM Usage Data
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

    static JScrollPane diskStatus(){
    APIClient apiclient = new APIClient();
    String nodeName = usrprefs.get("node", null);
    String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/disks/list");

    if (response == null) {
        System.err.println("Node Not Found.");
        return new JScrollPane(new JLabel("Node Not Found.")); // Return a label wrapped in JScrollPane in case of error
    } else {

        JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
        JsonArray disks = jsonData.getAsJsonArray("data");
        
        // Column headers for the table
        String[] columnNames = {"Vendor", "Model", "Disk Name", "Size", "Type", "Health"};
        
        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
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
            
            // Add row data to the table model
            Object[] rowData = {diskVendor, diskModel, diskName, diskSize, diskType, diskHealth};
            tableModel.addRow(rowData);
        }
        
        // Create JTable with the model and wrap it in a scroll pane
        JTable diskTable = new JTable(tableModel);
        diskTable.setDefaultEditor(Object.class, null);
        return new JScrollPane(diskTable); // Return the scroll pane containing the table
    }
}

    static JScrollPane networkStatus(){
    APIClient apiclient = new APIClient();
    String nodeName = usrprefs.get("node", null);
    String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/network");

    if (response == null) {
        System.err.println("Node Not Found.");
        return new JScrollPane(new JLabel("Node Not Found.")); // Return a label in case of error
    } else {

        JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
        JsonArray net = jsonData.getAsJsonArray("data");

        // Column headers for the table
        String[] columnNames = {"Type", "Active", "Address", "Interface", "VLAN ID", "VLAN Raw Device", "Comments"};
        
        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Iterate through each network entry
        for (JsonElement netElement : net) {
            JsonObject network = netElement.getAsJsonObject();
            
            // Extract relevant network details
            String netType = network.has("type") ? network.get("type").getAsString() : "N/A";
            String netActive = network.has("active") ? network.get("active").getAsString() : "N/A";
            String netAddr = network.has("address") ? network.get("address").getAsString() : "N/A";
            String netIface = network.has("iface") ? network.get("iface").getAsString() : "N/A";
            String netVlan = network.has("vlan-id") ? network.get("vlan-id").getAsString() : "N/A";
            String netVlanraw = network.has("vlan-raw-device") ? network.get("vlan-raw-device").getAsString() : "N/A";
            String netComments = network.has("comments") ? network.get("comments").getAsString() : "N/A";

            // Add row data to the table model
            Object[] rowData = {netType, netActive, netAddr, netIface, netVlan, netVlanraw, netComments};
            tableModel.addRow(rowData);
        }

        // Create JTable with the model and wrap it in a scroll pane
        JTable networkTable = new JTable(tableModel);
        networkTable.setDefaultEditor(Object.class, null);
        return new JScrollPane(networkTable); // Return the scroll pane containing the table
    }

    }
    static JScrollPane storageStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/storage");
    
        if (response == null) {
            System.err.println("Node Not Found.");
            return new JScrollPane(new JLabel("Node Not Found.")); // Return a label in case of error
        } else {
    
            JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
            JsonArray storage = jsonData.getAsJsonArray("data");
    
            // Column headers for the table
            String[] columnNames = {"Name", "Type", "Active", "Enabled", "Content", "Used"};
            
            // Create table model
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    
            // Iterate through each storage entry
            for (JsonElement storElement : storage) {
                JsonObject stor = storElement.getAsJsonObject();
                
                // Extract relevant storage details
                String storName = stor.has("storage") ? stor.get("storage").getAsString() : "N/A";
                String storType = stor.has("type") ? stor.get("type").getAsString() : "N/A";
                String storActive = stor.has("active") ? stor.get("active").getAsString() : "N/A";
                String storEnabled = stor.has("enabled") ? stor.get("enabled").getAsString() : "N/A";
                String storContent = stor.has("content") ? stor.get("content").getAsString() : "N/A";
                String storUsed = stor.has("used_fraction") ? stor.get("used_fraction").getAsString() : "N/A";
                double usedFraction = Double.parseDouble(storUsed);
                double usedPct = usedFraction * 100;
                String formattedPct = String.format("%.2f%%", usedPct);
    
                // Add row data to the table model
                Object[] rowData = {storName, storType, storActive, storEnabled, storContent, formattedPct};
                tableModel.addRow(rowData);
            }
    
            // Create JTable with the model and wrap it in a scroll pane
            JTable storageTable = new JTable(tableModel);
            storageTable.setDefaultEditor(Object.class, null);
            return new JScrollPane(storageTable); // Return the scroll pane containing the table
        }
    }

    
    static JScrollPane updateStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/apt/update");
    
        if (response == null) {
            System.err.println("Node Not Found.");
            return new JScrollPane(new JLabel("Node Not Found.")); // Return a label in case of error
        } else {
    
            JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
            JsonArray update = jsonData.getAsJsonArray("data");
    
            // Column headers for the table
            String[] columnNames = {"Title", "Priority", "Origin", "Version", "Old Version", "Description"};
            
            // Create table model
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    
            // Iterate through each update entry
            for (JsonElement updElement : update) {
                JsonObject updt = updElement.getAsJsonObject();
                
                // Extract relevant update details
                String updTitle = updt.has("Title") ? updt.get("Title").getAsString() : "N/A";
                String updPriority = updt.has("Priority") ? updt.get("Priority").getAsString() : "N/A";
                String updOrigin = updt.has("Origin") ? updt.get("Origin").getAsString() : "N/A";
                String updVer = updt.has("Version") ? updt.get("Version").getAsString() : "N/A";
                String updVerold = updt.has("OldVersion") ? updt.get("OldVersion").getAsString() : "N/A";
                String updDesc = updt.has("Description") ? updt.get("Description").getAsString() : "N/A";
    
                // Add row data to the table model
                Object[] rowData = {updTitle, updPriority, updOrigin, updVer, updVerold, updDesc};
                tableModel.addRow(rowData);
            }
    
            // Create JTable with the model and wrap it in a scroll pane
            JTable updateTable = new JTable(tableModel);
            updateTable.setDefaultEditor(Object.class, null);
            // Allows the user to sort the columns of the table
            //updateTable.setAutoCreateRowSorter(true);
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(updateTable.getModel());
            updateTable.setRowSorter(sorter);

            // Define a custom comparator for column 1
            sorter.setComparator(1, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    // Define the custom ranking order
                    List<String> order = List.of("required", "important", "standard", "optional");

                    // Get the index for each string
                    int index1 = order.indexOf(o1);
                    int index2 = order.indexOf(o2);

                    // If the value is not in the list, assign a high index
                    index1 = (index1 == -1) ? Integer.MAX_VALUE : index1;
                    index2 = (index2 == -1) ? Integer.MAX_VALUE : index2;

                    return Integer.compare(index1, index2);
                }
            });

            // Define default sort keys (column 1 with the custom comparator)
            List<RowSorter.SortKey> sortKeys = List.of(
                new RowSorter.SortKey(1, SortOrder.ASCENDING) // Column 1 with ascending order
            );
            sorter.setSortKeys(sortKeys);
            return new JScrollPane(updateTable); // Return the scroll pane containing the table
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
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        int itemCount = jsonArray.size();
        return itemCount;
        }
    }
}
