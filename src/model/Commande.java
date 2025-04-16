package model;

import java.sql.Date;

/**
 * Classe représentant une commande effectuée par un client.
 */
public class Commande {
    private int id;
    private int idClient;  // Clé étrangère vers Client
    private Date dateCommande;
    private double montantTotal;

    public Commande() {
    }

    public Commande(int id, int idClient, Date dateCommande, double montantTotal) {
        this.id = id;
        this.idClient = idClient;
        this.dateCommande = dateCommande;
        this.montantTotal = montantTotal;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Date getDateCommande() {
        return dateCommande;
    }
    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public double getMontantTotal() {
        return montantTotal;
    }
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", dateCommande=" + dateCommande +
                ", montantTotal=" + montantTotal +
                '}';
    }
}
