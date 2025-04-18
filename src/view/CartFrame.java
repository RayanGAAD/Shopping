package view;

import model.CartItem;
import service.CartService;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

// Import de la fenêtre de confirmation
import view.OrderConfirmationFrame;

/**
 * Fenêtre affichant le contenu du panier et permettant de passer à la confirmation de commande.
 */
public class CartFrame extends JFrame {
    private CartService cartService;
    private JTable table;
    private DefaultTableModel model;
    private JButton removeButton, checkoutButton;

    /**
     * Constructeur principal : on injecte le même CartService
     * que celui utilisé dans ArticleCatalogFrame.
     */
    public CartFrame(CartService cartService) {
        this.cartService = cartService;

        setTitle("Panier d'Achat");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Définition du modèle de table avec une colonne Quantité éditable
        String[] cols = {"ID", "Nom", "Prix Unitaire", "Quantité", "Total"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return col == 3; // seule la colonne Quantité est éditable
            }
        };
        table = new JTable(model);

        // Spinner pour éditer les quantités
        TableColumn qtyCol = table.getColumnModel().getColumn(3);
        qtyCol.setCellEditor(new SpinnerEditor());

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Boutons en bas
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        removeButton   = new JButton("Supprimer l'article");
        checkoutButton = new JButton("Valider la commande");
        bottom.add(removeButton);
        bottom.add(checkoutButton);
        add(bottom, BorderLayout.SOUTH);

        // Événements de mise à jour de quantité
        model.addTableModelListener(e -> {
            if (e.getColumn() == 3 && e.getFirstRow() >= 0) {
                int row = e.getFirstRow();
                int qty = (Integer) model.getValueAt(row, 3);
                CartItem ci = cartService.getCartItems().get(row);
                cartService.updateQuantity(ci.getArticle(), qty);
                model.setValueAt(ci.getTotalPrice(), row, 4);
            }
        });

        // Supprimer un article sélectionné
        removeButton.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                CartItem ci = cartService.getCartItems().get(r);
                cartService.removeFromCart(ci.getArticle());
                reloadTable();
            }
        });

        // Ouvrir la fenêtre de confirmation de commande
        checkoutButton.addActionListener(e -> {
            // Cacher la fenêtre du panier
            this.setVisible(false);
            // Ouvrir la confirmation en passant la référence
            OrderConfirmationFrame confFrame = new OrderConfirmationFrame(cartService, this);
            confFrame.setVisible(true);
        });

        // Chargement initial du panier
        reloadTable();
    }

    /**
     * Recharge le contenu du tableau depuis le panier.
     */
    public void reloadTable() {
        model.setRowCount(0);
        List<CartItem> items = cartService.getCartItems();
        for (CartItem ci : items) {
            Object[] row = {
                    ci.getArticle().getId(),
                    ci.getArticle().getNom(),
                    ci.getArticle().getPrixUnitaire(),
                    ci.getQuantity(),
                    ci.getTotalPrice()
            };
            model.addRow(row);
        }
    }

    /**
     * Méthode main de test : crée un CartService et l'affiche.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CartService sharedCart = new CartService();
            CartFrame frame = new CartFrame(sharedCart);
            frame.setVisible(true);
        });
    }
}

// Editor basé sur JSpinner pour la colonne quantité
class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
    private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    @Override public Object getCellEditorValue() { return spinner.getValue(); }
    @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }
}
