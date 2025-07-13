package src;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

public class VncConsoleOpener {
    public static void openVncConsoleByName(String host, String hostPort, String node, String vmName) {
        try {
            // Get VM ID by name
            String vmid = VmDataFetcher.getVmId(vmName);

            if (vmid == null) {
                JOptionPane.showMessageDialog(null, "VM not found: " + vmName, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (host == null) {
                JOptionPane.showMessageDialog(null, "Host Name is Null!", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Host Name is Null!");
                return;
            }
            if (hostPort == null) {
                JOptionPane.showMessageDialog(null, "Host Port is Null!", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Host Port is Null!");
                return;
            }
            if (node == null) {
                JOptionPane.showMessageDialog(null, "Node is Null!", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Node is Null!");
                return;
            }

            // Fetch VNC ticket and port
            APIClient.getVncTicketAndPort(vmid);
            JsonFetch vncResponse = APIClient.getVncTicketAndPort(vmid);
            String vncticket = vncResponse.getNestedValueByKey("data", "ticket").replace("\"", "");
            String vncport = vncResponse.getNestedValueByKey("data", "port").replace("\"", "");

            // Construct the VNC console URL with ticket and port
            String noVncUrl = "https://" + host + ":" + hostPort + "/?console=kvm&novnc=1&vmid=" + vmid +
                  "&path=websockify&port=" + vncport +
                  "&password=" + URLEncoder.encode(vncticket, "UTF-8") +
                  "&encrypt=1";

            
                    // Debug output to check the URL
            System.out.println("noVNC URL: " + noVncUrl);
            
            // Open URL in default browser
            Desktop.getDesktop().browse(new URI(noVncUrl));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open VNC console: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
