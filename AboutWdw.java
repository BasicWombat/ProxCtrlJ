import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

public class AboutWdw {
    // Create a new JFrame for the About window
    JFrame AboutWdw = new JFrame("About");
    Main mainApp = new Main("app.properties");
    AboutWdw(){
    AboutWdw.setSize(600, 400);
    //AboutWdw.setLocationRelativeTo(mainFrame);
    AboutWdw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    AboutWdw.setLayout(null);

    // Add labels with property information
    JLabel titleLabel = new JLabel("ProxCtrlJ");
    titleLabel.setBounds(50, 10, 200, 30);
    titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));
    AboutWdw.add(titleLabel);

    JLabel versionLabel = new JLabel("Version: " + mainApp.getProperty("app.version"));
    versionLabel.setBounds(50, 60, 300, 30);
    AboutWdw.add(versionLabel);

    JLabel authorLabel = new JLabel("Author: " + mainApp.getProperty("app.author"));
    authorLabel.setBounds(50, 100, 300, 30);
    AboutWdw.add(authorLabel);

    JButton closeBtn = new JButton("Close");
    closeBtn.setBounds(50, 200, 95, 30);
    closeBtn.addActionListener(e2 -> AboutWdw.dispose());
    AboutWdw.add(closeBtn);

    AboutWdw.setVisible(true);
    }
}
