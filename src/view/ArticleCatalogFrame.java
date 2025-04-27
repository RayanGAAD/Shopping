package view;

import model.Article;
import service.ArticleService;
import service.CartService;
import service.ClientService;
import service.CommandeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Interface catalogue des articles avec ajout au panier, recherche, historique
 * et gestion de session (affichage du nom + déconnexion).
 */
public class ArticleCatalogFrame extends JFrame {

    private final ArticleService   articleService;
    private final CartService      cartService;
    private final CommandeService  commandeService;
    private final ClientService    clientService;

    private JTable            articleTable;
    private DefaultTableModel tableModel;
    private JTextField        searchField;
    private JButton           searchButton;

    // Liste courante d'articles affichée
    private List<Article> currentArticles;

    public ArticleCatalogFrame(CartService cartService) {
        this.cartService     = cartService;
        this.articleService  = new ArticleService();
        this.commandeService = new CommandeService();
        this.clientService   = new ClientService();

        setTitle("Catalogue d'Articles");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // ==== Bandeau d'accueil + déconnexion ====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // 1) Ligne de bienvenue + bouton logout
        JPanel sessionPanel = new JPanel(new BorderLayout());
        // Récupère le client courant via son ID
        var client = clientService.findClientById(cartService.getClientId());
        String name = client != null ? client.getNom() : "Invité";
        JLabel welcomeLabel = new JLabel("Bienvenue, " + name);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        sessionPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Se déconnecter");
        logoutBtn.addActionListener((ActionEvent e) -> {
            // Retour à la connexion
            new LoginFrame().setVisible(true);
            dispose();
        });
        sessionPanel.add(logoutBtn, BorderLayout.EAST);

        // 2) Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Recherche par nom :"));
        searchField  = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Rechercher");
        searchPanel.add(searchButton);

        topPanel.add(sessionPanel);
        topPanel.add(searchPanel);
        add(topPanel, BorderLayout.NORTH);


        // ==== Tableau des articles ====
        String[] columnNames = {
                "ID", "Nom", "Description",
                "Prix Unitaire", "Prix Gros",
                "Quantité en stock", "Marque"
        };
        tableModel   = new DefaultTableModel(columnNames, 0);
        articleTable = new JTable(tableModel);
        add(new JScrollPane(articleTable), BorderLayout.CENTER);

        // ==== Boutons en bas ====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addToCartBtn = new JButton("Ajouter au panier");
        JButton viewCartBtn  = new JButton("Voir le panier");
        JButton historyBtn   = new JButton("Historique");
        bottomPanel.add(addToCartBtn);
        bottomPanel.add(viewCartBtn);
        bottomPanel.add(historyBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // ==== Hooks ====
        searchButton.addActionListener(e -> searchArticles());
        addToCartBtn.addActionListener(e -> addSelectedToCart());
        viewCartBtn.addActionListener(e -> {
            CartFrame cartFrame = new CartFrame(cartService, this);
            cartFrame.setVisible(true);
        });
        historyBtn.addActionListener(e -> {
            OrderHistoryFrame histo = new OrderHistoryFrame(commandeService, cartService.getClientId());
            histo.setVisible(true);
        });

        // Chargement initial
        loadAllArticles();
    }

    public void loadAllArticles() {
        currentArticles = articleService.getAllArticles();
        populateTable(currentArticles);
    }

    private void populateTable(List<Article> articles) {
        tableModel.setRowCount(0);
        for (Article art : articles) {
            Object[] row = {
                    art.getId(),
                    art.getNom(),
                    art.getDescription(),
                    art.getPrixUnitaire(),
                    art.getPrixGros(),
                    art.getQuantiteEnStock(),
                    art.getMarque()
            };
            tableModel.addRow(row);
        }
    }

    private void searchArticles() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadAllArticles();
        } else {
            currentArticles = articleService.searchArticlesByName(keyword);
            populateTable(currentArticles);
        }
    }

    private void addSelectedToCart() {
        int row = articleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionnez d'abord un article.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Article selected = currentArticles.get(row);
        String qtyStr = JOptionPane.showInputDialog(
                this,
                "Quantité pour \"" + selected.getNom() + "\" :",
                "1"
        );
        if (qtyStr == null) return;  // Annulé

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();

            boolean added = cartService.addToCart(selected, qty);
            if (added) {
                JOptionPane.showMessageDialog(
                        this,
                        qty + " exemplaire(s) ajouté(s) au panier."
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Stock insuffisant pour \"" + selected.getNom() + "\".",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Quantité invalide.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
