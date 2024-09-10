import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;


public class vmNodeWdw extends JFrame {
    Preferences usrprefs = Preferences.userNodeForPackage(Main.class);


    public vmNodeWdw(String vmName) throws Exception {
        System.out.println("Opening new window for node: " + vmName);
        JFrame vmNodewindow = new JFrame();
        setTitle("Node Display");

        JPanel nodePnl=new JPanel();
        nodePnl.setLayout(new GridBagLayout());

        GridBagConstraints gbcNode = new GridBagConstraints();
        gbcNode.insets = new Insets(5, 2, 5, 2); // Padding
        gbcNode.anchor = GridBagConstraints.NORTHWEST;

        JLabel vmnameLbl = new JLabel("VM Name: " + vmName);
        JLabel vmstatusLbl = new JLabel("Status: ");
        JLabel uptimeLbl = new JLabel("Uptime: ");
        JLabel vmidLbl = new JLabel("VM ID:");

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e2 -> vmNodewindow.dispose());


        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/" + nodeName + "/qemu/");
        VmDataFetcher vmFetcher = new VmDataFetcher(response);
        String vmID = vmFetcher.getVmId(vmName);
        String vncUrl = APIClient.getVNCWebSocketUrl(vmID);



        // Layout

        // Row 0
        gbcNode.gridx = 0; // Column 0
        gbcNode.gridy = 0; // Row 0
        nodePnl.add(vmnameLbl, gbcNode);
        gbcNode.gridx = 1;
        nodePnl.add(vmstatusLbl, gbcNode);

        // Row 1
        gbcNode.gridx = 0;
        gbcNode.gridy = 1;
        nodePnl.add(uptimeLbl, gbcNode);
        gbcNode.gridx = 1;
        nodePnl.add(vmidLbl, gbcNode);

        // Row 2
        gbcNode.gridx = 0;
        gbcNode.gridy = 2;


        // Row 2
        gbcNode.gridx = 0;
        gbcNode.gridy = 3;


        // Row 3
        gbcNode.gridx = 0;
        gbcNode.gridy = 4;
        nodePnl.add(new vncConsole(vncUrl));

        //Row 4
        gbcNode.gridx = 0;
        gbcNode.gridy = 5;
        nodePnl.add(closeBtn);

        vmNodewindow.setSize(600, 400);
        vmNodewindow.setLocationRelativeTo(getParent());
        vmNodewindow.add(nodePnl);
        vmNodewindow.setVisible(true);
        vmNodewindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
