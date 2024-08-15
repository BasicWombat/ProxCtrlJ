import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class SettingsDialog extends JFrame{

    public static void main(String[] args) {
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));

        // Create labels and text fields
        JLabel hostLbl = new JLabel("Host Address:");
        JTextField hostFld = new JTextField();
        JLabel apitknLbl = new JLabel("API Token:");
        JTextField apitknFld = new JTextField();
        JLabel apikeyLbl = new JLabel("API Key:");
        JTextField apikeyFld = new JTextField();
        
        // Create a save button
        JButton saveButton = new JButton("Save");

        // Add components to the frame
        frame.add(hostLbl);
        frame.add(hostFld);
        frame.add(apitknLbl);
        frame.add(apitknFld);
        frame.add(apikeyLbl);
        frame.add(apikeyFld);
        frame.add(new JLabel()); // Empty cell
        frame.add(saveButton);

        // Action listener for the save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings(hostFld.getText(), apitknFld.getText() ,apikeyFld.getText());
            }
        });

        // Display the window
        frame.setVisible(true);
    }

    private static void saveSettings(String setting1, String setting2, String setting3) {
        Properties properties = new Properties();
        properties.setProperty("host", setting1);
        properties.setProperty("apitoken", setting2);
        properties.setProperty("apikey", setting3);

        try (FileOutputStream out = new FileOutputStream("settings.properties")) {
            properties.store(out, "User Settings");
            JOptionPane.showMessageDialog(null, "Settings saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving settings.");
        }
    }
}
