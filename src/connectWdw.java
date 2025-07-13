package src;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;


public class connectWdw extends JPanel {


    private void restartMain() {
        Main.main(null);          // Re-run the main method
    }

    // Create a new JFrame for the Connect window
    JFrame connectWindow = new JFrame("Connect");
    Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
    Main mainSets = new Main("settings.properties");
    String host = usrprefs.get("host", null);
    String hostPort = usrprefs.get("hostport", null);
    String apiTokenID = usrprefs.get("apiTokenID", null);
    String apiSecret = usrprefs.get("apiSecret", null);
    String node = usrprefs.get("node", null);
    
    connectWdw(){
        
        connectWindow.setSize(400, 250);
        connectWindow.setLocationRelativeTo(getParent());
        connectWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        connectWindow.setLayout(null);

        JLabel titleLabel = new JLabel("Host Connect");
        titleLabel.setBounds(20, 10, 200, 30);
        titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));

        JLabel hostLabel = new JLabel("Current Host: "+ host +":"+hostPort);
        hostLabel.setBounds(20, 60, 300, 30);
        hostLabel.setToolTipText("Host Address can be changes in Settings");
        JLabel nodeLabel = new JLabel("Current Node: "+ node);
        nodeLabel.setBounds(20, 90, 300, 30);
        
        JButton connectBtn = new JButton("Connect");
        connectBtn.setBounds(20, 120, 95, 30);
        connectBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                restartMain();
                connectWindow.dispose();
            }
        });
        
        connectWindow.add(titleLabel);
        connectWindow.add(hostLabel);
        connectWindow.add(nodeLabel);
        connectWindow.add(connectBtn);
        connectWindow.setVisible(true);
    }
}
