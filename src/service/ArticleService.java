package service;

import DAO.ArticleDAO;
import model.Article;
import java.util.List;

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
}
