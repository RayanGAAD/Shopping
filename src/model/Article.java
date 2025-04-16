package model;

/**
 * Classe représentant un article à vendre.
 */
public class Article {
    private int id;
    private String nom;
    private String description;
    private double prixUnitaire;
    private double prixGros;
    private int quantiteEnStock;
    private String Marque;  // Si vous avez une table Marque

    public Article() {
    }

    public Article(int id, String nom, String description, double prixUnitaire, double prixGros, int quantiteEnStock, int idMarque) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
        this.prixGros = prixGros;
        this.quantiteEnStock = quantiteEnStock;
        this.Marque = Marque;
    }

    // Getters et setters
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

    public String getMarque() {
        return Marque;
    }

    public void setMarque(String Marque) {
        this.Marque = Marque;
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
                ", Marque=" + Marque +
                '}';
    }
}
