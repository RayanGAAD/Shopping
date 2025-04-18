package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe représentant une commande effectuée par un client.
 */
public class Commande {
    private int id;
    private int idClient;                  // Clé étrangère vers Client
    private LocalDateTime dateCommande;    // Date et heure de la commande
    private double montantTotal;           // Montant total de la commande
    private List<LigneCommande> lignes;    // Lignes de la commande

    public Commande() {
    }

    public Commande(int id, int idClient, LocalDateTime dateCommande, double montantTotal, List<LigneCommande> lignes) {
        this.id            = id;
        this.idClient      = idClient;
        this.dateCommande  = dateCommande;
        this.montantTotal  = montantTotal;
        this.lignes        = lignes;
    }

    // Getters / Setters

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

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }
    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public double getMontantTotal() {
        return montantTotal;
    }
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }
    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", dateCommande=" + dateCommande +
                ", montantTotal=" + montantTotal +
                ", lignes=" + lignes +
                '}';
    }
}
