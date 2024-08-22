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
            //Main mainSets = new Main("settings.properties");

            // Main Window Configuration
            JFrame mainFrame = new JFrame("ProxCtrlJ");
            JLabel heading = new JLabel("ProxCtrlJ"); 
            heading.setBounds(50,10, 200,30);
            heading.setFont(new Font("Sans Serif", Font.BOLD, 28));
            
            // Main Menu Bar
            JMenu fileItem, createItem, helpItem;
            JMenuItem connectItem, disconnectItem, settingsItem, createvmItem, createctItem, quitItem, aboutItem;
            JMenuBar mb = new JMenuBar();
            
            // File Menu
            fileItem = new JMenu("File");
            connectItem = new JMenuItem("Connect");
            disconnectItem = new JMenuItem("Disconnect");
            settingsItem = new JMenuItem("Settings");
            quitItem = new JMenuItem("Quit");
            fileItem.add(connectItem);
            fileItem.add(disconnectItem);
            fileItem.add(settingsItem);
            fileItem.add(quitItem);
            mb.add(fileItem);

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
                            
                            // Open a new window when a node is double-clicked
                            JFrame newWindow = new JFrame("New Window - " + nodeInfo);
                            newWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            newWindow.setSize(300, 200);
                            newWindow.setVisible(true);
                        }
                    }
                }
            });

            // Connect Window
            // This is probably a waste of time considering what I've learnt about the API connectivity :(
            // Confirmed via ChatGPT that for software that make frequent API requests, you can establish
            // a persistent connection.
            // TODO: Create class to handle API Connections
            connectItem.addActionListener(e -> {
                new ConnectWdw();
            });

            // Settings Window
            settingsItem.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    SettingsWindow settingsWdw = new SettingsWindow();
                    settingsWdw.setVisible(true);
                });
            });

            // About Window
            aboutItem.addActionListener(e -> {
                new AboutWdw();
            });
            
            createvmItem.addActionListener(e -> {
                JOptionPane.showMessageDialog(mainFrame,"Created New VM!");  
            });

            createctItem.addActionListener(e -> {
                JOptionPane.showMessageDialog(mainFrame,"Created New CT!");  
            });

            disconnectItem.addActionListener(e -> {
                JOptionPane.showMessageDialog(mainFrame,"Disconnected!");  
            });

            // Quit Application from Menu
            quitItem.addActionListener(e -> System.exit(0));

            //Tab Area

            //Panel 1: Node Status
            JPanel p1=new JPanel();
            JLabel authorLabel = new JLabel("Server: This is where stupid server infor will go!");
            p1.add(authorLabel);

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
            mainFrame.setSize(1024, 768);
            mainFrame.setLocationRelativeTo(null);
            
            mainFrame.setVisible(true);
    }
}
