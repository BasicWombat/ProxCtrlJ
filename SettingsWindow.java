import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsWindow extends JFrame {

    private JTextArea propertiesTextArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SettingsWindow::new);
    }

    public SettingsWindow() {
        // Set up the frame
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600, 400);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create labels and text fields
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel hostaddrLbl = new JLabel("Host Address:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(hostaddrLbl, gbc);
        
        JTextField hostaddrFld = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(hostaddrFld, gbc);

        JLabel hostportLbl = new JLabel("Host Port:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(hostportLbl, gbc);
        
        JTextField hostportFld = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(hostportFld, gbc);

        JLabel apitknLbl = new JLabel("API Token:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(apitknLbl, gbc);
        
        JTextField apitknFld = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(apitknFld, gbc);

        JLabel apikeyLbl = new JLabel("API Key:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(apikeyLbl, gbc);
        
        JTextField apikeyFld = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(apikeyFld, gbc);

        JButton saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

        // Text area to display settings.properties content
        propertiesTextArea = new JTextArea(10, 25);
        propertiesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(propertiesTextArea);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 6;
        gbc.fill = GridBagConstraints.BOTH;  // Ensure it fills the available space
        add(scrollPane, gbc);

        // Load the properties file content into the text area
        loadSettingsFromFile();

        // Action listener for the save button
        saveButton.addActionListener((ActionEvent e) -> {
            saveSettingsToFile(hostaddrFld.getText(), hostportFld.getText(), apitknFld.getText(), apikeyFld.getText());
            loadSettingsFromFile(); // Reload the properties file content after saving
        });

        // Display the window
        setVisible(true);
    }

    private void saveSettingsToFile(String host, String hostport, String apitoken, String apikey) {
        Properties properties = new Properties();
        properties.setProperty("host", host);
        properties.setProperty("hostport", hostport);
        properties.setProperty("apitoken", apitoken);
        properties.setProperty("apikey", apikey);

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
