package service;

import DAO.ArticleDAO;
import model.Article;

import java.util.List;

/**
 * Service applicatif pour la gestion des articles :
 * lecture, recherche, création, mise à jour et suppression.
 */
public class ArticleService {

    private ArticleDAO articleDAO = new ArticleDAO();

    /**
     * Récupère tous les articles depuis la base de données.
     * @return Une liste d'articles.
     */
    public List<Article> getAllArticles() {
        return articleDAO.getAllArticles();
    }

    /**
     * Recherche des articles dont le nom contient le mot-clé fourni.
     * @param keyword Le mot-clé pour la recherche.
     * @return Une liste d'articles correspondants.
     */
    public List<Article> searchArticlesByName(String keyword) {
        return articleDAO.searchArticlesByName(keyword);
    }

    /**
     * Récupère un article par son ID.
     * @param id L'identifiant de l'article.
     * @return L'article correspondant, ou null s'il n'existe pas.
     */
    public Article getArticleById(int id) {
        return articleDAO.getArticleById(id);
    }

    /**
     * Crée un nouvel article en base.
     * @param article L'article à ajouter.
     * @return true si l'insertion a réussi, false sinon.
     */
    public boolean addArticle(Article article) {
        return articleDAO.addArticle(article);
    }

    /**
     * Met à jour un article existant en base.
     * @param article L'article modifié.
     * @return true si la mise à jour a réussi, false sinon.
     */
    public boolean updateArticle(Article article) {
        return articleDAO.updateArticle(article);
    }

    /**
     * Supprime un article de la base.
     * @param id L'ID de l'article à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean deleteArticle(int id) {
        return articleDAO.deleteArticle(id);
    }
}
