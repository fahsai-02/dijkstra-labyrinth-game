import java.util.*;

// === เก็บข้อมูลของโหนดในกราฟ ===
class NodeInfo {
    static class Neighbor {
        public char nameNeighbor; // โหนดปลายทาง
        public int weight;        // น้ำหนักของเส้นเชื่อม (เช่น ระยะทาง)

        public Neighbor(char nameNeighbor, int weight) {
            this.nameNeighbor = nameNeighbor;
            this.weight = weight;
        }
    }

    public char typeNode;            // ประเภทของโหนด: 'S' start, 'E' end, 'M' monster, 'N' normal
    public List<Neighbor> neighbors; // รายชื่อโหนดที่เชื่อมต่อ
    public String nameMonster;       // ชื่อมอนสเตอร์ในห้อง (ถ้ามี)

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

// === โครงสร้างกราฟแบบ Adjacency List ===
class AdjacecyList {
    int numVertices;
    int numEgde;
    Map<Character, NodeInfo> adjL; // key = ชื่อโหนด, value = NodeInfo

    public AdjacecyList() {
        this.adjL = new HashMap<>();
    }

    public void addVertex(char nameVertex, char typeNode) {
        if (!adjL.containsKey(nameVertex)) {
            adjL.put(nameVertex, new NodeInfo(typeNode));
        }
    }

    public void addEdge(char src, char dest, int weight) {
        if (!(adjL.containsKey(src) && adjL.containsKey(dest))) {
            System.out.println("can't add edge");
        } else {
            adjL.get(src).addNeighbor(dest, weight);
            adjL.get(dest).addNeighbor(src, weight); // กราฟไม่มีทิศ
        }
    }
}

// === อัลกอริธึม Dijkstra สำหรับหาเส้นทางที่สั้นที่สุด ===
class Dijkstra {
    Map<Character, WeightAndPrevious> shortestPath;

    public Dijkstra() {
        this.shortestPath = new HashMap<>();
    }

    // คำนวณเส้นทางจากจุดเริ่มต้นไปยังทุกโหนด
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

    // สร้าง path จาก start → dest
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

// === เก็บข้อมูลน้ำหนัก และโหนดก่อนหน้า (ใช้ใน Dijkstra) ===
class WeightAndPrevious {
    int weight;
    Character previous;

    public WeightAndPrevious(int weight, Character previous) {
        this.weight = weight;
        this.previous = previous;
    }
}

// === คลาสหลักของกราฟที่ใช้ในเกม ===
public class MyGraph {
    public AdjacecyList graph;
    private Dijkstra shortestPath;
    private List<String> listOfMonster; // รายชื่อมอนสเตอร์ที่เอาไว้สุ่มใส่ห้อง

    public MyGraph() {
        this.graph = new AdjacecyList();
        this.shortestPath = new Dijkstra();
        this.listOfMonster = new ArrayList<>();
    }

    // === ฟังก์ชันช่วยหาประเภทโหนดพิเศษ ===
    public char findStartNode() {
        for (Map.Entry<Character, NodeInfo> entry : graph.adjL.entrySet()) {
            if (entry.getValue().typeNode == 'S') return entry.getKey();
        }
        return '-'; // ไม่พบ
    }

    public char findEndNode() {
        for (Map.Entry<Character, NodeInfo> entry : graph.adjL.entrySet()) {
            if (entry.getValue().typeNode == 'E') return entry.getKey();
        }
        return '-';
    }

    public List<Character> findMonsterNode() {
        List<Character> monsterRoom = new ArrayList<>();
        for (Map.Entry<Character, NodeInfo> entry : graph.adjL.entrySet()) {
            if (entry.getValue().typeNode == 'M') {
                monsterRoom.add(entry.getKey());
            }
        }
        return monsterRoom;
    }

    // === เพิ่มโหนดและเส้นทาง ===
    public void addVertex(char nameVertex, char typeNode) {
        this.graph.addVertex(nameVertex, typeNode);
    }

    public void addEdge(char src, char dest, int weight) {
        this.graph.addEdge(src, dest, weight);
    }

    // === Dijkstra ===
    public int shortest(char start, char end) {
        shortestPath.compute(graph.adjL, start);
        return shortestPath.shortestPath.get(end).weight;
    }

    public List<Character> getPath(char from, char to) {
        shortestPath.compute(graph.adjL, from);
        return shortestPath.getPath(to);
    }

    // === จัดการมอนสเตอร์ตามโหนด ===
    public void setMonsterInLevel(String nameMons) {
        this.listOfMonster.add(nameMons);
    }

    public void setNameMonsInRoom(char nameVertex, String nameMons) {
        graph.adjL.get(nameVertex).nameMonster = nameMons;
    }

    public String getNameMonsInRoom(char nameVertex) {
        return graph.adjL.get(nameVertex).nameMonster;
    }

    // === สุ่มมอนสเตอร์ใส่ในห้องที่ type 'M' ===
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
