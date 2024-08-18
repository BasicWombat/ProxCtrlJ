//import java.awt.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            Main mainSets = new Main("settings.properties");
            Main mainStatus = new Main("status.properties");

            // Main Window Configuration
            JFrame mainFrame = new JFrame("ProxCtrlJ");
            JLabel heading = new JLabel("ProxCtrlJ"); 
            heading.setBounds(50,10, 200,30);
            heading.setFont(new Font("Sans Serif", Font.BOLD, 28));
            
            // Main Menu Bar
            JMenu fileItem, helpItem;
            JMenuItem connectItem, settingsItem, quitItem, aboutItem;
            JMenuBar mb = new JMenuBar();
            fileItem = new JMenu("File");
            helpItem = new JMenu("Help");
            connectItem = new JMenuItem("Connect");
            settingsItem = new JMenuItem("Settings");
            quitItem = new JMenuItem("Quit");
            aboutItem = new JMenuItem("About");
            fileItem.add(connectItem);
            fileItem.add(settingsItem);
            fileItem.add(quitItem);
            helpItem.add(aboutItem);
            mb.add(fileItem);
            mb.add(helpItem);
            mainFrame.setJMenuBar(mb);

            // Connect Action (Uncomment and implement as needed)
            // connect.addActionListener(e -> { /* Connect logic */ });

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


            // Connect Window
            connectItem.addActionListener(e -> {

                // Create a new JFrame for the Connect window
                JFrame connectWindow = new JFrame("Connect");
                connectWindow.setSize(400, 200);
                connectWindow.setLocationRelativeTo(mainFrame);
                connectWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                connectWindow.setLayout(null);

                JLabel titleLabel = new JLabel("Host Connect");
                titleLabel.setBounds(20, 10, 200, 30);
                titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));

                JLabel hostLabel = new JLabel("Current Host: "+ mainSets.getProperty("host"));
                hostLabel.setBounds(20, 60, 300, 30);
                hostLabel.setToolTipText("Host Address can be changes in Settings");
                
                JButton connectBtn = new JButton("Connect");
                connectBtn.setBounds(20, 100, 95, 30);
                connectBtn.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Show a simple dialog when the button is clicked
                        JOptionPane.showMessageDialog(connectWindow, "Connect! (Maybe)", "Connect!", JOptionPane.INFORMATION_MESSAGE);
                        //TODO - Set Status Bar Status
                        connectWindow.dispose();
                    }
                });
                
                connectWindow.add(titleLabel);
                connectWindow.add(hostLabel);
                connectWindow.add(connectBtn);
                connectWindow.setVisible(true);

                
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
                // Create a new JFrame for the About window
                JFrame aboutWindow = new JFrame("About");
                aboutWindow.setSize(600, 400);
                aboutWindow.setLocationRelativeTo(mainFrame);
                aboutWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                aboutWindow.setLayout(null);

                // Add labels with property information
                JLabel titleLabel = new JLabel("ProxCtrlJ");
                titleLabel.setBounds(50, 10, 200, 30);
                titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));
                aboutWindow.add(titleLabel);

                JLabel versionLabel = new JLabel("Version: " + mainApp.getProperty("app.version"));
                versionLabel.setBounds(50, 60, 300, 30);
                aboutWindow.add(versionLabel);

                JLabel authorLabel = new JLabel("Author: " + mainApp.getProperty("app.author"));
                authorLabel.setBounds(50, 100, 300, 30);
                aboutWindow.add(authorLabel);

                JButton closeBtn = new JButton("Close");
                closeBtn.setBounds(50, 200, 95, 30);
                closeBtn.addActionListener(e2 -> aboutWindow.dispose());
                aboutWindow.add(closeBtn);

                aboutWindow.setVisible(true);
            });
            
            // Quit Application from Menu
            quitItem.addActionListener(e -> System.exit(0));

            // Label Example
            JLabel l1 = new JLabel("First Label.");
            l1.setBounds(50, 50, 100, 30);
            JLabel l2 = new JLabel("Second Label.");
            l2.setBounds(50, 100, 100, 30);
            JLabel epLabel = new JLabel("East Panel Label");
            epLabel.setBounds(50, 50, 100, 30);
            

            // Field Example
            JTextField tf = new JTextField();
            tf.setBounds(50, 150, 150, 20);

            // Button Example
            JButton b = new JButton("Click Here");
            b.setBounds(50, 200, 95, 30);
            b.addActionListener(e -> tf.setText("Welcome to Javatpoint."));

            // Create a label to be used as a status bar
            JLabel statusLabel = new JLabel(mainApp.getProperty("status.connectionStatus"));
            statusLabel.setBorder(BorderFactory.createEtchedBorder());

            // Create a panel to hold the status label
            JPanel statusPanel = new JPanel(new BorderLayout());
            statusPanel.add(statusLabel, BorderLayout.CENTER);
            
            // EAST Panel
            JPanel ePanel=new JPanel();
            ePanel.setBackground(Color.GRAY);
            ePanel.add(epLabel);
            

            // Center Panel
            JPanel cPanel=new JPanel();
            cPanel.setBackground(Color.LIGHT_GRAY);
            cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));

            cPanel.add(l1, BorderLayout.CENTER);
            cPanel.add(l2, BorderLayout.CENTER);

            cPanel.add(tf, BorderLayout.CENTER);
            cPanel.add(b, BorderLayout.CENTER);

            // Main Window Objects
            mainFrame.setLayout(new BorderLayout(20, 15));
            mainFrame.add(heading, BorderLayout.NORTH);

            mainFrame.add(jt, BorderLayout.WEST);
            mainFrame.add(cPanel, BorderLayout.CENTER);
            mainFrame.add(ePanel, BorderLayout.EAST);
            
            mainFrame.add(statusPanel,  BorderLayout.SOUTH);

            // Main Window Display
            mainFrame.setSize(1024, 768);
            mainFrame.setLocationRelativeTo(null);
            
            mainFrame.setVisible(true);
    }
}
