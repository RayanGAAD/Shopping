package view;

import model.Article;
import service.ArticleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Panel de gestion CRUD des articles pour l'interface admin.
 */
public class ArticleManagementPanel extends JPanel {
    private ArticleService articleService;
    private JTable table;
    private DefaultTableModel tableModel;

    // Colonnes à afficher dans le JTable
    private static final String[] COLUMN_NAMES = {
            "ID", "Nom", "Description", "Marque",
            "Prix Unitaire", "Prix Gros", "Quantité en Stock"
    };

    public ArticleManagementPanel(ArticleService articleService) {
        this.articleService = articleService;
        setLayout(new BorderLayout());
        initComponents();
        loadArticles();
    }

    private void initComponents() {
        // Modèle de table non éditable directement
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Scroll pane pour la table
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel de boutons en bas
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn    = new JButton("Ajouter");
        JButton editBtn   = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions des boutons
        addBtn.addActionListener(e -> showArticleDialog(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un article à modifier.",
                        "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Article art = getArticleFromRow(row);
            showArticleDialog(art);
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un article à supprimer.",
                        "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Article art = getArticleFromRow(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Supprimer l'article \"" + art.getNom() + "\" ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (articleService.deleteArticle(art.getId())) {
                    loadArticles();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Échec de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Charge la liste des articles depuis la base et rafraîchit la table.
     */
    private void loadArticles() {
        tableModel.setRowCount(0);
        List<Article> articles = articleService.getAllArticles();
        for (Article art : articles) {
            Object[] row = {
                    art.getId(),
                    art.getNom(),
                    art.getDescription(),
                    art.getMarque(),
                    art.getPrixUnitaire(),
                    art.getPrixGros(),
                    art.getQuantiteEnStock()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Lit un Article à partir d'une ligne sélectionnée du JTable.
     */
    private Article getArticleFromRow(int row) {
        Article art = new Article();
        art.setId((Integer)        tableModel.getValueAt(row, 0));
        art.setNom((String)        tableModel.getValueAt(row, 1));
        art.setDescription((String)tableModel.getValueAt(row, 2));
        art.setMarque((String)     tableModel.getValueAt(row, 3));
        art.setPrixUnitaire((Double) tableModel.getValueAt(row, 4));
        art.setPrixGros((Double)    tableModel.getValueAt(row, 5));
        art.setQuantiteEnStock((Integer) tableModel.getValueAt(row, 6));
        return art;
    }

    /**
     * Affiche un JDialog pour ajouter ou modifier un article.
     * @param existingArticle null pour ajout, non-null pour modification
     */
    private void showArticleDialog(Article existingArticle) {
        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                existingArticle == null ? "Ajouter un article" : "Modifier l'article",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));

        // Champs du formulaire
        JTextField nomField        = new JTextField();
        JTextField descField       = new JTextField();
        JTextField marqueField     = new JTextField();
        JTextField prixUnitField   = new JTextField();
        JTextField prixGrosField   = new JTextField();
        JTextField quantField      = new JTextField();

        if (existingArticle != null) {
            nomField.setText(existingArticle.getNom());
            descField.setText(existingArticle.getDescription());
            marqueField.setText(existingArticle.getMarque());
            prixUnitField.setText(String.valueOf(existingArticle.getPrixUnitaire()));
            prixGrosField.setText(String.valueOf(existingArticle.getPrixGros()));
            quantField.setText(String.valueOf(existingArticle.getQuantiteEnStock()));
        }

        // Ajout des labels et champs
        dialog.add(new JLabel("Nom :"));        dialog.add(nomField);
        dialog.add(new JLabel("Description :"));dialog.add(descField);
        dialog.add(new JLabel("Marque :"));     dialog.add(marqueField);
        dialog.add(new JLabel("Prix Unitaire :"));dialog.add(prixUnitField);
        dialog.add(new JLabel("Prix Gros :"));  dialog.add(prixGrosField);
        dialog.add(new JLabel("Quantité en stock :"));dialog.add(quantField);

        JButton saveBtn   = new JButton("Enregistrer");
        JButton cancelBtn = new JButton("Annuler");

        dialog.add(cancelBtn);
        dialog.add(saveBtn);

        // Action Annuler
        cancelBtn.addActionListener(e -> dialog.dispose());

        // Action Enregistrer
        saveBtn.addActionListener(e -> {
            try {
                String nom  = nomField.getText().trim();
                String desc = descField.getText().trim();
                String marque = marqueField.getText().trim();
                double prixUnit = Double.parseDouble(prixUnitField.getText().trim());
                double prixGros = Double.parseDouble(prixGrosField.getText().trim());
                int quant = Integer.parseInt(quantField.getText().trim());

                if (existingArticle == null) {
                    // Ajout
                    Article newArt = new Article();
                    newArt.setNom(nom);
                    newArt.setDescription(desc);
                    newArt.setMarque(marque);
                    newArt.setPrixUnitaire(prixUnit);
                    newArt.setPrixGros(prixGros);
                    newArt.setQuantiteEnStock(quant);
                    if (!articleService.addArticle(newArt)) {
                        throw new RuntimeException("Échec de la création");
                    }
                } else {
                    // Modification
                    existingArticle.setNom(nom);
                    existingArticle.setDescription(desc);
                    existingArticle.setMarque(marque);
                    existingArticle.setPrixUnitaire(prixUnit);
                    existingArticle.setPrixGros(prixGros);
                    existingArticle.setQuantiteEnStock(quant);
                    if (!articleService.updateArticle(existingArticle)) {
                        throw new RuntimeException("Échec de la mise à jour");
                    }
                }

                loadArticles();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Données invalides ou opération échouée : " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
}
