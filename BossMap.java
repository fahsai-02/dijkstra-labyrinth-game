import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BossMap extends JPanel {
    Map<Character, NodeInfo> graph;
    Map<Character, Point> nodePositions;
    Map<Character, Image> nodeImages;
    java.util.List<Character> path;
    char selected = '-';
    char currentNode = 'A';
    final int ICON_SIZE = 40;
    private RunGame runGame;
    private Image characterImage;
    private Set<Character> defeatedNodes = new HashSet<>();
    private CharacterStatus playerStatus;

    public  BossMap(Map<Character, NodeInfo> graph, java.util.List<Character> path, RunGame runGame, CharacterStatus status) {
        this.graph = graph;
        this.path = path;
        this.runGame = runGame;
        this.nodePositions = new HashMap<>();
        this.nodeImages = new HashMap<>();
        this.playerStatus = status;

        int offsetX = 200, offsetY = 100;
        // nodePositions.put('A', new Point(120 + offsetX, 100 + offsetY));
        nodePositions.put('A', new Point(120 + offsetX, 540 - offsetY));
        nodePositions.put('B', new Point(360 + offsetX, 500 - offsetY));
        nodePositions.put('C', new Point(360 + offsetX, 700 - offsetY));
        nodePositions.put('D', new Point(480 + offsetX, 500 - offsetY));
        nodePositions.put('E', new Point(480 + offsetX, 650 - offsetY));
        nodePositions.put('F', new Point(480 + offsetX, 900 - offsetY));
        nodePositions.put('G', new Point(600 + offsetX, 300 - offsetY));
        nodePositions.put('H', new Point(720 + offsetX, 300 - offsetY));
        nodePositions.put('I', new Point(720 + offsetX, 520 - offsetY));
        nodePositions.put('J', new Point(720 + offsetX, 700 - offsetY));
        nodePositions.put('K', new Point(720 + offsetX, 930 - offsetY));
        nodePositions.put('L', new Point(840 + offsetX, 520 - offsetY));
        nodePositions.put('M', new Point(840 + offsetX, 650 - offsetY));
        nodePositions.put('N', new Point(840 + offsetX, 900 - offsetY));
        nodePositions.put('O', new Point(1080 + offsetX, 400 - offsetY));
        nodePositions.put('P', new Point(1200 + offsetX, 250 - offsetY));
        nodePositions.put('Q', new Point(860 + offsetX, 250 - offsetY));
        nodePositions.put('R', new Point(1080 + offsetX, 700 - offsetY));
        nodePositions.put('T', new Point(1200 + offsetX, 900 - offsetY));
        nodePositions.put('U', new Point(1200 + offsetX, 500 - offsetY));
        nodePositions.put('V', new Point(360 + offsetX, 300 - offsetY));
        
        loadImage('A', "PicsTemp\\A.png");
        loadImage('B', "PicsTemp\\B.png");
        loadImage('C', "PicsTemp\\C.png");
        loadImage('D', "PicsTemp\\D.png");
        loadImage('E', "PicsTemp\\R.png");
        loadImage('F', "PicsTemp\\F.png");
        loadImage('G', "PicsTemp\\G.png");
        loadImage('H', "PicsTemp\\H.png");
        loadImage('I', "PicsTemp\\I.png");
        loadImage('J', "PicsTemp\\J.png");
        loadImage('K', "PicsTemp\\K.png");
        loadImage('L', "PicsTemp\\L.png");
        loadImage('M', "PicsTemp\\M.png");
        loadImage('N', "PicsTemp\\N.png");
        loadImage('O', "PicsTemp\\\\O.png");
        loadImage('P', "PicsTemp\\\\P.png");
        loadImage('Q', "PicsTemp\\\\Q.png");
        loadImage('R', "PicsTemp\\\\R.png");
        loadImage('S', "PicsTemp\\\\S.png");
        loadImage('U', "PicsTemp\\\\U.png");
        loadImage('V', "PicsTemp\\\\V.png");
        loadImage('T', "PicsTemp\\\\boss.png");

        try {
            characterImage = new ImageIcon("PicsTemp\\Character.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Error loading character image: " + e.getMessage());
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int radius = ICON_SIZE / 2 + 20;
                for (Map.Entry<Character, Point> entry : nodePositions.entrySet()) {
                    Point p = entry.getValue();
                    int dx = e.getX() - p.x;
                    int dy = e.getY() - p.y;
                    if (Math.abs(dx) <= radius && Math.abs(dy) <= radius) {
                        char clickedNode = entry.getKey();
                        NodeInfo current = graph.get(currentNode);
                        boolean isNeighbor = current.neighbors.stream().anyMatch(n -> n.nameNeighbor == clickedNode);
                        if (isNeighbor) {
                            Optional<NodeInfo.Neighbor> edge = current.neighbors.stream().filter(n -> n.nameNeighbor == clickedNode).findFirst();
                            int cost = edge.map(neighbor -> neighbor.weight).orElse(1);
                            if (playerStatus.getMp() < cost) {
                                JOptionPane.showMessageDialog( BossMap.this, "Not enough MP to move!");
                                return;
                            }
                            playerStatus.loseMP(cost);
                            currentNode = clickedNode;
                            selected = clickedNode;
                            NodeInfo clicked = graph.get(clickedNode);
                            if (clicked.typeNode == 'N' && !defeatedNodes.contains(clickedNode)) {
                                JOptionPane.showMessageDialog( BossMap.this, "Entering battle at " + clickedNode);
                                runGame.showFight(clickedNode);
                            } else if (clicked.typeNode == 'X') {
                                JOptionPane.showMessageDialog( BossMap.this, "You have entered the BOSS ROOM!");
                                runGame.showFight(clickedNode);
                            }
                            repaint();
                        }
                        break;
                    }
                }
            }
        });
    }

    private void loadImage(char nodeName, String path) {
        try {
            Image img = new ImageIcon(path).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            nodeImages.put(nodeName, img);
        } catch (Exception e) {
            System.err.println("Error loading image for " + nodeName + ": " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        for (Map.Entry<Character, NodeInfo> entry : graph.entrySet()) {
            char from = entry.getKey();
            Point p1 = nodePositions.get(from);
            if (p1 == null) continue;
            for (NodeInfo.Neighbor n : entry.getValue().neighbors) {
                Point p2 = nodePositions.get(n.nameNeighbor);
                if (p2 == null) continue;
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;
                g.setColor(Color.MAGENTA);
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString(String.valueOf(n.weight), midX, midY - 5);
                g.setColor(Color.BLACK);
            }
        }

        for (Map.Entry<Character, Point> entry : nodePositions.entrySet()) {
            char name = entry.getKey();
            Point p = entry.getValue();
            Image img = nodeImages.get(name);
            if (img != null) {
                g.drawImage(img, p.x - ICON_SIZE / 2, p.y - ICON_SIZE / 2, this);
                NodeInfo info = graph.get(name);
                if (info != null) {
                    switch (info.typeNode) {
                        case 'N':
                            g.setColor(Color.RED);
                            g.drawRect(p.x - ICON_SIZE / 2 - 3, p.y - ICON_SIZE / 2 - 3, ICON_SIZE + 6, ICON_SIZE + 6);
                            break;
                        case 'X':
                            g.setColor(Color.ORANGE);
                            g.drawRect(p.x - ICON_SIZE / 2 - 3, p.y - ICON_SIZE / 2 - 3, ICON_SIZE + 6, ICON_SIZE + 6);
                            break;
                        case '-':
                            g.setColor(Color.GRAY);
                            g.drawOval(p.x - ICON_SIZE / 2 - 2, p.y - ICON_SIZE / 2 - 2, ICON_SIZE + 4, ICON_SIZE + 4);
                            break;
                    }
                }
                if (name == selected) {
                    g.setColor(Color.GREEN);
                    g.drawOval(p.x - ICON_SIZE / 2 - 5, p.y - ICON_SIZE / 2 - 5, ICON_SIZE + 10, ICON_SIZE + 10);
                }
            }
        }

        if (characterImage != null) {
            Point cp = nodePositions.get(currentNode);
            g.drawImage(characterImage, cp.x - 25, cp.y - 60, this);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Player HP: " + playerStatus.getHp(), 20, 20);
            g.drawString("Player MP: " + playerStatus.getMp(), 20, 45);
        }
    }

    public void markDefeated(char node) {
        defeatedNodes.add(node);
        repaint();
    }
}
