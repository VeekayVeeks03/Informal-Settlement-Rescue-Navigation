package navigation.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import navigation.graph.Graph;
import navigation.graph.Node;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Handles drawing on the main canvas.
 * <p>
 * The canvas shows the original uploaded image, the selected start/end points
 * and the final BFS path. The black-and-white processing image is kept hidden.
 */
public class SettlementCanvas {

    private final Canvas canvas;
    private final GraphicsContext gc;

    private BufferedImage image;
    private Graph graph;
    private List<Node> path;
    private int startId = -1;
    private int endId = -1;

    /**
     * Creates the canvas used by the main screen.
     *
     * @param width canvas width
     * @param height canvas height
     */
    public SettlementCanvas(double width, double height) {
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        drawPlaceholder();
    }

    /**
     * @return the JavaFX canvas node
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * @return width used for drawing and graph conversion
     */
    public int getCanvasWidth() {
        return (int) canvas.getWidth();
    }

    /**
     * @return height used for drawing and graph conversion
     */
    public int getCanvasHeight() {
        return (int) canvas.getHeight();
    }

    /**
     * Loads a new image and graph into the canvas.
     *
     * @param img original image selected by the user
     * @param g graph built from the processed version of the image
     */
    public void loadImage(BufferedImage img, Graph g) {
        image = img;
        graph = g;
        path = null;
        startId = -1;
        endId = -1;
        redraw();
    }

    /**
     * Sets the start marker and redraws the canvas.
     */
    public void setStart(int id) {
        startId = id;
        path = null;
        redraw();
    }

    /**
     * Sets the end marker and redraws the canvas.
     */
    public void setEnd(int id) {
        endId = id;
        path = null;
        redraw();
    }

    /**
     * Stores and draws the BFS path.
     */
    public void setPath(List<Node> p) {
        path = p;
        redraw();
    }

    /**
     * Clears the whole canvas and returns it to the start state.
     */
    public void clearAll() {
        image = null;
        graph = null;
        path = null;
        startId = -1;
        endId = -1;
        drawPlaceholder();
    }

    /**
     * Redraws the uploaded image, then draws the path and markers over it.
     */
    private void redraw() {
        if (image == null) {
            drawPlaceholder();
            return;
        }

        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        WritableImage fxImage = toFXImage(image, width, height);
        gc.drawImage(fxImage, 0, 0, width, height);

        if (path != null && path.size() > 1) {
            drawPath();
        }

        drawStartMarker();
        drawEndMarker();
    }

    /**
     * Draws the selected path as a line over the original image.
     */
    private void drawPath() {
        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(3);

        for (int i = 0; i < path.size() - 1; i++) {
            Node a = path.get(i);
            Node b = path.get(i + 1);
            gc.strokeLine(a.x, a.y, b.x, b.y);
        }
    }

    /**
     * Draws the start marker.
     */
    private void drawStartMarker() {
        if (startId != -1 && graph != null && graph.containsNode(startId)) {
            Node start = graph.getNode(startId);
            gc.setFill(Color.DODGERBLUE);
            gc.fillOval(start.x - 10, start.y - 10, 20, 20);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            gc.fillText("S", start.x - 4, start.y + 5);
        }
    }

    /**
     * Draws the end marker.
     */
    private void drawEndMarker() {
        if (endId != -1 && graph != null && graph.containsNode(endId)) {
            Node end = graph.getNode(endId);
            gc.setFill(Color.RED);
            gc.fillOval(end.x - 10, end.y - 10, 20, 20);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            gc.fillText("E", end.x - 4, end.y + 5);
        }
    }

    /**
     * Shows a blank placeholder before an image is uploaded.
     */
    private void drawPlaceholder() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.DARKGRAY);
        gc.setFont(Font.font("Arial", 16));
        gc.fillText("Upload a settlement image to begin", width / 2 - 170, height / 2);
    }

    /**
     * Converts a BufferedImage to a JavaFX image without using SwingFXUtils.
     */
    private static WritableImage toFXImage(BufferedImage src, int targetW, int targetH) {
        BufferedImage scaled = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, targetW, targetH, null);
        g.dispose();

        WritableImage out = new WritableImage(targetW, targetH);
        PixelWriter pw = out.getPixelWriter();

        for (int y = 0; y < targetH; y++) {
            for (int x = 0; x < targetW; x++) {
                pw.setArgb(x, y, scaled.getRGB(x, y));
            }
        }

        return out;
    }
}
