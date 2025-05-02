// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
import java.util.*;

// ------- คลาสแทนข้อมูลของ node แต่ละจุดในกราฟ ------------
/*  ห้องว่าง Normal = 'N'
    ห้องสุ่มมอนเตอร์ Monster = 'M'
    ห่องเริ่ม Start = 'S'
    ห้องสุดท้าย End = 'E'
 */ 
class NodeInfo {
    // คลาสย่อยแทนข้อมูล Adjacency ในกราฟ โดยมีข้อมูล ชื่อ และ weight
    static class Neighbor {
        public char nameNeighbor; // ชื่อ node ปลายทางที่เชื่อมต่อ
        public int weight;        // น้ำหนักของเส้นทางนี้

        public Neighbor(char nameNeighbor, int weight) {
            this.nameNeighbor = nameNeighbor;
            this.weight = weight;
        }
    }

    public char typeNode;                   // ประเภทของห้อง node (S'=Start, 'E'=End, 'M'=Monster)
    public List<Neighbor> neighbors;        // รายชื่อ node ที่เชื่อมอยู่
    public String nameMonster;              // ชื่อมอนสเตอร์ในห้อว (ถ้ามี)

    public NodeInfo(char typeNode) {
        this.typeNode = typeNode;
        this.neighbors = new ArrayList<>(); 
    }

    public void addNeighbor(char nameNeighbor, int weight) {
        this.neighbors.add(new Neighbor(nameNeighbor, weight));
    }

    public void setMonster(String nameMonster) {
        this.nameMonster = nameMonster;
    }
}

// แทนโครงสร้างกราฟด้วย Adjacency List
class AdjacecyList {
    int numVertices;
    int numEgde;
    Map<Character, NodeInfo> adjL; // key = ชื่อ node, value = ข้อมูล node

    public AdjacecyList() {
        this.adjL = new HashMap<>();
    }

    // เพิ่ม node ใหม่เข้ากราฟ พร้อมกำหนดประเภท
    public void addVertex(char nameVertex, char typeNode) {
        if (!adjL.containsKey(nameVertex)) {
            adjL.put(nameVertex, new NodeInfo(typeNode));
        }
    }

    // เพิ่ม edge เชื่อมระหว่าง node สองตัว และกำหนดน้ำหนัก
    public void addEdge(char src, char dest, int weight) {
        if (!(adjL.containsKey(src) && adjL.containsKey(dest))) {
            System.out.println("can't add edge");
        } else {
            adjL.get(src).addNeighbor(dest, weight);
            adjL.get(dest).addNeighbor(src, weight);
        }
    }
}

// คำนวณเส้นทางสั้นที่สุดด้วย Dijkstra's Algorithm
class Dijkstra {
    Map<Character, WeightAndPrevious> shortestPath; // เก็บผลลัพธ์: ระยะทาง + node ก่อนหน้า

    public Dijkstra() {
        this.shortestPath = new HashMap<>();
    }

