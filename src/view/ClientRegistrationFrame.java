package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Client;
import service.ClientService;


public class ClientRegistrationFrame extends JFrame {

    // Instance du service pour accéder à la couche métier
    private ClientService clientService = new ClientService();

    // Composants de l'interface
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> typeComboBox;
    private JButton registerButton;

    // Constructeur
    public ClientRegistrationFrame() {
        setTitle("Inscription Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
        initUI();
    }

    // Initialisation de l'interface utilisateur
    private void initUI() {
        // Création d'un panel avec un layout en grille pour disposer les composants
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Création des labels et champs
        JLabel nameLabel = new JLabel("Nom :");
        nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email :");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordField = new JPasswordField();

        JLabel typeLabel = new JLabel("Type :");
        // Valeurs autorisées par la BDD : ancien, nouveau, client, admin
        String[] types = {"ancien", "nouveau", "client", "admin"};
        typeComboBox = new JComboBox<>(types);

        registerButton = new JButton("S'inscrire");

        // Ajout des composants dans le panel
        panel.add(nameLabel);
        panel.add(nameField);

        panel.add(emailLabel);
        panel.add(emailField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(typeLabel);
        panel.add(typeComboBox);

        // Ajoute un espace vide pour l'alignement et le bouton dans la deuxième case de la dernière ligne
        panel.add(new JLabel());
        panel.add(registerButton);

        // Ajout du panel au frame
        add(panel);

        // Ajout d'un ActionListener sur le bouton d'inscription
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerClient();
            }
        });
    }

    // Méthode pour récupérer les informations entrées et enregistrer le client via le service
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

        // Appel au service pour enregistrer le client
        boolean success = clientService.registerClient(client);
        if (success) {
            JOptionPane.showMessageDialog(this, "Client inscrit avec succès !");
            // Lancer le catalogue d'articles après une inscription réussie
            ArticleCatalogFrame catalogFrame = new ArticleCatalogFrame();
            catalogFrame.setVisible(true);
            // Fermer la fenêtre d'inscription
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription. Vérifiez vos données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode main pour lancer l'application
    public static void main(String[] args) {
        // Utilisation de SwingUtilities.invokeLater pour lancer l'interface dans le thread d'interface
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientRegistrationFrame frame = new ClientRegistrationFrame();
                frame.setVisible(true);
            }
        });
    }
}
