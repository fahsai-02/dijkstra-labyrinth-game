import javax.swing.*;
import java.util.List;

public class RunGame extends JFrame {
    private MainMenu mainMenu;
    private GamePanel gamePanel;
    private FightPanel fightPanel;
    private ItemSelection itemSelection;
    private CharacterStatus playerStatus;
    private int currentStage = 1;

    public RunGame() {
        setTitle("The Labyrinth - Tower of Trials");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        playerStatus = new CharacterStatus(150, 100);

        mainMenu = new MainMenu(this);
        add(mainMenu);

        setVisible(true);
    }

    public void startGame() {
        remove(mainMenu);
        gamePanel = new GamePanel(MapData.loadMap1(), this, playerStatus);
        add(gamePanel);
        revalidate();
        repaint();
    }

    public void showFightPanel(char node) {
        remove(gamePanel);
        fightPanel = new FightPanel(this, playerStatus);
        fightPanel.startFight(node);
        add(fightPanel);
        revalidate();
        repaint();
    }

    public void showMap() {
        remove(fightPanel);
        add(gamePanel);
        revalidate();
        repaint();
    }

    public void showShop() {
        remove(gamePanel);
        List<ItemList.Item> items = ItemList.getAllItems(currentStage);
        itemSelection = new ItemSelection(this, playerStatus, items);
        add(itemSelection);
        revalidate();
        repaint();
    }

    public void nextMap() {
        remove(itemSelection);
        currentStage++;

        if (currentStage == 2)
            gamePanel = new GamePanel(MapData.loadMap2(), this, playerStatus);
        else if (currentStage == 3)
            gamePanel = new GamePanel(MapData.loadMap3(), this, playerStatus);
            else {
            JOptionPane.showMessageDialog(this, "Congratulations! You've cleared all stages!");
            System.exit(0);
        }

        add(gamePanel);
        revalidate();
        repaint();
    }

    public GamePanel getMapPanel() {
        return gamePanel;
    }

    public int getCurrentStage() {
        return currentStage;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RunGame());
    }
}
