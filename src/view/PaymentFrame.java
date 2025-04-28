package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog factice qui simule un traitement de paiement pendant 2 secondes,
 * puis déclenche un callback.
 */
public class PaymentFrame extends JDialog {

    public PaymentFrame(Window owner, Runnable onDone) {
        super(owner, "Paiement en cours…", ModalityType.APPLICATION_MODAL);
        initUI(onDone);
    }

    private void initUI(Runnable onDone) {
        setContentPane(createGradientPanel());
        setLayout(new BorderLayout(10,10));

        JLabel label = new JLabel("Merci de patienter, votre paiement est en cours...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(label, BorderLayout.NORTH);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setBackground(new Color(255, 255, 255, 100)); // légèrement transparent
        add(bar, BorderLayout.CENTER);

        setSize(350, 150);
        setLocationRelativeTo(getOwner());

        // Timer Swing : après 2 secondes, on ferme et on appelle le callback
        new javax.swing.Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((javax.swing.Timer) e.getSource()).stop();
                dispose();
                SwingUtilities.invokeLater(onDone);
            }
        }).start();
    }

    private JPanel createGradientPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(100, 149, 237); // bleu clair
                Color color2 = new Color(186, 85, 211);  // violet doux
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
}
