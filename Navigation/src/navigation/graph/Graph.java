package navigation.graph;

import java.util.*;

/**
 * Custom Graph ADT — undirected, weighted, adjacency-list based.
 */
public class Graph {

    private final Map<Integer, Node>       nodes         = new LinkedHashMap<>();
    private final Map<Integer, List<Edge>> adjacencyList = new LinkedHashMap<>();
    private int edgeCount = 0;

    // ── CRUD ─────────────────────────────────────────────────────────────────

    public void addNode(Node n) {
        if (!nodes.containsKey(n.id)) {
            nodes.put(n.id, n);
            adjacencyList.put(n.id, new ArrayList<>());
        }
    }

    public boolean addEdge(int srcId, int dstId, double weight) {
        if (!nodes.containsKey(srcId) || !nodes.containsKey(dstId) || srcId == dstId)
            return false;
        Node src = nodes.get(srcId);
        // prevent duplicate
        for (Edge e : adjacencyList.get(srcId))
            if (e.getOther(src).id == dstId) return false;

        Edge edge = new Edge(src, nodes.get(dstId), weight);
        adjacencyList.get(srcId).add(edge);
        adjacencyList.get(dstId).add(edge);
        edgeCount++;
        return true;
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public Node            getNode(int id)    { return nodes.get(id); }
    public boolean         containsNode(int id){ return nodes.containsKey(id); }
    public Collection<Node>getAllNodes()       { return Collections.unmodifiableCollection(nodes.values()); }
    public List<Edge>      getEdges(int id)   {
        List<Edge> e = adjacencyList.get(id);
        return e != null ? e : Collections.emptyList();
    }
    public int getNodeCount() { return nodes.size(); }
    public int getEdgeCount() { return edgeCount; }

    // ── Stats for similarity ──────────────────────────────────────────────────

    public double getDensity() {
        int n = nodes.size();
        return n < 2 ? 0 : (2.0 * edgeCount) / ((double) n * (n - 1));
    }

    public double getAverageDegree() {
        return nodes.isEmpty() ? 0 : (2.0 * edgeCount) / nodes.size();
    }

    public double getAverageEdgeWeight() {
        Set<Edge> seen = new HashSet<>();
        double sum = 0; int count = 0;
        for (List<Edge> list : adjacencyList.values())
            for (Edge e : list)
                if (seen.add(e)) { sum += e.weight; count++; }
        return count == 0 ? 0 : sum / count;
    }

    public void clear() {
        nodes.clear();
        adjacencyList.clear();
        edgeCount = 0;
    }
}
