import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import view.WelcomeFrame;

/**
 * Point d'entrée principal de l'application.
 * Initialise le look & feel puis lance la fenêtre de connexion.
 */
public class Main {
    public static void main(String[] args) {
        // 1) Tenter d'appliquer le look & feel natif
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException
                 | InstantiationException
                 | IllegalAccessException
                 | UnsupportedLookAndFeelException ex) {
            // Si échec, on continue quand même avec le L&F par défaut
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomeFrame().setVisible(true);}
        });
    }
}