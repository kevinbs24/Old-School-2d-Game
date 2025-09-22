package zelda_like;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Mini Zelda-Like");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);

            GamePanel gp = new GamePanel();
            window.add(gp);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            gp.startGameThread();
        });
    }
}
