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
        map.setNodePosition('B', 300, 600);
        map.setNodePosition('C', 500, 450);
        map.setNodePosition('D', 700, 350);
        map.setNodePosition('E', 900, 200);
        map.setNodePosition('F', 800, 600);
        map.setNodePosition('G', 1000, 450);
        map.setNodePosition('Z', 1200, 600);

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

        map.setNodePosition('A', 100, 450);
        map.setNodePosition('B', 250, 400);
        map.setNodePosition('C', 250, 600);
        map.setNodePosition('D', 150, 200);
        map.setNodePosition('E', 400, 200);
        map.setNodePosition('F', 400, 500);
        map.setNodePosition('G', 400, 650);
        map.setNodePosition('H', 550, 400);
        map.setNodePosition('I', 550, 600);
        map.setNodePosition('J', 700, 200);
        map.setNodePosition('K', 700, 450);
        map.setNodePosition('L', 900, 250);
        map.setNodePosition('M', 1100, 450);
        map.setNodePosition('N', 900, 600);
        map.setNodePosition('O', 700, 650);
        
        map.setNodeImage('S', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('E', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('N', new ImageIcon("assets/GraphMap/General.PNG").getImage());
        map.setNodeImage('M', new ImageIcon("assets/GraphMap/MonsterRoom.PNG").getImage());
    
        return map;
    }
    
    public static MapData loadMap3() {
        MyGraph graph = new MyGraph();
    
        graph.addVertex('A', 'S');
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
        graph.addVertex('R', 'N');
        graph.addVertex('U', 'N');
        graph.addVertex('V', 'N');
        graph.addVertex('T', 'E');

        graph.addEdge('A', 'V', 155);
        graph.addEdge('A', 'B', 139);
        graph.addEdge('A', 'C', 122);
        graph.addEdge('B', 'D', 172);
        graph.addEdge('D', 'E', 107);
        graph.addEdge('E', 'I', 121);
        graph.addEdge('I', 'H', 142);
        graph.addEdge('I', 'L', 127);
        graph.addEdge('I', 'M', 185);
        graph.addEdge('E', 'J', 148);
        graph.addEdge('J', 'I', 141);
        graph.addEdge('J', 'M', 137);
        graph.addEdge('J', 'K', 130);
        graph.addEdge('E', 'F', 148);
        graph.addEdge('F', 'K', 107);
        graph.addEdge('K', 'M', 103);
        graph.addEdge('L', 'M', 132);
        graph.addEdge('M', 'T', 149);
        graph.addEdge('K', 'N', 200);
        graph.addEdge('N', 'T', 107);
        graph.addEdge('L', 'O', 150);
        graph.addEdge('V', 'G', 118);
        graph.addEdge('G', 'H', 122);
        graph.addEdge('H', 'O', 126);
        graph.addEdge('O', 'P', 101);
        graph.addEdge('P', 'Q', 177);
        graph.addEdge('Q', 'O', 156);
        graph.addEdge('O', 'R', 133);
        graph.addEdge('O', 'U', 190);
        graph.addEdge('O', 'T', 156);
        
        MapData map = new MapData("Map3", "assets/GraphMap/BgLv.png", graph, 'A');
        
        int offsetX = 50, offsetY = 50;

        map.setNodePosition('A', 100 + offsetX, 300 + offsetY);
        map.setNodePosition('B', 230 + offsetX, 200 + offsetY);
        map.setNodePosition('C', 200 + offsetX, 400 + offsetY);
        map.setNodePosition('D', 350 + offsetX, 150 + offsetY);
        map.setNodePosition('E', 500 + offsetX, 150 + offsetY);
        map.setNodePosition('F', 500 + offsetX, 300 + offsetY);
        map.setNodePosition('G', 150 + offsetX, 500 + offsetY);
        map.setNodePosition('H', 300 + offsetX, 500 + offsetY);
        map.setNodePosition('I', 500 + offsetX, 250 + offsetY);
        map.setNodePosition('J', 650 + offsetX, 150 + offsetY);
        map.setNodePosition('K', 650 + offsetX, 300 + offsetY);
        map.setNodePosition('L', 500 + offsetX, 400 + offsetY);
        map.setNodePosition('M', 700 + offsetX, 250 + offsetY);
        map.setNodePosition('N', 800 + offsetX, 350 + offsetY);
        map.setNodePosition('O', 400 + offsetX, 500 + offsetY);
        map.setNodePosition('P', 550 + offsetX, 550 + offsetY);
        map.setNodePosition('Q', 700 + offsetX, 550 + offsetY);
        map.setNodePosition('R', 300 + offsetX, 600 + offsetY);
        map.setNodePosition('T', 900 + offsetX, 250 + offsetY);
        map.setNodePosition('U', 250 + offsetX, 650 + offsetY);
        map.setNodePosition('V', 50 + offsetX, 200 + offsetY);

    
        map.setNodeImage('S', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('E', new ImageIcon("assets/GraphMap/StartAndEnd.PNG").getImage());
        map.setNodeImage('N', new ImageIcon("assets/GraphMap/General.PNG").getImage());
        map.setNodeImage('M', new ImageIcon("assets/GraphMap/MonsterRoom.PNG").getImage());
        map.setNodeImage('X', new ImageIcon("assets/GraphMap/BossRoom.PNG").getImage());
    
        return map;
    }
    
}
