import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

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
        String urlString = "https://" + host + ":" + hostPort+ "/api2/json/nodes/";

        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "PVEAPIToken=" + authToken);


            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray nodesArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < nodesArray.length(); i++) {
                JSONObject node = nodesArray.getJSONObject(i);
                DefaultMutableTreeNode nodeTreeNode = new DefaultMutableTreeNode(node.getString("node"));

                // Get VMs for each node
                String nodeName = node.getString("node");
                String vmUrl = urlString + nodeName + "/qemu";
                URI uri2 = new URI(vmUrl);
                URL url2 = uri2.toURL();
                HttpURLConnection vmConn = (HttpURLConnection) url2.openConnection();
                vmConn.setRequestMethod("GET");
                vmConn.setRequestProperty("Authorization", "PVEAPIToken=" + authToken);

                BufferedReader vmIn = new BufferedReader(new InputStreamReader(vmConn.getInputStream()));
                StringBuffer vmContent = new StringBuffer();
                while ((inputLine = vmIn.readLine()) != null) {
                    vmContent.append(inputLine);
                }
                vmIn.close();

                JSONObject vmResponse = new JSONObject(vmContent.toString());
                JSONArray vmsArray = vmResponse.getJSONArray("data");

                for (int j = 0; j < vmsArray.length(); j++) {
                    JSONObject vm = vmsArray.getJSONObject(j);
                    DefaultMutableTreeNode vmTreeNode = new DefaultMutableTreeNode(vm.getString("name"));
                    nodeTreeNode.add(vmTreeNode);
                }

                root.add(nodeTreeNode);
            }

        } catch (NoRouteToHostException e) {
            // Show an error dialog if there's no route to host
            JOptionPane.showMessageDialog(null, "Error: Cannot contact the server.\n" + e.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Show a general error dialog for any other exceptions
            JOptionPane.showMessageDialog(null, "An unexpected error occurred.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return new DefaultTreeModel(root);
    }

    public TreePath getPathForLocation(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPathForLocation'");
    }
}
