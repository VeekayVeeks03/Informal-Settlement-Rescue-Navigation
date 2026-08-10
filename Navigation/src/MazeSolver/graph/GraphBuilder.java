package MazeSolver.graph;

/**
 * Converts a 2D maze grid into a graph.
 * 0 = walkable path
 * 1 = wall / blocked cell
 */
public class GraphBuilder {
    public static final int Walkable = 0;
    public static final int Wall = 1;

    private static final int[][] Directions = {
        {-1, 0}, // up
        {1, 0},  // down
        {0, -1}, // left
        {0, 1}   // right
    };

    public Graph buildGraph(int[][] grid) {
        validateGrid(grid);

        Graph graph = new Graph();
        int rows = grid.length;
        int cols = grid[0].length;

        // First pass: create nodes for walkable cells.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == Walkable) {
                    graph.addNode(new Node(row, col));
                }
            }
        }

        // Second pass: connect neighbouring walkable cells.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == Walkable) {
                    Node current = graph.getNode(row, col);

                    for (int[] direction : Directions) {
                        int nextRow = row + direction[0];
                        int nextCol = col + direction[1];

                        if (isInsideGrid(nextRow, nextCol, rows, cols)
                                && grid[nextRow][nextCol] == Walkable) {
                            graph.addEdge(current, graph.getNode(nextRow, nextCol));
                        }
                    }
                }
            }
        }

        return graph;
    }

    // Check if the nodes are within the bounds of the graph
    private boolean isInsideGrid(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private void validateGrid(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            throw new IllegalArgumentException("Grid cannot be null or empty.");
        }

        int expectedCols = grid[0].length;
        for (int row = 0; row < grid.length; row++) {
            if (grid[row] == null || grid[row].length != expectedCols) {
                throw new IllegalArgumentException("Grid must be rectangular.");
            }
        }
    }
}
