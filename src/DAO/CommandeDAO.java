package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Commande;

public class CommandeDAO {

    // Méthode pour insérer une nouvelle commande dans la BDD
    public boolean addCommande(Commande commande) {
        String sql = "INSERT INTO commande (idClient, dateCommande, montantTotal) VALUES (?, ?, ?)";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commande.getIdClient());
            stmt.setDate(2, commande.getDateCommande());
            stmt.setDouble(3, commande.getMontantTotal());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode pour récupérer une commande par son ID
    public Commande getCommandeById(int id) {
        Commande commande = null;
        String sql = "SELECT * FROM commande WHERE id = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                commande = new Commande();
                commande.setId(rs.getInt("id"));
                commande.setIdClient(rs.getInt("idClient"));
                commande.setDateCommande(rs.getDate("dateCommande"));
                commande.setMontantTotal(rs.getDouble("montantTotal"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commande;
    }

    // Vous pouvez ajouter d'autres méthodes pour update, delete, ou récupérer toutes les commandes.
}
