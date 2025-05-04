import javax.swing.*;
import java.util.List;
// import java.awt.*;

public class RunGame extends JFrame {
    private MainMenu mainMenu;
    private GamePanel gamePanel;
    private FightPanel fightPanel;
    private ItemSelection itemSelection;
    private CharacterStatus playerStatus;
    private int currentStage = 1;
    private BossFightPanel bossFightPanel;

    public RunGame() {
        setTitle("The Labyrinth - Tower of Trials");
        setSize(1920, 1080); //1280 800
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
    
        int healAmount = (int)(playerStatus.getMaxHp() * 0.2);
        playerStatus.heal(healAmount);
        JOptionPane.showMessageDialog(this,"You feel refreshed!\nRecovered " + healAmount + " HP.","Healing Bonus", JOptionPane.INFORMATION_MESSAGE);
    
        if (currentStage == 2) {
            gamePanel = new GamePanel(MapData.loadMap2(), this, playerStatus);
        } else if (currentStage == 3) {
            gamePanel = new GamePanel(MapData.loadMap3(), this, playerStatus);
        } else {
            JOptionPane.showMessageDialog(this, "Congratulations! You've cleared all stages!");
            showBossFight('Z');
            return;
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

    public void showMainMenu() {
        getContentPane().removeAll();
        mainMenu = new MainMenu(this);
        add(mainMenu);
        revalidate();
        repaint();
    }
    
    public void showEndGame(int finalScore, boolean isWin) {
        getContentPane().removeAll();
        EndGamePanel endPanel = new EndGamePanel(finalScore, isWin, this);
        add(endPanel);
        revalidate();
        repaint();
    }

    public void showBossFight(char node) {
        if (bossFightPanel == null) {
            bossFightPanel = new BossFightPanel(this, playerStatus);
        }
        remove(gamePanel);
        add(bossFightPanel);
        revalidate();
        repaint();
    }

    public void jumpToStage(int stage) {
        currentStage = stage;
        if (stage == 1) {
            gamePanel = new GamePanel(MapData.loadMap1(), this, playerStatus);
        } else if (stage == 2) {
            gamePanel = new GamePanel(MapData.loadMap2(), this, playerStatus);
        } else if (stage == 3) {
            gamePanel = new GamePanel(MapData.loadMap3(), this, playerStatus);
        }
        getContentPane().removeAll();
        add(gamePanel);
        revalidate();
        repaint();
    }
    
    public void jumpToBossFight() {
        getContentPane().removeAll();
        add(new BossFightPanel(this, playerStatus));
        revalidate();
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RunGame());
    }
}
