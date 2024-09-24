import java.awt.event.*;
import java.util.prefs.Preferences;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import jiconfont.swing.IconFontSwing;
import jiconfont.icons.font_awesome.FontAwesome;

public class mainFrame {
    static Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
    
    JFrame mainFrame;
    mainFrame(){
        IconFontSwing.register(FontAwesome.getIconFont());

        // Main Window Configuration
        mainFrame = new JFrame("ProxCtrlJ");
        JLabel heading = new JLabel("ProxCtrlJ"); 
        heading.setBounds(50,10, 200,30);
        heading.setFont(new Font("Sans Serif", Font.BOLD, 28));

        
        // Main Menu Bar
        JMenu fileItem, viewItem, createItem, helpItem;
        JMenuItem connectItem, disconnectItem, settingsItem, createvmItem, createctItem, refreshItem, quitItem, aboutItem;
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
                        if (nodeInfo.contains(" - Name: ")) {
                            int startIndex = nodeInfo.indexOf(" - Name: ") + 9; // Start after " - Name: "
                            // If no trailing " (VM)" or " (CT)" exists, take the rest of the string as the name
                            vmName = nodeInfo.substring(startIndex).trim();
                        }
        
                        // If vmName was successfully extracted, open the window
                        if (vmName != null) {
                            try {
                                new vmNodeWdw(vmName);  // Pass only the VM/CT Name
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        } else {
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
        JLabel statusLbl = new JLabel("Status: ");
        southPanel.add(statusLbl);
                

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

        //Panel 1: Node Status
        JPanel p1=new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        String p1data = Status.nodeStatus();
        String p1ram = Status.ramUsagedata();
        JTextArea p1ta = new JTextArea(p1data);
        JTextArea p1ta2 = new JTextArea(p1ram);
        p1ta.setEditable(false);
        p1ta.setBounds(0, 0, 100, 100);
        JScrollPane p1sp = new JScrollPane(p1ta);
        p1sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        p1sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane p1sp2 = new JScrollPane(p1ta2);
        p1sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        p1sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        p1.add(p1sp);
        p1.add(p1sp2);


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
        p5.setLayout(new BoxLayout(p5, BoxLayout.Y_AXIS));

        int updateCnt = Status.updateCount();
        String updateCntStr = Integer.toString(updateCnt);
        JLabel updateCountLbl = new JLabel("Update Count: " + updateCntStr);
        
        // if (updateCnt > 5) {
        //     Icon updateIcon = IconFontSwing.buildIcon(FontAwesome.UPLOAD, 13, new Color(161, 22, 22));
        //     updateCountLbl.setIcon(updateIcon);
        //  }
        // else {
        //     Icon updateIcon = IconFontSwing.buildIcon(FontAwesome.UPLOAD, 13);
        //     updateCountLbl.setIcon(updateIcon);
        // }
        Icon updateIcon = IconFontSwing.buildIcon(FontAwesome.DOWNLOAD, 13);
        updateCountLbl.setIcon(updateIcon);
        p5.add(updateCountLbl);
        p5.add(Status.updateStatus());


        // Tabs are here!
        JTabbedPane tp=new JTabbedPane();  
        tp.setBounds(265,50,700,545);  
        tp.add("Node Status",p1);
        tp.add("Disks",p2);  
        tp.add("Network",p3);  
        tp.add("Storage",p4);
        tp.add("Updates", p5);
        
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