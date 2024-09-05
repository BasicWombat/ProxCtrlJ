import java.awt.*;
import javax.swing.*;


public class createctWdw extends JPanel {
    private JLabel titleLbl;
    private JLabel nodeLbl;
    private JComboBox<String> nodeDrp;
    private JLabel vmidLbl;
    private JTextField vmidTxt;
    private JLabel vmnameLbl;
    private JTextField vmnameTxt;
    private JLabel startbootLbl;
    private JCheckBox startbootBox;

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

            //construct components
            titleLbl = new JLabel ("Create CT");
            nodeLbl = new JLabel ("Node");
            nodeDrp = new JComboBox<String> (nodeDrpItems);
            vmidLbl = new JLabel ("VM ID");
            vmidTxt = new JTextField (5);
            vmnameLbl = new JLabel ("VM Name:");
            vmnameTxt = new JTextField (5);
            startbootLbl = new JLabel ("Start at Boot");
            startbootBox = new JCheckBox ("");

            generalPnl.add(titleLbl);
            generalPnl.add(nodeLbl);
            generalPnl.add(nodeDrp);
            generalPnl.add(vmidLbl);
            generalPnl.add(vmidTxt);
            generalPnl.add(vmnameLbl);
            generalPnl.add(vmnameTxt);
            generalPnl.add(startbootLbl);
            generalPnl.add(startbootBox);


        //Panel 2: OS
        JTextArea ta=new JTextArea(200,200);
        JPanel osPnl=new JPanel();
        osPnl.setLayout(new BoxLayout(osPnl, BoxLayout.Y_AXIS));
        osPnl.add(ta);

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

        createctWdw.add(titleLabel);
        createctWdw.add(tp);
        createctWdw.add(createBtn);

        createctWdw.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        createctWdw.setVisible (true);
             
    
    
    }
}