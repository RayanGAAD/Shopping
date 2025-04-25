package view;

import model.Client;
import service.ClientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Panel de gestion des clients pour l'interface d'administration.
 * Affiche tous les clients et permet de les supprimer.
 */
public class ClientManagementPanel extends JPanel {
    private final ClientService clientService;
    private final JTable table;
    private final DefaultTableModel tableModel;

    // Colonnes à afficher dans la JTable
    private static final String[] COLUMN_NAMES = {
            "ID", "Nom", "Email", "Type"
    };

    public ClientManagementPanel(ClientService clientService) {
        this.clientService = clientService;
        setLayout(new BorderLayout());
        // Initialisation de la table
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel de boutons en bas
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteBtn = new JButton("Supprimer");
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Chargement initial des clients
        loadClients();

        // Action du bouton "Supprimer"
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Sélectionnez d'abord un client à supprimer.",
                        "Aucune sélection",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            Client cli = getClientFromRow(row);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Supprimer le client \"" + cli.getNom() + "\" ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = clientService.deleteClient(cli.getId());
                if (ok) {
                    loadClients();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Échec de la suppression.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    /**
     * Recharge la liste des clients depuis la base et met à jour la JTable.
     */
    private void loadClients() {
        tableModel.setRowCount(0);
        List<Client> clients = clientService.getAllClients();
        for (Client c : clients) {
            Object[] row = {
                    c.getId(),
                    c.getNom(),
                    c.getEmail(),
                    c.getType()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Crée un objet Client à partir d'une ligne sélectionnée de la JTable.
     */
    private Client getClientFromRow(int row) {
        Client c = new Client();
        c.setId((Integer) tableModel.getValueAt(row, 0));
        c.setNom((String)  tableModel.getValueAt(row, 1));
        c.setEmail((String)tableModel.getValueAt(row, 2));
        c.setType((String) tableModel.getValueAt(row, 3));
        return c;
    }
}
