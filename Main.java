// import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;

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
        SwingUtilities.invokeLater(() -> {
            Main mainApp = new Main("app.properties");

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

            // Settings Window
            settingsItem.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    SettingsDialog settingsDialog = new SettingsDialog();
                    settingsDialog.setVisible(true); 
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

            quitItem.addActionListener(e -> System.exit(0));

            // Label Example
            JLabel l1 = new JLabel("First Label.");
            l1.setBounds(50, 50, 100, 30);
            JLabel l2 = new JLabel("Second Label.");
            l2.setBounds(50, 100, 100, 30);
            

            // Field Example
            JTextField tf = new JTextField();
            tf.setBounds(50, 150, 150, 20);

            // Button Example
            JButton b = new JButton("Click Here");
            b.setBounds(50, 200, 95, 30);
            b.addActionListener(e -> tf.setText("Welcome to Javatpoint."));

            // Create a label to be used as a status bar
            JLabel statusLabel = new JLabel("Ready");
            statusLabel.setBorder(BorderFactory.createEtchedBorder());

            // Create a panel to hold the status label
            JPanel statusPanel = new JPanel(new BorderLayout());
            statusPanel.add(statusLabel, BorderLayout.CENTER);
            

            // Main Window Objects
            mainFrame.setLayout(new BorderLayout(20, 15));
            mainFrame.add(heading, BorderLayout.NORTH);
            
            mainFrame.add(l1, BorderLayout.EAST);
            mainFrame.add(l2, BorderLayout.EAST);

            mainFrame.add(tf, BorderLayout.EAST);
            mainFrame.add(b, BorderLayout.EAST);
            mainFrame.add(statusPanel, BorderLayout.SOUTH);

            // Main Window Display
            mainFrame.setSize(800, 600);
            
            mainFrame.setVisible(true);
        });
    }
}
