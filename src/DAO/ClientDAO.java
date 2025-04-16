package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Client;

public class ClientDAO {

    // Méthode pour insérer un nouveau client dans la BDD
    public boolean addClient(Client client) {
        // On insère toutes les colonnes attendues : nom, email, mot_de_passe et type
        String sql = "INSERT INTO client (nom, email, mot_de_passe, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getMot_De_Passe());
            stmt.setString(4, client.getType());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode pour récupérer un client par son ID
    public Client getClientById(int id) {
        Client client = null;
        String sql = "SELECT * FROM client WHERE id = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setEmail(rs.getString("email"));
                client.setMot_De_Passe(rs.getString("mot_de_passe"));  // Attention au nom de la colonne
                client.setType(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    // Nouvelle méthode pour vérifier l'existence d'un client par email
    public Client getClientByEmail(String email) {
        Client client = null;
        String sql = "SELECT * FROM client WHERE email = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setEmail(rs.getString("email"));
                client.setMot_De_Passe(rs.getString("mot_de_passe"));  // Récupère le mot de passe
                client.setType(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    // Vous pouvez ajouter ici d'autres méthodes (update, delete, etc.) selon vos besoins.
}
