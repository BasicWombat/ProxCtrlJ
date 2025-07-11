import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);

        // Optional image
        JLabel imageLabel = new JLabel(new ImageIcon("assets/images/Jabiru.png")); 
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        content.add(imageLabel, BorderLayout.CENTER);

        // Loading text
        JLabel loadingLabel = new JLabel("Loading ProxCtrlJ...", JLabel.CENTER);
        loadingLabel.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        content.add(loadingLabel, BorderLayout.SOUTH);

        setContentPane(content);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    public void showSplash(int duration) {
        setVisible(true);
        try {
            Thread.sleep(duration); // Fake loading time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);
        dispose();
    }
}
