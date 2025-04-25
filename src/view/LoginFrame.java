package view;

import model.Client;
import service.ClientService;
import service.CartService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerLink;

    private final ClientService clientService = new ClientService();
    private final CartService   cartService   = new CartService();

    public LoginFrame() {
        setTitle("Connexion Client");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Email :"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe :"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel()); // vide
        loginButton = new JButton("Se connecter");
        panel.add(loginButton);

        panel.add(new JLabel()); // vide
        registerLink = new JButton("S'inscrire");
        panel.add(registerLink);

        add(panel);

        // Action du bouton Se connecter
        loginButton.addActionListener(e -> authenticate());

        // Lien vers l'inscription
        registerLink.addActionListener(e -> {
            new ClientRegistrationFrame().setVisible(true);
            dispose();
        });
    }

    private void authenticate() {
        String email = emailField.getText().trim();
        String pwd   = String.valueOf(passwordField.getPassword());

        Integer clientId = clientService.login(email, pwd);
        if (clientId != null) {
            Client current = clientService.getCurrentClient();

            if ("admin".equalsIgnoreCase(current.getType())) {
                // Si admin, affiche l'interface d'administration
                SwingUtilities.invokeLater(() -> {
                    new AdminFrame(current).setVisible(true);
                });
            } else {
                // Sinon, prépare le panier et affiche le catalogue client
                cartService.setClientId(clientId);
                SwingUtilities.invokeLater(() -> {
                    new ArticleCatalogFrame(cartService).setVisible(true);
                });
            }

            dispose();  // ferme la fenêtre de login
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Email ou mot de passe incorrect.",
                    "Erreur d'authentification",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
