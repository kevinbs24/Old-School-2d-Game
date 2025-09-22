package zelda_like;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class SpriteLoader {

    public static BufferedImage tryLoadImage(String path, int w, int h) {
        try (InputStream is = SpriteLoader.class.getResourceAsStream(path)) {
            if (is == null) return null;
            BufferedImage src = ImageIO.read(is);
            if (src == null) return null;
            if (src.getWidth() == w && src.getHeight() == h) return src;
            BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = scaled.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.drawImage(src, 0, 0, w, h, null);
            g2.dispose();
            return scaled;
        } catch (Exception e) {
            return null;
        }
    }

    public static BufferedImage solidColor(int r, int g, int b, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(r, g, b));
        g2.fillRect(0, 0, w, h);
        g2.dispose();
        return img;
    }

    public static BufferedImage striped(int r, int g, int b, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(r, g, b));
        g2.fillRect(0, 0, w, h);
        g2.setColor(new Color(0, 0, 0, 60));
        for (int y = 0; y < h; y += 6) g2.fillRect(0, y, w, 3);
        g2.dispose();
        return img;
    }

    public static BufferedImage keyGlyph(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(240, 220, 40));
        g2.fillOval(w/4, h/4, w/2, h/2);
        g2.fillRect(w/2, h/2 - 4, w/3, 8);
        g2.setColor(Color.black);
        g2.drawOval(w/4, h/4, w/2, h/2);
        g2.dispose();
        return img;
    }
}
