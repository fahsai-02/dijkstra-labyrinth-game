import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.List;

public class FightPanel extends JPanel {
    private boolean isPlayerTurn = true;

    private JLabel turnLabel;
    private RunGame parent;
    private char currentNode;

    private CharacterStatus playerStatus;
    private MonsterStatus monster;
    private FightScenePanel fightScene;
    private Image monsterImage;

    public FightPanel(RunGame parent, CharacterStatus status) {
        this.parent = parent;
        this.playerStatus = status;
        setLayout(new BorderLayout());

        fightScene = new FightScenePanel();
        add(fightScene, BorderLayout.CENTER);

        // ====== Top Turn Label ======
        turnLabel = new JLabel("Player's Turn");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 36));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.BLACK);
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setPreferredSize(new Dimension(1920, 60));
        add(turnLabel, BorderLayout.NORTH);

        // ====== LayeredPane for Overlay Buttons ======
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1920, 1020));
        layeredPane.add(fightScene, JLayeredPane.DEFAULT_LAYER);
        fightScene.setBounds(0, 0, 1920, 1020);

        // ====== Image Buttons ======
        JButton normalBtn = createImageButton("assets\\BossFight\\NormalAttack.PNG", e -> {
            if (isPlayerTurn) {
                attackMonster(playerStatus.getAtk());
            }
        });

        JButton hardBtn = createImageButton("assets\\BossFight\\HardAttack.PNG", e -> {
            if (isPlayerTurn && playerStatus.getMp() >= 15) {
                attackMonster(playerStatus.getAtk() + 15);
                playerStatus.loseMP(15);
            } else if (isPlayerTurn) {
                JOptionPane.showMessageDialog(this, "Not enough MP!");
            }
        });

        normalBtn.setBounds(600, 800, 350, 130);
        hardBtn.setBounds(1000, 800, 350, 130);
        layeredPane.add(normalBtn, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(hardBtn, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    private JButton createImageButton(String path, java.awt.event.ActionListener action) {
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(350, 130, Image.SCALE_SMOOTH);
        JButton btn = new JButton(new ImageIcon(scaled));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.addActionListener(action);
        return btn;
    }

    private void attackMonster(int damage) {
        int reduced = Math.max(0, damage - monster.getDef());
        monster.reduceHp(reduced);
        turnLabel.setText("Monster's Turn");
        fightScene.repaint();
        if (checkGameOver()) return;
        isPlayerTurn = false;
        botTurn();
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
                    .getScaledInstance(600, 450, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            monsterImage = null;
        }

        turnLabel.setText("Player's Turn");
        fightScene.repaint();
    }

    private void botTurn() {
        turnLabel.setText("Monster's Turn - Thinking...");
        fightScene.repaint();

        Timer delayTimer = new Timer(1000, e -> {
            playerStatus.damage(monster.getAtk());
            turnLabel.setText(monster.getName() + " attacks!");
            fightScene.repaint();

            if (checkGameOver()) return;

            Timer endBotTurnTimer = new Timer(1000, endEvent -> {
                turnLabel.setText("Player's Turn");
                isPlayerTurn = true;
                fightScene.repaint();
            });
            endBotTurnTimer.setRepeats(false);
            endBotTurnTimer.start();
        });

        delayTimer.setRepeats(false);
        delayTimer.start();
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
                int finalScore = playerStatus.getHp() + playerStatus.getMp();
                parent.showEndGame(finalScore, false);
            }

            return true;
        }
        return false;
    }

    class FightScenePanel extends JPanel {
        private Image playerImage;
        private Image backgroundImage;

        public FightScenePanel() {
            try {
                playerImage = new ImageIcon("assets\\BossFight\\MainCharacter.png").getImage().getScaledInstance(640, 360, Image.SCALE_SMOOTH);
                backgroundImage = new ImageIcon("assets/Monster/BgLvOne.JPG").getImage()
                        .getScaledInstance(1920, 1020, Image.SCALE_SMOOTH);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error loading image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null)
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            if (playerImage != null) g.drawImage(playerImage, 70, 350, this);
            if (monsterImage != null) g.drawImage(monsterImage, 1150, 300, this);

            int barW = 350;
            int pHPBar = (playerStatus.getHp() * barW) / playerStatus.getMaxHp();
            int pMPBar = (playerStatus.getMp() * barW) / playerStatus.getMaxMp();
            int bHPBar = (monster.getHp() * barW) / monster.getMaxHp();

            // Player Status
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.setColor(Color.WHITE);
            g.drawString("Player", 100, 100);
            g.setColor(Color.RED);
            g.fillRect(100, 120, pHPBar, 30);
            g.setColor(Color.BLUE);
            g.fillRect(100, 160, pMPBar, 20);
            g.setColor(Color.WHITE);
            g.drawRect(100, 120, barW, 30);
            g.drawRect(100, 160, barW, 20);
            g.drawString("HP: " + playerStatus.getHp() + " / " + playerStatus.getMaxHp(), 470, 140);
            g.drawString("MP: " + playerStatus.getMp() + " / " + playerStatus.getMaxMp(), 470, 175);
            g.drawString("DEF: " + playerStatus.getDef(), 470, 210);


            // Monster Status
            g.setColor(Color.WHITE);
            g.drawString(monster.getName(), 1200, 100);
            g.setColor(Color.RED);
            g.fillRect(1200, 120, bHPBar, 30);
            g.setColor(Color.WHITE);
            g.drawRect(1200, 120, barW, 30);
            g.drawString("HP: " + monster.getHp() + " / " + monster.getMaxHp(), 1570, 145);
            g.drawString("DEF: " + monster.getDef(), 1570, 185);
        }
    }
}
