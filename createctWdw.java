import java.awt.*;
import javax.swing.*;


public class createctWdw extends JPanel {

    public createctWdw() {

        JFrame createctWdw = new JFrame ("Create CT");
        //adjust size and set layout
        createctWdw.setSize(800, 600);
        createctWdw.setLayout (null);
        createctWdw.setLocationRelativeTo(getParent());
        createctWdw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createctWdw.setLayout(null);
        
        // Title Label
        JLabel titleLabel = new JLabel("Create CT");
        titleLabel.setBounds(50, 10, 200, 30);
        titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));

        // Create Button
        JButton createBtn = new JButton("Create");
        createBtn.setBounds(550, 520, 95, 30);
        

        //Tab Area

        //Panel 1: General
        JPanel generalPnl=new JPanel();
            //construct preComponents
            String[] nodeDrpItems = {"Item 1", "Item 2", "Item 3"};
            String[] resourceDrpItems = {"Item 1", "Item 2", "Item 3"};

            //construct components
            JLabel titleLbl = new JLabel ("Create CT");
            JLabel nodeLbl = new JLabel ("Node");
            JComboBox<String> nodeDrp = new JComboBox<String> (nodeDrpItems);
            JLabel resourceLbl = new JLabel ("Resource");
            JComboBox<String> resourceDrp = new JComboBox<String> (resourceDrpItems);
            JLabel ctidLbl = new JLabel ("CT ID");
            JTextField ctidTxt = new JTextField (5);
            JLabel ctnameLbl = new JLabel ("Hostname:");
            JTextField ctnameTxt = new JTextField (5);
            JLabel nestingLbl = new JLabel ("Nesting:");
            JCheckBox nestingBox = new JCheckBox ("");
            JLabel unprivLbl = new JLabel ("Unpriviledge Container:");
            JCheckBox unprivBox = new JCheckBox ("");
            JLabel pass1Lbl = new JLabel ("Password:");
            JTextField pass1Box = new JTextField ("");
            JLabel pass2Lbl = new JLabel ("Confirm Password:");
            JTextField pass2Box = new JTextField ("");

            generalPnl.add(titleLbl);
            generalPnl.add(nodeLbl);
            generalPnl.add(nodeDrp);
            generalPnl.add(resourceLbl);
            generalPnl.add(resourceDrp);
            generalPnl.add(ctidLbl);
            generalPnl.add(ctidTxt);
            generalPnl.add(ctnameLbl);
            generalPnl.add(ctnameTxt);
            generalPnl.add(nestingLbl);
            generalPnl.add(nestingBox);
            generalPnl.add(unprivLbl);
            generalPnl.add(unprivBox);
            generalPnl.add(pass1Lbl);
            generalPnl.add(pass1Box);
            generalPnl.add(pass2Lbl);
            generalPnl.add(pass2Box);




        //Panel 2: Template
        JPanel tempPanel = new JPanel();
        String[] isostorDrpItems = { "iso", "local"};
        String[] tempDrpItems = { "turnkey_linux", "turnkey_gameserver", "debian_linux"};

        JLabel storLbl = new JLabel ("Storage");
        JComboBox<String> storDrp = new JComboBox<String> (isostorDrpItems);
        JLabel tempLbl = new JLabel ("Template");
        JComboBox<String> tempDrp = new JComboBox<String> (tempDrpItems);

        tempPanel.add(storLbl);
        tempPanel.add(storDrp);
        tempPanel.add(tempLbl);
        tempPanel.add(tempDrp);


        //Panel 3: Disks
        JPanel diskPnl=new JPanel();
        String[] diskstorDrpItems = { "iso", "local"};
        JLabel diskstorLbl = new JLabel ("Storage");
        JComboBox<String> diskstorDrp = new JComboBox<String> (diskstorDrpItems);
        JLabel disksizeLbl = new JLabel ("Disk Size:");
        JTextField disksizeTxt = new JTextField (5);
        
        diskPnl.add(diskstorLbl);
        diskPnl.add(diskstorDrp);
        diskPnl.add(disksizeLbl);
        diskPnl.add(disksizeTxt);


        //Panel 4: CPU
        JPanel cpuPnl=new JPanel();

        JLabel cpuLbl = new JLabel ("Cores:");
        JTextField cpuFld = new JTextField (5);

        cpuPnl.add(cpuLbl);
        cpuPnl.add(cpuFld);


        //Panel 5: Memory
        JPanel ramPnl=new JPanel();

        JLabel ramLbl = new JLabel ("Memory (MiB):");
        JTextField ramFld = new JTextField (5);
        JLabel swapLbl = new JLabel ("SWAP (MiB):");
        JTextField swapFld = new JTextField (5);

        ramPnl.add(ramLbl);
        ramPnl.add(ramFld);
        ramPnl.add(swapLbl);
        ramPnl.add(swapFld);

        //Panel 6: Network
        JPanel netPnl=new JPanel();

        JLabel nicnameLbl = new JLabel ("Name:");
        JTextField nicnameFld = new JTextField ("eth0",8);
        JLabel bridgeLbl = new JLabel ("SWAP (MiB):");
        String[] bridgeItems = {"vmbr0","vmbr1","vmbr2"};
        JComboBox<String> bridgeDrp = new JComboBox<String> (bridgeItems);
        JLabel vlanLbl = new JLabel ("VLAN Tag:");
        JTextField vlanFld = new JTextField ("No VLAN",4);
        JLabel fwLbl = new JLabel ("Firewall:");
        JCheckBox fwBox = new JCheckBox ("");

        // IPv4 Settings

        JRadioButton ipv4staticOpt=new JRadioButton("Static");    
        JRadioButton ipv4dhcpOpt=new JRadioButton("DHCP");
        ButtonGroup ipv4optBGroup=new ButtonGroup();
        ipv4optBGroup.add(ipv4staticOpt);
        ipv4optBGroup.add(ipv4dhcpOpt);

        JLabel ipv4addrLbl = new JLabel ("IPv4/CIDR: ");
        JTextField ipv4addrFld = new JTextField (20);
        JLabel ipv4dnsLbl = new JLabel ("DNS: ");
        JTextField ipv4dnsFld = new JTextField (20);

        // IPv6 Settings

        JRadioButton ipv6staticOpt=new JRadioButton("Static");    
        JRadioButton ipv6dhcpOpt=new JRadioButton("DHCP");
        JRadioButton ipv6slaacOpt=new JRadioButton("SLAAC");
        ButtonGroup ipv6optBGroup=new ButtonGroup();
        ipv6optBGroup.add(ipv6staticOpt);
        ipv6optBGroup.add(ipv6dhcpOpt);
        ipv6optBGroup.add(ipv6slaacOpt);



        JLabel ipv6addrLbl = new JLabel ("IPv4/CIDR: ");
        JTextField ipv6addrFld = new JTextField (20);
        JLabel ipv6dnsLbl = new JLabel ("DNS: ");
        JTextField ipv6dnsFld = new JTextField (20);

        netPnl.add(nicnameLbl);
        netPnl.add(nicnameFld);
        netPnl.add(bridgeLbl);
        netPnl.add(bridgeDrp);
        netPnl.add(vlanLbl);
        netPnl.add(vlanFld);
        netPnl.add(fwLbl);
        netPnl.add(fwBox);

        netPnl.add(ipv4staticOpt);
        netPnl.add(ipv4dhcpOpt);
        netPnl.add(ipv4addrLbl);
        netPnl.add(ipv4addrFld);
        netPnl.add(ipv4dnsLbl);
        netPnl.add(ipv4dnsFld);

        netPnl.add(ipv6staticOpt);
        netPnl.add(ipv6dhcpOpt);
        netPnl.add(ipv6slaacOpt);
        netPnl.add(ipv6addrLbl);
        netPnl.add(ipv6addrFld);
        netPnl.add(ipv6dnsLbl);
        netPnl.add(ipv6dnsFld);

        //Panel 7: Summary
        JPanel summPnl=new JPanel();

        // Tabs are here!
        JTabbedPane tp = new JTabbedPane();  
        tp.setBounds(150,50,500,450);  
        tp.add("General",generalPnl);
        tp.add("Template",tempPanel);  
        tp.add("System",diskPnl);  
        tp.add("Disks",cpuPnl);
        tp.add("Memory", ramPnl);
        tp.add("Network", netPnl);
        tp.add("Summary", summPnl);

        createctWdw.add(titleLabel);
        createctWdw.add(tp);
        createctWdw.add(createBtn);

        createctWdw.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        createctWdw.setVisible (true);
             
    
    
    }
}