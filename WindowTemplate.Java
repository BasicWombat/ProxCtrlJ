import javax.swing.*;

public class SwingWindowTemplate {

    // Constructor to set up the JFrame
    public SwingWindowTemplate() {
        // Create a new JFrame container
        JFrame frame = new JFrame("Swing Window Template");

        // Set the initial size of the window
        frame.setSize(400, 300);

        // Specify an action for the close button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold components
        JPanel panel = new JPanel();
        
        // Add the panel to the frame
        frame.add(panel);
        
        // Place components in the panel
        placeComponents(panel);
        
        // Make the window visible
        frame.setVisible(true);
    }

    // Method to add components to the panel
    private void placeComponents(JPanel panel) {
        // Set the layout of the panel
        panel.setLayout(null);

        // Create a label and set its position
        JLabel label = new JLabel("Hello, Swing!");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        // Create a text field and set its position
        JTextField textField = new JTextField(20);
        textField.setBounds(100, 20, 165, 25);
        panel.add(textField);

        // Create a button and set its position
        JButton button = new JButton("Click Me");
        button.setBounds(10, 80, 80, 25);
        panel.add(button);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> new SwingWindowTemplate());
    }
}
