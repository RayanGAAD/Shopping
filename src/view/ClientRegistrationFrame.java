package view;

import model.Client;
import service.CartService;
import service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientRegistrationFrame extends JFrame {

    private ClientService clientService = new ClientService();
    private CartService   cartService   = new CartService();

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> typeComboBox;
    private JButton registerButton;

    public ClientRegistrationFrame() {
        setTitle("Inscription Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        panel.add(new JLabel());
        registerButton = new JButton("S'inscrire");
        panel.add(registerButton);

        add(panel);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerClient();
            }
        });
    }

    private void registerClient() {
        String nom        = nameField.getText().trim();
        String email      = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword());
        String type       = (String) typeComboBox.getSelectedItem();

        Client client = new Client();
        client.setNom(nom);
        client.setEmail(email);
        client.setMot_De_Passe(motDePasse);
        client.setType(type);

        // 1) On enregistre
        boolean success = clientService.registerClient(client);
        if (!success) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de l'inscription. Vérifiez vos données.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2) On récupère l'objet Client fraîchement créé
        Client saved = clientService.getClientByEmail(email);
        if (saved == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Inscription réussie, mais impossible de récupérer votre compte.",
                    "Attention",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3) Redirection selon le type
        if ("admin".equalsIgnoreCase(saved.getType())) {
            // Pour un admin, on ouvre l'interface d'administration
            AdminFrame adminFrame = new AdminFrame(saved);
            adminFrame.setVisible(true);
        } else {
            // Pour un client classique, on initialise le panier et on ouvre le catalogue
            cartService.setClientId(saved.getId());
            JOptionPane.showMessageDialog(this, "Client inscrit avec succès !");
            ArticleCatalogFrame catalogFrame = new ArticleCatalogFrame(cartService);
            catalogFrame.setVisible(true);
        }

        dispose();  // ferme la fenêtre d'inscription
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientRegistrationFrame().setVisible(true));
    }
}
