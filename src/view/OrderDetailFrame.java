package view;

import model.LigneCommande;
import model.Article;
import service.CommandeService;
import service.ArticleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderDetailFrame extends JFrame {
    private final CommandeService commandeService;
    private final ArticleService articleService = new ArticleService();
    private final int commandeId;
    private JTable table;
    private DefaultTableModel model;
    private JButton closeButton;

    public OrderDetailFrame(CommandeService commandeService, int commandeId) {
        this.commandeService = commandeService;
        this.commandeId      = commandeId;
        setTitle("Détail de la commande n°" + commandeId);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Colonnes : Article, Quantité, Prix Unitaire, Total ligne
        String[] cols = {"Article", "Quantité", "Prix Unitaire (€)", "Total (€)"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bouton Fermer
        closeButton = new JButton("Fermer");
        closeButton.addActionListener(e -> dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(closeButton);
        add(bottom, BorderLayout.SOUTH);

        loadDetails();
    }

    private void loadDetails() {
        model.setRowCount(0);
        List<LigneCommande> lines = commandeService.getOrderDetails(commandeId);
        for (LigneCommande lc : lines) {
            // Récupérer le nom de l'article
            Article art = articleService.getArticleById(lc.getIdArticle());
            String name = (art != null ? art.getNom() : "ID " + lc.getIdArticle());

            int qty = lc.getQuantite();
            double unitPrice = lc.getPrixUnitaire();
            double totalLine = unitPrice * qty;

            Object[] row = { name, qty, unitPrice, totalLine };
            model.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CommandeService svc = new CommandeService();
            // Testez avec un ID de commande existant
            OrderDetailFrame f = new OrderDetailFrame(svc, 1);
            f.setVisible(true);
        });
    }
}
