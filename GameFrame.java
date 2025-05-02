import javax.swing.*;

public class GameFrame extends JFrame {
    private MainMenu mainMenu;
    private GamePanel gamePanel;

    public GameFrame() {
        setTitle("The Labyrinth - Tower of Trials");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center screen

        mainMenu = new MainMenu(this);
        add(mainMenu);

        setVisible(true);
    }

    public void showGamePanel() {
        remove(mainMenu);
        gamePanel = new GamePanel(/* ส่ง graph หรือ player */);
        // add(gamePanel);
        revalidate();
        repaint();
    }
}
