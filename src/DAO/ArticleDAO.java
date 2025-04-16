package DAO;

import model.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    /**
     * Insère un nouvel article dans la base de données.
     * @param article L'objet Article à insérer.
     * @return true si l'insertion a réussi, false sinon.
     */
    public boolean addArticle(Article article) {
        // On utilise "marque" au lieu de "idMarque"
        String sql = "INSERT INTO article (nom, description, prixUnitaire, prixGros, quantiteEnStock, marque) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getDescription());
            stmt.setDouble(3, article.getPrixUnitaire());
            stmt.setDouble(4, article.getPrixGros());
            stmt.setInt(5, article.getQuantiteEnStock());
            stmt.setString(6, article.getMarque());  // Utilisation de setString pour la marque

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère un article de la base de données par son ID.
     * @param id L'identifiant de l'article.
     * @return L'objet Article correspondant, ou null s'il n'existe pas.
     */
    public Article getArticleById(int id) {
        Article article = null;
        String sql = "SELECT * FROM article WHERE id = ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                article = new Article();
                article.setId(rs.getInt("id"));
                article.setNom(rs.getString("nom"));
                article.setDescription(rs.getString("description"));
                article.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                article.setPrixGros(rs.getDouble("prixGros"));
                article.setQuantiteEnStock(rs.getInt("quantiteEnStock"));
                article.setMarque(rs.getString("marque")); // Lecture de la marque comme String
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    /**
     * Récupère la liste de tous les articles de la table 'article'.
     * @return Une liste d'objets Article.
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
                article.setMarque(rs.getString("marque")); // Utilisation d'un nom de colonne cohérent
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * Recherche les articles dont le nom correspond partiellement au mot-clé.
     * @param keyword Le mot-clé à rechercher dans la colonne 'nom'.
     * @return Une liste d'articles correspondants.
     */
    public List<Article> searchArticlesByName(String keyword) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE nom LIKE ?";
        try (Connection conn = DataCO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Le wildcard '%' permet une recherche partielle
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
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
}
