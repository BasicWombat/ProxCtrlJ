package src;
import java.awt.*;
import javax.swing.*;

public class AboutWdw extends Canvas{
    // Create a new JFrame for the About window
    JFrame AboutWdw = new JFrame("About");
    Main mainApp = new Main("app.properties");
    AboutWdw(){
    AboutWdw.setSize(600, 400);
    AboutWdw.setLocationRelativeTo(getParent());
    AboutWdw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    AboutWdw.setLayout(null);
    Image icon = Toolkit.getDefaultToolkit().getImage("assets/images/Jabiru.png");
    AboutWdw.setIconImage(icon);
    
    // Load and scale image to fit within the window size (adjusted for padding)
        ImageIcon imgIcon = new ImageIcon("assets/images/Jabiru.png");
        Image image = imgIcon.getImage();
        Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledImgIcon = new ImageIcon(scaledImage);

       // Add the scaled image to a JLabel
        JLabel imageLabel = new JLabel(scaledImgIcon);
        imageLabel.setBounds(350, 50, scaledImgIcon.getIconWidth(), scaledImgIcon.getIconHeight());
        AboutWdw.add(imageLabel);


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
