import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsWindow extends JFrame {

    private JTextArea propertiesTextArea;
    private JLabel headingLbl;
    private JLabel hostaddrLbl;
    private JLabel hostprtLbl;
    private JLabel apiTokenIDLbl;
    private JLabel apiSecretLbl;
    private JTextField apiSecretFld;
    private JButton saveBtn;
    private JButton clearBtn;
    private JLabel propertiesLbl;
    private JTextField apiTokenIDFld;
    private JTextField hostprtFld;
    private JTextField hostaddrFld;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SettingsWindow::new);
    }

    public SettingsWindow() {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        //adjust size and set layout
        setSize(600, 400);
        setPreferredSize (new Dimension (580, 382));
        setLayout (null);

        //construct components
        headingLbl = new JLabel ("Settings");
        hostaddrLbl = new JLabel ("Host Address:");
        hostprtLbl = new JLabel ("Host Port:");
        apiTokenIDLbl = new JLabel ("API Token ID:");
        apiSecretLbl = new JLabel ("API Secret:");
        apiSecretFld = new JTextField (5);
        saveBtn = new JButton ("Save");
        clearBtn = new JButton ("Clear");
        propertiesTextArea = new JTextArea (5, 5);
        propertiesLbl = new JLabel ("Current Settings");
        apiTokenIDFld = new JTextField (5);
        hostprtFld = new JTextField (5);
        hostaddrFld = new JTextField (5);

        //add components
        add (headingLbl);
        add (hostaddrLbl);
        add (hostprtLbl);
        add (apiTokenIDLbl);
        add (apiSecretLbl);
        add (apiSecretFld);
        add (saveBtn);
        add (clearBtn);
        add (propertiesTextArea);
        add (propertiesLbl);
        add (apiTokenIDFld);
        add (hostprtFld);
        add (hostaddrFld);

        //set component bounds (only needed by Absolute Positioning)
        headingLbl.setBounds (35, 25, 100, 25);
        hostaddrLbl.setBounds (40, 75, 100, 25);
        hostprtLbl.setBounds (40, 105, 100, 25);
        apiTokenIDLbl.setBounds (40, 135, 100, 25);
        apiSecretLbl.setBounds (40, 165, 100, 25);
        apiSecretFld.setBounds (135, 165, 195, 25);
        saveBtn.setBounds (450, 335, 100, 25);
        clearBtn.setBounds (340, 335, 100, 25);
        propertiesTextArea.setBounds (370, 75, 185, 120);
        propertiesLbl.setBounds (370, 45, 100, 25);
        apiTokenIDFld.setBounds (135, 135, 195, 25);
        hostprtFld.setBounds (135, 105, 195, 25);
        hostaddrFld.setBounds (135, 75, 195, 25);

        // Load the properties file content into the text area
        loadSettingsFromFile();

        // Action listener for the save button
        saveBtn.addActionListener((ActionEvent e) -> {
            saveSettingsToFile(hostaddrFld.getText(), hostprtFld.getText(), apiTokenIDFld.getText(), apiSecretFld.getText());
            loadSettingsFromFile(); // Reload the properties file content after saving
        });

        // Display the window
        setVisible(true);
    }

    private void saveSettingsToFile(String host, String hostport, String apiTokenID, String apiSecret) {
        Properties properties = new Properties();
        properties.setProperty("host", host);
        properties.setProperty("hostport", hostport);
        properties.setProperty("apiTokenID", apiTokenID);
        properties.setProperty("apiSecret", apiSecret);

        try (FileOutputStream out = new FileOutputStream("settings.properties")) {
            properties.store(out, "User Settings");
            JOptionPane.showMessageDialog(this, "Settings saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving settings.");
        }
    }

    private void loadSettingsFromFile() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("settings.properties")) {
            properties.load(in);
            StringBuilder sb = new StringBuilder();
            for (String key : properties.stringPropertyNames()) {
                sb.append(key).append("=").append(properties.getProperty(key)).append("\n");
            }
            propertiesTextArea.setText(sb.toString());
        } catch (IOException e) {
            propertiesTextArea.setText("Error loading settings.");
        }
    }
}
