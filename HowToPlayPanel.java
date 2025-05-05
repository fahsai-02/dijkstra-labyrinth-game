import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HowToPlayPanel extends JPanel {
    private Image backButtonImage;
    private RunGame parent;
    private Rectangle backButton;
    private Image bgImage;

    public HowToPlayPanel(RunGame parent) {
        this.parent = parent;
        try {
            bgImage = new ImageIcon("assets/StartPage/BgGuide.JPG").getImage();
            backButtonImage = new ImageIcon("assets/StartPage/Undo.PNG").getImage(); // รูปปุ่ม
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Can't load how-to-play or back button image");
        }
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backButton.contains(e.getPoint())) {
                    parent.returnToMainMenu();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }

        backButton = new Rectangle(70, getHeight()-150, 75, 75);

        if (backButtonImage != null) {
            g.drawImage(backButtonImage, backButton.x, backButton.y, backButton.width, backButton.height, this);
        }
    }

}
