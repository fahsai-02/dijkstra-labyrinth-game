import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BossFightPanel extends JPanel {
    private RunGame parent;
    private CharacterStatus playerStatus; // สถานะผู้เล่น
    private MonsterStatus boss; // โหลดบอสมาไว้ใช้งาน

    // รูปภาพต่าง ๆ ที่ใช้แสดงผล
    private Image bgImage, bossImage;
    private Image normalBtnImg, hardBtnImg;
    private Rectangle normalBtnBounds, hardBtnBounds; // พิกัดปุ่มโจมตี

    private JLabel turnLabel; // เอาไว้บอกว่าใครถึงตาเล่น
    private boolean isPlayerTurn = true;
    private boolean gameEnded = false;
    private BossFightCanvas canvas;

    public BossFightPanel(RunGame parent, CharacterStatus playerStatus) {
        this.parent = parent;
        this.playerStatus = playerStatus;
        this.boss = MonsterStatus.getMonster("Tung", 3); // ตั้งค่าบอส (ชื่อ, เลเวล)

        // โหลดภาพต่าง ๆ
        try {
            bgImage = new ImageIcon("assets/BossFight/BgBoss.JPG").getImage();
            bossImage = new ImageIcon(boss.getImagePath()).getImage();
            normalBtnImg = new ImageIcon("assets/Monster/buttons/NormalAttack.PNG").getImage();
            hardBtnImg = new ImageIcon("assets/Monster/buttons/HardAttack.PNG").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading boss fight images.");
        }

        setLayout(new BorderLayout());

        // label ด้านบน บอกว่าใครถึงตาเล่น
        turnLabel = new JLabel("PLAYER'S TURN", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Serif", Font.BOLD, 32));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.BLACK);
        turnLabel.setForeground(Color.WHITE);
        add(turnLabel, BorderLayout.NORTH);

        // สร้าง layeredPane สำหรับวาง canvas
        JLayeredPane layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        // สร้าง canvas สำหรับวาดทุกอย่าง
        canvas = new BossFightCanvas();
        canvas.setBounds(0, 0, 1920, 1080);
        layeredPane.add(canvas, JLayeredPane.DEFAULT_LAYER);

        // จัดการคลิกปุ่มโจมตี
        canvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                // ถ้าไม่ใช่ตาผู้เล่นหรือเกมจบแล้ว ก็ไม่ทำอะไร
                if (!isPlayerTurn || gameEnded) return;

                // ถ้าคลิกปุ่มโจมตีธรรมดา
                if (normalBtnBounds != null && normalBtnBounds.contains(p)) {
                    boss.reduceHp(playerStatus.getAtk());
                    isPlayerTurn = false;
                    canvas.repaint();
                    checkGameEnd();
                    if (boss.getHp() > 0) enemyTurn(); // ถ้ายังไม่ตาย ให้บอสสวน
                } 
                // ถ้าคลิกปุ่มโจมตีหนัก
                else if (hardBtnBounds != null && hardBtnBounds.contains(p)) {
                    if (playerStatus.getMp() >= 15) {
                        boss.reduceHp(playerStatus.getAtk() + 15); // โจมตีแรงขึ้น
                        playerStatus.loseMP(15); // เสีย MP
                        isPlayerTurn = false;
                        canvas.repaint();
                        checkGameEnd();
                        if (boss.getHp() > 0) enemyTurn();
                    } else {
                        JOptionPane.showMessageDialog(BossFightPanel.this, "Not enough MP!");
                    }
                }
            }
        });

        // อัปเดตขนาด canvas ถ้า resize หน้าต่าง
        layeredPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                canvas.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                canvas.repaint();
            }
        });
    }

    // เทิร์นของบอสโจมตีผู้เล่น
    private void enemyTurn() {
        Timer t = new Timer(1000, e -> {
            turnLabel.setText("BOSS'S TURN");
            playerStatus.damage(boss.getAtk()); // บอสตี
            canvas.repaint();
            isPlayerTurn = true; // คืนตาผู้เล่น
            checkGameEnd();
            turnLabel.setText("PLAYER'S TURN");
        });
        t.setRepeats(false);
        t.start();
    }

    // เช็คว่าเกมจบหรือยัง
    private void checkGameEnd() {
        if (gameEnded) return;

        if (!playerStatus.isAlive()) {
            gameEnded = true;
            JOptionPane.showMessageDialog(this, "You were defeated by the BOSS!");
            parent.showEndGame(0, false); // แพ้
        } else if (boss.getHp() <= 0) {
            gameEnded = true;
            JOptionPane.showMessageDialog(this, "You defeated the BOSS!");
            parent.showEndGame(playerStatus.getHp() + playerStatus.getMp(), true); // ชนะ
        }
    }

    // Canvas เอาไว้จัดการวาดกราฟิกทั้งหมด
    class BossFightCanvas extends JPanel {
        public BossFightCanvas() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth(), h = getHeight();

            // สีหลอด HP/MP
            Color hpMonster = new Color(150, 28, 28);
            Color hpColor = new Color(55, 125, 75);
            Color mpColor = new Color(54, 124, 171);

            // วาดพื้นหลัง + รูปบอส
            g.drawImage(bgImage, 0, 0, w, h, this);
            g.drawImage(bossImage, w / 2 - 300, 0, 600, 400, this);

            // วาดหลอด HP บอส
            int bossHpBar = boss.getHp() * 300 / boss.getMaxHp();
            g.setColor(hpMonster);
            g.fillRect(150, 160, bossHpBar, 30);
            g.setColor(Color.BLACK);
            g.drawRect(150, 160, 300, 30);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.BOLD, 28));
            g.drawString(boss.getName(), 160, 140);
            g.setFont(new Font("Serif", Font.PLAIN, 22));
            g.drawString("HP: " + boss.getHp(), 160, 180);
            g.drawString("DEF: " + boss.getDef(), 160, 220);

            // วาด HP/MP ผู้เล่น
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

            // ตำแหน่งปุ่มโจมตี
            int btnY = h - 180;
            int normalX = w / 2 + 50;
            int hardX = normalX + 270;

            // สร้างขอบเขตปุ่มไว้ตรวจคลิก
            normalBtnBounds = new Rectangle(normalX, btnY, 270, 120);
            hardBtnBounds = new Rectangle(hardX, btnY, 270, 120);

            // วาดปุ่มโจมตี
            if (normalBtnImg != null)
                g.drawImage(normalBtnImg, normalX, btnY, 270, 120, this);
            if (hardBtnImg != null)
                g.drawImage(hardBtnImg, hardX, btnY, 270, 120, this);
        }
    }
}
