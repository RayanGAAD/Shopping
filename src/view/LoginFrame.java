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
    private final CartService cartService = new CartService();

    public LoginFrame() {
        setTitle("Connexion Client");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dégradé doux
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(224, 255, 255); // bleu très clair
                Color color2 = new Color(255, 239, 213); // pêche clair
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Email :"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe :"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel()); // Vide pour l'alignement

        loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(173, 216, 230)); // Bleu clair
        loginButton.setForeground(Color.BLACK);             // Texte noir
        loginButton.setFocusPainted(false);
        panel.add(loginButton);

        panel.add(new JLabel()); // Vide pour l'alignement

        registerLink = new JButton("S'inscrire");
        registerLink.setBackground(new Color(144, 238, 144)); // Vert clair
        registerLink.setForeground(Color.BLACK);              // Texte noir
        registerLink.setFocusPainted(false);
        panel.add(registerLink);

        add(panel);

        // Action bouton Se connecter
        loginButton.addActionListener(e -> authenticate());

        // Action bouton S'inscrire
        registerLink.addActionListener(e -> {
            new ClientRegistrationFrame().setVisible(true);
            dispose();
        });
    }

    private void authenticate() {
        String email = emailField.getText().trim();
        String pwd = String.valueOf(passwordField.getPassword());

        Integer clientId = clientService.login(email, pwd);
        if (clientId != null) {
            Client current = clientService.getCurrentClient();

            if ("admin".equalsIgnoreCase(current.getType())) {
                SwingUtilities.invokeLater(() -> {
                    new AdminFrame(current).setVisible(true);
                });
            } else {
                cartService.setClientId(clientId);
                SwingUtilities.invokeLater(() -> {
                    new ArticleCatalogFrame(cartService).setVisible(true);
                });
            }

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
