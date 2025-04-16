package view;

import model.Article;
import service.ArticleService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ArticleCatalogFrame extends JFrame {

    private ArticleService articleService = new ArticleService(); // Service pour interroger les articles
    private JTable articleTable;
    private DefaultTableModel tableModel;

    // Composants de recherche
    private JTextField searchField;
    private JButton searchButton;

    public ArticleCatalogFrame() {
        setTitle("Catalogue d'Articles");
        setSize(800, 600); // Taille ajustée pour inclure la barre de recherche
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre
        initUI();
    }

    private void initUI() {
        // Utilisation d'un BorderLayout pour organiser la barre de recherche et le tableau
        setLayout(new BorderLayout());

        // Création d'un panel pour la recherche en haut
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Recherche par nom :"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Rechercher");
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Définir les colonnes du tableau
        String[] columnNames = {"ID", "Nom", "Description", "Prix Unitaire", "Prix Gros", "Quantité", "Marque"};
        tableModel = new DefaultTableModel(columnNames, 0);
        articleTable = new JTable(tableModel);

        // Placer le tableau dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(articleTable);
        add(scrollPane, BorderLayout.CENTER);

        // Charger tous les articles au démarrage
        loadAllArticles();

        // Ajout d'un ActionListener pour déclencher la recherche lorsqu'on clique sur le bouton
        searchButton.addActionListener((ActionEvent e) -> searchArticles());
    }

    /**
     * Charge l'ensemble des articles et les affiche dans la table.
     */
    private void loadAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        populateTable(articles);
    }

    /**
     * Met à jour le contenu du tableau avec la liste d'articles donnée.
     * @param articles Liste d'articles à afficher.
     */
    private void populateTable(List<Article> articles) {
        tableModel.setRowCount(0); // Efface le tableau
        for (Article article : articles) {
            Object[] rowData = {
                    article.getId(),
                    article.getNom(),
                    article.getDescription(),
                    article.getPrixUnitaire(),
                    article.getPrixGros(),
                    article.getQuantiteEnStock(),
                    article.getMarque()  // Assurez-vous que getMarque() renvoie une String
            };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Recherche les articles en fonction du nom saisi et met à jour le tableau.
     */
    private void searchArticles() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadAllArticles();
        } else {
            // On suppose que dans ArticleService existe une méthode searchArticlesByName(String)
            List<Article> filteredArticles = articleService.searchArticlesByName(keyword);
            populateTable(filteredArticles);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArticleCatalogFrame frame = new ArticleCatalogFrame();
            frame.setVisible(true);
        });
    }
}
