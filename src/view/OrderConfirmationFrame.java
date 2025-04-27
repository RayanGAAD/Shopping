package view;

import model.CartItem;
import service.CartService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Fenêtre de confirmation de commande : récapitule le panier, simule le paiement,
 * valide la commande, puis rafraîchit le panier et le catalogue.
 */
public class OrderConfirmationFrame extends JFrame {
    private final CartService cartService;
    private final CartFrame parentFrame;
    private final ArticleCatalogFrame catalogFrame;
    private final JTable table;
    private final DefaultTableModel model;
    private final JLabel totalLabel;
    private final JButton confirmButton;

    /**
     * @param cartService   Service de panier partagé
     * @param parentFrame   Fenêtre CartFrame appelante
     * @param catalogFrame  Fenêtre ArticleCatalogFrame à rafraîchir
     */
    public OrderConfirmationFrame(CartService cartService,
                                  CartFrame parentFrame,
                                  ArticleCatalogFrame catalogFrame) {
        super("Confirmation de commande");
        this.cartService  = cartService;
        this.parentFrame  = parentFrame;
        this.catalogFrame = catalogFrame;

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Tableau récapitulatif
        String[] cols = {"Nom", "Prix Unitaire (€)", "Quantité", "Total (€)"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel du bas avec total et bouton de confirmation
        JPanel bottom = new JPanel(new BorderLayout(10,10));
        totalLabel    = new JLabel("Total : 0.00 €");
        confirmButton = new JButton("Confirmer la commande");
        bottom.add(totalLabel, BorderLayout.WEST);
        bottom.add(confirmButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        loadCartDetails();
        attachListeners();
    }

    private void attachListeners() {
        confirmButton.addActionListener(e -> {
            // 1) Simuler le paiement
            PaymentFrame payment = new PaymentFrame(this, () -> {
                // 2) Après 2s de paiement, enregistrer la commande
                boolean ok = cartService.checkout();
                if (ok) {
                    JOptionPane.showMessageDialog(
                            this,
                            "✅ Paiement et commande réussis !",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "❌ Erreur lors du traitement de la commande.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                // 3) Rafraîchir le panier et le catalogue
                parentFrame.reloadTable();
                catalogFrame.loadAllArticles();  // doit être public
                parentFrame.setVisible(true);
                dispose();
            });
            payment.setVisible(true);
        });
    }

    /**
     * Charge et affiche les détails du panier, en appliquant les tarifs de gros.
     * Met également à jour le total global.
     */
    private void loadCartDetails() {
        model.setRowCount(0);
        List<CartItem> items = cartService.getCartItems();
        for (CartItem ci : items) {
            double lineTotal = cartService.computeLinePrice(ci.getArticle(), ci.getQuantity());
            model.addRow(new Object[]{
                    ci.getArticle().getNom(),
                    ci.getArticle().getPrixUnitaire(),
                    ci.getQuantity(),
                    lineTotal
            });
        }
        double total = cartService.getCartTotal();
        totalLabel.setText(String.format("Total : %.2f €", total));
    }
}
