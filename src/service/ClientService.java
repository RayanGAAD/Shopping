package service;

import DAO.ClientDAO;
import model.Client;

import java.util.List;

/**
 * Service applicatif pour la gestion des clients :
 * enregistrement, authentification, session et administration.
 */
public class ClientService {

    private ClientDAO clientDAO = new ClientDAO();
    private Client currentClient;  // client authentifié ou inscrit

    /**
     * Enregistre un nouveau client après validation des données.
     * Si l'enregistrement réussit, stocke directement le client en session.
     * @param client L'objet client à enregistrer.
     * @return true si l'enregistrement a réussi, false sinon.
     */
    public boolean registerClient(Client client) {
        // 1) Vérification des champs obligatoires
        if (client.getNom() == null || client.getNom().isEmpty() ||
                client.getEmail() == null || client.getEmail().isEmpty() ||
                client.getMot_De_Passe() == null || client.getMot_De_Passe().isEmpty() ||
                client.getType() == null || client.getType().isEmpty()) {
            System.out.println("Tous les champs sont obligatoires !");
            return false;
        }

        // 2) Vérification de l'unicité de l'email
        if (clientDAO.getClientByEmail(client.getEmail()) != null) {
            System.out.println("Erreur : Cet email est déjà utilisé !");
            return false;
        }

        // 3) Insertion
        boolean ok = clientDAO.addClient(client);
        if (ok) {
            // On récupère l'enregistrement fraîchement ajouté pour obtenir son ID
            this.currentClient = clientDAO.getClientByEmail(client.getEmail());
        }
        return ok;
    }

    /**
     * Authentifie un client par email et mot de passe.
     * Stocke le client courant si succès.
     * @param email      Email du client.
     * @param motDePasse Mot de passe du client.
     * @return L'ID du client si authentification réussie, sinon null.
     */
    public Integer login(String email, String motDePasse) {
        Client c = clientDAO.getClientByEmailAndPassword(email, motDePasse);
        if (c != null) {
            this.currentClient = c;
            return c.getId();
        }
        return null;
    }

    /** Termine la session du client courant. */
    public void logout() {
        this.currentClient = null;
    }

    /** @return Le client actuellement en session, ou null si personne. */
    public Client getCurrentClient() {
        return currentClient;
    }

    /** @return La liste de tous les clients (pour l’interface admin). */
    public List<Client> getAllClients() {
        return clientDAO.getAllClients();
    }

    /**
     * Supprime un client de la base.
     * @param id Identifiant du client à supprimer.
     * @return true si la suppression a bien eu lieu, false sinon.
     */
    public boolean deleteClient(int id) {
        return clientDAO.deleteClient(id);
    }

    /** Recherche un client par ID (utile pour recharger ses données). */
    public Client findClientById(int id) {
        return clientDAO.getClientById(id);
    }

    /** Recherche un client par email seul. */
    public Client getClientByEmail(String email) {
        return clientDAO.getClientByEmail(email);
    }
}
