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
    /**
     * @param owner    la fenêtre parente
     * @param onDone   callback à exécuter une fois le « paiement » terminé
     */
    public PaymentFrame(Window owner, Runnable onDone) {
        super(owner, "Paiement en cours…", ModalityType.APPLICATION_MODAL);
        initUI(onDone);
    }

    private void initUI(Runnable onDone) {
        setLayout(new BorderLayout(10,10));
        add(new JLabel("Merci de patienter, votre paiement est en cours...", SwingConstants.CENTER), BorderLayout.NORTH);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        add(bar, BorderLayout.CENTER);

        setSize(300, 120);
        setLocationRelativeTo(getOwner());

        // Timer Swing : après 2 secondes, on ferme et on appelle le callback
        new javax.swing.Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((javax.swing.Timer)e.getSource()).stop();
                dispose();
                // une fois le dialog fermé, on exécute le callback
                SwingUtilities.invokeLater(onDone);
            }
        }).start();
    }
}
