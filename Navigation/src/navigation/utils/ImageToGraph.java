package navigation.utils;

import navigation.graph.Graph;
import navigation.graph.Node;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Converts uploaded settlement images into a graph that BFS can use.
 *
 * The image is not shown as a maze on the GUI. The black-and-white conversion
 * is only used internally to decide which parts are walkable and which parts
 * are blocked.
 */
public class ImageToGraph {

    /*
     * Number of grid cells used for graph construction.
     * Bigger values give more detail, but can make the graph heavier.
     */
    private static final int GRID_ROWS = 100;
    private static final int GRID_COLS = 100;

    /*
     * Brightness limit used to decide whether an area is walkable.
     * Lower value means more areas become walkable.
     */
    private static final int THRESHOLD = 105;

    /**
     * Converts an image using its own size.
     *
     * @param img image selected by the user
     * @return graph created from the processed image
     */
    public static Graph convert(BufferedImage img) {
        return convert(img, img.getWidth(), img.getHeight());
    }

    /**
     * Converts the uploaded image into a graph using the same size as the GUI canvas.
     * This keeps click positions and graph nodes on the same scale.
     *
     * @param img source image
     * @param width canvas width
     * @param height canvas height
     * @return graph containing walkable nodes and edges
     */
    public static Graph convert(BufferedImage img, int width, int height) {
        BufferedImage resized = resize(img, width, height);
        return buildGridGraph(resized);
    }

    /**
     * Converts the image into a black-and-white image.
     * White means possible route, black means blocked.
     * This method is mainly useful for internal processing or testing.
     *
     * @param img source image
     * @return black-and-white image
     */
    public static BufferedImage toBlackAndWhite(BufferedImage img) {
        BufferedImage output = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                Color c = new Color(img.getRGB(x, y));
                int brightness = getBrightness(c);

                if (brightness >= THRESHOLD) {
                    output.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    output.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return output;
    }

    /**
     * Builds a graph from the image by dividing it into grid blocks.
     * Each block becomes one graph node if the block is mostly walkable.
     *
     * @param img resized uploaded image
     * @return graph created from walkable grid blocks
     */
    private static Graph buildGridGraph(BufferedImage img) {
        Graph graph = new Graph();

        int width = img.getWidth();
        int height = img.getHeight();

        int cellWidth = Math.max(1, width / GRID_COLS);
        int cellHeight = Math.max(1, height / GRID_ROWS);

        int[][] nodeIds = new int[GRID_ROWS][GRID_COLS];

        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {
                nodeIds[r][c] = -1;
            }
        }

        int id = 0;

        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {

                if (isCellWalkable(img, r, c, cellWidth, cellHeight)) {

                    int x = c * cellWidth + cellWidth / 2;
                    int y = r * cellHeight + cellHeight / 2;

                    graph.addNode(new Node(id, x, y));
                    nodeIds[r][c] = id;
                    id++;
                }
            }
        }

        /*
         * Four-direction movement only.
         * This matches the maze/graph approach from the mini project.
         */
        int[] dr = { -1, 1, 0, 0 };
        int[] dc = { 0, 0, -1, 1 };

        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {

                int sourceId = nodeIds[r][c];

                if (sourceId == -1) {
                    continue;
                }

                for (int i = 0; i < dr.length; i++) {

                    int nextRow = r + dr[i];
                    int nextCol = c + dc[i];

                    if (nextRow < 0 || nextRow >= GRID_ROWS || nextCol < 0 || nextCol >= GRID_COLS) {
                        continue;
                    }

                    int destinationId = nodeIds[nextRow][nextCol];

                    if (destinationId != -1) {
                        graph.addEdge(sourceId, destinationId, 1.0);
                    }
                }
            }
        }

        return graph;
    }

    /**
     * Checks if one grid cell should be walkable.
     * The decision is based on the average brightness of all pixels inside the cell.
     *
     * @param img resized image
     * @param row grid row
     * @param col grid column
     * @param cellWidth width of one grid block
     * @param cellHeight height of one grid block
     * @return true if the cell should become a graph node
     */
    private static boolean isCellWalkable(BufferedImage img, int row, int col, int cellWidth, int cellHeight) {
        int startX = col * cellWidth;
        int startY = row * cellHeight;

        int endX = Math.min(startX + cellWidth, img.getWidth());
        int endY = Math.min(startY + cellHeight, img.getHeight());

        int totalBrightness = 0;
        int count = 0;

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                Color color = new Color(img.getRGB(x, y));
                totalBrightness += getBrightness(color);
                count++;
            }
        }

        if (count == 0) {
            return false;
        }

        int averageBrightness = totalBrightness / count;

        return averageBrightness >= THRESHOLD;
    }

    /**
     * Gets a simple brightness value from a colour.
     *
     * @param color pixel colour
     * @return brightness from 0 to 255
     */
    private static int getBrightness(Color color) {
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3;
    }

    /**
     * Finds the closest graph node to the position clicked by the user.
     *
     * @param graph graph built from the processed image
     * @param px clicked x-coordinate
     * @param py clicked y-coordinate
     * @return closest node id, or -1 if the graph has no nodes
     */
    public static int closestNode(Graph graph, int px, int py) {
        int best = -1;
        double bestDistance = Double.MAX_VALUE;

        for (Node n : graph.getAllNodes()) {
            double distance = (double) (n.x - px) * (n.x - px)
                    + (double) (n.y - py) * (n.y - py);

            if (distance < bestDistance) {
                bestDistance = distance;
                best = n.id;
            }
        }

        return best;
    }

    /**
     * Resizes the image so that graph coordinates match the displayed image.
     *
     * @param src original image
     * @param width target width
     * @param height target height
     * @return resized image
     */
    private static BufferedImage resize(BufferedImage src, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();

        return resized;
    }
}
