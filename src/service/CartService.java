// service/CartService.java
package service;

import model.Panier;
import model.Article;
import model.CartItem;
import java.util.List;

public class CartService {
    private Panier panier = new Panier();

    public void addToCart(Article art, int qty) {
        panier.addArticle(art, qty);
    }

    public void updateQuantity(Article art, int qty) {
        panier.updateQuantity(art, qty);
    }

    public void removeFromCart(Article art) {
        panier.removeArticle(art);
    }

    public List<CartItem> getCartItems() {
        return panier.getItems();
    }

    public double getCartTotal() {
        return panier.getTotal();
    }

    /** Simule la validation de la commande : vous pouvez injecter CommandeService ici. */
    public boolean checkout() {
        if (panier.getItems().isEmpty()) return false;
        // TODO : appel à CommandeService pour créer la commande en base
        panier.clear();
        return true;
    }
}
