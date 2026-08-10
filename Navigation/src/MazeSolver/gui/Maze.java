package MazeSolver.gui;
/**
 * Defines how the maze should be
 */
public class Maze {

    public static final int PATH = 0;
    public static final int WALL = 1;

    private int[][] grid;

    public Maze(int[][] grid) {
        this.grid = grid;
    }

    public int getRows() {
        return grid.length;
    }

    public int getCols() {
        return grid[0].length;
    }

    public int getCell(int row, int col) {
        return grid[row][col];
    }
}