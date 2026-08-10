package navigation.graph;

/**
 * Represents a connection between two graph nodes.
 * <p>
 * The graph is undirected, so the same edge can be used from either side.
 */
public class Edge {

    public final Node source;
    public final Node destination;
    public final double weight;

    /**
     * Creates an edge between two nodes.
     *
     * @param source first node
     * @param destination second node
     * @param weight movement cost between the nodes
     */
    public Edge(Node source, Node destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Returns the node on the opposite side of this edge.
     *
     * @param n one side of the edge
     * @return the other side of the edge
     */
    public Node getOther(Node n) {
        return n.id == source.id ? destination : source;
    }
}
