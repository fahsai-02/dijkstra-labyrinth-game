import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BossFightPanel extends JPanel {
    private RunGame parent;
    private CharacterStatus player;
    private BossStatus boss;

    private Image backgroundImage, bossImage, playerImage, bossStatusBg;
    private JLabel turnLabel;
    private boolean isPlayerTurn = true;

    public BossFightPanel(RunGame parent, CharacterStatus player) {
        this.parent = parent;
        this.player = player;
        this.boss = new BossStatus("TUNG TUNG TUNG SAHUR", 1000, 100, 15, 10);

        setLayout(new BorderLayout());

        try {
            backgroundImage = new ImageIcon("assets/BossFight/BgBoss.JPG").getImage();
            bossImage = new ImageIcon("assets/BossFight/BOSS.PNG").getImage();
            playerImage = new ImageIcon("assets/BossFight/StatusPlayer.PNG").getImage();
            bossStatusBg = new ImageIcon("assets/BossFight/StatusBoss.PNG").getImage();
        } catch (Exception e) {
            backgroundImage = bossImage = playerImage = bossStatusBg = null;
        }

        turnLabel = new JLabel("PLAYER'S TURN", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Serif", Font.BOLD, 32));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.BLACK);
        turnLabel.setForeground(Color.WHITE);
        add(turnLabel, BorderLayout.NORTH);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1280, 960));

        BossFightCanvas canvas = new BossFightCanvas();
        canvas.setBounds(0, 0, 1920, 1080);
        layeredPane.add(canvas, JLayeredPane.DEFAULT_LAYER);

        // ปุ่มโจมตีปกติ
        JButton atkButton = new JButton(new ImageIcon("assets/BossFight/NormalAttack.PNG"));
        atkButton.setBounds(700, 750, 500, 170);
        atkButton.setBorder(BorderFactory.createEmptyBorder());
        atkButton.setFocusable(false);
        atkButton.addActionListener(e -> {
            if (isPlayerTurn) {
                boss.takeDamage(player.getAtk());
                isPlayerTurn = false;
                canvas.repaint();
                checkGameEnd(canvas);
                if (!boss.isDefeated()) enemyTurn(canvas);
            }
        });
        layeredPane.add(atkButton, JLayeredPane.PALETTE_LAYER);

        // ปุ่มสกิลหนัก
        JButton hardButton = new JButton(new ImageIcon("assets/BossFight/HardAttack.PNG"));
        hardButton.setBounds(1300, 750, 500, 170);
        hardButton.setBorder(BorderFactory.createEmptyBorder());
        hardButton.setFocusable(false);
        hardButton.addActionListener(e -> {
            if (isPlayerTurn && player.getMp() >= 15) {
                boss.takeDamage(player.getAtk() + 15);
                player.loseMP(15);
                isPlayerTurn = false;
                canvas.repaint();
                checkGameEnd(canvas);
                if (!boss.isDefeated()) enemyTurn(canvas);
            } else if (isPlayerTurn) {
                JOptionPane.showMessageDialog(this, "Not enough MP!");
            }
        });
        layeredPane.add(hardButton, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    private void enemyTurn(BossFightCanvas canvas) {
        Timer t = new Timer(1000, e -> {
            turnLabel.setText("BOSS'S TURN");
            player.damage(boss.getATK());
            canvas.repaint();
            isPlayerTurn = true;
            checkGameEnd(canvas);
            turnLabel.setText("PLAYER'S TURN");
        });
        t.setRepeats(false);
        t.start();
    }

    private void checkGameEnd(BossFightCanvas canvas) {
        if (!player.isAlive()) {
            JOptionPane.showMessageDialog(this, "You were defeated by the BOSS!");
            parent.showEndGame(0, false);
        } else if (boss.isDefeated()) {
            JOptionPane.showMessageDialog(this, "You defeated the BOSS!");
            parent.showEndGame(player.getHp() + player.getMp(), true);
        }
    }

    class BossFightCanvas extends JPanel {
        public BossFightCanvas() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) g.drawImage(backgroundImage, 0, 0, 1920, 1080, this);
            if (bossImage != null) g.drawImage(bossImage, 300, 0, 1280, 700, this);
            if (playerImage != null) g.drawImage(playerImage, 0, 0, 1920, 1000, this);
            if (bossStatusBg != null) g.drawImage(bossStatusBg, 150, 150, 400, 300, this);

            int barMaxW = 300;

            // Clamp ให้ไม่ล้น
            int bossHpBar = (int)(Math.min(boss.getHp(), boss.getMaxHp()) / (double)boss.getMaxHp() * barMaxW);
            int bossMpBar = (int)(Math.min(boss.getMp(), boss.getMaxMp()) / (double)boss.getMaxMp() * barMaxW);
            int playerHpBar = (int)(Math.min(player.getHp(), player.getMaxHp()) / (double)player.getMaxHp() * barMaxW);
            int playerMpBar = (int)(Math.min(player.getMp(), player.getMaxMp()) / (double)player.getMaxMp() * barMaxW);

            // Boss Status
            g.setFont(new Font("Serif", Font.BOLD, 36));
            g.setColor(Color.WHITE);
            g.drawString("BOSS", 300, 210);
            g.setFont(new Font("Serif", Font.BOLD, 24));
            g.drawString(boss.getName(), 200, 250);

            g.setColor(Color.GREEN);
            g.fillRect(200, 300, bossHpBar, 50);
            g.setColor(Color.BLACK);
            g.drawRect(200, 300, barMaxW, 50);
            g.drawString("HP " + boss.getHp(), 200, 330);

            g.setColor(Color.RED);
            g.fillRect(200, 370, bossMpBar, 50);
            g.setColor(Color.BLACK);
            g.drawRect(200, 370, barMaxW, 50);
            g.drawString("MP " + boss.getMp(), 200, 400);

            // Player Status
            g.setFont(new Font("Serif", Font.BOLD, 24));
            g.setColor(Color.BLACK);
            g.drawString("Player", 150, 720);

            g.setColor(Color.GREEN);
            g.fillRect(380, 730, playerHpBar, 50);
            g.setColor(Color.BLACK);
            g.drawRect(380, 730, barMaxW, 50);
            g.drawString("HP " + player.getHp(), 380, 765);

            g.setColor(Color.RED);
            g.fillRect(380, 790, playerMpBar, 50);
            g.setColor(Color.BLACK);
            g.drawRect(380, 790, barMaxW, 50);
            g.drawString("MP " + player.getMp(), 380, 825);
        }
    }
}
