import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import java.io.*;
import java.util.Properties;


public class Main {  
    private Properties properties;

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
            jt.setBounds (5, 50, 240, 545);
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
            JTextArea p1ta = new JTextArea(apiclient.readData("/api2/json/nodes/"));
            p1ta.setEditable(false);
            p1ta.setBounds(0, 0, 100, 100);
            p1.add(p1ta);

            //Panel 2: Main
            JTextArea ta=new JTextArea(200,200);
            JPanel p2=new JPanel();
            p2.add(ta);

            //Panel 3: Visit
            JPanel p3=new JPanel();

            //Panel 4: Help
            JPanel p4=new JPanel();

            // Tabs are here!
            JTabbedPane tp=new JTabbedPane();  
            tp.setBounds(265,50,500,545);  
            tp.add("Node Status",p1);
            tp.add("main",p2);  
            tp.add("visit",p3);  
            tp.add("help",p4);    

            // Label Example
            JLabel l1 = new JLabel("First Label.");
            l1.setBounds(260, 50, 100, 30);
            JLabel l2 = new JLabel("Second Label.");
            l2.setBounds(260, 100, 100, 30);
         

            // Field Example
            JTextField tf = new JTextField();
            tf.setBounds(260, 150, 150, 20);

            // Button Example
            JButton b = new JButton("Click Here");
            b.setBounds(260, 200, 95, 30);
            b.addActionListener(e -> tf.setText("Welcome to Javatpoint."));

            // Create a label to be used as a status bar
            JLabel statusLabel = new JLabel(mainApp.getProperty("status.connectionStatus"));
            statusLabel.setBorder(BorderFactory.createEtchedBorder());
            
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
