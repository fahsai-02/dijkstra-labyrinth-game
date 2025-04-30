import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class NodeInfo{
    static class Neighbor{
        public char nameNeighbor;
        public int weight;

        public Neighbor(char nameNeighbor, int weight){
            this.nameNeighbor = nameNeighbor;
            this.weight = weight;
        }
    }

    public char typeNode;
    public java.util.List<Neighbor> neighbors;

    public NodeInfo(char typeNode){
        this.typeNode = typeNode;
        this.neighbors = new ArrayList<>(); 
    }

    public void addNeighbor(char nameNeighbor, int weight){
        this.neighbors.add(new Neighbor(nameNeighbor, weight));
    }
}

class AdjacecyList{
    int numVertices;
    int numEgde;
    Map<Character, NodeInfo> adjL;

    public AdjacecyList(){
        this.adjL = new HashMap<>();
    }
    // เริ่มวาดกราฟ 
    public void addVertex(char nameVertex, char typeNode){
        if(!adjL.containsKey(nameVertex)){
            adjL.put(nameVertex, new NodeInfo(typeNode));
        }
    }
    public void addEdge(char src, char dest, int weight){
        if(!(adjL.containsKey(src) && adjL.containsKey(dest))){
            System.out.println("can't add egde");
        }else{
            adjL.get(src).addNeighbor(dest, weight);
            adjL.get(dest).addNeighbor(src, weight);
        }
    }
}

class Dijkstra {
    Map<Character, WeightAndPrevious> shortestPath;

    public Dijkstra() {
        this.shortestPath = new HashMap<>();
    }

    public void compute(Map<Character, NodeInfo> graph, char start) {
        // Step 1: เตรียม PriorityQueue
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.weight));
        Set<Character> visited = new HashSet<>();

        // Step 2: set ค่าเริ่มต้น
        for (Character c : graph.keySet()) {
            shortestPath.put(c, new WeightAndPrevious(Integer.MAX_VALUE, null));
        }
        shortestPath.get(start).weight = 0;
        pq.add(new Pair(start, 0));

        // Step 3: loop จนกว่าจะ empty
        while (!pq.isEmpty()) {
            Pair current = pq.poll();
            if (visited.contains(current.name)) continue;
            visited.add(current.name);

            NodeInfo node = graph.get(current.name);
            for (NodeInfo.Neighbor n : node.neighbors) {
                int newDist = shortestPath.get(current.name).weight + n.weight;
                if (newDist < shortestPath.get(n.nameNeighbor).weight) {
                    shortestPath.put(n.nameNeighbor, new WeightAndPrevious(newDist, current.name));
                    pq.add(new Pair(n.nameNeighbor, newDist));
                }
            }
        }
    }

    public java.util.List<Character> getPath(char dest) {
        java.util.List<Character> path = new ArrayList<>();
        for (Character at = dest; at != null; at = shortestPath.get(at).previous) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }
    
    static class Pair {
        char name;
        int weight;
        Pair(char name, int weight) {
            this.name = name;
            this.weight = weight;
        }
    }
}

class WeightAndPrevious {
    int weight;          // ระยะทางปัจจุบัน
    Character previous;  // node ที่มาก่อนหน้านี้

    public WeightAndPrevious(int weight, Character previous) {
        this.weight = weight;
        this.previous = previous;
    }
}

// --------- GraphPanel สำหรับวาดกราฟด้วย Swing ---------
class GraphPanel extends JPanel {
    Map<Character, NodeInfo> graph;
    Map<Character, Point> nodePositions;
    Map<Character, Image> nodeImages;
    java.util.List<Character> path;
    char selected = '-';
    final int ICON_SIZE = 40;

    public GraphPanel(Map<Character, NodeInfo> graph, java.util.List<Character> path) {
        this.graph = graph;
        this.path = path;
        this.nodePositions = new HashMap<>();
        this.nodeImages = new HashMap<>();

        // ตำแหน่งแต่ละ node
        nodePositions.put('A', new Point(100, 100));
        nodePositions.put('B', new Point(300, 100));
        nodePositions.put('C', new Point(200, 200));
        nodePositions.put('D', new Point(400, 200));

        // โหลดภาพ
        loadImage('A', "A.avif");
        loadImage('B', "B.png");
        loadImage('C', "C.avif");
        loadImage('D', "D.jpg");

        // ติด MouseListener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Map.Entry<Character, Point> entry : nodePositions.entrySet()) {
                    Point p = entry.getValue();
                    int dx = e.getX() - p.x;
                    int dy = e.getY() - p.y;

                    if (Math.abs(dx) <= ICON_SIZE / 2 && Math.abs(dy) <= ICON_SIZE / 2) {
                        selected = entry.getKey();
                        System.out.println("Clicked on node: " + selected);
                        repaint();
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

        // วาดเส้นเชื่อม
        g.setColor(Color.BLACK);
        for (Map.Entry<Character, NodeInfo> entry : graph.entrySet()) {
            char from = entry.getKey();
            Point p1 = nodePositions.get(from);
            for (NodeInfo.Neighbor n : entry.getValue().neighbors) {
                Point p2 = nodePositions.get(n.nameNeighbor);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // วาดภาพ node
        for (Map.Entry<Character, Point> entry : nodePositions.entrySet()) {
            char name = entry.getKey();
            Point p = entry.getValue();
            Image img = nodeImages.get(name);

            if (img != null) {
                g.drawImage(img, p.x - ICON_SIZE / 2, p.y - ICON_SIZE / 2, this);
                if (name == selected) {
                    g.setColor(Color.RED);
                    g.drawOval(p.x - ICON_SIZE / 2 - 2, p.y - ICON_SIZE / 2 - 2, ICON_SIZE + 4, ICON_SIZE + 4);
                }
            }
        }
    }
}

// --------- Main Program ---------
public class GraphVisualizerGUI {
    public static void main(String[] args) {
        // สร้างกราฟ
        AdjacecyList graph = new AdjacecyList();
        graph.addVertex('A', 'S');
        graph.addVertex('B', 'N');
        graph.addVertex('C', 'N');
        graph.addVertex('D', 'E');

        graph.addEdge('A', 'B', 4);
        graph.addEdge('A', 'C', 1);
        graph.addEdge('C', 'B', 2);
        graph.addEdge('B', 'D', 5);
        graph.addEdge('C', 'D', 8);

        // คำนวณ Dijkstra
        Dijkstra dj = new Dijkstra();
        dj.compute(graph.adjL, 'A');
        java.util.List<Character> path = dj.getPath('D');

        // สร้าง GUI
        JFrame frame = new JFrame("Graph Visualizer with Dijkstra");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.add(new GraphPanel(graph.adjL, path));
        frame.setVisible(true);
    }
}