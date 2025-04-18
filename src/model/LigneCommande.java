package model;

/**
 * Représente une ligne d'une commande : un article et la quantité commandée.
 */
public class LigneCommande {
    private int id;             // (facultatif si vous n'avez pas de PK sur la ligne)
    private int idCommande;     // clé étrangère vers Commande
    private int idArticle;      // clé étrangère vers Article
    private int quantite;       // quantité commandée
    private double prixUnitaire;// prix à l'unité au moment de la commande

    public LigneCommande() {}

    public LigneCommande(int idCommande, int idArticle, int quantite, double prixUnitaire) {
        this.idCommande   = idCommande;
        this.idArticle    = idArticle;
        this.quantite     = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCommande() { return idCommande; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }

    public int getIdArticle() { return idArticle; }
    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "idCommande=" + idCommande +
                ", idArticle=" + idArticle +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                '}';
    }
}
