package service;

import DAO.ClientDAO;
import model.Client;

public class ClientService {

    // Instance du DAO utilisé pour accéder aux données du client
    private ClientDAO clientDAO = new ClientDAO();

    /**
     * Enregistre un nouveau client après avoir validé les données.
     * @param client L'objet client à enregistrer.
     * @return true si l'enregistrement a réussi, false sinon.
     */
    public boolean registerClient(Client client) {
        // Vérification que tous les champs obligatoires sont renseignés
        if (client.getNom() == null || client.getNom().isEmpty() ||
                client.getEmail() == null || client.getEmail().isEmpty() ||
                client.getMot_De_Passe() == null || client.getMot_De_Passe().isEmpty() ||
                client.getType() == null || client.getType().isEmpty()) {
            System.out.println("Tous les champs sont obligatoires !");
            return false;
        }

        // Vérifier l'unicité de l'email avant l'insertion
        if (clientDAO.getClientByEmail(client.getEmail()) != null) {
            System.out.println("Erreur : Cet email est déjà utilisé !");
            return false;
        }

        // Appel à la méthode du DAO pour insérer le client dans la BDD
        return clientDAO.addClient(client);
    }

    /**
     * Récupère un client via son ID.
     * @param id L'identifiant du client.
     * @return Le client récupéré ou null s'il n'existe pas.
     */
    public Client findClientById(int id) {
        return clientDAO.getClientById(id);
    }

    // Vous pouvez ajouter d'autres méthodes pour mettre à jour ou supprimer un client si nécessaire.
}
