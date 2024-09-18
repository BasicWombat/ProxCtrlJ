import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

public class VncConsoleOpener {
    private static APIClient apiClient;
    private static String host;
    private static String hostPort;
    private static String node;

    public VncConsoleOpener(String host, String hostPort, String node, String apiTokenID, String apiSecret) {
        VncConsoleOpener.host = host;
        VncConsoleOpener.hostPort = hostPort;
        VncConsoleOpener.node = node;
        VncConsoleOpener.apiClient = new APIClient();
    }

    public static void openVncConsoleByName(String vmName) {
        try {
            // Get VM ID by name
            String vmid = vmDataFetcher.getVmId(vmName);

            if (vmid == null) {
                JOptionPane.showMessageDialog(null, "VM not found: " + vmName, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fetch VNC ticket and port
            JsonFetch vncResponse = APIClient.getVncTicketAndPort(vmid);
            String vncticket = vncResponse.getNestedValueByKey("data", "ticket").replace("\"", "");
            String port = vncResponse.getNestedValueByKey("data", "port").replace("\"", "");

            // Construct the VNC console URL
            String noVncUrl = "https://" + host + ":" + hostPort + "/?console=kvm&novnc=1&vmid=" + vmid + "&vmname=" + URLEncoder.encode(vmName, "UTF-8");

            // Open URL in default browser
            Desktop.getDesktop().browse(new URI(noVncUrl));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open VNC console: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
