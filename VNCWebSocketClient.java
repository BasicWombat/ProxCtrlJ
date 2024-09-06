import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class VNCWebSocketClient extends WebSocketClient {

    public VNCWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to VNC WebSocket");
    }

    @Override
    public void onMessage(String message) {
        // Handle incoming messages (VNC data)
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from VNC WebSocket");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
