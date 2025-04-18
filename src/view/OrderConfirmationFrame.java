package view;

import model.CartItem;
import service.CartService;
import view.CartFrame;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.SwingUtilities;


/**
 * Fenêtre de confirmation de commande : récapitule le panier et valide la commande,
 * puis rend la main à la fenêtre du panier (CartFrame).
 */
public class OrderConfirmationFrame extends JFrame {
    private CartService cartService;
    private CartFrame parentFrame;   // référence à la fenêtre parente du panier
    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel;
    private JButton confirmButton;

    /**
     * @param cartService Service de panier partagé
     * @param parentFrame Fenêtre CartFrame appelante
     */
    public OrderConfirmationFrame(CartService cartService, CartFrame parentFrame) {
        this.cartService = cartService;
        this.parentFrame = parentFrame;

        setTitle("Confirmation de Commande");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Tableau des articles du panier
        String[] cols = {"Nom", "Prix Unitaire", "Quantité", "Total"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel bas avec total et bouton de confirmation
        JPanel bottom = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total : 0.00 €");
        confirmButton = new JButton("Confirmer la commande");
        bottom.add(totalLabel, BorderLayout.WEST);
        bottom.add(confirmButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        loadCartDetails();

        confirmButton.addActionListener(e -> {
            boolean ok = cartService.checkout();
            if (ok) {
                JOptionPane.showMessageDialog(this, "Commande enregistrée avec succès !");
                // Réafficher et recharger le panier
                parentFrame.reloadTable();
                parentFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'enregistrement de la commande.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                parentFrame.setVisible(true);
                dispose();
            }
        });
    }

    private void loadCartDetails() {
        model.setRowCount(0);
        List<CartItem> items = cartService.getCartItems();
        double total = 0;
        for (CartItem ci : items) {
            double lineTotal = ci.getArticle().getPrixUnitaire() * ci.getQuantity();
            Object[] row = {
                    ci.getArticle().getNom(),
                    ci.getArticle().getPrixUnitaire(),
                    ci.getQuantity(),
                    lineTotal
            };
            model.addRow(row);
            total += lineTotal;
        }
        totalLabel.setText(String.format("Total : %.2f €", total));
    }

    // main de test si besoin (passer un CartFrame fictif ou null)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CartService demoCart = new CartService();
            CartFrame fakeParent = null; // ou nouvel instance minimale
            OrderConfirmationFrame frame = new OrderConfirmationFrame(demoCart, fakeParent);
            frame.setVisible(true);
        });
    }
}
