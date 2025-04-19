package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Client;

public class ClientDAO {

    /**
     * Insère un nouveau client dans la BDD.
     */
    public boolean addClient(Client client) {
        String sql = "INSERT INTO client (nom, email, mot_de_passe, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getMot_De_Passe());
            stmt.setString(4, client.getType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère un client par son ID.
     */
    public Client getClientById(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = mapResultSetToClient(rs);
                    return client;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère un client par son email.
     */
    public Client getClientByEmail(String email) {
        String sql = "SELECT * FROM client WHERE email = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = mapResultSetToClient(rs);
                    return client;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Vérifie l'email et mot de passe et retourne le Client si valides, sinon null.
     */
    public Client getClientByEmailAndPassword(String email, String motDePasse) {
        String sql = "SELECT * FROM client WHERE email = ? AND mot_de_passe = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convertit une ligne de ResultSet en objet Client.
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setNom(rs.getString("nom"));
        client.setEmail(rs.getString("email"));
        client.setMot_De_Passe(rs.getString("mot_de_passe"));
        client.setType(rs.getString("type"));
        return client;
    }
}
