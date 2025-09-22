package zelda_like;

import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;

    // Door support (tile 3 by default)
    public boolean isDoor = false;

    public Tile() {}
}
