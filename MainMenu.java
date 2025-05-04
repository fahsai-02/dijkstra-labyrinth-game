import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JPanel implements MouseListener {
    private Image bgImage;
    private Image playButton;
    private Image exitButton;
    private Rectangle playBounds;
    private Rectangle exitBounds;
    private RunGame parent;

    public MainMenu(RunGame parent) {
        this.parent = parent;

        bgImage = new ImageIcon("assets/StartPage/BG.JPG").getImage(); //1920, 1080
        playButton = new ImageIcon("assets/StartPage/Play.PNG").getImage().getScaledInstance(670, 310, Image.SCALE_SMOOTH);
        exitButton = new ImageIcon("assets/StartPage/Exit.PNG").getImage().getScaledInstance(332, 184, Image.SCALE_SMOOTH);

        addMouseListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        playBounds = new Rectangle(60, getHeight() - 255, 300, 155);
        exitBounds = new Rectangle(getWidth() - 200, getHeight() - 127, 165, 92);

        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(playButton, playBounds.x, playBounds.y, playBounds.width, playBounds.height, this);
        g.drawImage(exitButton, exitBounds.x, exitBounds.y, exitBounds.width, exitBounds.height, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (playBounds.contains(p)) {
            parent.startGame(); // เรียกเริ่มเกม
        } else if (exitBounds.contains(p)) {
            System.exit(0);
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
