import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

public class vmNodeWdw extends JFrame {
    Preferences usrprefs = Preferences.userNodeForPackage(Main.class);

    public vmNodeWdw(String vmName) throws Exception {
        System.out.println("Opening new window for node: " + vmName);
        JFrame vmNodewindow = new JFrame();
        vmNodewindow.setTitle("Node: " + vmName);

        JPanel nodePnl=new JPanel();
        nodePnl.setLayout(null); 

        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/" + nodeName + "/qemu/");
        VmDataFetcher vmFetcher = new VmDataFetcher(response);
        String vmID = vmFetcher.getVmId(vmName);
        String vmstatus = vmFetcher.getVmstatus(vmName);
        String vmuptime = vmFetcher.getVmuptime(vmName);
        JButton launchButton = new JButton("Open VNC Console");
       
        // Convert uptime to days, hours, and minutes
        long uptimeSeconds = Long.parseLong(vmuptime);
        long days = uptimeSeconds / 86400; // 86400 seconds in a day
        long hours = (uptimeSeconds % 86400) / 3600; // 3600 seconds in an hour
        long minutes = (uptimeSeconds % 3600) / 60; // 60 seconds in a minute

        StringBuilder vmUptimeSB = new StringBuilder();
        vmUptimeSB.append("Uptime: ").append(days).append(" days, ")
            .append(hours).append(" hours, ")
            .append(minutes).append(" minutes\n");

        JLabel vmnameLbl = new JLabel("VM Name: "+ vmName);
        JLabel vmstatusLbl = new JLabel("Status: ");
        JLabel vmstatusSts = new JLabel(vmstatus);
        JLabel uptimeLbl = new JLabel("Uptime: "+ vmUptimeSB);
        JLabel vmidLbl = new JLabel("VM ID:" + vmID);

        launchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!vmName.isEmpty()) {
                    VncConsoleOpener.openVncConsoleByName(vmName);
                } else {
                    JOptionPane.showMessageDialog(vmNodewindow, "Please enter a VM name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        vmnameLbl.setBounds(10, 10, 200, 30);

        // TODO: Need to separate the status from the label
        vmstatusLbl.setBounds(10, 40, 200, 30);
        vmstatusSts.setBounds(60, 40, 200, 30);
        if (vmstatus.equals("running")) {
            vmstatusSts.setForeground(Color.GREEN);
        } else {
            vmstatusSts.setForeground(Color.RED);
        }
        vmidLbl.setBounds(10, 70, 200, 30);
        uptimeLbl.setBounds(10, 100, 400, 30);
        launchButton.setBounds(10, 130, 200, 30);
        
        nodePnl.add(vmnameLbl);
        nodePnl.add(vmidLbl);      
        nodePnl.add(vmstatusLbl);
        nodePnl.add(vmstatusSts);
        nodePnl.add(uptimeLbl);
        nodePnl.add(launchButton);

        vmNodewindow.add(nodePnl);
        vmNodewindow.setSize(400, 500);
        vmNodewindow.setLocationRelativeTo(getParent());
        vmNodewindow.setVisible(true);
        vmNodewindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
