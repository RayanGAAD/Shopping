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
    private ClientService clientService = new ClientService();
    private int clientId;
    private JTable table;
    private DefaultTableModel model;
    private JButton detailButton, refreshButton;

    public OrderHistoryFrame(CommandeService commandeService, int clientId) {
        this.commandeService = commandeService;
        this.clientId = clientId;

        Client client = clientService.findClientById(clientId);

        setTitle("Historique de " + client.getNom());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(createGradientPanel());
        setLayout(new BorderLayout(10, 10));

        initUI(client);
    }

    private JPanel createGradientPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(224, 255, 255); // Bleu clair
                Color color2 = new Color(255, 239, 213); // Pêche clair
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private void initUI(Client client) {
        // ===== Header
        JLabel header = new JLabel(
                "Historique des commandes de " + client.getNom(),
                SwingConstants.CENTER
        );
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // ===== Tableau
        String[] cols = {"N° commande", "Date", "Montant (€)"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Boutons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);

        refreshButton = new JButton("Rafraîchir");
        refreshButton.setBackground(new Color(135, 206, 250)); // Bleu clair
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusPainted(false);

        detailButton = new JButton("Voir détails");
        detailButton.setBackground(new Color(255, 200, 100)); // Orange clair
        detailButton.setForeground(Color.BLACK);
        detailButton.setFocusPainted(false);

        bottom.add(refreshButton);
        bottom.add(detailButton);
        add(bottom, BorderLayout.SOUTH);

        // ===== Listeners
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
