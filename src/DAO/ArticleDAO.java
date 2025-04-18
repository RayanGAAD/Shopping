package DAO;

import model.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour gérer les articles en base : CRUD et mise à jour du stock.
 */
public class ArticleDAO {

    /**
     * Insère un nouvel article dans la base de données.
     */
    public boolean addArticle(Article article) {
        String sql = "INSERT INTO article (nom, description, prixUnitaire, prixGros, quantiteEnStock, marque) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getDescription());
            stmt.setDouble(3, article.getPrixUnitaire());
            stmt.setDouble(4, article.getPrixGros());
            stmt.setInt(5, article.getQuantiteEnStock());
            stmt.setString(6, article.getMarque());
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
                    Article article = new Article();
                    article.setId(rs.getInt("id"));
                    article.setNom(rs.getString("nom"));
                    article.setDescription(rs.getString("description"));
                    article.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                    article.setPrixGros(rs.getDouble("prixGros"));
                    article.setQuantiteEnStock(rs.getInt("quantiteEnStock"));
                    article.setMarque(rs.getString("marque"));
                    return article;
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
                Article article = new Article();
                article.setId(rs.getInt("id"));
                article.setNom(rs.getString("nom"));
                article.setDescription(rs.getString("description"));
                article.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                article.setPrixGros(rs.getDouble("prixGros"));
                article.setQuantiteEnStock(rs.getInt("quantiteEnStock"));
                article.setMarque(rs.getString("marque"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * Recherche les articles par mot‑clé sur le nom.
     */
    public List<Article> searchArticlesByName(String keyword) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE nom LIKE ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Article article = new Article();
                    article.setId(rs.getInt("id"));
                    article.setNom(rs.getString("nom"));
                    article.setDescription(rs.getString("description"));
                    article.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                    article.setPrixGros(rs.getDouble("prixGros"));
                    article.setQuantiteEnStock(rs.getInt("quantiteEnStock"));
                    article.setMarque(rs.getString("marque"));
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * Met à jour le stock d'un article après une vente.
     * Ne décrémente que si la quantité en stock est suffisante.
     */
    public boolean updateStock(int articleId, int qtyVendue) {
        String sql = "UPDATE article " +
                "SET quantiteEnStock = quantiteEnStock - ? " +
                "WHERE id = ? AND quantiteEnStock >= ?";
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
}
