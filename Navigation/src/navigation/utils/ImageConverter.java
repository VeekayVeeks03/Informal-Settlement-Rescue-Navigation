package navigation.utils;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageConverter {

    public static WritableImage toFXImage(BufferedImage src, int tw, int th) {
        BufferedImage scaled = new BufferedImage(tw, th, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, tw, th, null);
        g.dispose();

        WritableImage out = new WritableImage(tw, th);
        PixelWriter   pw  = out.getPixelWriter();
        for (int y = 0; y < th; y++)
            for (int x = 0; x < tw; x++)
                pw.setArgb(x, y, scaled.getRGB(x, y));
        return out;
    }

    /** Convenience — no scaling. */
    public static WritableImage toFXImage(BufferedImage src) {
        return toFXImage(src, src.getWidth(), src.getHeight());
    }
}
