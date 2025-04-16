import javax.swing.SwingUtilities;
import view.ClientRegistrationFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ClientRegistrationFrame frame = new ClientRegistrationFrame();
                frame.setVisible(true);
                System.out.println("Premier commit");
            }
        });
    }

}
