import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class FightPanel extends JPanel {
    private RunGame parent;
    private CharacterStatus playerStatus;
    private MonsterStatus monster;
    private Image bgImage, monsterImage;
    private Image normalBtnImg, hardBtnImg;

    private char currentNode;
    private boolean isPlayerTurn = true;

    private JLabel turnLabel;
    private JLayeredPane layeredPane;
    private FightCanvas canvas;

    private Rectangle normalBtnBounds, hardBtnBounds;

    public FightPanel(RunGame parent, CharacterStatus status) {
        this.parent = parent;
        this.playerStatus = status;

        try {
            bgImage = new ImageIcon("assets/Monster/BgBattle.JPG").getImage();
            normalBtnImg = new ImageIcon("assets/Monster/buttons/NormalAttack.PNG").getImage()
                    .getScaledInstance(180, 80, Image.SCALE_SMOOTH);
            hardBtnImg = new ImageIcon("assets/Monster/buttons/HardAttack.PNG").getImage()
                    .getScaledInstance(180, 80, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading images: " + e.getMessage());
        }

        // === Turn Label ===
        turnLabel = new JLabel("Player's Turn");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 36));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.BLACK);
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(turnLabel, BorderLayout.NORTH);

        // === Main Canvas ===
        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        canvas = new FightCanvas();
        canvas.setBounds(0, 0, 1920, 1020);
        layeredPane.add(canvas, JLayeredPane.DEFAULT_LAYER);

        // === Resize support ===
        layeredPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                canvas.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                canvas.repaint();
            }
        });

        // === Mouse click for buttons ===
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (normalBtnBounds != null && normalBtnBounds.contains(p)) {
                    if (isPlayerTurn) attackMonster(playerStatus.getAtk());
                } else if (hardBtnBounds != null && hardBtnBounds.contains(p)) {
                    if (isPlayerTurn && playerStatus.getMp() >= 15) {
                        attackMonster(playerStatus.getAtk() + 15);
                        playerStatus.loseMP(15);
                    } else {
                        JOptionPane.showMessageDialog(FightPanel.this, "Not enough MP!");
                    }
                }
            }
        });
    }

    public void startFight(char node) {
        this.currentNode = node;
        isPlayerTurn = true;

        int stageLevel = parent.getCurrentStage();
        List<String> names = MonsterStatus.getMonsterNamesForStage(stageLevel);
        String randomName = names.get(new Random().nextInt(names.size()));
        monster = MonsterStatus.getMonster(randomName, stageLevel);

        try {
            monsterImage = new ImageIcon(monster.getImagePath()).getImage()
                    .getScaledInstance(500, 350, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            monsterImage = null;
        }

        turnLabel.setText("Player's Turn");
        canvas.repaint();
    }

    private void attackMonster(int damage) {
        int reduced = Math.max(0, damage - monster.getDef());
        monster.reduceHp(reduced);
        turnLabel.setText("Monster's Turn");
        canvas.repaint();

        if (checkGameOver()) return;
        isPlayerTurn = false;
        botTurn();
    }

    private void botTurn() {
        turnLabel.setText("Monster's Turn - Thinking...");
        canvas.repaint();

        new Timer(1000, _ -> {
            playerStatus.damage(monster.getAtk());
            turnLabel.setText(monster.getName() + " attacks!");
            canvas.repaint();

            if (!checkGameOver()) {
                new Timer(1000, _ -> {
                    turnLabel.setText("Player's Turn");
                    isPlayerTurn = true;
                    canvas.repaint();
                }).start();
            }
        }).start();
    }

    private boolean checkGameOver() {
        if (!playerStatus.isAlive() || monster.getHp() <= 0) {
            String msg;
            if (playerStatus.isAlive()) {
                msg = "You defeated " + monster.getName() + "!\nGold +" + monster.getRewardGold();
                playerStatus.gainGold(monster.getRewardGold());
                parent.getMapPanel().markDefeated(currentNode);
                JOptionPane.showMessageDialog(this, msg);
                parent.showMap();
            } else {
                msg = "You were defeated by " + monster.getName();
                JOptionPane.showMessageDialog(this, msg);
                parent.showEndGame(playerStatus.getHp() + playerStatus.getMp(), false);
            }
            return true;
        }
        return false;
    }

    // === Canvas Drawing ===
    class FightCanvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth();
            int h = getHeight();

            // Background
            if (bgImage != null) g.drawImage(bgImage, 0, 0, w, h, this);

            // Monster Info
            if (monster != null) {
                g.setFont(new Font("Arial", Font.BOLD, 26));
                g.setColor(Color.WHITE);
                g.drawString(monster.getName(), 400, 500);

                int barW = 200;
                int mHPBar = (monster.getHp() * barW) / monster.getMaxHp();
                g.setColor(new Color(0, 128, 0));
                g.fillRect(100, 90, mHPBar, 25);
                g.setColor(Color.BLACK);
                g.drawRect(100, 90, barW, 25);
                g.setColor(Color.WHITE);
                g.drawString("HP: " + monster.getHp(), 100 + barW + 10, 110);
            }

            // Player HP/MP Bars
            int barX = 250, barY = h - 120;
            g.setColor(new Color(0, 160, 100));
            g.fillRect(barX, barY, playerStatus.getHp() * 100 / playerStatus.getMaxHp(), 20);
            g.setColor(Color.BLACK);
            g.drawRect(barX, barY, 100, 20);
            g.drawString("HP: " + playerStatus.getHp(), barX + 110, barY + 15);

            g.setColor(Color.RED);
            g.fillRect(barX, barY + 30, playerStatus.getMp() * 100 / playerStatus.getMaxMp(), 20);
            g.setColor(Color.BLACK);
            g.drawRect(barX, barY + 30, 100, 20);
            g.drawString("MP: " + playerStatus.getMp(), barX + 110, barY + 45);

            // Monster Image
            if (monsterImage != null) g.drawImage(monsterImage, w / 2, h / 3, this);

            // Buttons

            int normalX = w / 2 - 200;
            int hardX = w / 2 + 20;

            normalBtnBounds = new Rectangle(normalX, getHeight()-150, 180, 80);
            hardBtnBounds = new Rectangle(hardX, getHeight()-150, 180, 80);

            if (normalBtnImg != null) g.drawImage(normalBtnImg, normalX, getHeight()-150, this);
            if (hardBtnImg != null) g.drawImage(hardBtnImg, hardX, getHeight()-150, this);
        }
    }
}
