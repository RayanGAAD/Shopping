package view;

import model.Article;
import service.ArticleService;
import service.CartService;
import service.ClientService;
import service.CommandeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Interface catalogue des articles avec ajout au panier, recherche, historique
 * et gestion de session (affichage du nom + déconnexion),
 * présentée sous forme de « cards » plutôt que de JTable.
 */
public class ArticleCatalogFrame extends JFrame {

    private final ArticleService   articleService;
    private final CartService      cartService;
    private final CommandeService  commandeService;
    private final ClientService    clientService;

    // Panel qui contiendra les cartes d'articles
    private JPanel                 catalogPanel;
    private JScrollPane            catalogScroll;

    private JTextField             searchField;
    private JButton                searchButton;

    // Liste courante d'articles affichée
    private List<Article>          currentArticles;

    public ArticleCatalogFrame(CartService cartService) {
        this.cartService     = cartService;
        this.articleService  = new ArticleService();
        this.commandeService = new CommandeService();
        this.clientService   = new ClientService();

        setTitle("Catalogue d'Articles");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0,5));

        // ==== Bandeau d'accueil + déconnexion + recherche ====
        JPanel topPanel = new JPanel(new BorderLayout());
        // session
        JPanel sessionPanel = new JPanel(new BorderLayout());
        var client = clientService.findClientById(cartService.getClientId());
        String name = client != null ? client.getNom() : "Invité";
        sessionPanel.add(new JLabel("Bienvenue, " + name), BorderLayout.WEST);
        JButton logoutBtn = new JButton("Se déconnecter");
        logoutBtn.addActionListener((ActionEvent e) -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        sessionPanel.add(logoutBtn, BorderLayout.EAST);
        topPanel.add(sessionPanel, BorderLayout.NORTH);

        // recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Recherche par nom :"));
        searchField  = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> searchArticles());
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ==== Panneau de cartes ====
        catalogPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        catalogPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        catalogScroll = new JScrollPane(
                catalogPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        add(catalogScroll, BorderLayout.CENTER);

        // ==== Boutons bas ====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewCartBtn  = new JButton("Voir le panier");
        viewCartBtn.addActionListener(e -> {
            CartFrame cartFrame = new CartFrame(cartService, this);
            cartFrame.setVisible(true);
        });
        JButton historyBtn   = new JButton("Historique");
        historyBtn.addActionListener(e -> {
            OrderHistoryFrame histo = new OrderHistoryFrame(commandeService, cartService.getClientId());
            histo.setVisible(true);
        });
        bottomPanel.add(viewCartBtn);
        bottomPanel.add(historyBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Chargement initial
        loadAllArticles();
    }

    /** Charge et affiche toutes les cartes d'articles */
    public void loadAllArticles() {
        currentArticles = articleService.getAllArticles();
        catalogPanel.removeAll();
        for (Article art : currentArticles) {
            catalogPanel.add(createCard(art));
        }
        catalogPanel.revalidate();
        catalogPanel.repaint();
    }

    /** Recherche et met à jour le panneau de cartes */
    private void searchArticles() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) {
            loadAllArticles();
        } else {
            var filtered = articleService.searchArticlesByName(kw);
            catalogPanel.removeAll();
            for (Article art : filtered) {
                catalogPanel.add(createCard(art));
            }
            catalogPanel.revalidate();
            catalogPanel.repaint();
        }
    }

    /** Construis une « card » pour un article donné */
    private JPanel createCard(Article art) {
        JPanel card = new JPanel(new BorderLayout(5,5));
        card.setPreferredSize(new Dimension(180, 220));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Titre
        JLabel title = new JLabel(art.getNom(), SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        card.add(title, BorderLayout.NORTH);

        // Description
        JLabel desc = new JLabel(
                "<html><body style='text-align:center'>" + art.getDescription() + "</body></html>",
                SwingConstants.CENTER
        );
        card.add(desc, BorderLayout.CENTER);

        // Pied : prix + bouton
        JPanel footer = new JPanel(new GridLayout(3,1,0,4));
        footer.add(new JLabel("Unitaire : " + art.getPrixUnitaire() + " €", SwingConstants.CENTER));
        footer.add(new JLabel("Gros : "    + art.getPrixGros()     + " €", SwingConstants.CENTER));
        JButton btn = new JButton("Ajouter");
        btn.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(
                    this,
                    "Quantité pour « " + art.getNom() + " » :",
                    "1"
            );
            if (s!=null) {
                try {
                    int q = Integer.parseInt(s);
                    if (cartService.addToCart(art,q))
                        JOptionPane.showMessageDialog(this, q+" ajouté(s) au panier !");
                    else
                        JOptionPane.showMessageDialog(this,
                                "Stock insuffisant", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch(NumberFormatException ex) {
                    // ignore
                }
            }
        });
        footer.add(btn);

        card.add(footer, BorderLayout.SOUTH);
        return card;
    }
}
