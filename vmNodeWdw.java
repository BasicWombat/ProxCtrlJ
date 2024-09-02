import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class vmNodeWdw extends JFrame {

    public vmNodeWdw(String vmName) {
        APIClient apiclient = new APIClient();
        Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
        System.out.println("Opening new window for node: " + vmName);
        String node = usrprefs.get("node", null);
        JFrame vmNodewindow = new JFrame();
        setTitle("Node Display");

        //adjust size and set layout
        setSize(600, 400);
        setPreferredSize (new Dimension (580, 382));
        setLayout (null);

        JLabel vmnameLbl = new JLabel("VM Name: " + vmName);
        vmnameLbl.setBounds(50,50, 100,30);
        JLabel vmstatusLbl = new JLabel("Status: ");
        vmstatusLbl.setBounds(50,100, 100,30);
        JLabel uptimeLbl = new JLabel("Uptime: ");
        uptimeLbl.setBounds(50,150, 100,30);
        JLabel vmidLbl = new JLabel("VM ID:");
        vmidLbl.setBounds(50,200, 100,30);

        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(300, 250, 95, 30);
        closeBtn.addActionListener(e2 -> vmNodewindow.dispose());
        vmNodewindow.add(closeBtn);


        vmNodewindow.add(vmnameLbl);
        vmNodewindow.add(vmstatusLbl);
        vmNodewindow.add(uptimeLbl);
        vmNodewindow.add(vmidLbl);

        vmNodewindow.setLocationRelativeTo(getParent());
        vmNodewindow.setSize(400, 450);
        vmNodewindow.setVisible(true);
        vmNodewindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
