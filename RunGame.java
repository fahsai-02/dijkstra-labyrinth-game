import javax.swing.*;
import java.util.List;

public class RunGame extends JFrame {
    private MainMenu mainMenu;
    // private HowToPlayPanel howToPlayPanel;
    private GamePanel gamePanel;
    private FightPanel fightPanel;
    private ItemSelection itemSelection;
    private CharacterStatus playerStatus;
    private int currentStage = 1;
    private BossFightPanel bossFightPanel;

    public RunGame() {
        setTitle("The Labyrinth - Tower of Trials");
        setSize(1280, 720);                 
        setMinimumSize(new java.awt.Dimension(960, 600));
        setLocationRelativeTo(null);       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        playerStatus = new CharacterStatus(150, 250);
        mainMenu = new MainMenu(this);
        add(mainMenu);
    
        setVisible(true);
        setResizable(false);
    }
    

    public void startGame() {
        switchPanel(() -> {
            gamePanel = new GamePanel(MapData.loadMap1(), this, playerStatus);
            gamePanel.resetUndoHistory();
            return gamePanel;
        });
    }

    public void showHowToPlay() {
        switchPanel(() -> new HowToPlayPanel(this));
    }

    public void returnToMainMenu() {
        switchPanel(() -> {
            mainMenu = new MainMenu(this);
            return mainMenu;
        });
    }

    public void showFightPanel(char node) {
        switchPanel(() -> {
            fightPanel = new FightPanel(this, playerStatus);
            fightPanel.startFight(node);
            return fightPanel;
        });
    }

    public void showMap() {
        switchPanel(() -> gamePanel);
    }

    public void showShop() {
        switchPanel(() -> {
            List<ItemList.Item> items = ItemList.getAllItems(currentStage);
            itemSelection = new ItemSelection(this, playerStatus, items);
            return itemSelection;
        });
    }

    public void nextMap() {
        remove(itemSelection);
        currentStage++;

        int healAmount = (int) (playerStatus.getMaxHp() * 0.25);
        int mpRestore = (int) (playerStatus.getMaxMp() * 0.5);
        playerStatus.heal(healAmount);
        playerStatus.restoreMP(mpRestore);

        JOptionPane.showMessageDialog(this,
                "Entering Stage " + currentStage +
                        "\nHP restored: " + healAmount +
                        "\nMP restored: " + mpRestore);

        if (currentStage == 2) {
            gamePanel = new GamePanel(MapData.loadMap2(), this, playerStatus);
        } else if (currentStage == 3) {
            gamePanel = new GamePanel(MapData.loadMap3(), this, playerStatus);
        } else {
            JOptionPane.showMessageDialog(this, "Congratulations! You've cleared all stages!");
            showBossFight('Z');
            return;
        }

        gamePanel.resetUndoHistory();
        switchPanel(() -> gamePanel);
    }

    public GamePanel getMapPanel() {
        return gamePanel;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void showMainMenu() {
        switchPanel(() -> {
            mainMenu = new MainMenu(this);
            return mainMenu;
        });
    }

    public void resetGame() {
        this.playerStatus = new CharacterStatus(150, 250); // หรือค่าที่กำหนด
        this.currentStage = 1;
        this.mainMenu = new MainMenu(this);
        this.bossFightPanel = null;
        this.fightPanel = null;
        this.itemSelection = null;
        this.gamePanel = null;
        showMainMenu();
    }

    public void showEndGame(int ignoredScore, boolean isWin) {
        int totalScore = playerStatus.getTotalScore();
        switchPanel(() -> new EndGamePanel(totalScore, isWin, this));
    }

    public void showBossFight(char node) {
        if (bossFightPanel == null) {
            bossFightPanel = new BossFightPanel(this, playerStatus);
        }
        switchPanel(() -> bossFightPanel);
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

        gamePanel.resetUndoHistory();
        switchPanel(() -> gamePanel);
    }

    public void jumpToBossFight() {
        bossFightPanel = new BossFightPanel(this, playerStatus);
        switchPanel(() -> bossFightPanel);
    }

    private void switchPanel(PanelSupplier supplier) {
        getContentPane().removeAll();
        add(supplier.get());
        revalidate();
        repaint();

    }

    private interface PanelSupplier {
        JPanel get();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RunGame());
    }
}
