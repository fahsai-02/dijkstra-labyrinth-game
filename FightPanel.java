import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class FightPanel extends JPanel {
    private RunGame parent;
    private CharacterStatus playerStatus;  // สถานะผู้เล่น
    private MonsterStatus monster;          // มอนสเตอร์ที่เจอในแต่ละฉาก
    private Image bgImage, monsterImage, normalBtnImg, hardBtnImg;

    private char currentNode;  // โหนดปัจจุบันในแผนที่ (เอาไว้ทำเครื่องหมายว่าเคยชนะ)
    private boolean isPlayerTurn = true;

    private JLabel turnLabel;   // แสดงว่าใครถึงตาเล่น
    private JLayeredPane layeredPane;
    private FightCanvas canvas;

    private Rectangle normalBtnBounds, hardBtnBounds; // พื้นที่ปุ่มคลิกได้

    public FightPanel(RunGame parent, CharacterStatus status) {
        this.parent = parent;
        this.playerStatus = status;

        // โหลดภาพพื้นหลังและปุ่มโจมตี
        try {
            bgImage = new ImageIcon("assets/Monster/BgBattle.JPG").getImage();
            normalBtnImg = new ImageIcon("assets/Monster/buttons/NormalAttack.PNG").getImage();
            hardBtnImg = new ImageIcon("assets/Monster/buttons/HardAttack.PNG").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading image assets.");
        }

        // Label บอกตาผู้เล่น
        turnLabel = new JLabel("Player's Turn");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 36));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.BLACK);
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(turnLabel, BorderLayout.NORTH);

        // layeredPane สำหรับวาด canvas
        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        canvas = new FightCanvas();
        canvas.setBounds(0, 0, 1920, 1020);
        layeredPane.add(canvas, JLayeredPane.DEFAULT_LAYER);

        // ปรับขนาด canvas ตามขนาด panel
        layeredPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                canvas.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                canvas.repaint();
            }
        });

        // ตรวจจับการคลิกปุ่มโจมตีทั้งสองแบบ
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
                        JOptionPane.showMessageDialog(FightPanel.this, "You are attacking too fast!");
                    }
                }
            }
        });
    }

    // เริ่มการต่อสู้ในโหนดนี้
    public void startFight(char node) {
        this.currentNode = node;
        isPlayerTurn = true;
        this.gameEnded = false;

        // ดึงข้อมูลมอนสเตอร์ของเลเวลนี้มาแบบสุ่ม
        int stageLevel = parent.getCurrentStage();
        List<String> names = MonsterStatus.getMonsterNamesForStage(stageLevel);
        String randomName = names.get(new Random().nextInt(names.size()));
        monster = MonsterStatus.getMonster(randomName, stageLevel);

        try {
            // โหลดรูปมอนสเตอร์มาแสดง
            Image raw = new ImageIcon(monster.getImagePath()).getImage();
            monsterImage = raw.getScaledInstance(600, 420, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            monsterImage = null;
        }

        turnLabel.setText("Player's Turn");
        canvas.repaint();
    }

    // ผู้เล่นโจมตีมอนสเตอร์
    private void attackMonster(int damage) {
        // ลดพลังโจมตีตามค่า DEF ของมอนสเตอร์
        int reduced = Math.max(0, damage - monster.getDef());
        monster.reduceHp(reduced);
        turnLabel.setText("Monster's Turn");
        canvas.repaint();

        // ถ้าเกมจบให้หยุด
        if (checkGameOver()) return;

        isPlayerTurn = false;  // สลับตาไปให้มอนสเตอร์
        botTurn();
    }

    // มอนสเตอร์ตอบโต้
    private void botTurn() {
        turnLabel.setText("Monster's Turn - Thinking...");
        canvas.repaint();
    
        // หน่วงเวลา 1 วิ ก่อนมอนสเตอร์โจมตี
        Timer attackTimer = new Timer(1000, e -> {
            playerStatus.damage(monster.getAtk());
            turnLabel.setText(monster.getName() + " attacks!");
            canvas.repaint();
    
            if (!checkGameOver()) {
                // อีก 1 วิหลังโจมตีเสร็จ กลับเป็นตาผู้เล่น
                Timer endTurnTimer = new Timer(1000, k -> {
                    turnLabel.setText("Player's Turn");
                    isPlayerTurn = true;
                    canvas.repaint();
                });
                endTurnTimer.setRepeats(false);
                endTurnTimer.start();
            }
        });
    
        attackTimer.setRepeats(false); 
        attackTimer.start();
    }    

    private boolean gameEnded = false;
    private boolean checkGameOver() {
        if (gameEnded) return true; 

        // เช็คว่าผู้เล่นตาย หรือมอนสเตอร์ตาย
        if (!playerStatus.isAlive() || monster.getHp() <= 0) {
            gameEnded = true; 
    
            String msg;
            if (playerStatus.isAlive()) {
                // ชนะ ได้ทอง, อัปเดตแผนที่ว่าเคยชนะแล้ว
                msg = "You defeated " + monster.getName() + "!\nGold +" + monster.getRewardGold();
                playerStatus.gainGold(monster.getRewardGold());
                parent.getMapPanel().markDefeated(currentNode);
                JOptionPane.showMessageDialog(this, msg);
                parent.showMap();
            } else {
                // แพ้
                msg = "You were defeated by " + monster.getName();
                JOptionPane.showMessageDialog(this, msg);
                parent.showEndGame(playerStatus.getHp() + playerStatus.getMp(), false);
            }
            return true;
        }
        return false;
    }

    // ===================== Inner Drawing Canvas ======================
    class FightCanvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth(), h = getHeight();

            // กำหนดสี HP/MP
            Color hpMonster = new Color(150, 28, 28);
            Color hpColor = new Color(55, 125,75);
            Color mpColor = new Color(54,124,171);

            if (bgImage != null) g.drawImage(bgImage, 0, 0, w, h, this);

            // แสดงชื่อและ HP มอนสเตอร์
            if (monster != null) {
                g.setFont(new Font("Arial", Font.BOLD, 26));
                g.setColor(Color.WHITE);
                g.drawString(monster.getName(), 275, 250);

                int barW = 200;
                int mHPBar = (monster.getHp() * barW) / monster.getMaxHp();
                g.setColor(hpMonster);
                g.fillRect(275, 280, mHPBar, 25);
                g.setColor(Color.BLACK);
                g.drawRect(275, 280, barW, 25);
                g.setColor(Color.WHITE);
                g.drawString("HP: " + monster.getHp(), 275 + barW + 10, 300);
            }

            // วาด HP/MP ของผู้เล่น
            int barX = 250, barY = h - 145;
            g.setColor(hpColor);
            g.fillRect(barX, barY, playerStatus.getHp() * 200 / playerStatus.getMaxHp(), 30);
            g.setColor(Color.BLACK);
            g.drawRect(barX, barY, 200, 30);
            g.drawString("HP: " + playerStatus.getHp(), barX + 210, barY + 20);

            g.setColor(mpColor);
            g.fillRect(barX, barY + 40, playerStatus.getMp() * 200 / playerStatus.getMaxMp(), 30);
            g.setColor(Color.BLACK);
            g.drawRect(barX, barY + 40, 200, 30);
            g.drawString("MP: " + playerStatus.getMp(), barX + 210, barY + 65);

            // วาดรูปมอนสเตอร์
            if (monsterImage != null)
                g.drawImage(monsterImage, w / 2-70 , 50, this);

            // วาดปุ่มโจมตีทั้งสองปุ่ม
            int normalX = w / 2 +50;
            int hardX = normalX+270;
            int btnY = h - 180;

            normalBtnBounds = new Rectangle(normalX, btnY, 270, 120);
            hardBtnBounds = new Rectangle(hardX, btnY, 270, 120);

            if (normalBtnImg != null)
                g.drawImage(normalBtnImg, normalX, btnY, 270, 120, this);
            if (hardBtnImg != null)
                g.drawImage(hardBtnImg, hardX, btnY, 270, 120, this);
        }
    }
}
