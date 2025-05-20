import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Stack;

public class GamePanel extends JPanel implements MouseListener {

    // === Game state & player ===
    private MapData mapData;                      // ข้อมูลแผนที่ (node, edge, image ฯลฯ)
    private Image bgImage;                        // พื้นหลังของแผนที่
    private char playerNode;                      // โหนดที่ผู้เล่นยืนอยู่
    private RunGame parent;                       // ตัวหลักสำหรับเปลี่ยนหน้าต่าง
    private int totalDistance = 0;                // ระยะทางสะสมของผู้เล่น
    private CharacterStatus playerStatus;         // ข้อมูล stat ของตัวละคร

    // === สำหรับ undo move ===
    private Stack<Character> moveHistory = new Stack<>();
    private Stack<Integer> mpCostHistory = new Stack<>();
    private Stack<Integer> distanceHistory = new Stack<>();

    private int undoCount = 0;
    private final int undoLimit = 5;

    // === UI element ===
    private JLabel undoLabel;
    private JLabel feedbackLabel;
    private JButton backBtn;

    public GamePanel(MapData mapData, RunGame parent, CharacterStatus playerStatus) {
        this.mapData = mapData;
        this.parent = parent;
        this.playerStatus = playerStatus;
        this.playerNode = mapData.playerStartNode;
        this.bgImage = new ImageIcon(mapData.backgroundImagePath).getImage();

        setFocusable(true);
        setLayout(null);
        addMouseListener(this);

        // ปุ่ม Undo
        backBtn = new JButton("Undo Move");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setBounds(50, 900, 150, 40);
        backBtn.addActionListener(e -> undoMove());
        add(backBtn);

        // แสดงจำนวน undo ที่เหลือ
        undoLabel = new JLabel("Undo Left: " + (undoLimit - undoCount));
        undoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        undoLabel.setForeground(Color.WHITE);
        undoLabel.setBounds(220, 900, 200, 40);
        add(undoLabel);

        // แจ้งเตือน (แสดงรูป undo สำเร็จ ฯลฯ)
        feedbackLabel = new JLabel("");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        feedbackLabel.setForeground(Color.YELLOW);
        add(feedbackLabel);

        // จัดตำแหน่ง label เวลา resize หน้าจอ
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint();
                feedbackLabel.setBounds(20, getHeight() - 60, 232, 43);
            }
        });
    }

    // วาด stat ของผู้เล่นบนจอ
    private void drawPlayerStatus(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 22));
        g.drawString("HP:  " + playerStatus.getHp(), 150, 60);
        g.drawString("MP:  " + playerStatus.getMp(), 150, 100);
        g.drawString("ATK: " + playerStatus.getAtk(), 270, 60);
        g.drawString("DEF: " + playerStatus.getDef(), 270, 100);
    }

    // วาดฉากทั้งหมด
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        drawEdges(g);
        drawNodes(g);
        drawPlayer(g);
        drawPlayerStatus(g);
    }

    // วาดเส้นระหว่างโหนด พร้อมเว้นช่องกลางสำหรับตัวเลขระยะทาง
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

                    // เว้นกลางเส้นไว้ 28px สำหรับข้อความ
                    int mx = (p1.x + p2.x) / 2;
                    int my = (p1.y + p2.y) / 2;
                    double dx = p2.x - p1.x, dy = p2.y - p1.y;
                    double len = Math.sqrt(dx * dx + dy * dy);
                    int gapX = (int) (dx / len * 28);
                    int gapY = (int) (dy / len * 28);

                    // วาดเป็น 2 เส้นเว้นตรงกลาง
                    g2.drawLine(p1.x, p1.y, mx - gapX, my - gapY);
                    g2.drawLine(mx + gapX, my + gapY, p2.x, p2.y);

                    g2.drawString(neighbor.weight + " m", mx - 22, my + 3);
                }
            }
        }
    }

    // วาดโหนดทั้งหมดในแผนที่ (พร้อมชื่อ)
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

            // g.setColor(Color.WHITE);
            // g.drawString(String.valueOf(nodeName), p.x - 5, p.y + 5);
        }
    }

    // วาดตำแหน่งผู้เล่นบน node
    private void drawPlayer(Graphics g) {
        Point p = mapData.getNodePosition(playerNode);
        g.setColor(new Color(163, 73, 164));
        g.fillOval(p.x - 10, p.y - 29, 20, 20);
        g.setColor(new Color(106, 47, 107));
        g.drawString("Player", p.x - 24, p.y - 35);
    }

    // เมื่อคลิกเมาส์ซ้ายหรือขวา
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
                    var path = mapData.graph.getPath(playerNode, targetNode);
                    if (path != null && path.size() > 1) {
                        moveAlongPath(path);
                    }
                    return;
                }
            }
        }
    }

    // เปลี่ยน node ที่ผ่านแล้วให้เป็น type N (ธรรมดา)
    public void markDefeated(char nodeName) {
        mapData.graph.graph.adjL.get(nodeName).typeNode = 'N';
        repaint();
    }

    // หัก MP ตามระยะทาง ถ้าไม่พอจะหัก HP แทน
    private void deductPlayerEnergy(int distance) {
        double effectiveDistance = distance / 10.0;
        if (playerStatus.hasWingedBoots()) effectiveDistance *= 0.5;

        int mpCost = (int) Math.ceil(effectiveDistance);
        int currentMp = playerStatus.getMp();

        if (currentMp >= mpCost) {
            playerStatus.loseMP(mpCost);
        } else {
            int mpShort = mpCost - currentMp;
            playerStatus.loseMP(currentMp);
            int hpLoss = (int) Math.ceil((mpShort * 10.0) / 20.0);
            playerStatus.loseHP(hpLoss);
        }
    }

    // เดินผ่านแต่ละ node ทีละขั้นแบบ delay พร้อมเช็คเหตุการณ์พิเศษ
    private void moveAlongPath(java.util.List<Character> path) {
        new Thread(() -> {
            for (int i = 1; i < path.size(); i++) {
                char from = path.get(i - 1);
                char to = path.get(i);

                int weight = mapData.graph.graph.adjL.get(from).neighbors.stream()
                        .filter(n -> n.nameNeighbor == to).map(n -> n.weight).findFirst().orElse(0);

                // บันทึกประวัติเพื่อให้ undo ได้
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
                    // เจอมอนสเตอร์ — เคลียร์ประวัติ undo ทิ้งก่อนเข้าสู้
                    moveHistory.clear();
                    mpCostHistory.clear();
                    distanceHistory.clear();

                    SwingUtilities.invokeLater(() -> parent.showFightPanel(playerNode));
                    return;
                } else if (node.typeNode == 'E') {
                    // เจอจุดสิ้นสุด — คำนวณคะแนน และไปยังร้านค้า
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

    // ย้อนการเดินก่อนหน้า (สูงสุด 5 ครั้ง)
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

            // แสดงรูป undo notification เล็ก ๆ ด้านล่าง
            ImageIcon rawIcon = new ImageIcon("assets/GraphMap/undo_notice.PNG");
            Image resizedImage = rawIcon.getImage().getScaledInstance(232 , 43, Image.SCALE_SMOOTH);
            feedbackLabel.setIcon(new ImageIcon(resizedImage));

            Timer timer = new Timer(1700, evt -> feedbackLabel.setIcon(null));
            timer.setRepeats(false);
            timer.start();

            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No previous move to undo.");
        }
    }

    // เคลียร์ประวัติ undo (ใช้ก่อนเริ่มด่านใหม่)
    public void resetUndoHistory() {
        moveHistory.clear();
        mpCostHistory.clear();
        distanceHistory.clear();
        undoCount = 0;
        if (undoLabel != null) {
            undoLabel.setText("Undo Left: " + (undoLimit - undoCount));
        }
    }

    // เมธอดจาก MouseListener ที่ไม่ใช้
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
