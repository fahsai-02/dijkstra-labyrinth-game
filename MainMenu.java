import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JPanel implements MouseListener {
    private Image bgImage;
    private Image playButtonImg;
    private Image howToButtonImg;
    private Image exitButtonImg;

    private Rectangle playButtonBounds;
    private Rectangle howToButtonBounds;
    private Rectangle exitButtonBounds;

    private RunGame parent;

    public MainMenu(RunGame parent) {
        this.parent = parent;

        // Load Images
        try {
            bgImage = new ImageIcon("assets/StartPage/BG.JPG").getImage();
            playButtonImg = new ImageIcon("assets/StartPage/Play.PNG").getImage()
                    .getScaledInstance(670, 310, Image.SCALE_SMOOTH);
            howToButtonImg = new ImageIcon("assets/StartPage/Guide.PNG").getImage()
                    .getScaledInstance(172, 161, Image.SCALE_SMOOTH);
            exitButtonImg = new ImageIcon("assets/StartPage/Exit.PNG").getImage()
                    .getScaledInstance(332, 184, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading images");
        }

        addMouseListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        // Draw Background
        if (bgImage != null)
            g.drawImage(bgImage, 0, 0, w, h, this);

        // Define Bounds
        playButtonBounds = new Rectangle(60, h - 255, 300, 155);
        howToButtonBounds = new Rectangle(350, h - 210, 75, 75);
        exitButtonBounds = new Rectangle(w - 200, h - 127, 165, 92);

        // Draw Buttons
        if (playButtonImg != null)
            g.drawImage(playButtonImg, playButtonBounds.x, playButtonBounds.y,
                    playButtonBounds.width, playButtonBounds.height, this);

        if (howToButtonImg != null)
            g.drawImage(howToButtonImg, howToButtonBounds.x, howToButtonBounds.y,
                    howToButtonBounds.width, howToButtonBounds.height, this);

        if (exitButtonImg != null)
            g.drawImage(exitButtonImg, exitButtonBounds.x, exitButtonBounds.y,
                    exitButtonBounds.width, exitButtonBounds.height, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point click = e.getPoint();

        if (playButtonBounds != null && playButtonBounds.contains(click)) {
            parent.startGame();
        } else if (howToButtonBounds != null && howToButtonBounds.contains(click)) {
            parent.showHowToPlay();
        } else if (exitButtonBounds != null && exitButtonBounds.contains(click)) {
            System.exit(0);
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
