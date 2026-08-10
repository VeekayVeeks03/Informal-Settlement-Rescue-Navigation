package MazeSolver.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents one walkable cell in the maze grid.
 * row = y-coordinate, col = x-coordinate.
 */
public class Node {
    private final int row;
    private final int col;
    private final List<Node> neighbours;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        this.neighbours = new ArrayList<>();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void addNeighbour(Node node) {
        if (node != null && !neighbours.contains(node)) {
            neighbours.add(node);
        }
    }

    public List<Node> getNeighbours() {
        return Collections.unmodifiableList(neighbours);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Node)) {
            return false;
        }
        Node other = (Node) object;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
