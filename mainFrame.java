/** 
* The Main Window for the ProxCtrlJ Application.
* This is where everything starts from.
*/

import java.awt.event.*;
import java.util.prefs.Preferences;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import jiconfont.swing.IconFontSwing;
import jiconfont.icons.font_awesome.FontAwesome;

public class mainFrame {
    static Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
    
    /**
     * Main Window Method
     */
    JFrame mainFrame;
    mainFrame(){
        IconFontSwing.register(FontAwesome.getIconFont());

        /**
         * Main Window Configuration
         */
        mainFrame = new JFrame("ProxCtrlJ");
        JLabel heading = new JLabel("ProxCtrlJ"); 
        heading.setBounds(50,10, 200,30);
        heading.setFont(new Font("Sans Serif", Font.BOLD, 28));

        
        /**
         * Main Menu Bar
         */ 
        JMenu fileItem, nodeItem, viewItem, createItem, helpItem;
        JMenuItem connectItem, disconnectItem, settingsItem, createvmItem, createctItem, refreshItem, quitItem, aboutItem, shutdownItem, restartItem, consoleItem;
        JMenuBar mb = new JMenuBar();

        // File Menu
        fileItem = new JMenu("File");
        viewItem = new JMenu("View");

        connectItem = new JMenuItem("Connect");
        Icon connectIcon = IconFontSwing.buildIcon(FontAwesome.PLUG, 15);
        connectItem.setIcon(connectIcon);

        disconnectItem = new JMenuItem("Disconnect");
        Icon disconnectIcon = IconFontSwing.buildIcon(FontAwesome.TIMES, 15);
        disconnectItem.setIcon(disconnectIcon);


        settingsItem = new JMenuItem("Settings");
        Icon settingsIcon = IconFontSwing.buildIcon(FontAwesome.COG, 15);
        settingsItem.setIcon(settingsIcon);

        quitItem = new JMenuItem("Quit");
        Icon quitIcon = IconFontSwing.buildIcon(FontAwesome.SIGN_OUT, 15);
        quitItem.setIcon(quitIcon);


        fileItem.add(connectItem);
        fileItem.add(disconnectItem);
        fileItem.add(settingsItem);
        fileItem.add(quitItem);
        mb.add(fileItem);

        // Node Menu
        nodeItem = new JMenu("Node");
        shutdownItem = new JMenuItem("Shutdown");
        restartItem = new JMenuItem("Restart");
        consoleItem = new JMenuItem("Console");
        nodeItem.add(shutdownItem);
        nodeItem.add(restartItem);
        nodeItem.add(consoleItem);
        mb.add(nodeItem);

        //View Menu
        refreshItem = new JMenuItem("Refresh");
        viewItem.add(refreshItem);
        mb.add(viewItem);

        //Create Menu
        createItem = new JMenu("Create");
        createvmItem = new JMenuItem("Create VM");
        createctItem = new JMenuItem("Create CT");
        createItem.add(createvmItem);
        createItem.add(createctItem);
        mb.add(createItem);

        //Help Menu
        helpItem = new JMenu("Help");
        aboutItem = new JMenuItem("About");
        Icon aboutIcon = IconFontSwing.buildIcon(FontAwesome.INFO_CIRCLE, 15);
        aboutItem.setIcon(aboutIcon);
        helpItem.add(aboutItem);
        mb.add(helpItem);

        mainFrame.setJMenuBar(mb);


        // Server Tree View
        // Instantiate ProxmoxTree and get the tree model
        ProxmoxTree proxmoxTree = new ProxmoxTree();
        DefaultTreeModel proxTree = proxmoxTree.getProxmoxTreeModel(); 
        JTree jt=new JTree(proxTree);
        jt.setBounds (10, 50, 300, 545);
        jt.setBorder(BorderFactory.createEtchedBorder());
        // Add a mouse listener to handle double-clicks
        jt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // Check for double-click
                    TreePath path = jt.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        String nodeInfo = node.getUserObject().toString();
                        System.out.println("Double-clicked node: " + nodeInfo);
                        
                        // Extract VM/CT Name from the nodeInfo string
                        String vmName = null;
                        int dashIndex = nodeInfo.lastIndexOf(" - "); // Find the last occurrence of " - "
                        if (dashIndex != -1) {
                            // Extract name after the last " - "
                            vmName = nodeInfo.substring(dashIndex + 3).trim(); // Start after " - "
                        } else {
                            // If there's no " - ", take the entire string as the name
                            vmName = nodeInfo.trim();
                        }
            
                        // If vmName was successfully extracted, open the window
                        if (vmName != null && !vmName.isEmpty()) {
                            try {
                                // Opening vmNodeWdw for Node
                                new vmNodeWdw(vmName);  // Pass only the VM/CT Name
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(mainFrame, "Could not extract VM/CT Name from nodeInfo: " + nodeInfo, "Alert", JOptionPane.WARNING_MESSAGE);     
                            System.out.println("Could not extract VM/CT Name from nodeInfo: " + nodeInfo);
                        }
                    }
                }
            }
            
        });
        
        // EAST Panel
        JPanel eastPanel = new JPanel();

        // SOUTH Panel
        JPanel southPanel = new JPanel();
        JLabel statusLbl = new JLabel("Ready");
        southPanel.add(statusLbl);
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        southPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                

        connectItem.addActionListener(e -> {
            new connectWdw();
        });

        // Settings Window
        settingsItem.addActionListener(e -> {
            new settingsWdw();
        });

        // About Window
        aboutItem.addActionListener(e -> {
            new AboutWdw();
        });
        
        createvmItem.addActionListener(e -> {
            new createvmWdw();
        });

        createctItem.addActionListener(e -> {
            new createctWdw();
        });

        disconnectItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,"Disconnected!");  
        });

        // Quit Application from Menu
        quitItem.addActionListener(e -> System.exit(0));

        //Tab Area

        //Panel 1: Node Summary
        JPanel p1a = new JPanel();
        p1a.setLayout(new BoxLayout(p1a, BoxLayout.Y_AXIS));
        APIClient apiclient = new APIClient();
        String nodeName = usrprefs.get("node", null);
        String response = apiclient.readData("/api2/json/nodes/"+ nodeName +"/status");

        
      
        
        if (response == null) {
            System.err.println("Node Not Found.");
        } else {
            JsonFetch dataFetcher = new JsonFetch(response);

            String pveRelease = dataFetcher.getNestedValueByKey("data", "current-kernel", "release");
            String cpuModel = dataFetcher.getNestedValueByKey("data", "cpuinfo", "model");
            String cpuSockets = dataFetcher.getNestedValueByKey("data", "cpuinfo", "sockets");
            String cpuCores = dataFetcher.getNestedValueByKey("data", "cpuinfo", "cores");
            String bootMode = dataFetcher.getNestedValueByKey("data", "boot-info", "mode");
            String uptime = dataFetcher.getNestedValueByKey("data","uptime");
            int updateCnt = Status.updateCount();
            String updateCntStr = Integer.toString(updateCnt);
            String ramTotal = dataFetcher.getNestedValueByKey("data", "memory", "total");
            String ramUsed = dataFetcher.getNestedValueByKey("data", "memory", "used");
            
            // Convert uptime to days, hours, and minutes
            long uptimeSeconds = Long.parseLong(uptime);
            long days = uptimeSeconds / 86400; // 86400 seconds in a day
            long hours = (uptimeSeconds % 86400) / 3600; // 3600 seconds in an hour
            long minutes = (uptimeSeconds % 3600) / 60; // 60 seconds in a minute

            JLabel pveLabel, cpuModelLbl, cpuSocketsLbl, cpuCoresLbl, ramLabel, bootModeLabel, uptimeLabel, updateCntLabel;

            pveLabel = new JLabel("PVE Release: " + pveRelease);
            cpuModelLbl = new JLabel("CPU Model: " + cpuModel);
            cpuSocketsLbl = new JLabel("CPU Sockets: " + cpuSockets);
            cpuCoresLbl = new JLabel("CPU Cores: " + cpuCores);
            ramLabel = new JLabel("RAM Usage: " + ramUsed + " / " + ramTotal);
            bootModeLabel = new JLabel("Boot Mode: " + bootMode);
            uptimeLabel = new JLabel("Uptime: " + days + " days, " + hours + " hours, " + minutes + " minutes");
            updateCntLabel = new JLabel("Update Count: " + updateCntStr);
            

            p1a.add(pveLabel);
            p1a.add(cpuModelLbl);
            p1a.add(cpuSocketsLbl);
            p1a.add(cpuCoresLbl);
            p1a.add(ramLabel);
            p1a.add(bootModeLabel);
            p1a.add(uptimeLabel);
            p1a.add(updateCntLabel);
        }


        //Panel 2: Disk
        JPanel p2=new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.add(Status.diskStatus());
        p2.revalidate(); // Revalidate to refresh the UI
        p2.repaint(); // Repaint the panel to show the new content

        //Panel 3: Network
        JPanel p3=new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        p3.add(Status.networkStatus());


        //Panel 4: Storage
        JPanel p4=new JPanel();
        p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
        p4.add(Status.storageStatus());

        //Panel 5: Updates
        JPanel p5=new JPanel();
        JPanel p5a=new JPanel();
        JPanel p5b=new JPanel();
        p5.setLayout(new BoxLayout(p5, BoxLayout.Y_AXIS));
        p5a.setLayout(new BoxLayout(p5a, BoxLayout.X_AXIS));
        p5a.setAlignmentX(Component.LEFT_ALIGNMENT);
        p5b.setLayout(new BoxLayout(p5b, BoxLayout.Y_AXIS));

        int updateCnt = Status.updateCount();
        String updateCntStr = Integer.toString(updateCnt);
        JLabel updateCountLbl = new JLabel("Update Count: " + updateCntStr);
        JButton updrefreshBtn = new JButton("Refresh");
        JButton updateBtn = new JButton("Update");
        
        if (updateCnt > 5) {
            Icon updateIcon = IconFontSwing.buildIcon(FontAwesome.DOWNLOAD, 13, new Color(161, 22, 22));
            updateCountLbl.setIcon(updateIcon);
        }
         else {
            Icon updateIcon = IconFontSwing.buildIcon(FontAwesome.DOWNLOAD, 13);
            updateCountLbl.setIcon(updateIcon);
        }

        p5a.add(updateCountLbl);
        p5a.add(updrefreshBtn);
        p5a.add(updateBtn);
        
        //Add Table of Updates
        p5b.add(Status.updateStatus());

        p5.add(p5a);
        p5.add(p5b);

        updrefreshBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,"Updates Refreshed!");
            //updates.java will have the functions for this button
        });

        updateBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,"Updates Applied!");
            //updates.java will have the functions for this button
        });

        //Panel 6: System Log
        JPanel p6=new JPanel();
        p6.setLayout(new BoxLayout(p6, BoxLayout.Y_AXIS));
        String response_log = apiclient.readData("/api2/json/nodes/"+ nodeName +"/syslog");
        
        if (response == null) {
            System.err.println("Node Not Found.");
        } else {
            
            JsonFetch dataFetcher_log = new JsonFetch(response_log);
            String syslogData = dataFetcher_log.getNestedValueByKey("data");
            JTextArea p6ta = new JTextArea(syslogData);
            p6ta.setEditable(false);
            p6ta.setLineWrap(true);
            p6.add(p6ta);
        }


        // Tabs are here!
        JTabbedPane tp=new JTabbedPane();  
        tp.setBounds(265,50,700,545);
        tp.add("Node Summary", p1a);
        tp.add("Disks",p2);  
        tp.add("Network",p3);  
        tp.add("Storage",p4);
        tp.add("Updates", p5);
        tp.add("System Log", p6);
        
        // Main Window Objects
        mainFrame.setLayout(new BorderLayout(10, 10));
        mainFrame.add(heading, BorderLayout.NORTH);
        
        mainFrame.add(jt, BorderLayout.WEST);
        mainFrame.add(tp, BorderLayout.CENTER);
        mainFrame.add(eastPanel, BorderLayout.EAST);
        mainFrame.add(southPanel, BorderLayout.SOUTH);

        
        // Main Window Display
        Image icon = Toolkit.getDefaultToolkit().getImage("assets/images/Jabiru.png");
        mainFrame.setIconImage(icon);
        mainFrame.setSize(1024, 768);
        mainFrame.setLocationRelativeTo(null);
        
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}