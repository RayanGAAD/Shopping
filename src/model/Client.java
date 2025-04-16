package model;

/**
 * Classe représentant un client de l'application de shopping.
 */
public class Client {
    private int id;
    private String nom;
    private String email;
    private String mot_De_Passe;
    private String type;

    // Constructeur par défaut
    public Client() {
    }

    // Constructeur avec paramètres pour les champs principaux et supplémentaires
    public Client(int id, String nom, String email, String mot_De_Passe, String type) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.mot_De_Passe = mot_De_Passe;
        this.type = type;
    }

    // Constructeur avec paramètres pour les champs obligatoires (si besoin)
    public Client(int id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
    }

    // Getters et Setters pour id, nom et email
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters et Setters pour mot_De_Passe et type
    public String getMot_De_Passe() {
        return mot_De_Passe;
    }

    public void setMot_De_Passe(String mot_De_Passe) {
        this.mot_De_Passe = mot_De_Passe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Méthode toString() mise à jour pour afficher tous les attributs (optionnel)
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", mot_De_Passe='" + mot_De_Passe + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
