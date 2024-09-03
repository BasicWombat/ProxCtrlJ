import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import java.io.*;
import java.util.Properties;
import java.util.prefs.Preferences;

import com.google.gson.JsonArray;
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
        String response = apiclient.readData("/api2/json/nodes/");
            // Convert JSON response to a string with new lines
        String jsonString = response.toString();
        String formattedString = jsonString.replace(",", ",\n");
        System.out.println(formattedString);
        return formattedString;
    }

    static String diskStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/disks/list");
            // Convert JSON response to a string with new lines
        String jsonString = response.toString();
        String formattedString = jsonString.replace(",", ",\n");
        System.out.println(formattedString);
        return formattedString;
    }

    static String networkStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/network");
            // Convert JSON response to a string with new lines
        String jsonString = response.toString();
        String formattedString = jsonString.replace(",", ",\n");
        System.out.println(formattedString);
        return formattedString;
    }
    static String storageStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/storage");
            // Convert JSON response to a string with new lines
        String jsonString = response.toString();
        String formattedString = jsonString.replace(",", ",\n");
        System.out.println(formattedString);
        return formattedString;
    }

    static String updateStatus(){
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName + "/apt/update");
            // Convert JSON response to a string with new lines
        String jsonString = response.toString();
        String formattedString = jsonString.replace(",", ",\n");
        System.out.println(formattedString);
        return formattedString;
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
            Main mainApp = new Main("app.properties");
            APIClient apiclient = new APIClient();
            //Main propSettings = new Main("settings.properties");


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
            JTextArea p1ta = new JTextArea(p1data);
            p1ta.setEditable(false);
            p1ta.setBounds(0, 0, 100, 100);
            JScrollPane p1sp = new JScrollPane(p1ta);
            p1sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            p1sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            p1.add(p1sp);

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
            JLabel updateCount = new JLabel("Update Count: " + updateCountTotal);
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
