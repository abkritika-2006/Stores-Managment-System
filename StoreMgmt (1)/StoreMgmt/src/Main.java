import com.store.view.StoreManagementGUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StoreManagementGUI();
            }
        });
    }
}