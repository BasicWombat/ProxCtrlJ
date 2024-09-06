import javax.swing.*;
import java.awt.*;

public class vncConsole extends JPanel {
    private JEditorPane webView;

    public vncConsole(String vncWebSocketUrl) {
        setLayout(new BorderLayout());
        webView = new JEditorPane();
        webView.setContentType("text/html");
        webView.setText("<iframe src=\"" + vncWebSocketUrl + "\" width=\"800\" height=\"600\"></iframe>");
        add(new JScrollPane(webView), BorderLayout.CENTER);
    }
}