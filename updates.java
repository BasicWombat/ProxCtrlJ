/* This while class is a work in progress.
It's supposed to be a set of functions for checking for updates, and then performing them on the host. */



import java.util.prefs.Preferences;

public class updates {
    static Preferences usrprefs = Preferences.userNodeForPackage(Main.class);
    String host = usrprefs.get("host", null);
    String hostPort = usrprefs.get("hostport", null);
    String apiTokenID = usrprefs.get("apiTokenID", null);
    String apiSecret = usrprefs.get("apiSecret", null);
    String node = usrprefs.get("node", null);

    APIClient apiclient = new APIClient();
    String nodeName = usrprefs.get("node", null);
    String response = apiclient.readData("/api2/json/nodes/"+ nodeName +"/status");
    public void getUpdates(){
        String endpoint = "/api2/json/nodes/" + node + "/apt/update";
        String jsonPayload = "{\"upgrade\": false}";  // Example payload
        //String response = APIClient.postData(endpoint, jsonPayload);
        System.out.println(response); 
    }
}
