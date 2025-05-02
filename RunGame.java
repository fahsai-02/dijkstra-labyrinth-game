import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RunGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BossMap mapPanel;
    private FightPanel fightPanel;
    private CharacterStatus playerStatus = new CharacterStatus(200, 100);

    public RunGame() {
        setTitle("The Labyrinth Tower of Trials");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        //สร้างกราฟก่อน 
        AdjacecyList graph = new AdjacecyList();
        Random rand = new Random();

        char[] nodes = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V' };

        for (char node : nodes) {
            if (node == 'A') {
                graph.addVertex(node, 'S'); // Start
            } 
            else if (node == 'T') {
                graph.addVertex(node, 'X'); // Boss
            } 
            else {
                char type = rand.nextBoolean() ? 'N' : '-';
                graph.addVertex(node, type);
                graph.addVertex('V', type);
                graph.addVertex('B', type);
                graph.addVertex('D', type);
                graph.addVertex('E', type);
                graph.addVertex('F', type);
                graph.addVertex('I', type);
                graph.addVertex('H', type);
                graph.addVertex('L', type);
                graph.addVertex('M', type);
                graph.addVertex('R', type);
                graph.addVertex('U', type);
                graph.addVertex('Q', type);
                graph.addVertex('Q', type);
            }
        }

        graph.addEdge('A', 'V', 1);
        graph.addEdge('A', 'B', 2);
        graph.addEdge('A', 'C', 1);
        graph.addEdge('B', 'D', 1);
        graph.addEdge('D', 'E', 2);
        graph.addEdge('E', 'I', 1);
        graph.addEdge('I', 'H', 1);
        graph.addEdge('I', 'L', 3);
        graph.addEdge('I', 'M', 1);
        graph.addEdge('E', 'J', 1);
        graph.addEdge('J', 'I', 4);
        graph.addEdge('J', 'M', 3);
        graph.addEdge('J', 'K', 2);
        graph.addEdge('E', 'F', 1);
        graph.addEdge('F', 'K', 2);
        graph.addEdge('K', 'M', 1);
        graph.addEdge('L', 'M', 1);
        graph.addEdge('M', 'T', 2);
        graph.addEdge('K', 'N', 5);
        graph.addEdge('N', 'T', 4);
        graph.addEdge('L', 'O', 1);
        graph.addEdge('V', 'G', 3);
        graph.addEdge('G', 'H', 6);
        graph.addEdge('H', 'O', 1);
        graph.addEdge('O', 'P', 4);
        graph.addEdge('P', 'Q', 3);
        graph.addEdge('Q', 'O', 2);
        graph.addEdge('O', 'R', 1);
        graph.addEdge('O', 'U', 5);
        graph.addEdge('O', 'T', 1);
        
        Dijkstra dj = new Dijkstra();
        dj.compute(graph.adjL, 'A');
        List<Character> path = dj.getPath('G');

        // ✅ ---------- สร้าง Panels หลังจาก graph ถูกสร้าง ----------
        mapPanel = new  BossMap(graph.adjL, path, this, playerStatus);
        fightPanel = new FightPanel(this, playerStatus);

        mainPanel.add(mapPanel, "map");
        mainPanel.add(fightPanel, "fight");

        add(mainPanel);
        cardLayout.show(mainPanel, "map");

        setVisible(true);
    }

    public void showMap() {
        cardLayout.show(mainPanel, "map");
        mapPanel.repaint();
    }

    public void showFight(char node) {
        cardLayout.show(mainPanel, "fight");
        fightPanel.startFight(node);
    }

    public BossMap getMapPanel() {
        return mapPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RunGame::new);
    }
}
