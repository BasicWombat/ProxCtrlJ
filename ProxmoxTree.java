import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.prefs.Preferences;

public class ProxmoxTree {

    // Get the user preferences node for this class
    Preferences usrprefs = Preferences.userNodeForPackage(Main.class);

    public DefaultTreeModel getProxmoxTreeModel() {
        return fetchProxmoxTreeModel();
    }

    private DefaultTreeModel fetchProxmoxTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Proxmox Clusters");

        String host = usrprefs.get("host", null);
        String hostPort = usrprefs.get("hostport", null);
        String apiTokenID = usrprefs.get("apiTokenID", null);
        String apiSecret = usrprefs.get("apiSecret", null);
        String authToken = apiTokenID + "=" + apiSecret;
        String urlString = "https://" + host + ":" + hostPort + "/api2/json/nodes/";

        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "PVEAPIToken=" + authToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject jsonResponse = JsonParser.parseReader(in).getAsJsonObject();
            in.close();
            JsonArray nodesArray = jsonResponse.getAsJsonArray("data");

            for (JsonElement nodeElement : nodesArray) {
                JsonObject node = nodeElement.getAsJsonObject();
                DefaultMutableTreeNode nodeTreeNode = new DefaultMutableTreeNode(node.get("node").getAsString());

                // Get VMs for each node
                String nodeName = node.get("node").getAsString();
                String vmUrl = urlString + nodeName + "/qemu";
                URI uri2 = new URI(vmUrl);
                URL url2 = uri2.toURL();
                HttpURLConnection vmConn = (HttpURLConnection) url2.openConnection();
                vmConn.setRequestMethod("GET");
                vmConn.setRequestProperty("Authorization", "PVEAPIToken=" + authToken);

                BufferedReader vmIn = new BufferedReader(new InputStreamReader(vmConn.getInputStream()));
                JsonObject vmResponse = JsonParser.parseReader(vmIn).getAsJsonObject();
                vmIn.close();
                JsonArray vmsArray = vmResponse.getAsJsonArray("data");

                for (JsonElement vmElement : vmsArray) {
                    JsonObject vm = vmElement.getAsJsonObject();
                    DefaultMutableTreeNode vmTreeNode = new DefaultMutableTreeNode(vm.get("name").getAsString());
                    nodeTreeNode.add(vmTreeNode);
                }

                root.add(nodeTreeNode);
            }

        } catch (NoRouteToHostException e) {
            // Show an error dialog if there's no route to host
            //JOptionPane.showMessageDialog(null, "Error: Cannot contact the server.\n" + e.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error: Cannot contact the server.\n" + e.getMessage());
        } catch (Exception e) {
            // Show a general error dialog for any other exceptions
            System.err.println("An unexpected error occurred: " + e.getMessage());
            //JOptionPane.showMessageDialog(null, "An unexpected error occurred.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return new DefaultTreeModel(root);
    }

    public TreePath getPathForLocation(int x, int y) {
        throw new UnsupportedOperationException("Unimplemented method 'getPathForLocation'");
    }
}
