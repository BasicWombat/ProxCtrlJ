import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ConnectWdw {
    // Create a new JFrame for the Connect window
                JFrame connectWindow = new JFrame("Connect");
                Main mainSets = new Main("settings.properties");
                ConnectWdw(){
                connectWindow.setSize(400, 200);
                //connectWindow.setLocationRelativeTo(mainFrame);
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
                        connectWindow.dispose();
                    }
                });
                
                connectWindow.add(titleLabel);
                connectWindow.add(hostLabel);
                connectWindow.add(connectBtn);
                connectWindow.setVisible(true);
            }
}
