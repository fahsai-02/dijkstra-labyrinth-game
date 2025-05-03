import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class FightPanel extends JPanel {
    private int botHP = 100;
    private boolean isPlayerTurn = true;

    private JLabel turnLabel;
    private RunGame parent;
    private GamePanel gamePanel;
    private char currentNode;

    private CharacterStatus playerStatus;

    public FightPanel(RunGame parent, CharacterStatus status) {
        this.parent = parent;
        this.playerStatus = status;
        setLayout(new BorderLayout());

        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

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
                botHP -= 100;
                turnLabel.setText("Bot's Turn");
                gamePanel.repaint();
                if (checkGameOver()) return;
                isPlayerTurn = false;
                botTurn();
            }
        });

        hardATKbutton.addActionListener(e -> {
            if (isPlayerTurn && playerStatus.getMp() >= 15) {
                botHP -= 20;
                playerStatus.loseMP(15);
                turnLabel.setText("Bot's Turn");
                gamePanel.repaint();
                if (checkGameOver()) return;
                isPlayerTurn = false;
                botTurn();
            } else if (isPlayerTurn) {
                JOptionPane.showMessageDialog(this, "Not enough MP to perform Hard Attack!");
            }
        });
    }

    public void startFight(char node) {
        this.currentNode = node;
        isPlayerTurn = true;
        botHP = 100; 
        turnLabel.setText("Player's Turn");
        gamePanel.repaint();
    }

    private void botTurn() {
        turnLabel.setText("Bot's Turn - Thinking...");
        gamePanel.repaint();

        Timer delayTimer = new Timer(1000, e -> {
            String actionAnnounce;
            if (new Random().nextBoolean()) {
                playerStatus.damage(20);
                actionAnnounce = "Bot chose Hard Attack!";
            } else {
                playerStatus.damage(10);
                actionAnnounce = "Bot chose Normal Attack!";
            }

            turnLabel.setText(actionAnnounce);
            gamePanel.repaint();

            if (checkGameOver()) return;

            Timer endBotTurnTimer = new Timer(1000, endEvent -> {
                turnLabel.setText("Player's Turn");
                isPlayerTurn = true;
                gamePanel.repaint();
            });
            endBotTurnTimer.setRepeats(false);
            endBotTurnTimer.start();
        });

        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private boolean checkGameOver() {
        if (playerStatus.getHp() <= 0 || botHP <= 0) {
            botHP = Math.max(0, botHP); // กันติดลบ
            boolean isPlayerWin = playerStatus.getHp() > 0;
            String winner = isPlayerWin ? "Player Wins!" : "Bot Wins!";
            JOptionPane.showMessageDialog(this, winner);
    
            int finalScore = playerStatus.getHp() + playerStatus.getMp();
    
            if (isPlayerWin) {
                if (parent.getCurrentMap() instanceof Map3Panel boss) {
                    boss.markDefeated(currentNode);
                    if (boss.graph.get(currentNode).typeNode == 'E') {
                        parent.showEndGame(finalScore, true); //ชนะบอส
                        return true;
                    }
                } else if (parent.getCurrentMap() instanceof Map2Panel map2) {
                    map2.markDefeated(currentNode);
                }
                parent.showMap(); // กลับไปยังแมพหลังจากสู้เสร็จ
            } else {
                parent.showEndGame(finalScore, false); //แพ้บอส
            }
    
            return true;
        }
        return false;
    }
    
    
    class GamePanel extends JPanel {
        private Image playerImage;
        private Image botImage;

        public GamePanel() {
            try {
                playerImage = new ImageIcon("character.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                botImage = new ImageIcon("tungtungsahur.png").getImage().getScaledInstance(640, 360, Image.SCALE_SMOOTH);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error loading image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Font font = new Font("Arial", Font.BOLD, 20);
            g.setFont(font);

            if (playerImage != null) g.drawImage(playerImage, 30, 30, this);
            if (botImage != null) g.drawImage(botImage, 635, 300, this);

            int maxHP = 100, maxMP = 50, barW = 300, mpBarW = 150;

            int pHPBar = (playerStatus.getHp() * barW) / maxHP;
            int pMPBar = (playerStatus.getMp() * mpBarW) / maxMP;
            int bHPBar = (botHP * barW) / maxHP;

            g.setColor(Color.RED);
            g.fillRect(250, 70, pHPBar, 30);
            g.fillRect(780, 700, bHPBar, 30);

            g.setColor(Color.BLUE);
            g.fillRect(250, 120, pMPBar, 30);

            g.setColor(Color.WHITE);
            g.drawString("Player", 250, 50);
            g.drawString("Bot", 940, 300);
            g.drawString("HP: " + playerStatus.getHp(), 250 + 120, 90);
            g.drawString("MP: " + playerStatus.getMp(), 250 + 120, 140);
            g.drawString("HP: " + botHP, 780 + 120, 720);

        }
    }
}