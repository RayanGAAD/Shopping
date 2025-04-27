package view;

import model.Client;
import service.ArticleService;
import service.ClientService;
import service.CommandeService;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale de l'administration, accessible uniquement aux clients de type "admin".
 * Contient quatre onglets : gestion des articles, gestion des clients, consultation des commandes et reporting.
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

        // Barre de menu avec déconnexion
        setJMenuBar(createMenuBar());

        setLayout(new BorderLayout());
        initTabs();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuCompte = new JMenu("Compte");
        JMenuItem miDeconnexion = new JMenuItem("Se déconnecter");
        miDeconnexion.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        menuCompte.add(miDeconnexion);
        menuBar.add(menuCompte);
        return menuBar;
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
        tabs.addTab("Commandes",
                new OrderManagementPanel(commandeService, clientService)
        );

        // **Onglet 4 : Reporting / Statistiques**
        tabs.addTab("Reporting",
                new ReportingPanel(commandeService)
        );

        add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Client fakeAdmin = new Client();
        fakeAdmin.setNom("Admin Test");
        fakeAdmin.setType("admin");
        SwingUtilities.invokeLater(() -> new AdminFrame(fakeAdmin).setVisible(true));
    }
}
