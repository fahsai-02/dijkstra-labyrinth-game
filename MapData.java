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

        map.setNodePosition('S', 150, 400);
        map.setNodePosition('A', 300, 400);
        map.setNodePosition('B', 300, 650);
        map.setNodePosition('C', 500, 500);
        map.setNodePosition('D', 700, 400);
        map.setNodePosition('E', 900, 250);
        map.setNodePosition('F', 800, 650);
        map.setNodePosition('G', 1000, 500);
        map.setNodePosition('Z', 1200, 650);

        map.setNodeImage('S', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('E', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('N', new ImageIcon("assets/GraphMap/General.PNG").getImage());
        map.setNodeImage('M', new ImageIcon("assets/GraphMap/MonsterRoom.PNG").getImage());

        return map;
    }

    public static MapData loadMap2() {
        MyGraph graph = new MyGraph();
    
        ////////////////////////////////////////////////////// แก้ด้วย
        graph.addVertex('A', 'S');
        graph.addVertex('B', 'N');
        graph.addVertex('C', 'N');
        graph.addVertex('D', 'N');
        graph.addVertex('E', 'M');
        graph.addVertex('F', 'N');
        graph.addVertex('G', 'N');
        graph.addVertex('H', 'N');
        graph.addVertex('I', 'N');
        graph.addVertex('J', 'M');
        graph.addVertex('K', 'N');
        graph.addVertex('L', 'N');
        graph.addVertex('M', 'N');
        graph.addVertex('N', 'N');
        graph.addVertex('O', 'E');
    
        ////////////////////////////////////////////////////// แก้ด้วย
        graph.addEdge('A', 'B', 100);
        graph.addEdge('B', 'C', 100);
        graph.addEdge('A', 'D', 100);
        graph.addEdge('D', 'E', 100);
        graph.addEdge('E', 'F', 100);
        graph.addEdge('F', 'G', 100);
        graph.addEdge('E', 'H', 100);
        graph.addEdge('H', 'I', 100);
        graph.addEdge('E', 'J', 100);
        graph.addEdge('J', 'K', 100);
        graph.addEdge('K', 'L', 100);
        graph.addEdge('L', 'M', 100);
        graph.addEdge('M', 'N', 100);
        graph.addEdge('N', 'O', 100);
        graph.addEdge('I', 'O', 100);
    
        MapData map = new MapData("Map2", "assets/GraphMap/BgLv.png", graph, 'A');

        int offsetX = 200, offsetY = 100;
        map.setNodePosition('A', 120 + offsetX, 540 - offsetY);
        map.setNodePosition('B', 380 + offsetX, 500 - offsetY);
        map.setNodePosition('C', 380 + offsetX, 700 - offsetY);
        map.setNodePosition('D', 240 + offsetX, 270 - offsetY);
        map.setNodePosition('E', 600 + offsetX, 270 - offsetY);
        map.setNodePosition('F', 600 + offsetX, 600 - offsetY);
        map.setNodePosition('G', 600 + offsetX, 950 - offsetY);
        map.setNodePosition('H', 800 + offsetX, 500 - offsetY);
        map.setNodePosition('I', 800 + offsetX, 850 - offsetY);
        map.setNodePosition('J', 960 + offsetX, 270 - offsetY);
        map.setNodePosition('K', 960 + offsetX, 570 - offsetY);
        map.setNodePosition('L', 1250 + offsetX, 300 - offsetY);
        map.setNodePosition('M', 1500 + offsetX, 570 - offsetY);
        map.setNodePosition('N', 1250 + offsetX, 900 - offsetY);
        map.setNodePosition('O', 960 + offsetX, 950 - offsetY);
    
        map.setNodeImage('S', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('E', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('N', new ImageIcon("assets/GraphMap/General.PNG").getImage());
        map.setNodeImage('M', new ImageIcon("assets/GraphMap/MonsterRoom.PNG").getImage());
    
        return map;
    }
    

    public static MapData loadMap3() {
        MyGraph graph = new MyGraph();
    
        ////////////////////////////////////////////////////// แก้ด้วย
        graph.addVertex('A', 'S');
        graph.addVertex('B', 'N');
        graph.addVertex('C', 'N');
        graph.addVertex('D', 'N');
        graph.addVertex('E', 'N');
        graph.addVertex('F', 'N');
        graph.addVertex('G', 'N');
        graph.addVertex('H', 'N');
        graph.addVertex('I', 'N');
        graph.addVertex('J', 'N');
        graph.addVertex('K', 'N');
        graph.addVertex('L', 'N');
        graph.addVertex('M', 'N');
        graph.addVertex('N', 'N');
        graph.addVertex('O', 'N');
        graph.addVertex('P', 'N');
        graph.addVertex('Q', 'N');
        graph.addVertex('R', 'N');
        graph.addVertex('T', 'N');
        graph.addVertex('U', 'N');
        graph.addVertex('V', 'E');
    
        ////////////////////////////////////////////////////// แก้ด้วย
        graph.addEdge('A', 'B', 10);
        graph.addEdge('B', 'C', 10);
        graph.addEdge('B', 'D', 10);
        graph.addEdge('C', 'F', 10);
        graph.addEdge('D', 'E', 10);
        graph.addEdge('E', 'F', 10);
        graph.addEdge('D', 'G', 10);
        graph.addEdge('G', 'H', 10);
        graph.addEdge('H', 'I', 10);
        graph.addEdge('I', 'J', 10);
        graph.addEdge('J', 'K', 10);
        graph.addEdge('K', 'T', 10);
        graph.addEdge('I', 'L', 10);
        graph.addEdge('L', 'M', 10);
        graph.addEdge('M', 'N', 10);
        graph.addEdge('G', 'Q', 10);
        graph.addEdge('L', 'O', 10);
        graph.addEdge('O', 'P', 10);
        graph.addEdge('M', 'R', 10);
        graph.addEdge('K', 'U', 10);
        graph.addEdge('B', 'V', 10);
    
        MapData map = new MapData("Map3", "assets/GraphMap/BgLv.png", graph, 'A');
    
        int offsetX = 200, offsetY = 100;
    
        map.setNodePosition('A', 120 + offsetX, 540 - offsetY);
        map.setNodePosition('B', 360 + offsetX, 500 - offsetY);
        map.setNodePosition('C', 360 + offsetX, 700 - offsetY);
        map.setNodePosition('D', 480 + offsetX, 500 - offsetY);
        map.setNodePosition('E', 480 + offsetX, 650 - offsetY);
        map.setNodePosition('F', 480 + offsetX, 900 - offsetY);
        map.setNodePosition('G', 600 + offsetX, 300 - offsetY);
        map.setNodePosition('H', 720 + offsetX, 300 - offsetY);
        map.setNodePosition('I', 720 + offsetX, 520 - offsetY);
        map.setNodePosition('J', 720 + offsetX, 700 - offsetY);
        map.setNodePosition('K', 720 + offsetX, 930 - offsetY);
        map.setNodePosition('L', 840 + offsetX, 520 - offsetY);
        map.setNodePosition('M', 840 + offsetX, 650 - offsetY);
        map.setNodePosition('N', 840 + offsetX, 900 - offsetY);
        map.setNodePosition('O', 1080 + offsetX, 400 - offsetY);
        map.setNodePosition('P', 1200 + offsetX, 250 - offsetY);
        map.setNodePosition('Q', 860 + offsetX, 250 - offsetY);
        map.setNodePosition('R', 1080 + offsetX, 700 - offsetY);
        map.setNodePosition('T', 1200 + offsetX, 900 - offsetY);
        map.setNodePosition('U', 1200 + offsetX, 500 - offsetY);
        map.setNodePosition('V', 360 + offsetX, 300 - offsetY);
    
        map.setNodeImage('S', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('E', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('N', new ImageIcon("assets/GraphMap/General.PNG").getImage());
        map.setNodeImage('M', new ImageIcon("assets/GraphMap/MonsterRoom.PNG").getImage());
        map.setNodeImage('X', new ImageIcon("assets/GraphMap/BossRoom.PNG").getImage());
    
        return map;
    }
    
}
