import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EndGamePanel extends JPanel {
    private RunGame parent;
    private Image background;
    private Rectangle mainMenuBounds, exitBounds;
    private Image menuImg, exitImg;

    private boolean isWin;
    private int score;

    public EndGamePanel(int score, boolean isWin, RunGame parent) {
        this.score = score;
        this.isWin = isWin;
        this.parent = parent;

        try {
            String path = isWin ? "assets/BossFight/BgWin.JPG" : "assets/BossFight/BgLose.JPG";
            background = new ImageIcon(path).getImage();

            menuImg = new ImageIcon("assets/BossFight/MenuButton.PNG").getImage()
                    .getScaledInstance(300, 80, Image.SCALE_SMOOTH);
            exitImg = new ImageIcon("assets/BossFight/Exit.PNG").getImage()
                    .getScaledInstance(300, 120, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading end screen images");
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (mainMenuBounds != null && mainMenuBounds.contains(p)) {
                    parent.showMainMenu();
                } else if (exitBounds != null && exitBounds.contains(p)) {
                    System.exit(0);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        // BG
        if (background != null)
            g.drawImage(background, 0, 0, w, h, this);

        // Final score
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        g.drawString("Final Score: " + score + " / 3000", w / 2 - 250, h / 3);

        // Buttons
        int btnW = 300, btnH1 = 80, btnH2 = 120;
        int menuX = w / 2 - 350;
        int exitX = w / 2 + 50;
        int btnY = h - 200;

        mainMenuBounds = new Rectangle(menuX, btnY, btnW, btnH1);
        exitBounds = new Rectangle(exitX, btnY, btnW, btnH2);

        if (menuImg != null) g.drawImage(menuImg, menuX, btnY, this);
        if (exitImg != null) g.drawImage(exitImg, exitX, btnY, this);
    }
}
