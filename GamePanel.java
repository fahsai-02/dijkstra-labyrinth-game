import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Stack;

public class GamePanel extends JPanel implements MouseListener {
    private MapData mapData;
    private Image bgImage;
    private char playerNode;
    private RunGame parent;
    private int totalDistance = 0;
    private CharacterStatus playerStatus;

    private Stack<Character> moveHistory = new Stack<>();
    private Stack<Integer> mpCostHistory = new Stack<>();
    private Stack<Integer> distanceHistory = new Stack<>();

    private int undoCount = 0;
    private final int undoLimit = 5;
    private JLabel undoLabel;
    private JLabel feedbackLabel; // 🔹 แจ้งเตือนซ้ายล่าง

    private JButton backBtn;

    public GamePanel(MapData mapData, RunGame parent, CharacterStatus playerStatus) {
        this.mapData = mapData;
        this.playerNode = mapData.playerStartNode;
        this.bgImage = new ImageIcon(mapData.backgroundImagePath).getImage();
        this.parent = parent;
        this.playerStatus = playerStatus;

        setFocusable(true);
        setLayout(null);
        addMouseListener(this);

        // === Undo Button ===
        backBtn = new JButton("Undo Move");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setBounds(50, 900, 150, 40);
        backBtn.addActionListener(e -> undoMove());
        add(backBtn);

        // === Undo Label ===
        undoLabel = new JLabel("Undo Left: " + (undoLimit - undoCount));
        undoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        undoLabel.setForeground(Color.WHITE);
        undoLabel.setBounds(220, 900, 200, 40);
        add(undoLabel);

        // === Feedback Label ===
        feedbackLabel = new JLabel("");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        feedbackLabel.setForeground(Color.YELLOW);
        add(feedbackLabel);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint();
                feedbackLabel.setBounds(20, getHeight()-60, 232, 43);

            }
        });
    }

    private void drawPlayerStatus(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 22));
        g.drawString("HP:  " + playerStatus.getHp(), 150, 60);
        g.drawString("MP:  " + playerStatus.getMp(), 150, 100);
        g.drawString("ATK: " + playerStatus.getAtk(), 270, 60);
        g.drawString("DEF: " + playerStatus.getDef(), 270, 100);
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
    
        for (Map.Entry<Character, NodeInfo> entry : mapData.graph.graph.adjL.entrySet()) {
            char from = entry.getKey();
            Point p1 = mapData.getNodePosition(from);
            for (NodeInfo.Neighbor neighbor : entry.getValue().neighbors) {
                char to = neighbor.nameNeighbor;
                if (from < to) {
                    Point p2 = mapData.getNodePosition(to);
    
                    // คำนวณ midpoint และ vector ระยะเว้น
                    int mx = (p1.x + p2.x) / 2;
                    int my = (p1.y + p2.y) / 2;
                    double dx = p2.x - p1.x;
                    double dy = p2.y - p1.y;
                    double len = Math.sqrt(dx * dx + dy * dy);
                    double gap = 28; // ระยะเว้นจาก midpoint
    
                    // จุดก่อนและหลังช่องว่าง
                    int gapX = (int) (dx / len * gap);
                    int gapY = (int) (dy / len * gap);
    
                    int x1 = mx - gapX;
                    int y1 = my - gapY;
                    int x2 = mx + gapX;
                    int y2 = my + gapY;
    
                    // วาดเส้นแบบเว้นตรงกลาง
                    g2.drawLine(p1.x, p1.y, x1, y1);
                    g2.drawLine(x2, y2, p2.x, p2.y);
    
                    // วาดข้อความระยะทางตรงกลาง
                    g2.drawString(neighbor.weight + " m", mx-22 , my+3);
                }
            }
        }
    }
    

    private void drawNodes(Graphics g) {
        for (Map.Entry<Character, NodeInfo> entry : mapData.graph.graph.adjL.entrySet()) {
            char nodeName = entry.getKey();
            Point p = mapData.getNodePosition(nodeName);
            Image img = mapData.getNodeImage(entry.getValue().typeNode);
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
        g.setColor(new Color(163,73,164));
        g.fillOval(p.x - 10, p.y - 29, 20, 20);
        g.setColor(new Color(106,47,107));
        g.drawString("Player", p.x - 24, p.y - 35);
    }

    // @Override
    // public void mouseClicked(MouseEvent e) {
    //     Point click = e.getPoint();
    //     for (Map.Entry<Character, Point> entry : mapData.nodeCoordinates.entrySet()) {
    //         char targetNode = entry.getKey();
    //         if (click.distance(entry.getValue()) < 32 && targetNode != playerNode) {
    //             java.util.List<Character> path = mapData.graph.getPath(playerNode, targetNode);
    //             if (path != null && path.size() > 1) {
    //                 moveAlongPath(path);
    //             }
    //             return;
    //         }
    //     }
    // }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            undoMove();
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            Point click = e.getPoint();
            for (Map.Entry<Character, Point> entry : mapData.nodeCoordinates.entrySet()) {
                char targetNode = entry.getKey();
                if (click.distance(entry.getValue()) < 32 && targetNode != playerNode) {
                    java.util.List<Character> path = mapData.graph.getPath(playerNode, targetNode);
                    if (path != null && path.size() > 1) {
                        moveAlongPath(path);
                    }
                    return;
                }
            }
        }
    }

    public void markDefeated(char nodeName) {
        mapData.graph.graph.adjL.get(nodeName).typeNode = 'N';
        repaint();
    }

    private void deductPlayerEnergy(int distance) {
        double effectiveDistance = distance / 10.0;
        if (playerStatus.hasWingedBoots()) effectiveDistance *= 0.5;
    
        int mpCost = (int) Math.ceil(effectiveDistance);
        int currentMp = playerStatus.getMp();
    
        if (currentMp >= mpCost) {
            playerStatus.loseMP(mpCost);
        } else {
            int mpShort = mpCost - currentMp;
            playerStatus.loseMP(currentMp); // หมดเกลี้ยง
    
            // หัก HP แทน: ทุกๆ 20 ระยะที่เกิน หัก 1 HP
            int hpLoss = (int) Math.ceil((mpShort * 10.0) / 20.0);
            playerStatus.loseHP(hpLoss);
        }
    }
    

    private void moveAlongPath(java.util.List<Character> path) {
        new Thread(() -> {
            for (int i = 1; i < path.size(); i++) {
                char from = path.get(i - 1);
                char to = path.get(i);

                int weight = mapData.graph.graph.adjL.get(from).neighbors.stream()
                        .filter(n -> n.nameNeighbor == to).map(n -> n.weight).findFirst().orElse(0);

                double effectiveDistance = weight / 10.0;
                if (playerStatus.hasWingedBoots()) effectiveDistance *= 0.5;
                int mpCost = (int) Math.ceil(effectiveDistance);

                moveHistory.push(from);
                mpCostHistory.push(mpCost);
                distanceHistory.push(weight);

                totalDistance += weight;
                deductPlayerEnergy(weight);

                if (playerStatus.getHp() <= 0) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "You ran out of energy and collapsed!");
                        int score = playerStatus.getHp() + playerStatus.getMp();
                        parent.showEndGame(score, false);
                    });
                    return;
                }

                playerNode = to;
                repaint();

                try { Thread.sleep(400); } catch (InterruptedException ignored) {}

                NodeInfo node = mapData.graph.graph.adjL.get(playerNode);
                if (node.typeNode == 'M') {
                    // 🔒 ห้าม Undo หลังเริ่มสู้: ล้าง Stack
                    moveHistory.clear();
                    mpCostHistory.clear();
                    distanceHistory.clear();

                    SwingUtilities.invokeLater(() -> parent.showFightPanel(playerNode));
                    return;
                } else if (node.typeNode == 'E') {
                    int shortest = mapData.graph.shortest(mapData.getStartNode(), playerNode);
                    int score = ScoreCalculator.calculateStageScore(shortest, totalDistance, 1000);
                    playerStatus.addScore(score);
                    JOptionPane.showMessageDialog(this, "Stage Complete!\nShortest path: " + shortest +
                            "\nYour path: " + totalDistance + "\nScore: " + score + " / 1000");
                    SwingUtilities.invokeLater(() -> parent.showShop());
                    return;
                }
            }
        }).start();
    }

    private void undoMove() {
        if (undoCount >= undoLimit) {
            JOptionPane.showMessageDialog(this, "Undo limit reached! You can only undo 5 times.");
            return;
        }
        if (!moveHistory.isEmpty() && !mpCostHistory.isEmpty() && !distanceHistory.isEmpty()) {
            char previous = moveHistory.pop();
            int refundedMp = mpCostHistory.pop();
            int refundedDist = distanceHistory.pop();
    
            playerNode = previous;
            playerStatus.restoreMP(refundedMp);
            totalDistance -= refundedDist;
    
            undoCount++;
            undoLabel.setText("Undo Left: " + (undoLimit - undoCount));
    
            // ✅ แสดงภาพแจ้งเตือน
            ImageIcon rawIcon = new ImageIcon("assets/GraphMap/undo_notice.PNG"); // หรือเปลี่ยน path ตามต้องการ
            Image resizedImage = rawIcon.getImage().getScaledInstance(232 , 43, Image.SCALE_SMOOTH); // ปรับขนาดตามต้องการ
            feedbackLabel.setIcon(new ImageIcon(resizedImage));
                
            // หายไปใน 3 วิ
            Timer timer = new Timer(1700, evt -> feedbackLabel.setIcon(null));
            timer.setRepeats(false);
            timer.start();
    
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No previous move to undo.");
        }
    }
    
    public void resetUndoHistory() {
        moveHistory.clear();
        mpCostHistory.clear();
        distanceHistory.clear();
        undoCount = 0;
        if (undoLabel != null) {
            undoLabel.setText("Undo Left: " + (undoLimit - undoCount));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
