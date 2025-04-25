package view;

import model.Client;
import service.ArticleService;
import service.ClientService;
import service.CommandeService;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale de l'administration, accessible uniquement aux clients de type "admin".
 * Contient trois onglets : gestion des articles, gestion des clients et gestion des commandes.
 */
public class AdminFrame extends JFrame {
    private final Client admin;
    private final ArticleService  articleService   = new ArticleService();
    private final ClientService   clientService    = new ClientService();
    private final CommandeService commandeService  = new CommandeService();

    public AdminFrame(Client admin) {
        this.admin = admin;
        setTitle("Administration – " + admin.getNom());
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initTabs();
    }

    private void initTabs() {
        JTabbedPane tabs = new JTabbedPane();

        // Onglet 1 : CRUD Articles
        tabs.addTab("Articles",
                new ArticleManagementPanel(articleService)
        );

        // Onglet 2 : CRUD Clients
        tabs.addTab("Clients",
                new ClientManagementPanel(clientService)
        );

        // Onglet 3 : Consultation des commandes
        // On passe maintenant les 2 services : CommandeService et ArticleService
        tabs.addTab("Commandes",
                new OrderManagementPanel(commandeService, clientService)
        );

        add(tabs, BorderLayout.CENTER);
    }

    // Point d’entrée de test si besoin
    public static void main(String[] args) {
        // Exemple : création d’un Client factice de type admin
        Client fakeAdmin = new Client();
        fakeAdmin.setNom("Admin Test");
        fakeAdmin.setType("admin");
        SwingUtilities.invokeLater(() -> {
            new AdminFrame(fakeAdmin).setVisible(true);
        });
    }
}
