import java.util.*;

public class AdjacecyList {
    public Map<Character, NodeInfo> adjL;

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
        } 
        else {
            adjL.get(src).addNeighbor(dest, weight);
            adjL.get(dest).addNeighbor(src, weight);
        }
    }
}