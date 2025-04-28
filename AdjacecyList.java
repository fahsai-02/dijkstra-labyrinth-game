import java.util.*;

class NodeInfo{
    static class Neighbor{
        public char nameAdj;
        public int weight;

        public Neighbor(char nameAdj, int weight){
            this.nameAdj = nameAdj;
            this.weight = weight;
        }
    }

    public char typeNode;
    public List<Neighbor> neighbor;

    public NodeInfo(char typeNode){
        this.typeNode = typeNode;
        this.neighbor = new ArrayList<>(); 
    }

    public void addNaighbor(char nameAdj, int weight){
        this.neighbor.add(new Neighbor(nameAdj, weight));
    }
}

public class AdjacecyList{
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
            adjL.get(src).addNaighbor(dest, weight);
            adjL.get(dest).addNaighbor(src, weight);
        }
    }
}
