import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import java.io.*;
import java.util.Properties;
import java.util.prefs.Preferences;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.swing.FontIcon;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;


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
        JsonFetch dataFetcher = new JsonFetch(response);
            
        // Example: Extracting specific values
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

        // Example: Creating a formatted string with these values
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

        // Method to convert bytes to megabytes
        public static double bytesToMegabytes(long bytes) {
        return bytes / 1_048_576.0; // 1 MB = 1,048,576 bytes
    }
    static String ramUsagedata(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName +"/status");
        
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


    static String diskStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/disks/list");

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

    static String networkStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/network");
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
    static String storageStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/storage");
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

    static String updateStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/apt/update");
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

    static int updateCount(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/apt/update");
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data"); // Assuming the JSON contains an array named "data"
        int itemCount = jsonArray.size();
        return itemCount;
    }


    public static void main(String[] args) {
            // Main Window Configuration
            JFrame mainFrame = new JFrame("ProxCtrlJ");
            JLabel heading = new JLabel("ProxCtrlJ"); 
            heading.setBounds(50,10, 200,30);
            heading.setFont(new Font("Sans Serif", Font.BOLD, 28));
            
            // Main Menu Bar
            JMenu fileItem, viewItem, createItem, helpItem;
            JMenuItem connectItem, disconnectItem, settingsItem, createvmItem, createctItem, refreshItem, quitItem, aboutItem;
            JMenuBar mb = new JMenuBar();

            // File Menu
            fileItem = new JMenu("File");
            viewItem = new JMenu("View");
            connectItem = new JMenuItem("Connect");
            disconnectItem = new JMenuItem("Disconnect");
            settingsItem = new JMenuItem("Settings");
            quitItem = new JMenuItem("Quit");
            fileItem.add(connectItem);
            fileItem.add(disconnectItem);
            fileItem.add(settingsItem);
            fileItem.add(quitItem);
            mb.add(fileItem);

            //View Menu
            refreshItem = new JMenuItem("Refresh");
            viewItem.add(refreshItem);
            mb.add(viewItem);

            //Create Menu
            createItem = new JMenu("Create");
            createvmItem = new JMenuItem("Create VM");
            createctItem = new JMenuItem("Create CT");
            createItem.add(createvmItem);
            createItem.add(createctItem);
            mb.add(createItem);

            //Help Menu
            helpItem = new JMenu("Help");
            aboutItem = new JMenuItem("About");
            helpItem.add(aboutItem);
            mb.add(helpItem);

            mainFrame.setJMenuBar(mb);

            // Server Tree View
            // Instantiate ProxmoxTree and get the tree model
            ProxmoxTree proxmoxTree = new ProxmoxTree();
            DefaultTreeModel proxTree = proxmoxTree.getProxmoxTreeModel();  
            JTree jt=new JTree(proxTree);
            jt.setBounds (10, 50, 240, 545);
            jt.setBorder(BorderFactory.createEtchedBorder());
                    // Add a mouse listener to handle double-clicks
            jt.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {  // Check for double-click
                        TreePath path = jt.getPathForLocation(e.getX(), e.getY());
                        if (path != null) {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                            String nodeInfo = node.getUserObject().toString();
                            System.out.println("Double-clicked node: " + nodeInfo);
                            
                            // Open new window for node
                            new vmNodeWdw(nodeInfo);
                        }
                    }
                }
            });

            connectItem.addActionListener(e -> {
                new connectWdw();
            });

            // Settings Window
            settingsItem.addActionListener(e -> {
                new settingsWdw();
            });

            // About Window
            aboutItem.addActionListener(e -> {
                new AboutWdw();
            });
            
            createvmItem.addActionListener(e -> {
                new createvmWdw();
            });

            createctItem.addActionListener(e -> {
                new createctWdw();
            });

            disconnectItem.addActionListener(e -> {
                JOptionPane.showMessageDialog(mainFrame,"Disconnected!");  
            });

            // Quit Application from Menu
            quitItem.addActionListener(e -> System.exit(0));

            //Tab Area

            //Panel 1: Node Status
            JPanel p1=new JPanel();
            p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
            String p1data = nodeStatus();
            String p1ram = ramUsagedata();
            JTextArea p1ta = new JTextArea(p1data);
            JTextArea p1ta2 = new JTextArea(p1ram);
            p1ta.setEditable(false);
            p1ta.setBounds(0, 0, 100, 100);
            JScrollPane p1sp = new JScrollPane(p1ta);
            p1sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            p1sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JScrollPane p1sp2 = new JScrollPane(p1ta2);
            p1sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            p1sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            p1.add(p1sp);
            p1.add(p1sp2);


            //Panel 2: Disk
            JPanel p2=new JPanel();
            p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
            String p2data = diskStatus();
            JTextArea p2ta = new JTextArea(p2data);
            p2ta.setEditable(false);
            p2ta.setBounds(0, 0, 100, 100);
            JScrollPane p2sp = new JScrollPane(p2ta);
            p2sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            p2sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            p2.add(p2sp);

            //Panel 3: Network
            JPanel p3=new JPanel();
            p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
            String p3data = networkStatus();
            JTextArea p3ta = new JTextArea(p3data);
            p3ta.setEditable(false);
            p3ta.setBounds(0, 0, 100, 100);
            JScrollPane p3sp = new JScrollPane(p3ta);
            p3sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            p3sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            p3.add(p3sp);


            //Panel 4: Storage
            JPanel p4=new JPanel();
            p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
            String p4data = storageStatus();
            JTextArea p4ta = new JTextArea(p4data);
            p4ta.setEditable(false);
            p4ta.setBounds(0, 0, 100, 100);
            JScrollPane p4sp = new JScrollPane(p4ta);
            p4sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            p4sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            p4.add(p4sp);

            //Panel 5: Updates
            JPanel p5=new JPanel();
            p5.setLayout(new BoxLayout(p5, BoxLayout.Y_AXIS));
            String p5data = updateStatus();
            int updateCountTotal = updateCount();
            JTextArea p5ta = new JTextArea(p5data);
            FontIcon updateIcon = new FontIcon();
            updateIcon.setIkon(FluentUiFilledMZ.PHONE_UPDATE_24);
            updateIcon.setIconSize(24);
            if (updateCountTotal > 5) {
                updateIcon.setIconColor(Color.RED);
            }
            JLabel updateCount = new JLabel("Update Count: " + updateCountTotal);
            updateCount.setIcon(updateIcon);
            p5ta.setEditable(false);
            p5ta.setBounds(0, 0, 100, 100);
            JScrollPane p5sp = new JScrollPane(p5ta);
            p5sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            p5sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            p5.add(updateCount);
            p5.add(p5sp);


            // Tabs are here!
            JTabbedPane tp=new JTabbedPane();  
            tp.setBounds(265,50,700,545);  
            tp.add("Node Status",p1);
            tp.add("Disks",p2);  
            tp.add("Network",p3);  
            tp.add("Storage",p4);
            tp.add("Updates", p5);
            
            // Main Window Objects
            mainFrame.setLayout(null);
            mainFrame.add(heading);
            
            mainFrame.add(jt);
            mainFrame.add(tp);

            
            // Main Window Display
            Image icon = Toolkit.getDefaultToolkit().getImage("assets/images/ProxCtrlJ_logo.png");
            mainFrame.setIconImage(icon);
            mainFrame.setSize(1024, 768);
            mainFrame.setLocationRelativeTo(null);
            
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
