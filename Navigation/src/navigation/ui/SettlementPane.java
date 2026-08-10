package navigation.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import navigation.algorithms.BFS;
import navigation.graph.Graph;
import navigation.graph.Node;
import navigation.utils.ImageToGraph;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * Main screen of the application.
 * <p>
 * This class keeps the Test3 GUI style, but the image is processed internally
 * before pathfinding. The user only sees the real uploaded image.
 */
public class SettlementPane extends BorderPane {

    private final Button btnUpload = new Button("Upload Image");
    private final Button btnShortestPath = new Button("Find Shortest Path");
    private final Button btnClear = new Button("Clear");
    private final Button btnCompare = new Button("Compare Settlements");

    private final Label lblHint = new Label("");
    private final Label lblStatus = new Label("Upload a settlement image to begin.");

    private final SettlementCanvas canvas = new SettlementCanvas(860, 560);

    private BufferedImage currentImage;
    private Graph currentGraph;
    private int startId = -1;
    private int endId = -1;
    private boolean settingStart = true;

    private final Stage stage;

    /**
     * Creates the main pane used by the JavaFX scene.
     *
     * @param stage main application stage
     */
    public SettlementPane(Stage stage) {
        this.stage = stage;
        buildLayout();
        wireActions();
    }

    /**
     * Builds the top toolbar, image canvas area and status bar.
     */
    private void buildLayout() {
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(8));
        toolbar.setAlignment(Pos.CENTER_LEFT);

        btnShortestPath.setDisable(true);

        Separator sep = new Separator(javafx.geometry.Orientation.VERTICAL);
        toolbar.getChildren().addAll(btnUpload, btnShortestPath, btnClear, sep, btnCompare, new Spacer(), lblHint);

        ScrollPane scroll = new ScrollPane(canvas.getCanvas());
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        HBox statusBar = new HBox(lblStatus);
        statusBar.setPadding(new Insets(5, 8, 5, 8));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");

        setTop(toolbar);
        setCenter(scroll);
        setBottom(statusBar);
    }

    /**
     * Connects the buttons and canvas clicks to the program logic.
     */
    private void wireActions() {
        btnUpload.setOnAction(e -> loadSettlementImage());

        canvas.getCanvas().setOnMouseClicked(e -> {
            if (currentGraph == null) {
                lblStatus.setText("Upload an image first.");
                return;
            }

            int nodeId = ImageToGraph.closestNode(currentGraph, (int) e.getX(), (int) e.getY());
            if (nodeId == -1) {
                lblStatus.setText("No walkable point found near that click.");
                return;
            }

            Node node = currentGraph.getNode(nodeId);

            if (settingStart) {
                startId = nodeId;
                endId = -1;
                canvas.setStart(startId);
                settingStart = false;
                btnShortestPath.setDisable(true);
                lblHint.setText("Now click map to set END");
                lblStatus.setText("Start set at (" + node.x + ", " + node.y + "). Click the destination point.");
            } else {
                endId = nodeId;
                canvas.setEnd(endId);
                settingStart = true;
                btnShortestPath.setDisable(false);
                lblHint.setText("Click Find Shortest Path");
                lblStatus.setText("End set at (" + node.x + ", " + node.y + "). Click Find Shortest Path.");
            }
        });

        btnShortestPath.setOnAction(e -> runBFS());

        btnClear.setOnAction(e -> clearEverything());

        btnCompare.setOnAction(e -> new CompareWindow().show(stage));
    }

    /**
     * Lets the user upload the original image and builds a hidden graph from
     * its black-and-white processed version.
     */
    private void loadSettlementImage() {
        File file = pickImageFile();
        if (file == null) {
            return;
        }

        lblStatus.setText("Loading image and building graph, please wait...");

        new Thread(() -> {
            try {
                BufferedImage image = ImageIO.read(file);
                Graph graph = ImageToGraph.convert(image, canvas.getCanvasWidth(), canvas.getCanvasHeight());

                Platform.runLater(() -> {
                    currentImage = image;
                    currentGraph = graph;
                    startId = -1;
                    endId = -1;
                    settingStart = true;

                    canvas.loadImage(image, graph);
                    btnShortestPath.setDisable(true);
                    lblHint.setText("Click map to set START");
                    lblStatus.setText("Loaded: " + file.getName()
                            + "  |  Nodes: " + graph.getNodeCount()
                            + "  |  Edges: " + graph.getEdgeCount());
                });
            } catch (Exception ex) {
                Platform.runLater(() -> lblStatus.setText("Error loading image: " + ex.getMessage()));
            }
        }).start();
    }

    /**
     * Runs BFS between the points chosen by the user.
     */
    private void runBFS() {
        if (currentGraph == null) {
            lblStatus.setText("Upload an image first.");
            return;
        }

        if (startId == -1 || endId == -1) {
            lblStatus.setText("Please choose both a start point and an end point first.");
            return;
        }

        long startTime = System.currentTimeMillis();
        List<Node> path = new BFS().findPath(currentGraph, startId, endId);
        long ms = System.currentTimeMillis() - startTime;

        canvas.setPath(path);

        if (path.isEmpty()) {
            lblStatus.setText("BFS: No path found between the selected points.");
        } else {
            lblStatus.setText("BFS complete - " + path.size() + " steps in path  |  " + ms + " ms");
        }
    }

    /**
     * Clears the current image, graph, markers and path.
     */
    private void clearEverything() {
        currentImage = null;
        currentGraph = null;
        startId = -1;
        endId = -1;
        settingStart = true;

        canvas.clearAll();
        btnShortestPath.setDisable(true);
        lblHint.setText("");
        lblStatus.setText("Cleared. Upload an image to begin.");
    }

    /**
     * Opens the image chooser used by the upload button.
     */
    private File pickImageFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Settlement Image");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );
        return fc.showOpenDialog(stage);
    }

    /**
     * Small spacer used to push the hint label to the right side.
     */
    private static class Spacer extends Region {
        Spacer() {
            HBox.setHgrow(this, Priority.ALWAYS);
        }
    }
}
