package MazeSolver.ImageProcessing;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
public class pixels {
	public static void main(String[] args) {
        try {
            // Load image
            BufferedImage image = ImageIO.read(new File("data/maze.png"));

            int width = image.getWidth();
            int height = image.getHeight();

            // 2D array to store 0s and 1s
            int[][] binaryPixels = new int[height][width];

            // Threshold (you can adjust this)
            int threshold = 128;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    int pixel = image.getRGB(x, y);

                    // Extract RGB
                    int red   = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue  = pixel & 0xff;

                    // Convert to grayscale (brightness)
                    int gray = (red + green + blue) / 3;

                    // Convert to binary (0 or 1)
                    if (gray >= threshold) {
                        binaryPixels[y][x] = 1; // white
                    } else {
                        binaryPixels[y][x] = 0; // black
                    }
                }
            }

            // Display result
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    System.out.print(binaryPixels[y][x] + " ");
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
