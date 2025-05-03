import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BossFightPanel extends JPanel {
    private int bossHP = 300;
    private boolean isPlayerTurn = true;

    private JLabel statusLabel;
    private RunGame parent;
    private Image playerImage, bossImage;
    private CharacterStatus playerStatus;

    public BossFightPanel(RunGame parent, CharacterStatus status) {
        this.parent = parent;
        this.playerStatus = status;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // Heal ก่อนเริ่ม
        playerStatus.healFull();

        statusLabel = new JLabel("Prepare to Fight the Demon King!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 30));
        statusLabel.setForeground(Color.ORANGE);
        add(statusLabel, BorderLayout.NORTH);

        try {
            playerImage = new ImageIcon("PicsTemp/Character.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            bossImage = new ImageIcon("PicsTemp/Boss.png").getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image not found.");
        }

        JButton attackBtn = new JButton("Attack");
        attackBtn.setFont(new Font("Arial", Font.BOLD, 22));
        attackBtn.addActionListener(e -> playerAttack());

        JPanel centerPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (playerImage != null) g.drawImage(playerImage, 100, 250, this);
                if (bossImage != null) g.drawImage(bossImage, 600, 100, this);

                g.setColor(Color.RED);
                g.fillRect(600, 50, bossHP, 20);
                g.setColor(Color.GREEN);
                g.fillRect(100, 220, playerStatus.getHp() * 2, 20);
            }
        };

        add(centerPanel, BorderLayout.CENTER);
        add(attackBtn, BorderLayout.SOUTH);
    }

    private void playerAttack() {
        if (!isPlayerTurn) return;

        bossHP -= 30;
        statusLabel.setText("You hit the boss!");
        repaint();

        if (bossHP <= 0) {
            JOptionPane.showMessageDialog(this, "You defeated the Boss!");
            int finalScore = playerStatus.getHp() + playerStatus.getMp();
            parent.showEndGame(finalScore, true); // ✅ เพิ่ม isWin = true
            return;
        }

        isPlayerTurn = false;
        bossAttack();
    }

    private void bossAttack() {
        Timer timer = new Timer(1000, e -> {
            int dmg = new Random().nextInt(20) + 10;
            playerStatus.damage(dmg);
            statusLabel.setText("The Boss strikes for " + dmg + " damage!");

            if (playerStatus.getHp() <= 0) {
                JOptionPane.showMessageDialog(this, "You were defeated by the Boss.");
                int finalScore = playerStatus.getHp() + playerStatus.getMp();
                parent.showEndGame(finalScore, false); // ✅ เพิ่ม isWin = false
                return;
            }

            isPlayerTurn = true;
            repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
