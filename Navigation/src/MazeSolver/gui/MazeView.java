package MazeSolver.gui;

import MazeSolver.graph.Node;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.function.Consumer;
/**
 * Drawing the maze to the GUI 
 */
public class MazeView extends GridPane {

    private Pane[][] cells;
    private Maze maze;

    private static final int CELLSIZE = 6;

    private Consumer<Node> cellClickHandler;

    public MazeView() {

        setAlignment(Pos.CENTER);
        setHgap(0);
        setVgap(0);
        setStyle("-fx-background-color: #f4f4f4;");
    }

    public void setCellClickHandler(Consumer<Node> cellClickHandler) {
        this.cellClickHandler = cellClickHandler;
    }
 
    // Function that draws the maze
    public void DrawMaze(Maze maze) {

        this.maze = maze;

        getChildren().clear();

        cells = new Pane[maze.getRows()][maze.getCols()];

        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {

                Pane cell = new Pane();

                // set size 
                cell.setPrefSize(CELLSIZE, CELLSIZE);
                cell.setMinSize(CELLSIZE, CELLSIZE);
                cell.setMaxSize(CELLSIZE, CELLSIZE);

                final int row = r;
                final int col = c;

                if (maze.getCell(r, c) == Maze.WALL) {

                    cell.setStyle("-fx-background-color: #1f2933;");

                } else {

                    cell.setStyle("-fx-background-color: #f8fafc;");

                    cell.setOnMouseClicked(e -> {

                        if (cellClickHandler != null) {
                            cellClickHandler.accept(new Node(row, col));
                        }
                    });
                }

                cells[r][c] = cell;
                add(cell, c, r);
            }
        }
    }

    // Function that shows the start dot
    public void highlightStart(Node start) {

        if (start == null || cells == null) {
            return;
        }

        int row = start.getRow();
        int col = start.getCol();

        if (isValidCell(row, col)) {
            cells[row][col].setStyle("-fx-background-color: #3b82f6;");
        }
    }
 
    // Function that shows the end dot
    public void highlightEnd(Node end) {

        if (end == null || cells == null) {
            return;
        }

        int row = end.getRow();
        int col = end.getCol();

        if (isValidCell(row, col)) {
            cells[row][col].setStyle("-fx-background-color: #ef4444;");
        }
    }

    public void highlightPath(List<Node> path) {

        if (path == null || path.isEmpty() || cells == null) {
            return;
        }

        for (Node n : path) {

            int row = n.getRow();
            int col = n.getCol();

            if (isValidCell(row, col)) {
                cells[row][col].setStyle("-fx-background-color: #22c55e;");
            }
        }

        Node start = path.get(0);
        Node end = path.get(path.size() - 1);

        highlightStart(start);
        highlightEnd(end);
    }

    // Check for if it is in bounds
    private boolean isValidCell(int row, int col) {

        return row >= 0 && row < cells.length
                && col >= 0 && col < cells[0].length;
    }
}