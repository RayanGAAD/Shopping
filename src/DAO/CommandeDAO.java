package DAO;

import model.Commande;
import model.LigneCommande;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * DAO pour persister les commandes et leurs lignes en base de données,
 * et produire des statistiques pour le reporting.
 */
public class CommandeDAO {

    /**
     * Crée une commande dans la table `commande` et retourne l'ID généré.
     */
    public int createCommande(Commande commande) {
        String sql = "INSERT INTO commande (client_id, date_commande, montant_total) VALUES (?, ?, ?)";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, commande.getIdClient());
            stmt.setTimestamp(2, Timestamp.valueOf(commande.getDateCommande()));
            stmt.setDouble(3, commande.getMontantTotal());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Insère une ligne de commande dans `commande_article`.
     */
    public boolean createLigneCommande(LigneCommande ligne) {
        String sql = "INSERT INTO commande_article (commande_id, article_id, quantite) VALUES (?, ?, ?)";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ligne.getIdCommande());
            stmt.setInt(2, ligne.getIdArticle());
            stmt.setInt(3, ligne.getQuantite());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère une commande (sans ses lignes) par son ID.
     */
    public Commande getCommandeById(int id) {
        Commande commande = null;
        String sql = "SELECT * FROM commande WHERE id = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    commande = mapResultSetToCommande(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commande;
    }

    /**
     * Récupère toutes les commandes passées par un client.
     * @param clientId L'identifiant du client.
     * @return Liste d'objets Commande.
     */
    public List<Commande> getCommandesByClientId(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande WHERE client_id = ? ORDER BY date_commande DESC";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    commandes.add(mapResultSetToCommande(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    /**
     * Récupère toutes les commandes (tous clients), triées par date décroissante.
     * Utile pour l'administration.
     * @return Liste d'objets Commande.
     */
    public List<Commande> getAllCommandes() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande ORDER BY date_commande DESC";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    /**
     * Récupère toutes les lignes associées à une commande donnée,
     * en allant chercher le prix unitaire (champ `prixUnitaire`) depuis la table `article`.
     * @param commandeId L'identifiant de la commande.
     * @return Liste d'objets LigneCommande, avec prixUnitaire correctement rempli.
     */
    public List<LigneCommande> getLignesByCommandeId(int commandeId) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql =
                "SELECT la.commande_id, la.article_id, la.quantite, a.prixUnitaire " +
                        "FROM commande_article la " +
                        "JOIN article a ON la.article_id = a.id " +
                        "WHERE la.commande_id = ?";

        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, commandeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LigneCommande ligne = new LigneCommande();
                    ligne.setIdCommande(   rs.getInt("commande_id") );
                    ligne.setIdArticle(    rs.getInt("article_id") );
                    ligne.setQuantite(     rs.getInt("quantite") );
                    ligne.setPrixUnitaire( rs.getDouble("prixUnitaire") );
                    lignes.add(ligne);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignes;
    }

    /**
     * Ventes cumulées par marque (somme des quantités vendues).
     * @return Map<marque, quantité_totale_vendue>
     */
    public Map<String, Integer> getSalesByBrand() {
        Map<String, Integer> ventesParMarque = new LinkedHashMap<>();
        String sql =
                "SELECT a.marque, SUM(ca.quantite) AS total " +
                        "FROM commande_article ca " +
                        "JOIN article a ON ca.article_id = a.id " +
                        "GROUP BY a.marque " +
                        "ORDER BY a.marque";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ventesParMarque.put(
                        rs.getString("marque"),
                        rs.getInt("total")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventesParMarque;
    }

    /**
     * Nombre de commandes passées par jour.
     * @return Map<date_commande (YYYY-MM-DD), nombre_de_commandes>
     */
    public Map<LocalDate, Integer> getOrdersPerDay() {
        Map<LocalDate, Integer> commandesParJour = new TreeMap<>();
        String sql =
                "SELECT DATE(date_commande) AS jour, COUNT(*) AS count " +
                        "FROM commande " +
                        "GROUP BY jour " +
                        "ORDER BY jour";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                commandesParJour.put(
                        rs.getDate("jour").toLocalDate(),
                        rs.getInt("count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandesParJour;
    }

    /**
     * Extrait un objet Commande à partir d'un ResultSet positionné sur la ligne courante.
     */
    private Commande mapResultSetToCommande(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("id"));
        commande.setIdClient(rs.getInt("client_id"));
        Timestamp ts = rs.getTimestamp("date_commande");
        if (ts != null) {
            commande.setDateCommande(ts.toLocalDateTime());
        }
        commande.setMontantTotal(rs.getDouble("montant_total"));
        return commande;
    }
}
