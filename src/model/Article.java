package model;

/**
 * Classe représentant un article à vendre, avec support tarifaire en gros.
 */
public class Article {
    private int id;
    private String nom;
    private String description;
    private double prixUnitaire;
    private double prixGros;
    private int quantiteEnStock;
    private int quantiteEnGros;
    private String marque;
    private String imagePath; // AJOUT

    public Article() {}

    public Article(int id,
                   String nom,
                   String description,
                   double prixUnitaire,
                   double prixGros,
                   int quantiteEnStock,
                   int quantiteEnGros,
                   String marque,
                   String imagePath) { // AJOUT
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
        this.prixGros = prixGros;
        this.quantiteEnStock = quantiteEnStock;
        this.quantiteEnGros = quantiteEnGros;
        this.marque = marque;
        this.imagePath = imagePath; // AJOUT
    }

    // --- Getters & setters ---

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getPrixGros() {
        return prixGros;
    }
    public void setPrixGros(double prixGros) {
        this.prixGros = prixGros;
    }

    public int getQuantiteEnStock() {
        return quantiteEnStock;
    }
    public void setQuantiteEnStock(int quantiteEnStock) {
        this.quantiteEnStock = quantiteEnStock;
    }

    public int getQuantiteEnGros() {
        return quantiteEnGros;
    }
    public void setQuantiteEnGros(int quantiteEnGros) {
        this.quantiteEnGros = quantiteEnGros;
    }

    public String getMarque() {
        return marque;
    }
    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getImagePath() {     // AJOUT
        return imagePath;
    }
    public void setImagePath(String imagePath) {    // AJOUT
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prixUnitaire=" + prixUnitaire +
                ", prixGros=" + prixGros +
                ", quantiteEnStock=" + quantiteEnStock +
                ", quantiteEnGros=" + quantiteEnGros +
                ", marque='" + marque + '\'' +
                ", imagePath='" + imagePath + '\'' + // AJOUT
                '}';
    }
}
