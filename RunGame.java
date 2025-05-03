import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RunGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BossMap bossMap;
    private Map3Panel map3Panel;
    private FightPanel fightPanel;
    private EndGamePanel endPanel;
    private CharacterStatus playerStatus = new CharacterStatus(200, 100);
    private JPanel currentMap;

    public JPanel getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(JPanel map) {
        this.currentMap = map;
    }

    public RunGame() {
        setTitle("The Labyrinth Tower of Trials");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
    
        // ✅ ใช้ MyGraph.createMap3() ที่มีทั้ง vertex + edge พร้อมแล้ว
        MyGraph map3Graph = MyGraph.createMap3();
        map3Panel = new Map3Panel(map3Graph.graph.adjL, this, playerStatus);
        mainPanel.add(map3Panel, "map3");
        currentMap = map3Panel;
    
        fightPanel = new FightPanel(this, playerStatus);
        mainPanel.add(fightPanel, "fight");
    
        endPanel = new EndGamePanel(0, this);
        mainPanel.add(endPanel, "end");
    
        add(mainPanel);
        cardLayout.show(mainPanel, "map3");
    
        setVisible(true);
    }    

    public void showMap() {
        if (currentMap != null) {
            if (currentMap instanceof BossMap) {
                cardLayout.show(mainPanel, "bossMap");
                bossMap.repaint();
            } 
            else if (currentMap instanceof Map3Panel) {
                cardLayout.show(mainPanel, "map3");
                map3Panel.repaint();
            }
        }
    }

    public void showBossMap() {
        MyGraph bossGraph = MyGraph.createBossMap();
    
        char start = bossGraph.findStartNode();
        char end = bossGraph.findEndNode();
        int optimalCost = bossGraph.shortest(start, end);
        List<Character> path = bossGraph.getPath(start, end);
    
        ScoreCalculator scoreCalculator = new ScoreCalculator(optimalCost);
    
        bossMap = new BossMap(bossGraph.graph.adjL, path, this, playerStatus, scoreCalculator);
        mainPanel.add(bossMap, "bossMap");
        cardLayout.show(mainPanel, "bossMap");
        currentMap = bossMap;
    }
    
    public void showFight(char node) {
        cardLayout.show(mainPanel, "fight");
        fightPanel.startFight(node);
    }

    public void showEndGame(int finalScore) {
        endPanel = new EndGamePanel(finalScore, this);
        mainPanel.add(endPanel, "end");
        cardLayout.show(mainPanel, "end");
    }

    public void showMenu() {
        JOptionPane.showMessageDialog(this, "Menu system not implemented yet.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RunGame::new);
    }
}
