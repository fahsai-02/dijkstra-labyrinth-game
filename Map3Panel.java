import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Map3Panel extends JPanel {
    private Map<Character, NodeInfo> graph;
    private Map<Character, Point> nodePositions;
    private Map<Character, Image> nodeImages;
    private CharacterStatus playerStatus;
    private RunGame runGame;
    private Image characterImage;
    private char currentNode = 'A';
    private char selected = '-';
    private final int ICON_SIZE = 40;

    public Map3Panel(Map<Character, NodeInfo> graph, RunGame runGame, CharacterStatus status) {
        this.graph = graph;
        this.runGame = runGame;
        this.playerStatus = status;
        this.nodePositions = new HashMap<>();
        this.nodeImages = new HashMap<>();

        int offsetX = 200, offsetY = 100;
        nodePositions.put('A', new Point(120 + offsetX, 540 - offsetY));
        nodePositions.put('B', new Point(380 + offsetX, 500 - offsetY));
        nodePositions.put('C', new Point(380 + offsetX, 700 - offsetY));
        nodePositions.put('D', new Point(240 + offsetX, 270 - offsetY));
        nodePositions.put('E', new Point(600 + offsetX, 270 - offsetY));
        nodePositions.put('F', new Point(600 + offsetX, 600 - offsetY));
        nodePositions.put('G', new Point(600 + offsetX, 950 - offsetY));
        nodePositions.put('H', new Point(800 + offsetX, 500 - offsetY));
        nodePositions.put('I', new Point(800 + offsetX, 850 - offsetY));
        nodePositions.put('J', new Point(960 + offsetX, 270 - offsetY));
        nodePositions.put('K', new Point(960 + offsetX, 570 - offsetY));
        nodePositions.put('L', new Point(1250 + offsetX, 300 - offsetY));
        nodePositions.put('M', new Point(1500 + offsetX, 570 - offsetY));
        nodePositions.put('N', new Point(1250 + offsetX, 900 - offsetY));
        nodePositions.put('O', new Point(960 + offsetX, 950 - offsetY)); //จุด end

        // โหลดรูปภาพ node ถ้ามี
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
        loadImage('O', "PicsTemp\\O.png");

        // โหลดตัวละคร
        try {
            characterImage = new ImageIcon("PicsTemp\\Character.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Error loading character image: " + e.getMessage());
        }

        // คลิกเพื่อเคลื่อนที่
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
                            int cost = edge.map(n -> n.weight).orElse(1);
                            if (playerStatus.getMp() < cost) {
                                JOptionPane.showMessageDialog(Map3Panel.this, "Not enough MP!");
                                return;
                            }
                            playerStatus.loseMP(cost);
                            currentNode = clickedNode;
                            selected = clickedNode;

                            if (clickedNode == 'O') {
                                JOptionPane.showMessageDialog(Map3Panel.this, "Stage 3 Complete!");
                                SwingUtilities.invokeLater(() -> runGame.showBossMap()); // เปลี่ยนไปแมพบอส
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

        // วาดเส้น
        for (Map.Entry<Character, NodeInfo> entry : graph.entrySet()) {
            Point p1 = nodePositions.get(entry.getKey());
            for (NodeInfo.Neighbor n : entry.getValue().neighbors) {
                Point p2 = nodePositions.get(n.nameNeighbor);
                if (p1 != null && p2 != null) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
        
                    //น้ำหนักเส้น
                    int midX = (p1.x + p2.x) / 2;
                    int midY = (p1.y + p2.y) / 2;
                    g.setColor(Color.BLUE);
                    g.setFont(new Font("Arial", Font.BOLD, 14));
                    g.drawString(String.valueOf(n.weight), midX, midY - 5);
                    g.setColor(Color.BLACK); //กลับเป็นสีเดิม
                }
            }
        }
        

        // วาดโหนด
        for (Map.Entry<Character, Point> entry : nodePositions.entrySet()) {
            char name = entry.getKey();
            Point p = entry.getValue();
            Image img = nodeImages.get(name);
            if (img != null) {
                g.drawImage(img, p.x - ICON_SIZE / 2, p.y - ICON_SIZE / 2, this);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillOval(p.x - 20, p.y - 20, 40, 40);
            }
            if (name == selected) {
                g.setColor(Color.GREEN);
                g.drawOval(p.x - 25, p.y - 25, 50, 50);
            }
        }

        if (characterImage != null) {
            Point p = nodePositions.get(currentNode);
            if (p != null) g.drawImage(characterImage, p.x - 25, p.y - 60, this);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("HP: " + playerStatus.getHp(), 20, 30);
        g.drawString("MP: " + playerStatus.getMp(), 20, 55);
    }

    private Set<Character> defeatedNodes = new HashSet<>();

    public void markDefeated(char node) {
        defeatedNodes.add(node);
        repaint();
    }

}
