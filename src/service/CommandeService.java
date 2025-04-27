package service;

import DAO.CommandeDAO;
import DAO.ArticleDAO;
import model.CartItem;
import model.Commande;
import model.LigneCommande;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service gérant le passage de commande : persistance en base,
 * mise à jour des stocks, historique et extraction de toutes les commandes,
 * ainsi que reporting des statistiques.
 */
public class CommandeService {
    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final ArticleDAO   articleDAO   = new ArticleDAO();

    /**
     * Passe la commande : enregistre la commande + ses lignes, met à jour le stock,
     * puis retourne le succès ou l'échec.
     */
    public boolean placeOrder(int idClient, List<CartItem> cartItems) {
        double total = cartItems.stream()
                .mapToDouble(ci -> ci.getArticle().getPrixUnitaire() * ci.getQuantity())
                .sum();

        Commande cmd = new Commande();
        cmd.setIdClient(idClient);
        cmd.setDateCommande(LocalDateTime.now());
        cmd.setMontantTotal(total);

        int idCmd = commandeDAO.createCommande(cmd);
        if (idCmd < 0) return false;

        for (CartItem ci : cartItems) {
            LigneCommande ligne = new LigneCommande();
            ligne.setIdCommande(idCmd);
            ligne.setIdArticle(ci.getArticle().getId());
            ligne.setQuantite(ci.getQuantity());
            ligne.setPrixUnitaire(ci.getArticle().getPrixUnitaire());

            if (!commandeDAO.createLigneCommande(ligne)) return false;
            if (!articleDAO.updateStock(ci.getArticle().getId(), ci.getQuantity())) return false;
        }
        return true;
    }

    /**
     * Historique des commandes d’un client.
     */
    public List<Commande> getOrderHistory(int clientId) {
        return commandeDAO.getCommandesByClientId(clientId);
    }

    /**
     * Détail (lignes) d’une commande.
     */
    public List<LigneCommande> getOrderDetails(int commandeId) {
        return commandeDAO.getLignesByCommandeId(commandeId);
    }

    /**
     * Pour l’administration, récupère toutes les commandes de tous les clients.
     */
    public List<Commande> getAllOrders() {
        return commandeDAO.getAllCommandes();
    }

    /**
     * Reporting : nombre total d’articles vendus par marque.
     * @return Map<marque, quantité_totale_vendue>
     */
    public Map<String, Integer> getSalesByBrand() {
        return commandeDAO.getSalesByBrand();
    }

    /**
     * Reporting : nombre de commandes passées par jour.
     * @return Map<date (YYYY-MM-DD), nombre_de_commandes>
     */
    public Map<LocalDate, Integer> getOrdersPerDay() {
        return commandeDAO.getOrdersPerDay();
    }
}
