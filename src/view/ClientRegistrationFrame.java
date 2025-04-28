package view;

import model.Client;
import service.CartService;
import service.ClientService;

import javax.swing.*;
import java.awt.*;

public class ClientRegistrationFrame extends JFrame {

    private final ClientService clientService = new ClientService();
    private final CartService cartService = new CartService();

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> typeComboBox;
    private JButton registerButton;

    public ClientRegistrationFrame() {
        setTitle("Inscription Client");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(224, 255, 255); // bleu clair
                Color color2 = new Color(255, 239, 213); // pêche clair
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nom :"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email :"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe :"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Type :"));
        typeComboBox = new JComboBox<>(new String[]{"ancien", "nouveau", "client", "admin"});
        panel.add(typeComboBox);

        panel.add(new JLabel()); // vide pour alignement

        registerButton = new JButton("S'inscrire");
        registerButton.setBackground(new Color(144, 238, 144)); // Vert clair
        registerButton.setForeground(Color.BLACK);             // Texte noir
        registerButton.setFocusPainted(false);
        panel.add(registerButton);

        add(panel);

        registerButton.addActionListener(e -> registerClient());
    }

    private void registerClient() {
        String nom = nameField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword());
        String type = (String) typeComboBox.getSelectedItem();

        Client client = new Client();
        client.setNom(nom);
        client.setEmail(email);
        client.setMot_De_Passe(motDePasse);
        client.setType(type);

        boolean success = clientService.registerClient(client);
        if (!success) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de l'inscription. Vérifiez vos données.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client saved = clientService.getClientByEmail(email);
        if (saved == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Inscription réussie, mais impossible de récupérer votre compte.",
                    "Attention",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("admin".equalsIgnoreCase(saved.getType())) {
            AdminFrame adminFrame = new AdminFrame(saved);
            adminFrame.setVisible(true);
        } else {
            cartService.setClientId(saved.getId());
            JOptionPane.showMessageDialog(this, "Client inscrit avec succès !");
            ArticleCatalogFrame catalogFrame = new ArticleCatalogFrame(cartService);
            catalogFrame.setVisible(true);
        }

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientRegistrationFrame().setVisible(true));
    }
}
