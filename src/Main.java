import javax.swing.SwingUtilities;
import view.LoginFrame;

/**
 * Point d'entrée principal de l'application.
 * Lance la fenêtre de connexion (LoginFrame).
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Ouvre directement l'écran de connexion
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
