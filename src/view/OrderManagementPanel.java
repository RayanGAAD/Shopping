package view;

import model.Commande;
import model.Client;
import service.CommandeService;
import service.ClientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel d’administration des commandes : liste toutes les commandes et
 * affiche le nom du client, la date, le total, avec accès au détail.
 */
public class OrderManagementPanel extends JPanel {
    private final CommandeService commandeService;
    private final ClientService   clientService;
    private final JTable          table;
    private final DefaultTableModel tableModel;

    private static final String[] COLUMN_NAMES = {
            "ID", "Client", "Date", "Montant Total (€)"
    };

    /**
     * @param commandeService service pour récupérer/insérer des commandes
     * @param clientService   service pour récupérer les infos client
     */
    public OrderManagementPanel(CommandeService commandeService, ClientService clientService) {
        this.commandeService = commandeService;
        this.clientService   = clientService;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshBtn = new JButton("Rafraîchir");
        JButton detailBtn  = new JButton("Détails");
        buttonPanel.add(refreshBtn);
        buttonPanel.add(detailBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadOrders());
        detailBtn .addActionListener(e -> showDetails());

        loadOrders();
    }

    /** Recharge et affiche toutes les commandes (tous clients) */
    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Commande> commandes = commandeService.getAllOrders();
        for (Commande cmd : commandes) {
            Client c = clientService.findClientById(cmd.getIdClient());
            String clientName = (c != null) ? c.getNom() : "–";
            Object[] row = {
                    cmd.getId(),
                    clientName,
                    cmd.getDateCommande().toString(),
                    cmd.getMontantTotal()
            };
            tableModel.addRow(row);
        }
    }

    /** Ouvre le détail de la commande sélectionnée */
    private void showDetails() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Sélectionnez d'abord une commande.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int orderId = (Integer) tableModel.getValueAt(row, 0);
        OrderDetailFrame detailFrame = new OrderDetailFrame(commandeService, orderId);
        detailFrame.setVisible(true);
    }
}
