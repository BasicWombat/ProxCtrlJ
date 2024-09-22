import java.awt.Desktop;
import java.net.URI;
import javax.swing.JOptionPane;

public class VncConsoleOpener {
    public static void openVncConsoleByName(String vmName) {
        try {
            // Get VM ID by name
            String vmid = VmDataFetcher.getVmId(vmName);

            if (vmid == null) {
                JOptionPane.showMessageDialog(null, "VM not found: " + vmName, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fetch VNC ticket and port
            APIClient.getVncTicketAndPort(vmid);
            String noVncUrl = APIClient.getVNCWebSocketUrl(vmid, vmName);
            System.out.println("NoVNC URL: " + noVncUrl);

            // Construct the VNC console URL
            //String noVncUrl = "https://" + host + ":" + hostPort + "/?console=kvm&novnc=1&vmid=" + vmid + "&vmname=" + URLEncoder.encode(vmName, "UTF-8");

            // Open URL in default browser
            Desktop.getDesktop().browse(new URI(noVncUrl));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open VNC console: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
