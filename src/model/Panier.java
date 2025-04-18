// model/Panier.java
package model;

import java.util.*;

public class Panier {
    private List<CartItem> items = new ArrayList<>();

    public void addArticle(Article art, int qty) {
        // si déjà présent, on incrémente
        for (CartItem ci : items) {
            if (ci.getArticle().getId() == art.getId()) {
                ci.setQuantity(ci.getQuantity() + qty);
                return;
            }
        }
        items.add(new CartItem(art, qty));
    }

    public void updateQuantity(Article art, int qty) {
        items.removeIf(ci -> {
            if (ci.getArticle().getId() == art.getId()) {
                if (qty <= 0) return true;
                ci.setQuantity(qty);
            }
            return false;
        });
    }

    public void removeArticle(Article art) {
        items.removeIf(ci -> ci.getArticle().getId() == art.getId());
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public void clear() {
        items.clear();
    }
}
