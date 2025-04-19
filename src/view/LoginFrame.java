package view;

import service.ClientService;
import service.CartService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fenêtre de connexion pour authentifier le client.
 */
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private ClientService clientService = new ClientService();
    private CartService cartService = new CartService();

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
        JButton registerLink = new JButton("S'inscrire");
        panel.add(registerLink);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });

        registerLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClientRegistrationFrame().setVisible(true);
                dispose();
            }
        });
    }

    private void authenticate() {
        String email = emailField.getText().trim();
        String pwd = new String(passwordField.getPassword());
        Integer clientId = clientService.login(email, pwd);
        if (clientId != null) {
            // On fixe l'ID du client dans le CartService
            cartService.setClientId(clientId);

            // On passe désormais cartService au constructeur du catalogue
            ArticleCatalogFrame catalogFrame = new ArticleCatalogFrame(cartService);
            catalogFrame.setVisible(true);
            dispose();
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
