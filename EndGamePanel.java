import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EndGamePanel extends JPanel {
    private RunGame parent;
    private Image background;
    private Image menuImg, exitImg;
    private Rectangle mainMenuBounds, exitBounds;

    private boolean isWin;
    private int score;

    public EndGamePanel(int score, boolean isWin, RunGame parent) {
        this.score = score;
        this.isWin = isWin;
        this.parent = parent;

        try {
            // BG ชนะหรือแพ้
            background = new ImageIcon(isWin 
                ? "assets/Result/BgWin.JPG"
                : "assets/Result/BgLose.JPG"
            ).getImage();

            // ปุ่ม back to menu ของแต่ละกรณี
            menuImg = new ImageIcon(isWin
                ? "assets/Result/BackToMenuWin.PNG"
                : "assets/Result/BackToMenuLose.PNG"
            ).getImage().getScaledInstance(400, 120, Image.SCALE_SMOOTH);

            // ปุ่ม Exit ใช้เหมือนกัน
            exitImg = new ImageIcon("assets/StartPage/Exit.PNG")
                .getImage().getScaledInstance(130, 40, Image.SCALE_SMOOTH);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading end screen images: " + e.getMessage());
        }

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (mainMenuBounds != null && mainMenuBounds.contains(p)) {
                    parent.resetGame(); // รีเซ็ตทั้งหมด
                } else if (exitBounds != null && exitBounds.contains(p)) {
                    System.exit(0);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();

        if (background != null)
            g.drawImage(background, 0, 0, w, h, this);

        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        g.drawString("Final Score: " + score + " / 3000", w / 2 - 220, h / 3);

        // วาดปุ่ม
        int btnW = 400, btnH1 = 120;
        int menuX = w / 2-200;
        int exitX = w-180;
        int exitY = h-70;
        int btnY = h - 175;

        mainMenuBounds = new Rectangle(menuX, btnY, btnW, btnH1);
        exitBounds = new Rectangle(exitX, exitY, 130, 40);

        if (menuImg != null) g.drawImage(menuImg, menuX, btnY, this);
        if (exitImg != null) g.drawImage(exitImg, exitX, exitY, this);
    }
}
