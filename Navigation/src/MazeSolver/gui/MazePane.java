package MazeSolver.gui;

import MazeSolver.graph.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Setting up the GUI using BorderPane 
 * Defines all the UI components 
 */
public class MazePane extends BorderPane {
    
	// Declares Buttons, labels, images and start and end nodes
    private Label lblStatus;

    private Maze maze;
    private MazeView view;

    private Image originalImage;
    private Image bwImage;

    private PathOverlayView originalView;
    private PathOverlayView bwView;
    private PathOverlayView solvedView;

    private TabPane tabPane;

    private Node selectedStart;
    private Node selectedEnd;

    Button btnUpload;
    Button btnConvert;
    Button btnSolve;
    Button btnCompare;

    // Loads the GUI by calling functions
    public MazePane(Stage stage) {

        view = new MazeView();
        originalView = new PathOverlayView();
        bwView = new PathOverlayView();
        solvedView = new PathOverlayView();

        setupGUI(stage);
        setupActions(stage);
    }

    // Setting up the GUI and Buttons their functionality
    private void setupGUI(Stage stage) {

    	// Button text
        btnUpload = new Button("Upload Image");
        btnConvert = new Button("Convert to Maze");
        btnSolve = new Button("Find Shortest Path");
        btnCompare = new Button("Compare Mazes");
       
        // The buttons are made inactive before certain operations are done
        btnConvert.setDisable(true);
        btnSolve.setDisable(true);

        btnUpload.setStyle(buttonStyle());
        btnConvert.setStyle(buttonStyle());
        btnSolve.setStyle(buttonStyle());
        btnCompare.setStyle(buttonStyle());

        HBox toolbar = new HBox(10, btnUpload, btnConvert, btnSolve, btnCompare);
        toolbar.setPadding(new Insets(12));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setStyle("-fx-background-color: #e5e7eb;");

        Tab originalTab = new Tab("Original Image", originalView);
        Tab bwTab = new Tab("Black and White", bwView);
        Tab mazeTab = new Tab("Converted Maze", view);
        Tab solvedTab = new Tab("Solved Result", solvedView);

        originalTab.setClosable(false);
        bwTab.setClosable(false);
        mazeTab.setClosable(false);
        solvedTab.setClosable(false);

        tabPane = new TabPane();
        tabPane.getTabs().addAll(originalTab, bwTab, mazeTab, solvedTab);

        lblStatus = new Label("Upload a maze image to begin.");
        lblStatus.setPadding(new Insets(10));
        lblStatus.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");

        setTop(toolbar);
        setCenter(tabPane);
        setBottom(lblStatus);

        setStyle("-fx-background-color: #f9fafb;");
    }

    private void setupActions(Stage stage) {

        btnUpload.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Maze Image");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")
            );

            File file = fileChooser.showOpenDialog(stage);

            if (file == null) {
                lblStatus.setText("No maze image selected.");
                return;
            }

            originalImage = ImageLoad.LoadOriginalImage(file);
            bwImage = ImageLoad.ConvertToBlackAndWhite(originalImage);

            if (originalImage == null || bwImage == null) {
                lblStatus.setText("Image could not be loaded.");
                return;
            }

            originalView.showImage(originalImage);
            bwView.showImage(bwImage);
            solvedView.showImage(originalImage);

            maze = null;
            selectedStart = null;
            selectedEnd = null;

            btnConvert.setDisable(false);
            btnSolve.setDisable(true);

            tabPane.getSelectionModel().select(0);

            lblStatus.setText("Image uploaded and converted to black and white. Click Convert to Maze.");
        });

        btnConvert.setOnAction(e -> {

            if (bwImage == null) {
                lblStatus.setText("Please upload an image first.");
                return;
            }

            maze = ImageLoad.LoadMazeFromImage(bwImage);

            if (maze == null) {
                lblStatus.setText("Image could not be converted to maze.");
                return;
            }

            selectedStart = null;
            selectedEnd = null;

            view.DrawMaze(maze);

            originalView.setMazeSize(maze.getRows(), maze.getCols());
            solvedView.setMazeSize(maze.getRows(), maze.getCols());

            originalView.setImageClickHandler(node -> handleSelectedPoint(node));
            view.setCellClickHandler(node -> handleSelectedPoint(node));

            btnSolve.setDisable(false);

            tabPane.getSelectionModel().select(2);

            lblStatus.setText("Maze converted. Choose start and end on the original image or converted maze.");
        });

        btnSolve.setOnAction(e -> {

            if (maze == null) {
                lblStatus.setText("Please convert the image to a maze first.");
                return;
            }

            if (selectedStart == null || selectedEnd == null) {
                lblStatus.setText("Please select both a start point and an end point first.");
                return;
            }

            BFSPathFinder bfs = new BFSPathFinder();
            List<Node> path = bfs.solve(maze, selectedStart, selectedEnd);

            if (path == null || path.isEmpty()) {
                lblStatus.setText("No path found between the selected points.");
                return;
            }

            view.highlightPath(path);

            solvedView.showImage(originalImage);
            solvedView.setMazeSize(maze.getRows(), maze.getCols());
            solvedView.drawPath(path, maze.getRows(), maze.getCols());

            tabPane.getSelectionModel().select(3);

            lblStatus.setText("Solved. Path size: " + path.size());
        });

        btnCompare.setOnAction(e -> {
            lblStatus.setText("Compare Mazes not implemented in GUI yet.");
        });
    }

    private void handleSelectedPoint(Node node) {

        if (maze == null) {
            lblStatus.setText("Please convert the image to a maze first.");
            return;
        }

        int row = node.getRow();
        int col = node.getCol();

        if (row < 0 || row >= maze.getRows() || col < 0 || col >= maze.getCols()) {
            lblStatus.setText("Selected point is outside the maze.");
            return;
        }

        if (maze.getCell(row, col) == Maze.WALL) {
            lblStatus.setText("That point is blocked. Please choose a white/walkable area.");
            return;
        }

        if (selectedStart == null) {

            selectedStart = node;

            view.highlightStart(selectedStart);
            originalView.drawPoint(selectedStart, maze.getRows(), maze.getCols(), true);

            lblStatus.setText("Start point selected: " + selectedStart + ". Now choose the end point.");

        } else if (selectedEnd == null) {

            selectedEnd = node;

            view.highlightEnd(selectedEnd);
            originalView.drawPoint(selectedEnd, maze.getRows(), maze.getCols(), false);

            lblStatus.setText("End point selected: " + selectedEnd + ". Now click Find Shortest Path.");

        } else {

            selectedStart = node;
            selectedEnd = null;

            view.DrawMaze(maze);
            view.highlightStart(selectedStart);

            originalView.showImage(originalImage);
            originalView.setMazeSize(maze.getRows(), maze.getCols());
            originalView.drawPoint(selectedStart, maze.getRows(), maze.getCols(), true);

            lblStatus.setText("New start point selected: " + selectedStart + ". Now choose the end point.");
        }
    }

    // The styling of the button 
    private String buttonStyle() {
        return """
                -fx-background-color: #2563eb;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-background-radius: 8;
                -fx-padding: 8 14 8 14;
                -fx-cursor: hand;
                """;
    }
}
