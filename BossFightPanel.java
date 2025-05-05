import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BossFightPanel extends JPanel {
    private RunGame parent;
    private CharacterStatus player;
    private BossStatus boss;

    private Image bgImage, bossImage, playerUI, bossUI;
    private JLabel turnLabel;
    private boolean isPlayerTurn = true;

    private BossFightCanvas canvas;

    public BossFightPanel(RunGame parent, CharacterStatus player) {
        this.parent = parent;
        this.player = player;
        this.gameEnded = false;
        this.boss = new BossStatus("TUNG TUNG TUNG SAHUR", 1000, 100, 30, 10);

        setLayout(new BorderLayout());

        // === Load images ===
        try {
            bgImage = new ImageIcon("assets/BossFight/BgBoss.JPG").getImage();
            bossImage = new ImageIcon("assets/BossFight/BOSS.PNG").getImage();
            playerUI = new ImageIcon("assets/BossFight/StatusPlayer.PNG").getImage();
            bossUI = new ImageIcon("assets/BossFight/StatusBoss.PNG").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading boss fight images.");
        }

        // === Turn Label ===
        turnLabel = new JLabel("PLAYER'S TURN", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Serif", Font.BOLD, 32));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.BLACK);
        turnLabel.setForeground(Color.WHITE);
        add(turnLabel, BorderLayout.NORTH);

        // === LayeredPane ===
        JLayeredPane layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        canvas = new BossFightCanvas();
        canvas.setBounds(0, 0, 1920, 1080); // Will be resized
        layeredPane.add(canvas, JLayeredPane.DEFAULT_LAYER);

        layeredPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                canvas.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                canvas.repaint();
            }
        });

        // === Buttons ===
        JButton atkButton = createButton("assets/BossFight/NormalAttack.PNG", e -> {
            if (isPlayerTurn) {
                boss.takeDamage(player.getAtk());
                isPlayerTurn = false;
                canvas.repaint();
                checkGameEnd();
                if (!boss.isDefeated()) enemyTurn();
            }
        });

        JButton hardButton = createButton("assets/BossFight/HardAttack.PNG", e -> {
            if (isPlayerTurn && player.getMp() >= 15) {
                boss.takeDamage(player.getAtk() + 15);
                player.loseMP(15);
                isPlayerTurn = false;
                canvas.repaint();
                checkGameEnd();
                if (!boss.isDefeated()) enemyTurn();
            } else if (isPlayerTurn) {
                JOptionPane.showMessageDialog(this, "Not enough MP!");
            }
        });

        atkButton.setBounds(600, 750, 300, 80);
        hardButton.setBounds(950, 750, 300, 80);

        layeredPane.add(atkButton, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(hardButton, JLayeredPane.PALETTE_LAYER);
    }

    private JButton createButton(String path, ActionListener action) {
        ImageIcon raw = new ImageIcon(path);
        Image scaled = raw.getImage().getScaledInstance(300, 80, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaled));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.addActionListener(action);
        return button;
    }

    private void enemyTurn() {
        Timer t = new Timer(1000, e -> {
            turnLabel.setText("BOSS'S TURN");
            player.damage(boss.getATK());
            canvas.repaint();
            isPlayerTurn = true;
            checkGameEnd();
            turnLabel.setText("PLAYER'S TURN");
        });
        t.setRepeats(false);
        t.start();
    }

    private boolean gameEnded = false;
    private void checkGameEnd() {
        if (gameEnded) return;
    
        if (!player.isAlive()) {
            gameEnded = true;
            JOptionPane.showMessageDialog(this, "You were defeated by the BOSS!");
            parent.showEndGame(0, false);
        } else if (boss.isDefeated()) {
            gameEnded = true;
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
            int w = getWidth(), h = getHeight();

            if (bgImage != null) g.drawImage(bgImage, 0, 0, w, h, this);
            if (bossImage != null) g.drawImage(bossImage, w / 2 - 300, 0, 600, 400, this);
            if (playerUI != null) g.drawImage(playerUI, 0, h - 250, w, 250, this);
            if (bossUI != null) g.drawImage(bossUI, 50, 50, 400, 200, this);

            int barMaxW = 300;

            // BOSS Status
            int bossHpBar = (int)(Math.min(boss.getHp(), boss.getMaxHp()) * barMaxW / (double)boss.getMaxHp());

            g.setFont(new Font("Serif", Font.BOLD, 28));
            g.setColor(Color.WHITE);
            g.drawString("BOSS", 200, 90);
            g.setFont(new Font("Serif", Font.PLAIN, 22));
            g.drawString(boss.getName(), 160, 120);

            g.setColor(Color.GREEN);
            g.fillRect(150, 140, bossHpBar, 30);
            g.setColor(Color.BLACK);
            g.drawRect(150, 140, barMaxW, 30);
            g.drawString("HP: " + boss.getHp(), 150, 170);
            g.setColor(Color.WHITE);
            g.drawString("DEF: " + boss.getDEF(), 150, 200);

            // PLAYER Status
            int playerHpBar = (int)(Math.min(player.getHp(), player.getMaxHp()) * barMaxW / (double)player.getMaxHp());
            int playerMpBar = (int)(Math.min(player.getMp(), player.getMaxMp()) * barMaxW / (double)player.getMaxMp());

            g.setFont(new Font("Serif", Font.BOLD, 24));
            g.setColor(Color.BLACK);
            g.drawString("Player", 150, h - 180);

            g.setColor(Color.GREEN);
            g.fillRect(300, h - 180, playerHpBar, 30);
            g.setColor(Color.BLACK);
            g.drawRect(300, h - 180, barMaxW, 30);
            g.drawString("HP: " + player.getHp(), 300, h - 150);

            g.setColor(Color.RED);
            g.fillRect(300, h - 120, playerMpBar, 30);
            g.setColor(Color.BLACK);
            g.drawRect(300, h - 120, barMaxW, 30);
            g.drawString("MP: " + player.getMp(), 300, h - 90);
        }
    }
}
