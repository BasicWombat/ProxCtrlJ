/*On different operating systems, the preferences are stored in different locations:

Windows: In the Windows registry under HKEY_CURRENT_USER\Software\JavaSoft\Prefs.
macOS/Linux: In XML files under a directory such as ~/.java/.userPrefs.

*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;

public class settingsWdw extends JFrame {

    private JLabel headingLbl;
    private JLabel hostaddrLbl;
    private JLabel hostprtLbl;
    private JLabel apiTokenIDLbl;
    private JLabel apiSecretLbl;
    private JTextField apiSecretFld;
    private JButton saveBtn;
    private JButton clearBtn;
    private JTextField apiTokenIDFld;
    private JTextField hostprtFld;
    private JTextField hostaddrFld;
    private JLabel nodeLbl;
    private JTextField nodeFld;

    // Get the user preferences node for this class
    Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
    
    public settingsWdw() {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());

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
        apiSecretFld = new JTextField (usrprefs.get("apiSecret", null));
        saveBtn = new JButton ("Save");
        clearBtn = new JButton ("Clear");
        apiTokenIDFld = new JTextField (usrprefs.get("apiTokenID", null));
        hostprtFld = new JTextField (usrprefs.get("hostport", null));
        hostaddrFld = new JTextField (usrprefs.get("host",null));
        nodeLbl = new JLabel("Node Name:");
        nodeFld = new JTextField(usrprefs.get("node", null));

        //add components
        add (headingLbl);
        add (hostaddrLbl);
        add (hostprtLbl);
        add (apiTokenIDLbl);
        add (apiSecretLbl);
        add (apiSecretFld);
        add (saveBtn);
        add (clearBtn);
        add (apiTokenIDFld);
        add (hostprtFld);
        add (hostaddrFld);
        add (nodeFld);
        add (nodeLbl);

        //set component bounds (only needed by Absolute Positioning)
        headingLbl.setBounds (35, 25, 100, 25);

        hostaddrLbl.setBounds (40, 75, 100, 25);
        hostaddrFld.setBounds (135, 75, 195, 25);

        hostprtLbl.setBounds (40, 105, 100, 25);
        hostprtFld.setBounds (135, 105, 195, 25);

        apiTokenIDLbl.setBounds (40, 135, 100, 25);
        apiTokenIDFld.setBounds (135, 135, 195, 25);
        
        apiSecretLbl.setBounds (40, 165, 100, 25);
        apiSecretFld.setBounds (135, 165, 195, 25);

        nodeLbl.setBounds (40, 195, 100, 25);
        nodeFld.setBounds (135, 195, 195, 25);

        saveBtn.setBounds (450, 335, 100, 25);
        clearBtn.setBounds (340, 335, 100, 25);

        // Action listener for the save button
        saveBtn.addActionListener((ActionEvent e) -> {
            saveSettingsToFile(hostaddrFld.getText(), hostprtFld.getText(), apiTokenIDFld.getText(), apiSecretFld.getText(), nodeFld.getText());
            JOptionPane.showMessageDialog(this, "Settings Saved!", "Settings", JOptionPane.INFORMATION_MESSAGE);
        });

        clearBtn.addActionListener((ActionEvent e) -> {
            apiSecretFld.setText("");
            apiTokenIDFld.setText("");
            hostaddrFld.setText("");
            hostprtFld.setText("");
            nodeFld.setText("");
            saveSettingsToFile(hostaddrFld.getText(), hostprtFld.getText(), apiTokenIDFld.getText(), apiSecretFld.getText(), nodeFld.getText());
            JOptionPane.showMessageDialog(this, "Settings Cleared!", "Settings", JOptionPane.INFORMATION_MESSAGE);
        });

        // Display the window
        setVisible(true);
    }

    private void saveSettingsToFile(String host, String hostport, String apiTokenID, String apiSecret, String node) {
        //Properties properties = new Properties();
        usrprefs.put("host", host);
        usrprefs.put("hostport", hostport);
        usrprefs.put("apiTokenID", apiTokenID);
        usrprefs.put("apiSecret", apiSecret);
        usrprefs.put("node", node);
    }
}
