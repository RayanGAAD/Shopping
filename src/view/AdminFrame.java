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
    private final ArticleService articleService = new ArticleService();
    private final ClientService clientService = new ClientService();
    private final CommandeService commandeService = new CommandeService();

    public AdminFrame(Client admin) {
        this.admin = admin;
        setTitle("Administration – " + admin.getNom());
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Barre de menu avec déconnexion
        setJMenuBar(createMenuBar());

        // Dégradé de fond léger
        setContentPane(createGradientPanel());

        setLayout(new BorderLayout());
        initTabs();
    }

    private JPanel createGradientPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(224, 255, 255); // Bleu clair
                Color color2 = new Color(255, 239, 213); // Pêche clair
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuCompte = new JMenu("Compte");
        menuCompte.setFont(new Font("Arial", Font.BOLD, 14));

        JMenuItem miDeconnexion = new JMenuItem("Se déconnecter");
        miDeconnexion.setBackground(new Color(255, 150, 150)); // Rouge clair
        miDeconnexion.setForeground(Color.BLACK);
        miDeconnexion.setFocusPainted(false);
        miDeconnexion.setFont(new Font("Arial", Font.BOLD, 13));

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
        tabs.setFont(new Font("Arial", Font.PLAIN, 14));

        tabs.addTab("Articles",
                new ArticleManagementPanel(articleService)
        );
        tabs.addTab("Clients",
                new ClientManagementPanel(clientService)
        );
        tabs.addTab("Commandes",
                new OrderManagementPanel(commandeService, clientService)
        );
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
