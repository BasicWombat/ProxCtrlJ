import java.awt.*;
import javax.swing.*;


public class createVM extends JPanel {
    private JLabel titleLbl;
    private JLabel nodeLbl;
    private JComboBox nodeDrp;
    private JLabel vmidLbl;
    private JTextField vmidTxt;
    private JLabel vmnameLbl;
    private JTextField vmnameTxt;
    private JLabel startbootLbl;
    private JCheckBox startbootBox;

    public createVM() {

        JFrame createvmWdw = new JFrame ("Create VM");
        //adjust size and set layout
        createvmWdw.setSize(800, 600);
        createvmWdw.setLayout (null);
        createvmWdw.setLocationRelativeTo(getParent());
        createvmWdw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createvmWdw.setLayout(null);
        
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        //construct preComponents
        String[] nodeDrpItems = {"Item 1", "Item 2", "Item 3"};

        //construct components
        titleLbl = new JLabel ("Create VM");
        nodeLbl = new JLabel ("Node");
        nodeDrp = new JComboBox (nodeDrpItems);
        vmidLbl = new JLabel ("VM ID");
        vmidTxt = new JTextField (5);
        vmnameLbl = new JLabel ("VM Name:");
        vmnameTxt = new JTextField (5);
        startbootLbl = new JLabel ("Start at Boot");
        startbootBox = new JCheckBox ("");

        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        generalPnl.add(nodeLbl, gbc);
        gbc.gridx = 1; // Column 1
        generalPnl.add(nodeDrp, gbc);

        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        generalPnl.add(vmidLbl, gbc);
        gbc.gridx = 1; // Column 1
        generalPnl.add(vmidTxt, gbc);


        gbc.gridx = 2; // Column 
        gbc.gridy = 0; // Row 
        generalPnl.add(vmnameLbl, gbc);
        gbc.gridx = 3; // Column 
        generalPnl.add(vmnameTxt, gbc);

        gbc.gridx = 2; // Column 
        gbc.gridy = 1; // Row 
        generalPnl.add(startbootLbl, gbc);
        gbc.gridx = 3; // Column 
        generalPnl.add(startbootBox, gbc);


        //Panel 2: OS
        JPanel osPnl=new JPanel();
        
        //Panel 3: Disks
        JPanel diskPnl=new JPanel();

        //Panel 4: CPU
        JPanel cpuPnl=new JPanel();

        //Panel 5: Memory
        JPanel ramPnl=new JPanel();

        //Panel 6: Network
        JPanel netPnl=new JPanel();

        //Panel 7: Summary
        JPanel summPnl=new JPanel();

        // Tabs are here!
        JTabbedPane tp = new JTabbedPane();  
        tp.setBounds(150,50,500,450);  
        tp.add("General",generalPnl);
        tp.add("OS",osPnl);  
        tp.add("System",diskPnl);  
        tp.add("Disks",cpuPnl);
        tp.add("Memory", ramPnl);
        tp.add("Network", netPnl);
        tp.add("Summary", summPnl);

        createvmWdw.add(titleLabel);
        createvmWdw.add(tp);
        createvmWdw.add(createBtn);

        createvmWdw.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        createvmWdw.setVisible (true);
             
    
    
    }
}