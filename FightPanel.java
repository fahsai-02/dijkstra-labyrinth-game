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

        turnLabel = new JLabel("Player's Turn");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 30));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.BLACK);
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton normATKbutton = new JButton("Normal Attack");
        JButton hardATKbutton = new JButton("Hard Attack");

        normATKbutton.setPreferredSize(new Dimension(500, 50));
        hardATKbutton.setPreferredSize(new Dimension(500, 50));
        normATKbutton.setFont(new Font("Arial", Font.BOLD, 18));
        hardATKbutton.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        normATKbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hardATKbutton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(turnLabel);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(normATKbutton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(hardATKbutton);

        add(buttonPanel, BorderLayout.SOUTH);

        normATKbutton.addActionListener(e -> {
            if (isPlayerTurn) {
                attackMonster(playerStatus.getAtk());
            }
        });

        hardATKbutton.addActionListener(e -> {
            if (isPlayerTurn && playerStatus.getMp() >= 15) {
                attackMonster(playerStatus.getAtk() + 15);
                playerStatus.loseMP(15);
            } else if (isPlayerTurn) {
                JOptionPane.showMessageDialog(this, "Not enough MP to perform Hard Attack!");
            }
        });
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
                    .getScaledInstance(400, 300, Image.SCALE_SMOOTH);
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
    
                int finalScore = playerStatus.getHp() + playerStatus.getMp();  // หรือคำนวณตามจริง
                parent.showEndGame(finalScore, false);  // <-- แพ้!
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
                playerImage = new ImageIcon("character.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                backgroundImage = new ImageIcon("assets/Monster/BgLvOne.JPG").getImage()
                        .getScaledInstance(1280, 800, Image.SCALE_SMOOTH);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error loading image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null)
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            g.setFont(new Font("Arial", Font.BOLD, 20));

            if (playerImage != null) g.drawImage(playerImage, 100, 400, this);
            if (monsterImage != null) g.drawImage(monsterImage, 700, 300, this);

            int barW = 300;
            int mpBarW = 150;

            int pHPBar = (playerStatus.getHp() * barW) / playerStatus.getMaxHp();
            int pMPBar = (playerStatus.getMp() * mpBarW) / 50;
            int bHPBar = (monster.getHp() * barW) / monster.getMaxHp();

            g.setColor(Color.RED);
            g.fillRect(100, 80, pHPBar, 25);
            g.fillRect(850, 80, bHPBar, 25);

            g.setColor(Color.BLUE);
            g.fillRect(100, 120, pMPBar, 20);

            g.setColor(Color.WHITE);
            g.drawString("Player", 100, 60);
            g.drawString("HP: " + playerStatus.getHp(), 220, 100);
            g.drawString("MP: " + playerStatus.getMp(), 220, 140);

            g.drawString(monster.getName(), 850, 60);
            g.drawString("HP: " + monster.getHp() + " / " + monster.getMaxHp(), 960, 100);
        }
    }
}
