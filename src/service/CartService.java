package service;

import model.Panier;
import model.Article;
import model.CartItem;
import DAO.ArticleDAO;
import service.CommandeService;
import java.util.List;

/**
 * Service de gestion du panier d'achat, avec intégration de la persistance de commandes
 * et vérification de la disponibilité en stock.
 */
public class CartService {
    private Panier panier = new Panier();
    private CommandeService commandeService = new CommandeService();
    private ArticleDAO articleDAO = new ArticleDAO();
    private int clientId; // Identifiant du client courant

    /**
     * Définit l'identifiant du client qui passera la commande.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Retourne l'identifiant du client courant.
     */
    public int getClientId() {
        return this.clientId;
    }

    /**
     * Ajoute un article avec quantité dans le panier après vérification du stock.
     * @param art Article à ajouter.
     * @param qty Quantité désirée.
     * @return true si l'ajout a réussi, false si stock insuffisant.
     */
    public boolean addToCart(Article art, int qty) {
        Article dbArt = articleDAO.getArticleById(art.getId());
        if (dbArt.getQuantiteEnStock() < qty) {
            return false;
        }
        panier.addArticle(art, qty);
        return true;
    }

    /**
     * Met à jour la quantité d'un article dans le panier après vérification du stock.
     * @param art Article à mettre à jour.
     * @param qty Nouvelle quantité désirée.
     * @return true si la mise à jour a réussi, false si stock insuffisant.
     */
    public boolean updateQuantity(Article art, int qty) {
        Article dbArt = articleDAO.getArticleById(art.getId());
        if (dbArt.getQuantiteEnStock() < qty) {
            return false;
        }
        panier.updateQuantity(art, qty);
        return true;
    }

    /**
     * Retire un article du panier.
     * @param art Article à retirer.
     */
    public void removeFromCart(Article art) {
        panier.removeArticle(art);
    }

    /**
     * Retourne la liste des items du panier.
     * @return Liste de CartItem.
     */
    public List<CartItem> getCartItems() {
        return panier.getItems();
    }

    /**
     * Calcule le montant total du panier.
     * @return Total en euros.
     */
    public double getCartTotal() {
        return panier.getTotal();
    }

    /**
     * Valide la commande en base pour le client courant et vide le panier si succès.
     * @return true si la commande a été enregistrée et le stock mis à jour, false sinon.
     */
    public boolean checkout() {
        List<CartItem> items = panier.getItems();
        if (items.isEmpty()) {
            return false;
        }
        boolean success = commandeService.placeOrder(clientId, items);
        if (success) {
            panier.clear();
        }
        return success;
    }
}
