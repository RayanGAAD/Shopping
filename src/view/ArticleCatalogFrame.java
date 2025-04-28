package view;

import model.Article;
import service.ArticleService;
import service.CartService;
import service.ClientService;
import service.CommandeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.util.List;

public class ArticleCatalogFrame extends JFrame {

    private final ArticleService articleService;
    private final CartService cartService;
    private final CommandeService commandeService;
    private final ClientService clientService;

    private JPanel catalogPanel;
    private JScrollPane catalogScroll;
    private JTextField searchField;
    private JButton searchButton;
    private List<Article> currentArticles;

    public ArticleCatalogFrame(CartService cartService) {
        this.cartService = cartService;
        this.articleService = new ArticleService();
        this.commandeService = new CommandeService();
        this.clientService = new ClientService();

        setTitle("Catalogue d'Articles");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 5));

        // ==== Bandeau d'accueil + déconnexion + recherche ====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(224, 255, 255)); // Bleu clair

        JPanel sessionPanel = new JPanel(new BorderLayout());
        sessionPanel.setBackground(new Color(224, 255, 255));
        var client = clientService.findClientById(cartService.getClientId());
        String name = client != null ? client.getNom() : "Invité";
        JLabel welcomeLabel = new JLabel("Bienvenue, " + name);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sessionPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Se déconnecter");
        logoutBtn.setBackground(new Color(255, 150, 150)); // Rouge clair
        logoutBtn.setForeground(Color.BLACK);              // Texte noir
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.addActionListener((ActionEvent e) -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        sessionPanel.add(logoutBtn, BorderLayout.EAST);
        topPanel.add(sessionPanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(224, 255, 255));
        searchPanel.add(new JLabel("Recherche par nom :"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Rechercher");
        searchButton.setBackground(new Color(173, 216, 230)); // Bleu clair
        searchButton.setForeground(Color.BLACK);              // Texte noir
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> searchArticles());
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ==== Panneau de cartes ====
        catalogPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        catalogPanel.setBackground(new Color(255, 239, 213)); // Fond pêche clair
        catalogScroll = new JScrollPane(
                catalogPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        catalogScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(catalogScroll, BorderLayout.CENTER);

        // ==== Boutons bas ====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(224, 255, 255));
        JButton viewCartBtn = new JButton("Voir le panier");
        viewCartBtn.setBackground(new Color(144, 238, 144)); // Vert clair
        viewCartBtn.setForeground(Color.BLACK);              // Texte noir
        viewCartBtn.setFocusPainted(false);

        JButton historyBtn = new JButton("Historique");
        historyBtn.setBackground(new Color(255, 228, 181)); // Orange clair
        historyBtn.setForeground(Color.BLACK);              // Texte noir
        historyBtn.setFocusPainted(false);

        viewCartBtn.addActionListener(e -> {
            CartFrame cartFrame = new CartFrame(cartService, this);
            cartFrame.setVisible(true);
        });
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

    public void loadAllArticles() {
        currentArticles = articleService.getAllArticles();
        catalogPanel.removeAll();
        for (Article art : currentArticles) {
            catalogPanel.add(createCard(art));
        }
        catalogPanel.revalidate();
        catalogPanel.repaint();
    }

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

    private JPanel createCard(Article art) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(255, 165, 0), 2, true));
        card.setPreferredSize(new Dimension(180, 260));

        // Hover effet
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(255, 250, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        // Image
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            if (art.getImagePath() != null && !art.getImagePath().isEmpty()) {
                java.net.URL imgURL = getClass().getClassLoader().getResource(art.getImagePath());
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(img));
                } else {
                    imgLabel.setText("Pas d'image");
                }
            } else {
                imgLabel.setText("Pas d'image");
            }
        } catch (Exception e) {
            imgLabel.setText("Pas d'image");
        }

        card.add(imgLabel, BorderLayout.NORTH);

        // Titre
        JLabel title = new JLabel(art.getNom(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(255, 140, 0));
        card.add(title, BorderLayout.CENTER);

        // Pied : prix + bouton
        JPanel footer = new JPanel(new GridLayout(3, 1, 0, 4));
        footer.setOpaque(false);
        footer.add(new JLabel("Unitaire : " + art.getPrixUnitaire() + " €", SwingConstants.CENTER));
        footer.add(new JLabel("Gros : " + art.getPrixGros() + " €", SwingConstants.CENTER));

        JButton btn = new JButton("Ajouter");
        btn.setBackground(new Color(173, 216, 230)); // Bleu clair
        btn.setForeground(Color.BLACK);              // Texte noir
        btn.setFocusPainted(false);
        btn.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(
                    this,
                    "Quantité pour « " + art.getNom() + " » :",
                    "1"
            );
            if (s != null) {
                try {
                    int q = Integer.parseInt(s);
                    if (cartService.addToCart(art, q))
                        JOptionPane.showMessageDialog(this, q + " ajouté(s) au panier !");
                    else
                        JOptionPane.showMessageDialog(this, "Stock insuffisant", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    // Ignore
                }
            }
        });

        footer.add(btn);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }
}
