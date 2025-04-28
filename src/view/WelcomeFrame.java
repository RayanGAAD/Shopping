package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Bienvenue - ShoppingApp");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal avec fond dégradé
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dégradé vertical bleu -> vert menthe
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(173, 216, 230); // Bleu clair
                Color color2 = new Color(152, 251, 152); // Vert menthe plus vif
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Titre
        JLabel welcomeLabel = new JLabel("Bienvenue sur ShoppingApp", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(new Color(40, 40, 40)); // Gris foncé

        // Slogan
        JLabel sloganLabel = new JLabel("Trouvez tout ce dont vous rêvez", SwingConstants.CENTER);
        sloganLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sloganLabel.setForeground(new Color(80, 80, 80)); // Gris moyen

        // Bouton commencer
        JButton startButton = new JButton("Commencer");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(0, 123, 255)); // Bleu pétant
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(160, 45));
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // ferme la WelcomeFrame
                new LoginFrame().setVisible(true); // ouvre LoginFrame
            }
        });

        // Espacements
        panel.add(Box.createVerticalStrut(40));
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(sloganLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(startButton);

        add(panel);
    }
}
