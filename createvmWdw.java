import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class createvmWdw extends JPanel {

    public createvmWdw() {

        JFrame createvmWdw = new JFrame ("Create VM");
        //adjust size and set layout
        createvmWdw.setSize(800, 600);
        createvmWdw.setLayout (null);
        createvmWdw.setLocationRelativeTo(getParent());
        createvmWdw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createvmWdw.setLayout(null);
        createvmWdw.setVisible (true);
        
        // Title Label
        JLabel titleLabel = new JLabel("Create VM");
        titleLabel.setBounds(50, 10, 200, 30);
        titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));

        // Create Button
        JButton createBtn = new JButton("Create");
        createBtn.setBounds(550, 520, 95, 30);
        

        //Tab Area

        //Panel 1: General
        JPanel generalPnl=new JPanel();
        generalPnl.setLayout(new GridBagLayout());

        GridBagConstraints gbcP1 = new GridBagConstraints();
        gbcP1.fill = GridBagConstraints.HORIZONTAL;
        gbcP1.insets = new Insets(5, 2, 5, 2); // Padding

        //construct preComponents
        String[] nodeDrpItems = {"Item 1", "Item 2", "Item 3"};
        String[] resDrpItems = {"Item 1", "Item 2", "Item 3"};

        //construct components
        JLabel nodeLbl = new JLabel ("Node");
        JComboBox<String> nodeDrp = new JComboBox<String> (nodeDrpItems);
        JLabel resLbl = new JLabel ("Node");
        JComboBox<String> resDrp = new JComboBox<String> (resDrpItems);
        JLabel vmidLbl = new JLabel ("VM ID");
        JTextField vmidTxt = new JTextField (5);
        JLabel vmnameLbl = new JLabel ("VM Name:");
        JTextField vmnameTxt = new JTextField (5);
        JLabel startbootLbl = new JLabel ("Start at Boot");
        JCheckBox startbootBox = new JCheckBox ("");
        JLabel startupOrderLbl = new JLabel ("Startup Order");
        JTextField startupOrderTxt = new JTextField (5);
        startupOrderTxt.setText("any");
        JLabel startupDelayLbl = new JLabel ("Startup Delay");
        JTextField startupDelayTxt = new JTextField (5);
        JLabel shutdownDelayLbl = new JLabel ("Shutdown Delay");
        JTextField shutdownDelayTxt = new JTextField (5);

        // Row 0

        gbcP1.gridx = 0; // Column 0
        gbcP1.gridy = 0; // Row 0
        generalPnl.add(nodeLbl, gbcP1);
        gbcP1.gridx = 1; // Column 1
        generalPnl.add(nodeDrp, gbcP1);

        gbcP1.gridx = 2; // Column 
        gbcP1.gridy = 0; // Row 
        generalPnl.add(resLbl, gbcP1);
        gbcP1.gridx = 3; // Column 
        generalPnl.add(resDrp, gbcP1);

        // Row 1

        gbcP1.gridx = 0; // Column 0
        gbcP1.gridy = 1; // Row 1
        generalPnl.add(vmidLbl, gbcP1);
        gbcP1.gridx = 1; // Column 1
        generalPnl.add(vmidTxt, gbcP1);

        gbcP1.gridx = 2; // Column 
        gbcP1.gridy = 1; // Row 
        generalPnl.add(vmnameLbl, gbcP1);
        gbcP1.gridx = 3; // Column 
        generalPnl.add(vmnameTxt, gbcP1);

        // Row 2

        gbcP1.gridx = 0; // Column 0
        gbcP1.gridy = 2; // Row 1
        generalPnl.add(startupOrderLbl, gbcP1);
        gbcP1.gridx = 1; // Column 1
        generalPnl.add(startupOrderTxt, gbcP1);

        // Row 3

        gbcP1.gridx = 0; // Column 0
        gbcP1.gridy = 3; // Row 1
        generalPnl.add(shutdownDelayLbl, gbcP1);
        gbcP1.gridx = 1; // Column 1
        generalPnl.add(shutdownDelayTxt, gbcP1);

        gbcP1.gridx = 2; // Column 
        gbcP1.gridy = 3; // Row 
        generalPnl.add(startupDelayLbl, gbcP1);
        gbcP1.gridx = 3; // Column 
        generalPnl.add(startupDelayTxt, gbcP1);

        // Row 5

        gbcP1.gridx = 2; // Column 
        gbcP1.gridy = 6; // Row 
        generalPnl.add(startbootLbl, gbcP1);
        gbcP1.gridx = 3; // Column 
        generalPnl.add(startbootBox, gbcP1);
            

        //Panel 2: OS
        JPanel osPnl=new JPanel();
        osPnl.setLayout(new GridBagLayout());

        GridBagConstraints gbcP2 = new GridBagConstraints();
        gbcP2.fill = GridBagConstraints.HORIZONTAL;
        gbcP2.insets = new Insets(5, 2, 5, 2); // Padding
        
        JRadioButton isoOpt=new JRadioButton("Use ISO Disc Image");    
        JRadioButton phyOpt=new JRadioButton("Use Physical Media");
        JRadioButton nomodOpt=new JRadioButton("No Media");
        ButtonGroup optBGroup=new ButtonGroup();
        optBGroup.add(isoOpt);
        optBGroup.add(phyOpt);
        optBGroup.add(nomodOpt);
        String[] isostorDrpItems = { "iso", "local"};
        String[] isoitemsDrpItems = { "win11.iso", "win10.iso", "linux.iso"};

        JLabel isostorLbl = new JLabel ("Storage:");
        JLabel isoitemsLbl = new JLabel ("ISO Image:");
        String[] ostypeDrpItems = { "Linux", "Windows", "Solaris", "Other" };
        String[] linuxVersions = { "Linux 2.6 - 6.X Kernel", "Linux 2.4 Kernel" };
        String[] windowsVersions = { "Microsoft Windows 11/2022/2025", "Microsoft Windows 10/2016/2019", "Microsoft Windows 8/2012/2012r2",
        "Microsoft Windows 7", "Microsoft Windows Vista", "Microsoft Windows 2008", "Microsoft Windows 2003", "Microsoft Windows 2003", "Microsoft Windows XP"};      
        String[] solarisVersions = { "Solaris/OpenSolaris/OpenIndiania kernel"};
        String[] otherVersions = { "Other" };

        JComboBox<String> isostorDrp = new JComboBox<String> (isostorDrpItems);
        JComboBox<String> isoitemsDrp = new JComboBox<String> (isoitemsDrpItems);
        JLabel osLbl = new JLabel ("Guest OS:");
        JLabel ostypeLbl = new JLabel ("Type: ");
        JComboBox<String> ostypeDrp = new JComboBox<String> (ostypeDrpItems);
        JLabel osverLbl = new JLabel ("Version: ");
        JComboBox<String> osverDrp = new JComboBox<>();

        // Initially hide the combo boxes
        isostorLbl.setVisible(false);
        isoitemsLbl.setVisible(false);
        isostorDrp.setVisible(false);
        isoitemsDrp.setVisible(false);

        // Add action listener to the radio button
        ActionListener radioActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show combo boxes only when isoOpt is selected
                if (isoOpt.isSelected()) {
                    isostorLbl.setVisible(true);
                    isoitemsLbl.setVisible(true);
                    isostorDrp.setVisible(true);
                    isoitemsDrp.setVisible(true);
                } else {
                    isostorLbl.setVisible(false);
                    isoitemsLbl.setVisible(false);
                    isostorDrp.setVisible(false);
                    isoitemsDrp.setVisible(false);
                }
            }
        };

        isoOpt.addActionListener(radioActionListener);
        phyOpt.addActionListener(radioActionListener);
        nomodOpt.addActionListener(radioActionListener);
        

        ostypeDrp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOS = (String) ostypeDrp.getSelectedItem();
                
                // Update the osverDrp items based on the selected OS type
                switch (selectedOS) {
                    case "Linux":
                        osverDrp.setModel(new DefaultComboBoxModel<>(linuxVersions));
                        break;
                    case "Windows":
                        osverDrp.setModel(new DefaultComboBoxModel<>(windowsVersions));
                        break;
                    case "Solaris":
                        osverDrp.setModel(new DefaultComboBoxModel<>(solarisVersions));
                        break;
                    case "Other":
                        osverDrp.setModel(new DefaultComboBoxModel<>(otherVersions));
                        break;
                }
            }
        });
        
        // Row 0
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 0; // Row 0
        osPnl.add(isoOpt, gbcP2);

        // Row 1
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 2; // Row 1
        osPnl.add(phyOpt, gbcP2);

        // Row 2
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 3; // Row 2
        osPnl.add(nomodOpt, gbcP2);

        // Row 3
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 4; // Row 3
        osPnl.add(isostorLbl, gbcP2);
        gbcP2.gridx = 1;
        osPnl.add(isostorDrp, gbcP2);

        // Row 4
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 5;
        osPnl.add(isoitemsLbl, gbcP2);
        gbcP2.gridx = 1;
        osPnl.add(isoitemsDrp, gbcP2);


        // Row 5
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 6;
        JSeparator sepP2 = new JSeparator();
        osPnl.add(sepP2, gbcP2);

        // Row 6 - Guest OS Label Heading
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 10;
        osPnl.add(osLbl, gbcP2);

        // Row 7
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 11;
        osPnl.add(ostypeLbl, gbcP2);
        gbcP2.gridx = 1;
        osPnl.add(ostypeDrp, gbcP2);

        // Row 8
        gbcP2.gridx = 0; // Column 0
        gbcP2.gridy = 12;
        osPnl.add(osverLbl, gbcP2);
        gbcP2.gridx = 1;
        osPnl.add(osverDrp, gbcP2);

        
        //Panel 3: System
        JPanel sysPnl=new JPanel();
        sysPnl.setLayout(new GridBagLayout());
        GridBagConstraints gbcP3 = new GridBagConstraints();
        gbcP3.fill = GridBagConstraints.HORIZONTAL;
        gbcP3.insets = new Insets(5, 2, 5, 2); // Padding
        JLabel gfxLbl = new JLabel ("Graphics Card:");
        JLabel machineLbl = new JLabel ("Machine:");

        JLabel scsiLbl = new JLabel ("SCSI Controller:");
        JLabel qemuagtLbl = new JLabel ("Qemu Agent:");
        JCheckBox qemuagtBox = new JCheckBox ("");

        JLabel fwhdLbl = new JLabel ("Firmware");
        JLabel biosLbl = new JLabel ("BIOS:");
        JLabel tpmLbl = new JLabel ("TPM:", JLabel.RIGHT);
        JCheckBox tpmBox = new JCheckBox ("");

        String[] gfxDrpItems = { "Default", "VGA", "VMware", "SPICE", "VirtIO" };
        String[] machineDrpItems = { "Intel", "VirtIO" };
        String[] scsiDrpItems = { "VirtIO SCSI Single", "LSI", "VMware"};      
        String[] biosDrpItems = { "SeaBIOS","OVMF"};


        JComboBox<String> gfxDrp = new JComboBox<String> (gfxDrpItems);
        JComboBox<String> machineDrp = new JComboBox<String> (machineDrpItems);
        JComboBox<String> scsiDrp = new JComboBox<String> (scsiDrpItems);
        JComboBox<String> biosDrp = new JComboBox<String> (biosDrpItems);

        // Row 0
        gbcP3.gridx = 0; // Column 0
        gbcP3.gridy = 0; // Row 0
        sysPnl.add(gfxLbl, gbcP3);
        gbcP3.gridx = 1;
        sysPnl.add(gfxDrp, gbcP3);
        gbcP3.gridx = 2;
        sysPnl.add(scsiLbl, gbcP3);
        gbcP3.gridx = 3;
        sysPnl.add(scsiDrp, gbcP3);
        

        // Row 1
        gbcP3.gridx = 0; // Column 0
        gbcP3.gridy = 1; // Row 1
        sysPnl.add(machineLbl, gbcP3);
        gbcP3.gridx = 1;
        sysPnl.add(machineDrp, gbcP3);

        // Row 2
        gbcP3.gridx = 0; // Column 0
        gbcP3.gridy = 2; // Row 2
        sysPnl.add(qemuagtLbl, gbcP3);
        gbcP3.gridx = 1;
        sysPnl.add(qemuagtBox, gbcP3);

        // Row 3
        JSeparator sepP3 = new JSeparator();
        gbcP3.gridx = 0; // Column 0
        gbcP3.gridy = 3; // Row 3
        gbcP3.gridwidth = 4;
        gbcP3.fill = GridBagConstraints.HORIZONTAL;
        sysPnl.add(sepP3, gbcP3);

        // Row 4
        gbcP3.gridx = 0; // Column 0    
        gbcP3.gridy = 4; // Row 4
        gbcP3.gridwidth = 1;
        sysPnl.add(fwhdLbl, gbcP3);

        // Row 5
        gbcP3.gridx = 0; // Column 0
        gbcP3.gridy = 5; // Row 5
        sysPnl.add(biosLbl, gbcP3);
        gbcP3.gridx = 1;
        sysPnl.add(biosDrp, gbcP3);
        gbcP3.gridx = 2;
        sysPnl.add(tpmLbl, gbcP3);
        gbcP3.gridx = 3;
        sysPnl.add(tpmBox, gbcP3);


        //Panel 4: Disks
        JPanel diskPnl=new JPanel();

        //Panel 5: CPU
        JPanel cpuPnl=new JPanel();

        //Panel 6: Memory
        JPanel ramPnl=new JPanel();

        //Panel 7: Network
        JPanel netPnl=new JPanel();

        //Panel 8: Summary
        JPanel summPnl=new JPanel();

        // Tabs are here!
        JTabbedPane tp = new JTabbedPane();  
        tp.setBounds(150,50,500,450);  
        tp.add("General",generalPnl);
        tp.add("OS",osPnl);  
        tp.add("System",sysPnl);
        tp.add("Disks",diskPnl);  
        tp.add("CPU",cpuPnl);
        tp.add("Memory", ramPnl);
        tp.add("Network", netPnl);
        tp.add("Summary", summPnl);

        createvmWdw.add(titleLabel);
        createvmWdw.add(tp);
        createvmWdw.add(createBtn);

    }
}