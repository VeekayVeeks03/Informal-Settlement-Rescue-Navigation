package MazeSolver.gui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;

/**
*This class enables the loading of the image into the GUI 
*/ 
public class ImageLoad {

    private static final int ROWS = 100;
    private static final int COLS = 100;
    private static final double THRESHOLD = 0.55;

    // Function for loading the image 
    public static Image LoadOriginalImage(File file) {
        if (file == null) {
            return null;
        }

        return new Image(file.toURI().toString());
    }

    // Converts the image to black and white to help in Path finding
    public static Image ConvertToBlackAndWhite(Image originalImage) {
        if (originalImage == null) {
            return null;
        }

        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        PixelReader pr = originalImage.getPixelReader();

        if (pr == null) {
            return null;
        }

        WritableImage bwImage = new WritableImage(width, height);
        PixelWriter pw = bwImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color color = pr.getColor(x, y);
                double brightness = color.getBrightness();

                if (brightness >= THRESHOLD) {
                    pw.setColor(x, y, Color.WHITE);
                } else {
                    pw.setColor(x, y, Color.BLACK);
                }
            }
        }

        return bwImage;
    }

    // Loading the maze
    public static Maze LoadMaze(File file) {
        Image originalImage = LoadOriginalImage(file);
        Image bwImage = ConvertToBlackAndWhite(originalImage);

        return LoadMazeFromImage(bwImage);
    }

    // Load the maze from the original image.
    // Uses the maze class to define the general characteristics of a maze 
    public static Maze LoadMazeFromImage(Image image) {
        if (image == null) {
            return null;
        }

        PixelReader pr = image.getPixelReader();

        if (pr == null) {
            return null;
        }

        int imgWidth = (int) image.getWidth();
        int imgHeight = (int) image.getHeight();

        int cellWidth = Math.max(1, imgWidth / COLS);
        int cellHeight = Math.max(1, imgHeight / ROWS);

        int[][] grid = new int[ROWS][COLS];

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                double total = 0;
                int count = 0;

                int startX = c * cellWidth;
                int startY = r * cellHeight;

                int endX = Math.min(startX + cellWidth, imgWidth);
                int endY = Math.min(startY + cellHeight, imgHeight);

                for (int y = startY; y < endY; y++) {
                    for (int x = startX; x < endX; x++) {
                        total += pr.getColor(x, y).getBrightness();
                        count++;
                    }
                }

                double avg = count == 0 ? 1.0 : total / count;

                if (avg < 0.5) {
                    grid[r][c] = Maze.WALL;
                } else {
                    grid[r][c] = Maze.PATH;
                }
            }
        }

        return new Maze(grid);
    }
}