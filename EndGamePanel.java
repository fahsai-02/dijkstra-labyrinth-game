import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EndGamePanel extends JPanel {
    private RunGame parent;
    private Image background; // รูปพื้นหลัง ขึ้นกับว่าเกมจบแบบชนะหรือแพ้
    private Image menuImg, exitImg; // รูปปุ่มกลับเมนูและออกเกม
    private Rectangle mainMenuBounds, exitBounds; // ขอบเขตปุ่มเอาไว้ตรวจคลิก

    private boolean isWin; // ตัวบอกว่าชนะหรือแพ้
    private int score;     // คะแนนสุดท้าย

    public EndGamePanel(int score, boolean isWin, RunGame parent) {
        this.score = score;
        this.isWin = isWin;
        this.parent = parent;

        try {
            // โหลดพื้นหลังต่างกันถ้าชนะหรือแพ้
            background = new ImageIcon(isWin 
                ? "assets/Result/BgWin.JPG"
                : "assets/Result/BgLose.JPG"
            ).getImage();

            // โหลดปุ่มกลับเมนู แยกภาพตามผลชนะ/แพ้
            menuImg = new ImageIcon(isWin
                ? "assets/Result/BackToMenuWin.PNG"
                : "assets/Result/BackToMenuLose.PNG"
            ).getImage().getScaledInstance(400, 120, Image.SCALE_SMOOTH);

            // ปุ่ม Exit ใช้ภาพเดียวกันตลอด
            exitImg = new ImageIcon("assets/StartPage/Exit.PNG")
                .getImage().getScaledInstance(130, 40, Image.SCALE_SMOOTH);

        } catch (Exception e) {
            // ถ้าภาพโหลดผิดพลาด แจ้งให้ผู้เล่นรู้
            JOptionPane.showMessageDialog(this, "Error loading end screen images: " + e.getMessage());
        }

        // ติดตั้งตัวจับคลิกเมาส์บน JPanel
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                // ถ้าคลิกในขอบเขตปุ่มกลับเมนู
                if (mainMenuBounds != null && mainMenuBounds.contains(p)) {
                    parent.resetGame(); // เริ่มเกมใหม่ (รีเซ็ตทุกอย่าง)
                } 
                // ถ้าคลิกปุ่มออกเกม
                else if (exitBounds != null && exitBounds.contains(p)) {
                    System.exit(0); // ปิดโปรแกรม
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();

        // วาดภาพพื้นหลังเต็มจอ
        if (background != null)
            g.drawImage(background, 0, 0, w, h, this);

        // แสดงคะแนนที่ได้แบบใหญ่ ๆ ตรงกลาง
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        g.drawString("Final Score: " + score + " / 3000", w / 2 - 220, h / 3);

        // กำหนดตำแหน่งและขนาดปุ่มต่าง ๆ
        int btnW = 400, btnH1 = 120;
        int menuX = w / 2 - 200;
        int exitX = w - 180;
        int exitY = h - 70;
        int btnY = h - 175;

        // เก็บขอบเขตปุ่มไว้เช็คคลิก
        mainMenuBounds = new Rectangle(menuX, btnY, btnW, btnH1);
        exitBounds = new Rectangle(exitX, exitY, 130, 40);

        // วาดปุ่มกลับเมนูและปุ่มออกเกม
        if (menuImg != null) g.drawImage(menuImg, menuX, btnY, this);
        if (exitImg != null) g.drawImage(exitImg, exitX, exitY, this);
    }
}
