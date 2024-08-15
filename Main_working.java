import java.awt.event.*;  
import javax.swing.*;


public class Main_working {  
  public static void main(String[] args) {  
    JFrame f=new JFrame("ProxCtrlJ");

    // Main Menu Bar
    JMenu file;
    JMenuItem connect, settings, quit;
    JMenuBar mb=new JMenuBar();
    file=new JMenu("File");
    connect=new JMenuItem("Connect");
    settings=new JMenuItem("Settings");
    quit=new JMenuItem("Quit");

    // connect.addActionListener(this);
    // settings.addActionListener(this);
    // quit.addActionListener(this);

    file.add(connect); file.add(settings); file.add(quit);
    mb.add(file);
    f.setJMenuBar(mb);
    f.setSize(400,400);
    f.setLayout(null);  
    f.setVisible(true);

    // Label Example
    JLabel l1,l2;  
    l1=new JLabel("First Label.");  
    l1.setBounds(50,50, 100,30);  
    l2=new JLabel("Second Label.");  
    l2.setBounds(50,100, 100,30);  
    f.add(l1); f.add(l2);  
    f.setSize(300,300);  
    f.setLayout(null);  
    f.setVisible(true);

    // Field Example
    final JTextField tf=new JTextField();  
    tf.setBounds(50,150, 150,20);  

    // Button Example
    JButton b=new JButton("Click Here");  
    b.setBounds(50,200,95,30);  
    b.addActionListener(new ActionListener(){  
      public void actionPerformed(ActionEvent e){  
              tf.setText("Welcome to Javatpoint.");  
          }  
      });  
      f.add(b);f.add(tf);  
      f.setSize(400,400);  
      f.setLayout(null);  
      f.setVisible(true);   
  }  
}  