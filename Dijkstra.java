import java.util.*;

public class Dijkstra {
    Map<Character, WeightAndPrevious> shortestPath;

    public Dijkstra() {
        this.shortestPath = new HashMap<>();
    }

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
