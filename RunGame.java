import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RunGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map3Panel map3Panel;
    private Map2Panel map2Panel;
    private FightPanel fightPanel;
    private EndGamePanel endPanel;
    private CharacterStatus playerStatus = new CharacterStatus(200, 100);
    private JPanel currentMap;
    private BossFightPanel bossFightPanel;

    public JPanel getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(JPanel map) {
        this.currentMap = map;
    }

    //Constructure
    public RunGame() {
        setTitle("The Labyrinth Tower of Trials");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ใช้ MyGraph.createMap2() สำหรับ Map 2
        MyGraph map2Graph = MyGraph.createMap2();
        map2Panel = new Map2Panel(map2Graph.graph.adjL, this, playerStatus);
        mainPanel.add(map2Panel, "map2");
        currentMap = map2Panel;

        fightPanel = new FightPanel(this, playerStatus);
        mainPanel.add(fightPanel, "fight");

        bossFightPanel = new BossFightPanel(this, playerStatus);
        mainPanel.add(bossFightPanel, "bossFight");

        add(mainPanel);
        cardLayout.show(mainPanel, "map2");

        setVisible(true);
    }

    public void showMap() {
        if (currentMap != null) {
            if (currentMap instanceof Map3Panel) {
                cardLayout.show(mainPanel, "map3");
                map3Panel.repaint();
            } else if (currentMap instanceof Map2Panel) {
                cardLayout.show(mainPanel, "map2");
                map2Panel.repaint();
            }
        }
    }

    public void showMap3() {
        MyGraph map3Graph = MyGraph.createMap3();

        char start = map3Graph.findStartNode();
        char end = map3Graph.findEndNode();
        int optimalCost = map3Graph.shortest(start, end);
        List<Character> path = map3Graph.getPath(start, end);

        ScoreCalculator scoreCalculator = new ScoreCalculator(optimalCost);

        map3Panel = new Map3Panel(map3Graph.graph.adjL, path, this, playerStatus, scoreCalculator);
        mainPanel.add(map3Panel, "map3");
        cardLayout.show(mainPanel, "map3");
        currentMap = map3Panel;
    }

    public void showFight(char node) {
        cardLayout.show(mainPanel, "fight");
        fightPanel.startFight(node);
    }

    public void showBossFight(char node) {
        bossFightPanel = new BossFightPanel(this, playerStatus); // สร้างใหม่เสมอ
        mainPanel.add(bossFightPanel, "bossFight");
        cardLayout.show(mainPanel, "bossFight");
    }

    public void showEndGame(int finalScore, boolean isWin) {
        endPanel = new EndGamePanel(finalScore, isWin, this);
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