    // คำนวณเส้นทางสั้นที่สุดจาก node เริ่มต้นไปยังทุก node ที่เหลือ
    public void compute(Map<Character, NodeInfo> graph, char start) {
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.weight));
        Set<Character> visited = new HashSet<>();

        for (Character c : graph.keySet()) {
            shortestPath.put(c, new WeightAndPrevious(Integer.MAX_VALUE, null));
        }
        shortestPath.get(start).weight = 0;
        pq.add(new Pair(start, 0));

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

    // คืนเส้นทางจากจุดเริ่มต้นไปยังปลายทาง
    public List<Character> getPath(char dest) {
        List<Character> path = new ArrayList<>();
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

// ใช้เก็บผลลัพธ์ของ Dijkstra: ระยะทาง และ node ที่มาก่อนหน้า
class WeightAndPrevious {
    int weight;
    Character previous;

    public WeightAndPrevious(int weight, Character previous) {
        this.weight = weight;
        this.previous = previous;
    }
}

// คลาสหลัก เพื่อจัดการกราฟ
public class MyGraph {
    AdjacecyList graph;                 // โครงสร้างกราฟหลัก
    Dijkstra shortestPath;              // ตัวช่วยหาทางเดิน
    List<String> listOfMonster;         // ลิสต์มอนสเตอร์ที่มีในด่านนี้

    public MyGraph() {
        this.graph = new AdjacecyList();
        this.shortestPath = new Dijkstra();
        this.listOfMonster = new ArrayList<>();
    }

    // หาจุดเริ่มต้น (type == 'S')
    public char findStartNode() {
        for (Map.Entry<Character, NodeInfo> entry : graph.adjL.entrySet()) {
            if (entry.getValue().typeNode == 'S') {
                return entry.getKey();
            }
        }
        return ('-');
    }

    // หาจุดสิ้นสุด (type == 'E')
    public char findEndNode() {
        for (Map.Entry<Character, NodeInfo> entry : graph.adjL.entrySet()) {
            if (entry.getValue().typeNode == 'E') {
                return entry.getKey();
            }
        }
        return ('-');
    }

    // คืนลิสต์ของ node ที่เป็นห้องมอนสเตอร์ (type == 'M')
    public List<Character> findMonsterNode() {
        List<Character> monsterRoom = new ArrayList<>();
        for (Map.Entry<Character, NodeInfo> entry : graph.adjL.entrySet()) {
            if (entry.getValue().typeNode == 'M') {
                monsterRoom.add(entry.getKey());
            }
        }
        return monsterRoom;
    }

    // เพิ่ม node ใหม่เข้ากราฟ
    public void addVertex(char nameVertex, char typeNode) {
        this.graph.addVertex(nameVertex, typeNode);
    }

    // เพิ่มเส้นทางเชื่อมระหว่าง node
    public void addEdge(char src, char dest, int weight) {
        this.graph.addEdge(src, dest, weight);
    }

    // ใช้ Dijkstra หาเส้นทางสั้นที่สุดจาก start → end
    public int shortest(char start, char end) {
        shortestPath.compute(graph.adjL, start);
        return shortestPath.shortestPath.get(end).weight;
    }

    // เพิ่มมอนสเตอร์ใหม่เข้าลิสต์ของด่านนี้
    public void setMonsterInLevel(String nameMons) {
        this.listOfMonster.add(nameMons);
    }

    // ใส่มอนสเตอร์ชื่อเฉพาะลงในห้องที่กำหนด
    public void setNameMonsInRoom(char nameVertex, String nameMons) {
        graph.adjL.get(nameVertex).nameMonster = nameMons;
    }

    // คืนชื่อมอนสเตอร์ที่อยู่ใน node นั้น
    public String getNameMonsInRoom(char nameVertex) {
        return graph.adjL.get(nameVertex).nameMonster;
    }

    // สุ่มมอนสเตอร์จากลิสต์กลาง แล้วใส่ลงในทุกห้องมอนสเตอร์
    public void assignRandomMonstersToAllRooms() {
        Random rand = new Random();
        for (char room : findMonsterNode()) {
            if (!listOfMonster.isEmpty()) {
                String randomMons = listOfMonster.get(rand.nextInt(listOfMonster.size()));
                graph.adjL.get(room).setMonster(randomMons);
            }
        }
    }
}


// // --------- GraphPanel สำหรับวาดกราฟด้วย Swing ---------
// class GraphPanel extends JPanel {
//     Map<Character, NodeInfo> graph;
//     Map<Character, Point> nodePositions;
//     Map<Character, Image> nodeImages;
//     List<Character> path;
//     char selected = '-';
//     final int ICON_SIZE = 40;

//     public GraphPanel(Map<Character, NodeInfo> graph, List<Character> path) {
//         this.graph = graph;
//         this.path = path;
//         this.nodePositions = new HashMap<>();
//         this.nodeImages = new HashMap<>();

//         // ตำแหน่งแต่ละ node
//         nodePositions.put('A', new Point(100, 100));
//         nodePositions.put('B', new Point(300, 100));
//         nodePositions.put('C', new Point(200, 200));
//         nodePositions.put('D', new Point(400, 200));

//         // โหลดภาพ
//         loadImage('A', "A.avif");
//         loadImage('B', "B.png");
//         loadImage('C', "C.avif");
//         loadImage('D', "D.jpg");

//         // ติด MouseListener
//         addMouseListener(new MouseAdapter() {
//             @Override
//             public void mouseClicked(MouseEvent e) {
//                 for (Map.Entry<Character, Point> entry : nodePositions.entrySet()) {
//                     Point p = entry.getValue();
//                     int dx = e.getX() - p.x;
//                     int dy = e.getY() - p.y;

//                     if (Math.abs(dx) <= ICON_SIZE / 2 && Math.abs(dy) <= ICON_SIZE / 2) {
//                         selected = entry.getKey();
//                         System.out.println("Clicked on node: " + selected);
//                         repaint();
//                         break;
//                     }
//                 }
//             }
//         });
//     }

//     private void loadImage(char nodeName, String path) {
//         try {
//             Image img = new ImageIcon(path).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
//             nodeImages.put(nodeName, img);
//         } catch (Exception e) {
//             System.err.println("Error loading image for " + nodeName + ": " + e.getMessage());
//         }
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);

//         // วาดเส้นเชื่อม
//         g.setColor(Color.BLACK);
//         for (Map.Entry<Character, NodeInfo> entry : graph.entrySet()) {
//             char from = entry.getKey();
//             Point p1 = nodePositions.get(from);
//             for (NodeInfo.Neighbor n : entry.getValue().neighbors) {
//                 Point p2 = nodePositions.get(n.nameNeighbor);
//                 g.drawLine(p1.x, p1.y, p2.x, p2.y);
//             }
//         }

//         // วาดภาพ node
//         for (Map.Entry<Character, Point> entry : nodePositions.entrySet()) {
//             char name = entry.getKey();
//             Point p = entry.getValue();
//             Image img = nodeImages.get(name);

//             if (img != null) {
//                 g.drawImage(img, p.x - ICON_SIZE / 2, p.y - ICON_SIZE / 2, this);
//                 if (name == selected) {
//                     g.setColor(Color.RED);
//                     g.drawOval(p.x - ICON_SIZE / 2 - 2, p.y - ICON_SIZE / 2 - 2, ICON_SIZE + 4, ICON_SIZE + 4);
//                 }
//             }
//         }
//     }
// }

// --------- Main Program ---------
// public class GraphVisualizerGUI {
//     public static void main(String[] args) {
//         // สร้างกราฟ
//         AdjacecyList graph = new AdjacecyList();
//         graph.addVertex('A', 'S');
//         graph.addVertex('B', 'N');
//         graph.addVertex('C', 'N');
//         graph.addVertex('D', 'E');

//         graph.addEdge('A', 'B', 4);
//         graph.addEdge('A', 'C', 1);
//         graph.addEdge('C', 'B', 2);
//         graph.addEdge('B', 'D', 5);
//         graph.addEdge('C', 'D', 8);

//         // คำนวณ Dijkstra
//         Dijkstra dj = new Dijkstra();
//         dj.compute(graph.adjL, 'A');
//         List<Character> path = dj.getPath('D');

//         // สร้าง GUI
//         JFrame frame = new JFrame("Graph Visualizer with Dijkstra");
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setSize(600, 400);
//         frame.add(new GraphPanel(graph.adjL, path));
//         frame.setVisible(true);
//     }
// }