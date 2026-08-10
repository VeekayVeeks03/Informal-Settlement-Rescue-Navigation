package navigation.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import navigation.algorithms.SimilarityDetector;
import navigation.graph.Graph;
import navigation.utils.ImageToGraph;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Popup window for comparing two settlement images.
 *
 * Layout:
 *   TOP    - title label
 *   CENTER - two side-by-side image panels (each with Upload button + canvas + info)
 *   BOTTOM - Compare button, score label, result text area
 *
 * Steps for the user:
 *   1. Click "Upload Settlement A" → image shown on left canvas
 *   2. Click "Upload Settlement B" → image shown on right canvas
 *   3. Click "Compare" → similarity score + feature table shown
 */
public class CompareWindow {

    private static final int CANVAS_W = 370;
    private static final int CANVAS_H = 300;

    // Settlement A
    private Graph  graphA;
    private Canvas canvasA;
    private Label  lblInfoA;

    // Settlement B
    private Graph  graphB;
    private Canvas canvasB;
    private Label  lblInfoB;

    // Result widgets
    private Label    lblScore;
    private TextArea txtFeatures;

    public void show(Stage owner) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(owner);
        window.setTitle("Compare Settlements");

        // -- Title --
        Label title = new Label("Settlement Similarity Comparison");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        HBox titleRow = new HBox(title);
        titleRow.setPadding(new Insets(10, 10, 6, 10));

        // -- Two image panels side by side --
        VBox panelA = buildImagePanel("Settlement A", true,  window);
        VBox panelB = buildImagePanel("Settlement B", false, window);

        HBox imagesRow = new HBox(12, panelA, panelB);
        imagesRow.setPadding(new Insets(0, 10, 0, 10));
        HBox.setHgrow(panelA, Priority.ALWAYS);
        HBox.setHgrow(panelB, Priority.ALWAYS);

        // -- Bottom: compare button + results --
        Button btnCompare = new Button("Compare");
        btnCompare.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btnCompare.setOnAction(e -> runComparison());

        lblScore = new Label("Click Compare after loading both images.");
        lblScore.setFont(Font.font("Arial", 13));

        txtFeatures = new TextArea();
        txtFeatures.setEditable(false);
        txtFeatures.setFont(Font.font("Courier New", 11));
        txtFeatures.setPrefHeight(110);
        txtFeatures.setText("Feature breakdown will appear here.");

        HBox btnRow = new HBox(btnCompare);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(8, 0, 6, 0));

        VBox bottomBox = new VBox(6, new Separator(), btnRow, lblScore, txtFeatures);
        bottomBox.setPadding(new Insets(0, 10, 10, 10));

        // -- Assemble scene --
        VBox root = new VBox(titleRow, imagesRow, bottomBox);
        Scene scene = new Scene(root, 820, 640);

        window.setScene(scene);
        window.show();
    }

    // -------------------------------------------------------------------------
    //  Build one image panel (left = A, right = B)
    // -------------------------------------------------------------------------

    private VBox buildImagePanel(String label, boolean isA, Stage window) {

        // Canvas for displaying the loaded image
        Canvas canvas = new Canvas(CANVAS_W, CANVAS_H);
        drawEmptyCanvas(canvas, label);
        if (isA) canvasA = canvas;
        else     canvasB = canvas;

        // Info label showing filename + node/edge counts
        Label info = new Label("No image loaded");
        info.setFont(Font.font("Arial", 11));
        if (isA) lblInfoA = info;
        else     lblInfoB = info;

        // Upload button
        Button btnUpload = new Button("Upload " + label);
        btnUpload.setMaxWidth(Double.MAX_VALUE);
        btnUpload.setOnAction(e -> loadImage(isA, window));

        // Panel box
        VBox panel = new VBox(8, new Label(label), canvas, info, btnUpload);
        panel.setPadding(new Insets(8));
        panel.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1;");

        Label heading = (Label) panel.getChildren().get(0);
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        return panel;
    }

    // -------------------------------------------------------------------------
    //  Load image for side A or B
    // -------------------------------------------------------------------------

    private void loadImage(boolean isA, Stage window) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Settlement Image");
        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );
        File file = fc.showOpenDialog(window);
        if (file == null) return;

        Label info = isA ? lblInfoA : lblInfoB;
        info.setText("Building graph...");

        new Thread(() -> {
            try {
                BufferedImage img   = ImageIO.read(file);
                Graph         graph = ImageToGraph.convert(img);

                Platform.runLater(() -> {
                    if (isA) { graphA = graph; }
                    else     { graphB = graph; }

                    // Draw image on the canvas
                    Canvas target = isA ? canvasA : canvasB;
                    drawImageOnCanvas(img, target);

                    info.setText(file.getName()
                        + "  |  Nodes: " + graph.getNodeCount()
                        + "  |  Edges: " + graph.getEdgeCount());
                });

            } catch (Exception ex) {
                Platform.runLater(() -> (isA ? lblInfoA : lblInfoB).setText("Error: " + ex.getMessage()));
            }
        }).start();
    }

    // -------------------------------------------------------------------------
    //  Run comparison
    // -------------------------------------------------------------------------

    private void runComparison() {
        if (graphA == null || graphB == null) {
            lblScore.setText("Please load both Settlement A and Settlement B first.");
            return;
        }

        SimilarityDetector det   = new SimilarityDetector();
        double             score = det.compare(graphA, graphB);
        String             interp = det.interpret(score);

        lblScore.setText(String.format("Similarity Score: %.1f%%  —  %s", score * 100, interp));

        // Build a feature comparison table
        String[]  names = det.featureNames();
        double[]  fA    = det.features(graphA);
        double[]  fB    = det.features(graphB);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-32s %12s %12s%n", "Feature", "Settlement A", "Settlement B"));
        sb.append("-".repeat(58)).append("\n");
        for (int i = 0; i < names.length; i++) {
            sb.append(String.format("%-32s %12.4f %12.4f%n", names[i], fA[i], fB[i]));
        }
        sb.append("-".repeat(58)).append("\n");
        sb.append(String.format("Cosine Similarity: %.4f  →  %s%n", score, interp));

        txtFeatures.setText(sb.toString());
    }

    // -------------------------------------------------------------------------
    //  Drawing helpers
    // -------------------------------------------------------------------------

    private void drawImageOnCanvas(BufferedImage img, Canvas canvas) {
        int w = (int) canvas.getWidth();
        int h = (int) canvas.getHeight();

        // Scale image to fit canvas
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();

        // Convert to JavaFX WritableImage
        WritableImage fxImg = new WritableImage(w, h);
        PixelWriter   pw    = fxImg.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                pw.setArgb(x, y, scaled.getRGB(x, y));

        canvas.getGraphicsContext2D().drawImage(fxImg, 0, 0, w, h);
    }

    private void drawEmptyCanvas(Canvas canvas, String label) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.DARKGRAY);
        gc.setFont(Font.font("Arial", 13));
        gc.fillText("No image loaded for " + label, 20, canvas.getHeight() / 2);
    }
}
