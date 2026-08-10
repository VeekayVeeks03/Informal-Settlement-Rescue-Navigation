package MazeSolver.gui;

import MazeSolver.graph.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.function.Consumer;

public class PathOverlayView extends StackPane {

    private ImageView imageView;
    private Canvas canvas;

    private Image originalImage;

    private int mazeRows;
    private int mazeCols;

    private Consumer<Node> imageClickHandler;

    public PathOverlayView() {

        imageView = new ImageView();
        canvas = new Canvas();

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(650);
        imageView.setFitHeight(550);

        getChildren().addAll(imageView, canvas);

        setStyle("-fx-background-color: white; -fx-padding: 10;");

        imageView.setOnMouseClicked(e -> {

            if (imageClickHandler == null || originalImage == null || mazeRows <= 0 || mazeCols <= 0) {
                return;
            }

            double displayedWidth = imageView.getBoundsInParent().getWidth();
            double displayedHeight = imageView.getBoundsInParent().getHeight();

            if (displayedWidth <= 0 || displayedHeight <= 0) {
                return;
            }

            double x = e.getX();
            double y = e.getY();

            int col = (int) ((x / displayedWidth) * mazeCols);
            int row = (int) ((y / displayedHeight) * mazeRows);

            if (row < 0) {
                row = 0;
            }

            if (col < 0) {
                col = 0;
            }

            if (row >= mazeRows) {
                row = mazeRows - 1;
            }

            if (col >= mazeCols) {
                col = mazeCols - 1;
            }

            imageClickHandler.accept(new Node(row, col));
        });
    }

    public void showImage(Image image) {

        this.originalImage = image;

        imageView.setImage(image);

        canvas.setWidth(imageView.getFitWidth());
        canvas.setHeight(imageView.getFitHeight());

        clearPath();
    }

    public void setMazeSize(int mazeRows, int mazeCols) {
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
    }

    public void setImageClickHandler(Consumer<Node> imageClickHandler) {
        this.imageClickHandler = imageClickHandler;
    }

    public void clearPath() {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawPoint(Node node, int mazeRows, int mazeCols, boolean isStart) {

        if (node == null || originalImage == null) {
            return;
        }

        double displayedWidth = imageView.getBoundsInParent().getWidth();
        double displayedHeight = imageView.getBoundsInParent().getHeight();

        if (displayedWidth <= 0 || displayedHeight <= 0) {
            displayedWidth = imageView.getFitWidth();
            displayedHeight = imageView.getFitHeight();
        }

        canvas.setWidth(displayedWidth);
        canvas.setHeight(displayedHeight);

        double cellWidth = displayedWidth / mazeCols;
        double cellHeight = displayedHeight / mazeRows;

        double x = (node.getCol() + 0.5) * cellWidth;
        double y = (node.getRow() + 0.5) * cellHeight;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (isStart) {
            gc.setFill(Color.DODGERBLUE);
        } else {
            gc.setFill(Color.RED);
        }

        gc.fillOval(x - 7, y - 7, 14, 14);
    }

    public void drawPath(List<Node> path, int mazeRows, int mazeCols) {

        if (originalImage == null || path == null || path.isEmpty()) {
            return;
        }

        double displayedWidth = imageView.getBoundsInParent().getWidth();
        double displayedHeight = imageView.getBoundsInParent().getHeight();

        if (displayedWidth <= 0 || displayedHeight <= 0) {
            displayedWidth = imageView.getFitWidth();
            displayedHeight = imageView.getFitHeight();
        }

        canvas.setWidth(displayedWidth);
        canvas.setHeight(displayedHeight);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double cellWidth = displayedWidth / mazeCols;
        double cellHeight = displayedHeight / mazeRows;

        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(3);

        Node first = path.get(0);

        double startX = (first.getCol() + 0.5) * cellWidth;
        double startY = (first.getRow() + 0.5) * cellHeight;

        gc.beginPath();
        gc.moveTo(startX, startY);

        for (Node node : path) {

            double x = (node.getCol() + 0.5) * cellWidth;
            double y = (node.getRow() + 0.5) * cellHeight;

            gc.lineTo(x, y);
        }

        gc.stroke();

        drawStartAndEndPoints(gc, path, cellWidth, cellHeight);
    }

    private void drawStartAndEndPoints(GraphicsContext gc, List<Node> path, double cellWidth, double cellHeight) {

        Node start = path.get(0);
        Node end = path.get(path.size() - 1);

        double startX = (start.getCol() + 0.5) * cellWidth;
        double startY = (start.getRow() + 0.5) * cellHeight;

        double endX = (end.getCol() + 0.5) * cellWidth;
        double endY = (end.getRow() + 0.5) * cellHeight;

        gc.setFill(Color.DODGERBLUE);
        gc.fillOval(startX - 7, startY - 7, 14, 14);

        gc.setFill(Color.RED);
        gc.fillOval(endX - 7, endY - 7, 14, 14);
    }
}