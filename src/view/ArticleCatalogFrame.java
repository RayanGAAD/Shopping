package view;

import model.Article;
import service.ArticleService;
import service.CartService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Import des autres vues
import view.CartFrame;

/**
 * Interface catalogue des articles avec ajout au panier et recherche.
 */
public class ArticleCatalogFrame extends JFrame {

    private ArticleService articleService = new ArticleService();
    private CartService cartService       = new CartService();  // instance partagée

    private JTable articleTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;

    // Liste courante d'articles affichée
    private List<Article> currentArticles;

    public ArticleCatalogFrame() {
        setTitle("Catalogue d'Articles");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Exemple : définir un clientId avant toute action (à adapter après login)
        cartService.setClientId(1);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Recherche par nom :"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Rechercher");
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Tableau des articles
        String[] columnNames = {"ID", "Nom", "Description", "Prix Unitaire", "Prix Gros", "Quantité en stock", "Marque"};
        tableModel = new DefaultTableModel(columnNames, 0);
        articleTable = new JTable(tableModel);
        add(new JScrollPane(articleTable), BorderLayout.CENTER);

        // Boutons bas
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addToCartBtn = new JButton("Ajouter au panier");
        JButton viewCartBtn  = new JButton("Voir le panier");
        bottomPanel.add(addToCartBtn);
        bottomPanel.add(viewCartBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        searchButton.addActionListener((ActionEvent e) -> searchArticles());
        addToCartBtn.addActionListener((ActionEvent e) -> addSelectedToCart());
        viewCartBtn.addActionListener((ActionEvent e) -> {
            // On passe la même instance de cartService et la référence du catalogue
            CartFrame cartFrame = new CartFrame(cartService, this);
            cartFrame.setVisible(true);
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
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Article selected = currentArticles.get(row);
        String qtyStr = JOptionPane.showInputDialog(
                this,
                "Quantité pour \"" + selected.getNom() + "\" :",
                "1");
        if (qtyStr == null) return;  // Annulé

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();

            boolean added = cartService.addToCart(selected, qty);
            if (added) {
                JOptionPane.showMessageDialog(
                        this,
                        qty + " exemplaire(s) ajouté(s) au panier.");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Stock insuffisant pour \"" + selected.getNom() + "\".",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Quantité invalide.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArticleCatalogFrame frame = new ArticleCatalogFrame();
            frame.setVisible(true);
        });
    }
}
