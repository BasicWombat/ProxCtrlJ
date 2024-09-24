import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import jiconfont.swing.IconFontSwing;
import jiconfont.icons.font_awesome.FontAwesome;


public class vmNodeWdw extends JFrame {
    Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
    

    public vmNodeWdw(String vmName) throws Exception {

        IconFontSwing.register(FontAwesome.getIconFont());
        System.out.println("Opening new window for node: " + vmName);
        JFrame vmNodewindow = new JFrame();
        vmNodewindow.setTitle(vmName);

        JPanel nodePnl=new JPanel();
        nodePnl.setLayout(null); 

        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        
        // Getting VM ID Information
        String response = apiclient.readData("/api2/json/nodes/" + nodeName + "/qemu/");
        VmDataFetcher vmFetcher = new VmDataFetcher(response);

        String vmID = VmDataFetcher.getVmId(vmName);
        String vmstatus = vmFetcher.getVmstatus(vmName);
        String vmuptime = vmFetcher.getVmuptime(vmName);
        
        // Getting VM OS Information
        String vmosdataresponse = apiclient.readData("/api2/json/nodes/" + nodeName + "/qemu/" + vmID + "/agent/get-osinfo");
        JsonFetch vmosdataFetcher = new JsonFetch(vmosdataresponse);
        String vmos = vmosdataFetcher.getNestedValueByKey("data", "result", "pretty-name");
        System.out.println("VM OS: " + vmos);

        // Getting VM OS Type Information
        String vmostypedataresponse = apiclient.readData("/api2/json/nodes/" + nodeName + "/qemu/" + vmID + "/config");
        JsonFetch vmostypedataFetcher = new JsonFetch(vmostypedataresponse);
        String vmostype = vmostypedataFetcher.getNestedValueByKey("data", "ostype");
        System.out.println("VM OS Type: " + vmos);
        

       
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
        JLabel osLbl = new JLabel("OS: " + vmos);
        JLabel osTypeLbl = new JLabel("OS Type: " + vmostype);

        // Main Menu Bar
        JMenu vmMenu;
        JMenuItem startVM, stopVM, launchVNC;
        JMenuBar vmMenuBar = new JMenuBar();

        vmMenu = new JMenu("Control");
        startVM = new JMenuItem("Start VM");
        Icon startIcon = IconFontSwing.buildIcon(FontAwesome.PLAY_CIRCLE, 15);
        startVM.setIcon(startIcon);
        stopVM = new JMenuItem("Stop VM");
        Icon stopIcon = IconFontSwing.buildIcon(FontAwesome.STOP_CIRCLE, 15);
        stopVM.setIcon(stopIcon);
        launchVNC = new JMenuItem("Launch VNC");
        Icon vncIcon = IconFontSwing.buildIcon(FontAwesome.DESKTOP, 15);
        launchVNC.setIcon(vncIcon);
        vmMenu.add(startVM);
        vmMenu.add(stopVM);
        vmMenu.add(launchVNC);
        vmMenuBar.add(vmMenu);

        // Button Actions
                

        launchVNC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!vmID.isEmpty()) {
                    VncConsoleOpener.openVncConsoleByName(vmName);
                } else {
                    JOptionPane.showMessageDialog(vmNodewindow, "Please enter a VM name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        startVM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!vmID.isEmpty()) {
                    JOptionPane.showMessageDialog(vmNodewindow, "Launching VM...", "Launching VM", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(vmNodewindow, "Please enter a VM name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        stopVM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!vmID.isEmpty()) {
                    JOptionPane.showMessageDialog(vmNodewindow, "Stopping VM...", "Stopping VM", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(vmNodewindow, "Please enter a VM name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        // Window Layout

        // Set Icon based on OS
        switch (vmostype) {
            case "wxp":
                
                break;
            case "w2k":
                
                break;
            case "w2k3":
                
                break;
            case "w2k8":
                
                break;
            case "wvista":
                
                break;
            case "w7":
                
                break;
            case "w10":
                
                break;
            case "w11":
                
                break;
            case "l24":
                
                break;
            case "l26":
                
                break;
            case "solaris":
                
                break;
            case "other":
                
                break;
            default:
                break;
        }

        Icon winIcon = IconFontSwing.buildIcon(FontAwesome.DESKTOP, 50);
        JLabel winLbl = new JLabel(winIcon);
        

        vmnameLbl.setBounds(10, 10, 200, 30);
        vmstatusLbl.setBounds(10, 40, 200, 30);
        vmstatusSts.setBounds(60, 40, 200, 30);
        if (vmstatus.equals("running")) {
            vmstatusSts.setForeground(Color.GREEN);
        } else {
            vmstatusSts.setForeground(Color.RED);
        }
        vmidLbl.setBounds(10, 70, 200, 30);
        uptimeLbl.setBounds(10, 100, 400, 30);
        winLbl.setBounds(400, 10, 200, 200);
        osLbl.setBounds(10, 130, 200, 30);
        osTypeLbl.setBounds(10, 160, 200, 30);
        
        nodePnl.add(vmnameLbl);
        nodePnl.add(vmidLbl);      
        nodePnl.add(vmstatusLbl);
        nodePnl.add(vmstatusSts);
        nodePnl.add(uptimeLbl);
        nodePnl.add(winLbl);
        nodePnl.add(osLbl);
        nodePnl.add(osTypeLbl);

        vmNodewindow.setJMenuBar(vmMenuBar);
        vmNodewindow.add(nodePnl);
        vmNodewindow.setSize(600, 400);
        vmNodewindow.setLocationRelativeTo(getParent());
        vmNodewindow.setVisible(true);
        vmNodewindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
