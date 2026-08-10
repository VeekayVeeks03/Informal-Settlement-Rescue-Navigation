package navigation.main;

import navigation.ui.SettlementPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starts the JavaFX application.
 */
public class Main extends Application {

    /**
     * Builds the main window and loads the settlement navigation pane.
     */
    @Override
    public void start(Stage primaryStage) {
        SettlementPane root = new SettlementPane(primaryStage);

        Scene scene = new Scene(root, 1050, 720);

        primaryStage.setTitle("Informal Settlement Rescue Navigation");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(760);
        primaryStage.setMinHeight(520);
        primaryStage.show();
    }

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
