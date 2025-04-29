package DAO;

import model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour gérer les articles en base : CRUD, tarif gros,
 * mise à jour du stock, et support du chemin d’image.
 */
public class ArticleDAO {

    /**
     * Insère un nouvel article dans la base de données.
     */
    public boolean addArticle(Article article) {
        String sql = "INSERT INTO article "
                + "(nom, description, marque, prixUnitaire, prixGros, quantiteEnStock, quantiteEnGros, imagePath) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getDescription());
            stmt.setString(3, article.getMarque());
            stmt.setDouble(4, article.getPrixUnitaire());
            stmt.setDouble(5, article.getPrixGros());
            stmt.setInt(6, article.getQuantiteEnStock());
            stmt.setInt(7, article.getQuantiteEnGros());
            stmt.setString(8, article.getImagePath());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère un article par son ID.
     */
    public Article getArticleById(int id) {
        String sql = "SELECT * FROM article WHERE id = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToArticle(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère la liste complète des articles.
     */
    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM article";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                articles.add(mapResultSetToArticle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * Recherche les articles par mot-clé sur le nom.
     */
    public List<Article> searchArticlesByName(String keyword) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE nom LIKE ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    articles.add(mapResultSetToArticle(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * Met à jour un article existant (CRUD admin).
     */
    public boolean updateArticle(Article article) {
        String sql = "UPDATE article SET "
                + "nom = ?, description = ?, marque = ?, "
                + "prixUnitaire = ?, prixGros = ?, "
                + "quantiteEnStock = ?, quantiteEnGros = ?, imagePath = ? "
                + "WHERE id = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getDescription());
            stmt.setString(3, article.getMarque());
            stmt.setDouble(4, article.getPrixUnitaire());
            stmt.setDouble(5, article.getPrixGros());
            stmt.setInt(6, article.getQuantiteEnStock());
            stmt.setInt(7, article.getQuantiteEnGros());
            stmt.setString(8, article.getImagePath());
            stmt.setInt(9, article.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un article par son ID.
     */
    public boolean deleteArticle(int id) {
        String sql = "DELETE FROM article WHERE id = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour le stock d'un article après une vente.
     * Ne décrémente que si la quantité en stock est suffisante.
     */
    public boolean updateStock(int articleId, int qtyVendue) {
        String sql = "UPDATE article "
                + "SET quantiteEnStock = quantiteEnStock - ? "
                + "WHERE id = ? AND quantiteEnStock >= ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, qtyVendue);
            stmt.setInt(2, articleId);
            stmt.setInt(3, qtyVendue);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Transforme la ligne courante d'un ResultSet en un Article.
     */
    private Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        Article a = new Article();
        a.setId(rs.getInt("id"));
        a.setNom(rs.getString("nom"));
        a.setDescription(rs.getString("description"));
        a.setMarque(rs.getString("marque"));
        a.setPrixUnitaire(rs.getDouble("prixUnitaire"));
        a.setPrixGros(rs.getDouble("prixGros"));
        a.setQuantiteEnStock(rs.getInt("quantiteEnStock"));
        a.setQuantiteEnGros(rs.getInt("quantiteEnGros"));
        a.setImagePath(rs.getString("imagePath"));  // ← chargement du chemin d’image
        return a;
    }
}
