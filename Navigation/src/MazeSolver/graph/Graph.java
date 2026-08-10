package MazeSolver.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adjacency-list graph for the maze.
 * Each node is a walkable grid cell.
 * Defining the graph
 */
public class Graph {
    private final Map<String, Node> nodes; // Nodes converted to graph coordinates

    public Graph() {
        nodes = new HashMap<>();
    }

    private String key(int row, int col) {
        return row + "," + col;
    }

    public void addNode(Node node) {
        nodes.put(key(node.getRow(), node.getCol()), node);
    }

    public Node getNode(int row, int col) {
        return nodes.get(key(row, col));
    }

    public boolean CheckNode(int row, int col) {
        return nodes.containsKey(key(row, col)); 
    }

    public void addEdge(Node from, Node to) {
        if (from != null && to != null) {
            from.addNeighbour(to);
        }
    }

    
    public List<Node> getNeighbours(Node node) {
        if (node == null) {
            return Collections.emptyList();
        }
        return node.getNeighbours();
    }

    public Collection<Node> getAllNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    // NodeDiff in similarity
    public int getNodeCount() {
        return nodes.size();
    }

    // EdgeDiff for similarity
    // Counts number of all neighbour connections for each node in the graph
    public int getEdgeCount() {
        int total = 0;
        for (Node node : nodes.values()) {
            total += node.getNeighbours().size();
        }
        return total;
    }

    public void printGraph() {
        for (Node node : nodes.values()) {
            System.out.print(node + " -> ");
            for (Node neighbour : node.getNeighbours()) {
                System.out.print(neighbour + " ");
            }
            System.out.println();
        }
    }
}
