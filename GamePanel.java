import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class GamePanel extends JPanel implements MouseListener {
    private MapData mapData;
    private Image bgImage;
    private char playerNode;
    private RunGame parent;
    private int totalDistance = 0;
    private CharacterStatus playerStatus;

    public GamePanel(MapData mapData, RunGame parent, CharacterStatus playerStatus) {
        this.mapData = mapData;
        this.playerNode = mapData.playerStartNode;
        this.bgImage = new ImageIcon(mapData.backgroundImagePath).getImage();
        this.parent = parent;
        this.playerStatus = playerStatus;

        addMouseListener(this);
        setFocusable(true);
    }

    private void drawPlayerStatus(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 22));

        int x = 20, y = 40, lineHeight = 50;

        g.drawString("HP:  " + playerStatus.getHp(), x + 200, y + 40);
        g.drawString("MP:  " + playerStatus.getMp(), x + 200, y + lineHeight + 40);
        g.drawString("ATK: " + playerStatus.getAtk(), x + 400, y + 40);
        g.drawString("DEF: " + playerStatus.getDef(), x + 400, y + lineHeight + 40);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        drawEdges(g);
        drawNodes(g);
        drawPlayer(g);
        drawPlayerStatus(g);
    }

    private void drawEdges(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setStroke(new BasicStroke(3));

        int textOffsetX = -22;
        int textOffsetY = 5;

        for (Map.Entry<Character, NodeInfo> entry : mapData.graph.graph.adjL.entrySet()) {
            char from = entry.getKey();
            Point p1 = mapData.getNodePosition(from);

            for (NodeInfo.Neighbor neighbor : entry.getValue().neighbors) {
                char to = neighbor.nameNeighbor;

                if (from < to) {
                    Point p2 = mapData.getNodePosition(to);

                    double dx = p2.x - p1.x;
                    double dy = p2.y - p1.y;
                    double length = Math.sqrt(dx * dx + dy * dy);
                    double gap = 50;
                    double ratio = (length - gap) / (2 * length);

                    int x1 = (int) (p1.x + dx * ratio);
                    int y1 = (int) (p1.y + dy * ratio);
                    int x2 = (int) (p2.x - dx * ratio);
                    int y2 = (int) (p2.y - dy * ratio);

                    g2.drawLine(p1.x, p1.y, x1, y1);
                    g2.drawLine(x2, y2, p2.x, p2.y);

                    String text = neighbor.weight + " m";
                    int mx = (p1.x + p2.x) / 2 + textOffsetX;
                    int my = (p1.y + p2.y) / 2 + textOffsetY;

                    g2.drawString(text, mx, my);
                }
            }
        }
    }

    private void drawNodes(Graphics g) {
        for (Map.Entry<Character, NodeInfo> entry : mapData.graph.graph.adjL.entrySet()) {
            char nodeName = entry.getKey();
            NodeInfo node = entry.getValue();
            Point p = mapData.getNodePosition(nodeName);
            Image img = mapData.getNodeImage(node.typeNode);

            if (img != null) {
                g.drawImage(img, p.x - 32, p.y - 32, 64, 64, this);
            } else {
                g.setColor(Color.GRAY);
                g.fillOval(p.x - 20, p.y - 20, 40, 40);
            }

            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(nodeName), p.x - 5, p.y + 5);
        }
    }

    private void drawPlayer(Graphics g) {
        Point p = mapData.getNodePosition(playerNode);
        g.setColor(Color.BLUE);
        g.fillOval(p.x - 10, p.y - 60, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Player", p.x - 20, p.y - 70);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point click = e.getPoint();

        for (Map.Entry<Character, Point> entry : mapData.nodeCoordinates.entrySet()) {
            char targetNode = entry.getKey();
            Point target = entry.getValue();

            if (click.distance(target) < 32 && targetNode != playerNode) {
                java.util.List<Character> path = mapData.graph.getPath(playerNode, targetNode);
                if (path != null && path.size() > 1) {
                    moveAlongPath(path);
                }
                return;
            }
        }
    }

    public void markDefeated(char nodeName) {
        mapData.graph.graph.adjL.get(nodeName).typeNode = 'N';
        repaint();
    }

    private void deductPlayerEnergy(int distance) {
        double mpPer10m = 1.0;
        double effectiveDistance = distance / 10.0;

        if (playerStatus.hasWingedBoots()) {
            effectiveDistance *= 0.5;
        }

        int mpLoss = (int) Math.ceil(effectiveDistance);
        int currentMp = playerStatus.getMp();

        if (currentMp >= mpLoss) {
            playerStatus.loseMP(mpLoss);
        } else {
            int overflow = mpLoss - currentMp;
            playerStatus.loseMP(currentMp);
            playerStatus.loseHP(overflow);
        }
    }

    private void moveAlongPath(java.util.List<Character> path) {
        new Thread(() -> {
            for (int i = 1; i < path.size(); i++) {
                char from = path.get(i - 1);
                char to = path.get(i);

                int weight = mapData.graph.graph.adjL.get(from).neighbors.stream()
                    .filter(n -> n.nameNeighbor == to)
                    .map(n -> n.weight)
                    .findFirst()
                    .orElse(0);

                totalDistance += weight;
                deductPlayerEnergy(weight);
                playerNode = to;
                repaint();

                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {}

                NodeInfo node = mapData.graph.graph.adjL.get(playerNode);
                if (node.typeNode == 'M') {
                    SwingUtilities.invokeLater(() -> parent.showFightPanel(playerNode));
                    return;
                } else if (node.typeNode == 'E') {
                    int shortest = mapData.graph.shortest(mapData.getStartNode(), playerNode);
                    int lost = Math.max(0, totalDistance - shortest);
                    int score = Math.max(0, 1000 - (lost * 5));
                    JOptionPane.showMessageDialog(this,
                        "Stage Complete!\nShortest path: " + shortest +
                        "\nYour path: " + totalDistance +
                        "\nScore: " + score + " / 1000");

                    SwingUtilities.invokeLater(() -> parent.showShop());
                    return;
                }
            }
        }).start();
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
