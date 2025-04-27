package view;

import model.CartItem;
import service.CartService;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

/**
 * Fenêtre affichant le contenu du panier et permettant de passer à la confirmation de commande.
 */
public class CartFrame extends JFrame {
    private final CartService cartService;
    private final ArticleCatalogFrame catalogFrame;
    private final JTable table;
    private final DefaultTableModel model;
    private final JButton removeButton;
    private final JButton checkoutButton;

    /**
     * Constructeur principal : on injecte le même CartService
     * et la référence à ArticleCatalogFrame pour rafraîchir le catalogue après achat.
     */
    public CartFrame(CartService cartService, ArticleCatalogFrame catalogFrame) {
        super("Panier d'Achat");
        this.cartService  = cartService;
        this.catalogFrame = catalogFrame;

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Création du modèle de table
        String[] columns = {"ID", "Nom", "Prix Unitaire (€)", "Quantité", "Total (€)"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                // Seule la colonne Quantité (index 3) est éditable
                return col == 3;
            }
        };
        table = new JTable(model);

        // Éditeur à base de JSpinner pour la colonne Quantité
        TableColumn qtyCol = table.getColumnModel().getColumn(3);
        qtyCol.setCellEditor(new SpinnerEditor());

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel des boutons en bas
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        removeButton   = new JButton("Supprimer l'article");
        checkoutButton = new JButton("Valider la commande");
        bottom.add(removeButton);
        bottom.add(checkoutButton);
        add(bottom, BorderLayout.SOUTH);

        // Listener sur modification de la quantité directement dans la JTable
        model.addTableModelListener(e -> {
            if (e.getColumn() == 3 && e.getFirstRow() >= 0) {
                int row = e.getFirstRow();
                int qty = (Integer) model.getValueAt(row, 3);
                CartItem ci = cartService.getCartItems().get(row);
                // Met à jour la quantité dans le service
                cartService.updateQuantity(ci.getArticle(), qty);
                // Recalcule et affiche le total de la ligne avec le prix en gros
                double lineTotal = cartService.computeLinePrice(ci.getArticle(), qty);
                model.setValueAt(lineTotal, row, 4);
            }
        });

        // Action du bouton Supprimer
        removeButton.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                CartItem ci = cartService.getCartItems().get(r);
                cartService.removeFromCart(ci.getArticle());
                reloadTable();
            }
        });

        // Action du bouton Valider la commande
        checkoutButton.addActionListener(e -> {
            this.setVisible(false);
            // Ouvre la fenêtre de confirmation (avec simulation de paiement)
            OrderConfirmationFrame conf = new OrderConfirmationFrame(cartService, this, catalogFrame);
            conf.setVisible(true);
        });

        // Chargement initial du panier
        reloadTable();
    }

    /**
     * Recharge le contenu du tableau depuis le panier.
     * Utilise computeLinePrice(...) pour appliquer le tarif gros.
     */
    public void reloadTable() {
        model.setRowCount(0);
        List<CartItem> items = cartService.getCartItems();
        for (CartItem ci : items) {
            double lineTotal = cartService.computeLinePrice(ci.getArticle(), ci.getQuantity());
            model.addRow(new Object[]{
                    ci.getArticle().getId(),
                    ci.getArticle().getNom(),
                    ci.getArticle().getPrixUnitaire(),
                    ci.getQuantity(),
                    lineTotal
            });
        }
    }

    /**
     * Main de test (catalogFrame peut être null pour un test rapide).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CartService demoCart = new CartService();
            CartFrame frame = new CartFrame(demoCart, null);
            frame.setVisible(true);
        });
    }
}

/**
 * Éditeur de cellule pour la colonne Quantité, basé sur JSpinner.
 */
class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
    private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        spinner.setValue((Integer) value);
        return spinner;
    }
}
