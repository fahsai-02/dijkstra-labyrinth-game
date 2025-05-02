import java.util.*;

public class NodeInfo {
    static class Neighbor {
        public char nameNeighbor;
        public int weight;

        public Neighbor(char nameNeighbor, int weight) {
            this.nameNeighbor = nameNeighbor;
            this.weight = weight;
        }
    }

    public char typeNode;
    public List<Neighbor> neighbors;

    public NodeInfo(char typeNode) {
        this.typeNode = typeNode;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(char nameNeighbor, int weight) {
        this.neighbors.add(new Neighbor(nameNeighbor, weight));
    }
}