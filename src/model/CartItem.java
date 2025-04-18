// model/CartItem.java
package model;

public class CartItem {
    private Article article;
    private int quantity;

    public CartItem(Article article, int quantity) {
        this.article = article;
        this.quantity = quantity;
    }

    public Article getArticle() { return article; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        return article.getPrixUnitaire() * quantity;
    }
}
