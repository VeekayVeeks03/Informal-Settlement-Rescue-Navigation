package navigation.graph;

/**
 * Represents one walkable point in the settlement graph.
 * <p>
 * The id is used by the graph structure, while x and y store the position of
 * the node on the uploaded image/canvas.
 */
public class Node {

    public final int id;
    public final int x;
    public final int y;

    /**
     * Creates a graph node.
     *
     * @param id unique node id
     * @param x x-coordinate on the image
     * @param y y-coordinate on the image
     */
    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * Nodes are compared using their id because each id is unique in the graph.
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Node && ((Node) o).id == id;
    }

    /**
     * Keeps Node working properly in hash based structures like HashSet.
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Gives readable output when debugging the path.
     */
    @Override
    public String toString() {
        return "Node(" + x + "," + y + ")";
    }
}
