package service;

import model.Panier;
import model.Article;
import model.CartItem;
import java.util.List;

/**
 * Service de gestion du panier d'achat, avec intégration de la persistance de commandes.
 */
public class CartService {
    private Panier panier = new Panier();
    private CommandeService commandeService = new CommandeService();
    private int clientId; // Identifiant du client courant

    /**
     * Définit l'identifiant du client qui passera la commande.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Ajoute un article avec quantité dans le panier.
     */
    public void addToCart(Article art, int qty) {
        panier.addArticle(art, qty);
    }

    /**
     * Met à jour la quantité d'un article dans le panier.
     */
    public void updateQuantity(Article art, int qty) {
        panier.updateQuantity(art, qty);
    }

    /**
     * Retire un article du panier.
     */
    public void removeFromCart(Article art) {
        panier.removeArticle(art);
    }

    /**
     * Retourne la liste des items du panier.
     */
    public List<CartItem> getCartItems() {
        return panier.getItems();
    }

    /**
     * Calcule le montant total du panier.
     */
    public double getCartTotal() {
        return panier.getTotal();
    }

    /**
     * Valide la commande en base pour le client courant et vide le panier si succès.
     * @return true si la commande a été enregistrée, false sinon.
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
