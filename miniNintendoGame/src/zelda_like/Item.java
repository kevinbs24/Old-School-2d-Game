package zelda_like;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Item {
    protected final GamePanel gp;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(8, 8, 32, 32);
    public boolean collected = false;
    protected BufferedImage sprite;

    public Item(GamePanel gp, int worldX, int worldY) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
        this.solidArea = new Rectangle(6, 6, gp.tileSize - 12, gp.tileSize - 12);
    }

    public abstract void onPickup(Player player);

    public void draw(Graphics2D g2, int camX, int camY) {
        if (collected) return;
        int sx = worldX - camX;
        int sy = worldY - camY;
        if (sprite != null) {
            g2.drawImage(sprite, sx, sy, null);
        } else {
            g2.setColor(Color.YELLOW);
            g2.fillOval(sx + 8, sy + 8, gp.tileSize - 16, gp.tileSize - 16);
        }
    }
}
