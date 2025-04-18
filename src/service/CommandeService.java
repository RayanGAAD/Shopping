package service;

import DAO.CommandeDAO;
import model.CartItem;
import model.Commande;
import model.LigneCommande;

import java.time.LocalDateTime;
import java.util.List;

public class CommandeService {
    private CommandeDAO commandeDAO = new CommandeDAO();

    /**
     * Passe la commande : enregistre la commande + ses lignes, puis vide le panier si tout réussit.
     * @param idClient   ID du client qui commande.
     * @param cartItems  Liste des articles et quantités du panier.
     * @return true si la commande a bien été enregistrée, false sinon.
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

        // 2) Créer chacune des lignes de commande
        for (CartItem ci : cartItems) {
            LigneCommande ligne = new LigneCommande();
            ligne.setIdCommande(idCmd);
            ligne.setIdArticle(ci.getArticle().getId());
            ligne.setQuantite(ci.getQuantity());
            ligne.setPrixUnitaire(ci.getArticle().getPrixUnitaire());

            boolean ok = commandeDAO.createLigneCommande(ligne);
            if (!ok) {
                return false;
            }
        }

        return true;
    }
}
