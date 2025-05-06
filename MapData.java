import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MapData {
    public String name;
    public String backgroundImagePath;
    public MyGraph graph;
    public char playerStartNode;
    public Map<Character, Point> nodeCoordinates;
    public Map<Character, Image> nodeImages;

    public MapData(String name, String bgPath, MyGraph graph, char startNode) {
        this.name = name;
        this.backgroundImagePath = bgPath;
        this.graph = graph;
        this.playerStartNode = startNode;
        this.nodeCoordinates = new HashMap<>();
        this.nodeImages = new HashMap<>();
    }

    public void setNodePosition(char nodeName, int x, int y) {
        nodeCoordinates.put(nodeName, new Point(x, y));
    }

    public Point getNodePosition(char nodeName) {
        return nodeCoordinates.get(nodeName);
    }

    public void setNodeImage(char type, Image img) {
        nodeImages.put(type, img);
    }

    public Image getNodeImage(char type) {
        return nodeImages.getOrDefault(type, null);
    }

    public char getStartNode() {
        return playerStartNode;
    }

    public static MapData loadMap1() {
        MyGraph graph = new MyGraph();

        graph.addVertex('S', 'S');
        graph.addVertex('A', 'N');
        graph.addVertex('B', 'N');
        graph.addVertex('C', 'N');
        graph.addVertex('D', 'M');
        graph.addVertex('E', 'M');
        graph.addVertex('F', 'N');
        graph.addVertex('G', 'N');
        graph.addVertex('Z', 'E');

        graph.addEdge('S', 'A', 120);
        graph.addEdge('A', 'B', 200);
        graph.addEdge('B', 'C', 150);
        graph.addEdge('C', 'D', 80);
        graph.addEdge('D', 'E', 30);
        graph.addEdge('D', 'F', 130);
        graph.addEdge('E', 'G', 70);
        graph.addEdge('F', 'G', 190);
        graph.addEdge('G', 'Z', 100); // ไป END

        MapData map = new MapData("Map1", "assets/GraphMap/BgLv.png", graph, 'S');

        map.setNodePosition('S', 150, 350);
        map.setNodePosition('A', 300, 350);
        map.setNodePosition('B', 450, 540);
        map.setNodePosition('C', 600, 400);
        map.setNodePosition('D', 750, 275);
        map.setNodePosition('E', 900, 200);
        map.setNodePosition('F', 750, 420);
        map.setNodePosition('G', 960, 370);
        map.setNodePosition('Z', 1000, 540);

        map.setNodeImage('S', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('E', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('N', new ImageIcon("assets/GraphMap/General.PNG").getImage());
        map.setNodeImage('M', new ImageIcon("assets/GraphMap/MonsterRoom.PNG").getImage());

        return map;
    }

    public static MapData loadMap2() {
        MyGraph graph = new MyGraph();
    
        graph.addVertex('A', 'S');
        graph.addVertex('B', 'N');
        graph.addVertex('C', 'N');
        graph.addVertex('D', 'N');
        graph.addVertex('E', 'M');
        graph.addVertex('F', 'N');
        graph.addVertex('G', 'N');
        graph.addVertex('H', 'N');
        graph.addVertex('I', 'M');
        graph.addVertex('J', 'M');
        graph.addVertex('K', 'N');
        graph.addVertex('L', 'N');
        graph.addVertex('M', 'N');
        graph.addVertex('N', 'M');
        graph.addVertex('O', 'E');
    
        graph.addEdge('A', 'C', 101);
        graph.addEdge('A', 'B', 123);
        graph.addEdge('B', 'D', 116);
        graph.addEdge('B', 'E', 102);
        graph.addEdge('B', 'F', 107);
        graph.addEdge('B', 'H', 101);
        graph.addEdge('C', 'F', 112);
        graph.addEdge('C', 'I', 107);
        graph.addEdge('I', 'H', 134);
        graph.addEdge('I', 'O', 112);
        graph.addEdge('I', 'G', 106);
        graph.addEdge('H', 'J', 142);
        graph.addEdge('G', 'O', 137);
        graph.addEdge('J', 'L', 142);
        graph.addEdge('J', 'K', 108);
        graph.addEdge('K', 'L', 104);
        graph.addEdge('K', 'M', 104);
        graph.addEdge('K', 'N', 108);
        graph.addEdge('N', 'O', 102);
        graph.addEdge('K', 'O', 103);
        
    
        MapData map = new MapData("Map2", "assets/GraphMap/BgLv.png", graph, 'A');

        map.setNodePosition('A', 150, 430);
        map.setNodePosition('B', 300, 360);
        map.setNodePosition('C', 300, 530);
        map.setNodePosition('D', 300, 180);
        map.setNodePosition('E', 450, 180);
        map.setNodePosition('F', 450, 430);
        map.setNodePosition('G', 450, 630);
        map.setNodePosition('H', 600, 360);
        map.setNodePosition('I', 600, 530);
        map.setNodePosition('J', 750, 180);
        map.setNodePosition('K', 750, 430);
        map.setNodePosition('L', 950, 180);
        map.setNodePosition('M', 1100, 430);
        map.setNodePosition('N', 950, 530);
        map.setNodePosition('O', 750, 630);
        
        map.setNodeImage('S', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('E', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('N', new ImageIcon("assets/GraphMap/General.PNG").getImage());
        map.setNodeImage('M', new ImageIcon("assets/GraphMap/MonsterRoom.PNG").getImage());
    
        return map;
    }
    
    public static MapData loadMap3() {
        MyGraph graph = new MyGraph();
    
        // เพิ่มจุดทั้งหมดพร้อมประเภท (S=Start, E=End, M=Monster, N=Normal)
        graph.addVertex('A', 'S');
        graph.addVertex('T', 'E');
        graph.addVertex('B', 'N');
        graph.addVertex('C', 'M');
        graph.addVertex('D', 'N');
        graph.addVertex('E', 'N');
        graph.addVertex('F', 'N');
        graph.addVertex('G', 'M');
        graph.addVertex('H', 'N');
        graph.addVertex('I', 'N');
        graph.addVertex('J', 'M');
        graph.addVertex('K', 'M');
        graph.addVertex('L', 'N');
        graph.addVertex('M', 'N');
        graph.addVertex('N', 'N');
        graph.addVertex('O', 'M');
        graph.addVertex('P', 'N');
        graph.addVertex('Q', 'N');
        graph.addVertex('U', 'M');
        graph.addVertex('R', 'N');
        graph.addVertex('S', 'N');
    
        // เพิ่มเส้นทางและระยะทางแบบสุ่มตามที่กำหนด (100–300 เมตร)
        graph.addEdge('A', 'B', 296);
        graph.addEdge('A', 'C', 114);
        graph.addEdge('A', 'I', 186);
        graph.addEdge('B', 'D', 233);
        graph.addEdge('C', 'G', 60);
        graph.addEdge('D', 'E', 232);
        graph.addEdge('E', 'F', 122);
        graph.addEdge('F', 'T', 267);
        graph.addEdge('G', 'H', 152);
        graph.addEdge('H', 'K', 188);
        graph.addEdge('H', 'O', 213);
        graph.addEdge('I', 'J', 285);
        graph.addEdge('J', 'L', 206);
        graph.addEdge('K', 'T', 290);
        graph.addEdge('L', 'M', 154);
        graph.addEdge('M', 'N', 215);
        graph.addEdge('N', 'T', 174);
        graph.addEdge('O', 'P', 225);
        graph.addEdge('O', 'U', 253);
        graph.addEdge('P', 'Q', 122);
        graph.addEdge('Q', 'T', 186);
        graph.addEdge('U', 'R', 226);
        graph.addEdge('L', 'E', 56);
        graph.addEdge('R', 'S', 201);
        graph.addEdge('B', 'H', 172);
        graph.addEdge('S', 'T', 179);
    
        MapData map = new MapData("Map3", "assets/GraphMap/BgLv.png", graph, 'A');
    
        // ตำแหน่งของ node (ค่าประมาณตาม layout ใหม่)
        int offsetX = 100, offsetY = 100;
        map.setNodePosition('A', 75 + offsetX, 300 + offsetY);
        map.setNodePosition('B', 300 + offsetX, 200 + offsetY);
        map.setNodePosition('C', 225 + offsetX, 350 + offsetY);
        map.setNodePosition('D', 500 + offsetX, 200 + offsetY);
        map.setNodePosition('E', 650 + offsetX, 175 + offsetY);
        map.setNodePosition('F', 800 + offsetX, 200 + offsetY);
        map.setNodePosition('G', 375 + offsetX, 350 + offsetY);
        map.setNodePosition('H', 550 + offsetX, 300 + offsetY);
        map.setNodePosition('I', 175 + offsetX, 125 + offsetY);
        map.setNodePosition('J', 350 + offsetX, 75 + offsetY);
        map.setNodePosition('K', 775 + offsetX, 300 + offsetY);
        map.setNodePosition('L', 525 + offsetX, 75 + offsetY);
        map.setNodePosition('M', 700 + offsetX, 100 + offsetY);
        map.setNodePosition('N', 900 + offsetX, 150 + offsetY);
        map.setNodePosition('O', 470 + offsetX, 450 + offsetY);
        map.setNodePosition('P', 675 + offsetX, 400 + offsetY);
        map.setNodePosition('Q', 875 + offsetX, 400 + offsetY);
        map.setNodePosition('R', 800 + offsetX, 500 + offsetY);
        map.setNodePosition('S', 950 + offsetX, 450 + offsetY);
        map.setNodePosition('T', 1000 + offsetX, 250 + offsetY);
        map.setNodePosition('U', 650 + offsetX, 500 + offsetY);
    
        // ใส่ไอคอนให้แต่ละประเภทห้อง
        map.setNodeImage('S', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('E', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('N', new ImageIcon("assets/GraphMap/General.PNG").getImage());
        map.setNodeImage('M', new ImageIcon("assets/GraphMap/MonsterRoom.PNG").getImage());
    
        return map;
    }
}
