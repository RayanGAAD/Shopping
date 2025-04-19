package view;

import model.Commande;
import model.Client;
import service.CommandeService;
import service.ClientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderHistoryFrame extends JFrame {
    private CommandeService commandeService;
    private ClientService clientService = new ClientService();  // pour récupérer les infos du client
    private int clientId;
    private JTable table;
    private DefaultTableModel model;
    private JButton detailButton, refreshButton;

    public OrderHistoryFrame(CommandeService commandeService, int clientId) {
        this.commandeService = commandeService;
        this.clientId = clientId;

        // Récupérer le client pour afficher son nom
        Client client = clientService.findClientById(clientId);

        setTitle("Historique de " + client.getNom());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI(client);
    }

    private void initUI(Client client) {
        setLayout(new BorderLayout());

        // ===== Header avec le nom du client
        JLabel header = new JLabel(
                "Historique des commandes de " + client.getNom(),
                SwingConstants.CENTER
        );
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // ===== Tableau des commandes
        String[] cols = {"N° commande", "Date", "Montant (€)"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== Boutons en bas
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Rafraîchir");
        detailButton  = new JButton("Voir détails");
        bottom.add(refreshButton);
        bottom.add(detailButton);
        add(bottom, BorderLayout.SOUTH);

        // ===== Actions
        refreshButton.addActionListener(e -> loadOrders());
        detailButton.addActionListener(e -> openDetail());

        loadOrders();
    }

    private void loadOrders() {
        model.setRowCount(0);
        List<Commande> orders = commandeService.getOrderHistory(clientId);
        for (Commande o : orders) {
            Object[] row = {
                    o.getId(),
                    o.getDateCommande(),
                    o.getMontantTotal()
            };
            model.addRow(row);
        }
    }

    private void openDetail() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionnez d'abord une commande.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        int cmdId = (Integer) model.getValueAt(row, 0);
        OrderDetailFrame detail = new OrderDetailFrame(commandeService, cmdId);
        detail.setVisible(true);
    }
}
