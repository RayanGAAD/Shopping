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
        this.commandeId = commandeId;
        setTitle("Détail de la commande n°" + commandeId);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(createGradientPanel());
        setLayout(new BorderLayout(10, 10));

        initUI();
    }

    private JPanel createGradientPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(224, 255, 255); // Bleu très clair
                Color color2 = new Color(255, 239, 213); // Pêche
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private void initUI() {
        // ===== Tableau
        String[] cols = {"Article", "Quantité", "Prix Unitaire (€)", "Total (€)"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Bouton Fermer
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);

        closeButton = new JButton("Fermer");
        closeButton.setBackground(new Color(255, 182, 193)); // Rose clair
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> dispose());

        bottom.add(closeButton);
        add(bottom, BorderLayout.SOUTH);

        loadDetails();
    }

    private void loadDetails() {
        model.setRowCount(0);
        List<LigneCommande> lines = commandeService.getOrderDetails(commandeId);
        for (LigneCommande lc : lines) {
            Article art = articleService.getArticleById(lc.getIdArticle());
            String name = (art != null) ? art.getNom() : "ID " + lc.getIdArticle();

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
            OrderDetailFrame f = new OrderDetailFrame(svc, 1);
            f.setVisible(true);
        });
    }
}
