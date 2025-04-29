import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class FightScene {
    private int playerHP = 100;
    private int botHP = 100;
    private int playerMP = 50;
    private int botMP = 50;
    private boolean isplayerTurn = true;
    private JLabel turnLabel;
    private GamePanel gamePanel;

    //Graphics หลักของเกมอยู่ตรงนี้
    class GamePanel extends JPanel {
        private Image playerImage;
        private Image botImage;

        public GamePanel() { //โหลดรูปเข้า พร้อมปรับขนาด
            try {
                playerImage = new ImageIcon("character.png").getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                botImage = new ImageIcon("bot.png").getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH); 
            } catch (Exception e) { //ถ้าโหลดรูปเข้าไม่ได้จะแจ้งเตือนผ่าน Box ในโปรแกรม เดะเอาออกทีหลังได้ 
                JOptionPane.showMessageDialog(null, "Error loading images: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //เคลียร์หน้าจอก่อน และกำหนดให้ใช้ g. เรียกใช้คำสั่งใน JPanel
            Font statusFont = new Font("Arial", Font.BOLD, 20);
            g.setFont(statusFont);

            //Insert รูปตลค
            if (playerImage != null) { 
                g.drawImage(playerImage, 480, 270, this);
            }
            if (botImage != null) {
                g.drawImage(botImage, 1140, 270, this);
            }

            int maxHPBarWidth = 300;
            int maxMPBarWidth = 150;
            int maxHP = 100;
            int maxMP = 50;

            //Player Componetns
            int p1HPBarWidth = (playerHP * maxHPBarWidth) / maxHP;
            int p1MPBarWidth = (playerMP * maxMPBarWidth) / maxMP;
            g.setColor(Color.RED);
            g.fillRect(480, 600, p1HPBarWidth, 30);
            g.setColor(Color.BLUE);
            g.fillRect(480, 650, p1MPBarWidth, 30);
            g.setColor(Color.WHITE);
            g.drawString("HP: " + playerHP, 480 + p1HPBarWidth / 2 - g.getFontMetrics().stringWidth("HP: " + playerHP) / 2, 620);
            g.drawString("MP: " + playerMP, 480 + p1MPBarWidth / 2 - g.getFontMetrics().stringWidth("MP: " + playerMP) / 2, 670);

            //Bot Components
            int p2HPBarWidth = (botHP * maxHPBarWidth) / maxHP;
            int p2MPBarWidth = (botMP * maxMPBarWidth) / maxMP;
            g.setColor(Color.RED);
            g.fillRect(1140, 600, p2HPBarWidth, 30);
            g.setColor(Color.BLUE);
            g.fillRect(1140, 650, p2MPBarWidth, 30);
            g.setColor(Color.WHITE);
            g.drawString("HP: " + botHP, 1140 + p2HPBarWidth / 2 - g.getFontMetrics().stringWidth("HP: " + botHP) / 2, 620);
            g.drawString("MP: " + botMP, 1140 + p2MPBarWidth / 2 - g.getFontMetrics().stringWidth("MP: " + botMP) / 2, 670);

            //ใส่ชื่อเฉย ๆ 
            g.setColor(Color.BLACK);
            g.drawString("Player", 480, 590);
            g.drawString("Bot", 1140, 590);
        }
    }

    //สร้าง GUI แล้วก็เริ่มเกม!
    public void startGame() {
        JFrame frame = new JFrame("The labyrinth Tower of Trials");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel();

        turnLabel = new JLabel("Player's Turn");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 30));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setOpaque(true); //ใส่ BG ให้กล่อง turn
        turnLabel.setBackground(Color.BLACK); //กำหนดสีกล่อง turn
        turnLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton normalAttackButton = new JButton("Normal Attack");
        JButton hardAttackButton = new JButton("Hard Attack");
        normalAttackButton.setPreferredSize(new Dimension(500, 100));
        hardAttackButton.setPreferredSize(new Dimension(500, 100));

        Font buttonFont = new Font("Arial", Font.BOLD, 18); //เปลี่ยน Font ได้นะถ้าไม่เข้ากับธีม 5555+
        normalAttackButton.setFont(buttonFont);
        hardAttackButton.setFont(buttonFont);

        //ทำให้ปุ่มเลือกสกิลอยู่ตรงกลาง
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        normalAttackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hardAttackButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(turnLabel);
        buttonPanel.add(Box.createVerticalStrut(10)); //เว้นระยะเล็กน้อยย
        buttonPanel.add(normalAttackButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(hardAttackButton);

        frame.setLayout(new BorderLayout()); //จัดหน้า GUI
        frame.add(gamePanel, BorderLayout.CENTER); //ให้ GUI แสดงที่กลางจอไม่งั้นรูปจะบินหายยย
        frame.add(buttonPanel, BorderLayout.SOUTH); //โยนปุ่มไว้ด้านล่าง

        //ปุ่มตีปกติ
        normalAttackButton.addActionListener(e -> {
            if (isplayerTurn) {
                botHP -= 10; //ลดเลือด 10 หน่วย
                turnLabel.setText("Bot's Turn"); //เปลี่ยนข้อความเฉยๆ เลย
                gamePanel.repaint(); //อัพเดท GUI ใหม่

                if (gameOver(frame)) return;
                isplayerTurn = false; //เปลี่ยน state หลังตีแล้ว
                botTurn(); //สลับเทิร์น
            }
        });

        //ปุ่มตีเจ็บๆ 
        hardAttackButton.addActionListener(e -> {
            if (isplayerTurn && playerMP >= 15) {
                botHP -= 20;
                playerMP -= 15; //หัก MP ถ้ากด
                turnLabel.setText("Bot's Turn");
                gamePanel.repaint(); 

                if (gameOver(frame)) return; // Game over check
                isplayerTurn = false; // Switch to bot's turn
                botTurn(); // Bot attacks
            } 
            else if (isplayerTurn) { 
                JOptionPane.showMessageDialog(frame, "Not enough MP to perform Hard Attack!"); //ขึ้นกล่องเตือนว่า MP ไม่พอใช้สกิล
            }
        });
        frame.setVisible(true);
    }

    //Logic แรนด้อมตีของบอท
    private void botTurn() {
        turnLabel.setText("Bot's Turn - Thinking...");
        gamePanel.repaint();
    
        Random random = new Random();
        int action = random.nextInt(2); //สุ่มเลข 0 เป็นตีปกติ หรือ 1 เป็นตีแรง (ใช้สกิล)
    
        //ใส่ดีเลย์ให้เหมือนบอทกำลังเลือก actoin
        Timer delayTimer = new Timer(1000, e -> { //ดีเลย์ 1 วิ (จริงๆ ใช้หน่วย ms)
            String actionAnnounce; //ประกาศ state ของบอท
    
            if (action == 0 || botMP < 15) {
                playerHP -= 10;
                actionAnnounce = "Bot chose Normal Attack!";
            } 
            else {
                playerHP -= 20;
                botMP -= 15;
                actionAnnounce = "Bot chose Hard Attack!";
            }

            turnLabel.setText(actionAnnounce);
            gamePanel.repaint();
    
            Timer endBotTurnTimer = new Timer(1000, endEvent -> {
                turnLabel.setText("Player's Turn");
                if (gameOver(null)){
                    return;
                }
                isplayerTurn = true; //เปลี่ยนเทิร์น
                gamePanel.repaint();
            });
            endBotTurnTimer.setRepeats(false); //กันตาย ไม่ให้โค้ดรันดีเลย์ซ้ำำ เดี๋ยวมันเบิ้ลเทิร์น55555+
            endBotTurnTimer.start(); //ให้บอทดีเลย์ตอนสลับถึงเทิร์นบอท
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private boolean gameOver(JFrame frame) {
        if (playerHP <= 0 || botHP <= 0) {
            String winner;

            if (playerHP > 0) {
                winner = "Player Wins!";
            } else {
                winner = "Bot Wins!";
            }

            JOptionPane.showMessageDialog(frame, winner);
            System.exit(0); //จบเกม ออกโปรแกรมโล้ด เดะไว้มาแก้นะะะะ
            return true;
        }
        return false; // Game continues
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FightScene().startGame());
    }
}