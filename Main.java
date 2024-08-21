//import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

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
            DefaultMutableTreeNode cluster=new DefaultMutableTreeNode("Cluster");  
            DefaultMutableTreeNode host1=new DefaultMutableTreeNode("Host1");
            DefaultMutableTreeNode host2=new DefaultMutableTreeNode("Host2");  
            cluster.add(host1);  
            cluster.add(host2);  
            DefaultMutableTreeNode red=new DefaultMutableTreeNode("red");  
            DefaultMutableTreeNode blue=new DefaultMutableTreeNode("blue");  
            DefaultMutableTreeNode black=new DefaultMutableTreeNode("black");  
            DefaultMutableTreeNode green=new DefaultMutableTreeNode("green");  
            host1.add(red); host2.add(blue); host2.add(black); host2.add(green);      
            JTree jt=new JTree(cluster);
            jt.setBounds (5, 50, 240, 545);
            jt.setBorder(BorderFactory.createEtchedBorder());


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
            JTextArea ta=new JTextArea(200,200);
            JPanel p1=new JPanel();
            p1.add(ta);
            JPanel p2=new JPanel();
            JPanel p3=new JPanel();
            JTabbedPane tp=new JTabbedPane();  
            tp.setBounds(265,50,500,545);  
            tp.add("main",p1);  
            tp.add("visit",p2);  
            tp.add("help",p3);    

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
