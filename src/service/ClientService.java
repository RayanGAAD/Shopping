package service;

import DAO.ClientDAO;
import model.Client;

/**
 * Service applicatif pour la gestion des clients :
 * enregistrement, authentification et session.
 */
public class ClientService {

    private ClientDAO clientDAO = new ClientDAO();
    private Client currentClient;  // client authentifié

    /**
     * Enregistre un nouveau client après validation des données.
     * @param client L'objet client à enregistrer.
     * @return true si l'enregistrement a réussi, false sinon.
     */
    public boolean registerClient(Client client) {
        // Vérification des champs obligatoires
        if (client.getNom() == null || client.getNom().isEmpty() ||
                client.getEmail() == null || client.getEmail().isEmpty() ||
                client.getMot_De_Passe() == null || client.getMot_De_Passe().isEmpty() ||
                client.getType() == null || client.getType().isEmpty()) {
            System.out.println("Tous les champs sont obligatoires !");
            return false;
        }

        // Vérification de l'unicité de l'email
        if (clientDAO.getClientByEmail(client.getEmail()) != null) {
            System.out.println("Erreur : Cet email est déjà utilisé !");
            return false;
        }

        return clientDAO.addClient(client);
    }

    /**
     * Authentifie un client par email et mot de passe.
     * Stocke le client courant si succès.
     * @param email      Email du client.
     * @param motDePasse Mot de passe du client.
     * @return L'ID du client si authentification réussie, sinon null.
     */
    public Integer login(String email, String motDePasse) {
        // NB : vous devez avoir implémenté getClientByEmailAndPassword dans ClientDAO
        Client c = clientDAO.getClientByEmailAndPassword(email, motDePasse);
        if (c != null) {
            this.currentClient = c;
            return c.getId();
        }
        return null;
    }

    /**
     * Se déconnecte (efface la session).
     */
    public void logout() {
        this.currentClient = null;
    }

    /**
     * Récupère le client courant (après connexion).
     * @return Le Client authentifié, ou null si pas connecté.
     */
    public Client getCurrentClient() {
        return currentClient;
    }

    /**
     * Récupère un client via son ID.
     * @param id Identifiant du client.
     * @return Le client ou null s'il n'existe pas.
     */
    public Client findClientById(int id) {
        return clientDAO.getClientById(id);
    }

    /**
     * Récupère un client via son email seul.
     * Utile pour divers contrôles.
     * @param email Email à rechercher.
     * @return Le client ou null s'il n'existe pas.
     */
    public Client getClientByEmail(String email) {
        return clientDAO.getClientByEmail(email);
    }
}
