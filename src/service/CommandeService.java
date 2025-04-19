package service;

import DAO.CommandeDAO;
import DAO.ArticleDAO;
import model.CartItem;
import model.Commande;
import model.LigneCommande;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service gérant le passage de commande : persistance en base,
 * mise à jour des stocks et historique des commandes.
 */
public class CommandeService {
    private CommandeDAO commandeDAO = new CommandeDAO();
    private ArticleDAO articleDAO   = new ArticleDAO();

    /**
     * Passe la commande : enregistre la commande + ses lignes, met à jour le stock,
     * puis retourne le succès ou l'échec.
     * @param idClient  ID du client qui commande.
     * @param cartItems Liste des articles et quantités du panier.
     * @return true si la commande a bien été enregistrée et le stock mis à jour, false sinon.
     */
    public boolean placeOrder(int idClient, List<CartItem> cartItems) {
        // Calcul du total
        double total = cartItems.stream()
                .mapToDouble(ci -> ci.getArticle().getPrixUnitaire() * ci.getQuantity())
                .sum();

        // Préparer l'objet Commande
        Commande cmd = new Commande();
        cmd.setIdClient(idClient);
        cmd.setDateCommande(LocalDateTime.now());
        cmd.setMontantTotal(total);

        // 1) Créer la commande et récupérer son ID
        int idCmd = commandeDAO.createCommande(cmd);
        if (idCmd < 0) {
            return false;
        }

        // 2) Créer chacune des lignes de commande et mettre à jour le stock
        for (CartItem ci : cartItems) {
            LigneCommande ligne = new LigneCommande();
            ligne.setIdCommande(idCmd);
            ligne.setIdArticle(ci.getArticle().getId());
            ligne.setQuantite(ci.getQuantity());
            ligne.setPrixUnitaire(ci.getArticle().getPrixUnitaire());

            boolean ligneOk = commandeDAO.createLigneCommande(ligne);
            if (!ligneOk) {
                return false;
            }

            boolean stockOk = articleDAO.updateStock(ci.getArticle().getId(), ci.getQuantity());
            if (!stockOk) {
                return false; // stock insuffisant ou erreur
            }
        }

        return true;
    }

    /**
     * Récupère l'historique des commandes d'un client.
     * @param clientId ID du client.
     * @return Liste des commandes passées par ce client, triées par date décroissante.
     */
    public List<Commande> getOrderHistory(int clientId) {
        return commandeDAO.getCommandesByClientId(clientId);
    }

    /**
     * Récupère le détail des lignes pour une commande donnée.
     * @param commandeId ID de la commande.
     * @return Liste des lignes de cette commande.
     */
    public List<LigneCommande> getOrderDetails(int commandeId) {
        return commandeDAO.getLignesByCommandeId(commandeId);
    }
}
