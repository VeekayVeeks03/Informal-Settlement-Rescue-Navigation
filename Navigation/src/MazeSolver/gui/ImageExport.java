
package MazeSolver.gui; 
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;

// Enable you to pick an image from your file explorer or device

public class ImageExport{

    public static void save(GridPane grid, Stage stage) {

        try {
            WritableImage img = grid.snapshot(new SnapshotParameters(), null);

            FileChooser fc = new FileChooser();
            File file = fc.showSaveDialog(stage); // Shows the File manager window 

            if (file != null) {
                ImageIO.write(
                    SwingFXUtils.fromFXImage(img, null),
                    "png",
                    file
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}