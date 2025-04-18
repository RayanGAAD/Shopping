package view;

import model.Article;
import service.ArticleService;
import service.CartService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ArticleCatalogFrame extends JFrame {

    private ArticleService articleService = new ArticleService();
    private CartService cartService       = new CartService();  // instance partagée

    private JTable articleTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;

    // On garde en mémoire la liste affichée pour retrouver l'objet Article à partir de la ligne
    private List<Article> currentArticles;

    public ArticleCatalogFrame() {
        setTitle("Catalogue d'Articles");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // === Barre de recherche ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Recherche par nom :"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Rechercher");
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // === Tableau des articles ===
        String[] columnNames = {"ID", "Nom", "Description", "Prix Unitaire", "Prix Gros", "Quantité en stock", "Marque"};
        tableModel = new DefaultTableModel(columnNames, 0);
        articleTable = new JTable(tableModel);
        add(new JScrollPane(articleTable), BorderLayout.CENTER);

        // === Panel des boutons bas ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addToCartBtn = new JButton("Ajouter au panier");
        JButton viewCartBtn  = new JButton("Voir le panier");
        bottomPanel.add(addToCartBtn);
        bottomPanel.add(viewCartBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Actions ===
        searchButton.addActionListener((ActionEvent e) -> searchArticles());
        addToCartBtn.addActionListener((ActionEvent e) -> addSelectedToCart());
        viewCartBtn.addActionListener((ActionEvent e) -> {
            // On passe la même instance de cartService au CartFrame
            CartFrame cartFrame = new CartFrame(cartService);
            cartFrame.setVisible(true);
        });

        // Chargement initial
        loadAllArticles();
    }

    private void loadAllArticles() {
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
            JOptionPane.showMessageDialog(this, "Sélectionnez d'abord un article.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Article selected = currentArticles.get(row);
        String qtyStr = JOptionPane.showInputDialog(this, "Quantité pour \"" + selected.getNom() + "\" :", "1");
        if (qtyStr == null) return;  // Annulé

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();
            cartService.addToCart(selected, qty);
            JOptionPane.showMessageDialog(this, qty + " exemplaire(s) ajouté(s) au panier.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantité invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArticleCatalogFrame frame = new ArticleCatalogFrame();
            frame.setVisible(true);
        });
    }
}
