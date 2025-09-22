package zelda_like;

import java.awt.*;

public class CollisionChecker {
    private final GamePanel gp;

    public CollisionChecker(GamePanel gp) { this.gp = gp; }

    /** True if next position is blocked by collidable tiles or out-of-bounds. */
    public boolean checkTileCollision(Entity e, int nextWorldX, int nextWorldY) {
        Rectangle box = new Rectangle(
                nextWorldX + e.solidArea.x,
                nextWorldY + e.solidArea.y,
                e.solidArea.width,
                e.solidArea.height
        );

        int leftCol   = box.x / gp.tileSize;
        int rightCol  = (box.x + box.width - 1) / gp.tileSize;
        int topRow    = box.y / gp.tileSize;
        int bottomRow = (box.y + box.height - 1) / gp.tileSize;

        if (leftCol < 0 || rightCol >= gp.tileM.getMapCols() ||
            topRow < 0 || bottomRow >= gp.tileM.getMapRows()) {
            return true; // treat outside as blocked
        }

        for (int row = topRow; row <= bottomRow; row++) {
            for (int col = leftCol; col <= rightCol; col++) {
                Tile t = gp.tileM.getTileAt(col, row);
                if (t != null && t.collision) return true;
            }
        }
        return false;
    }

    public boolean entityIntersectsItem(Entity e, Item it) {
        Rectangle a = new Rectangle(e.worldX + e.solidArea.x, e.worldY + e.solidArea.y,
                e.solidArea.width, e.solidArea.height);
        Rectangle b = new Rectangle(it.worldX + it.solidArea.x, it.worldY + it.solidArea.y,
                it.solidArea.width, it.solidArea.height);
        return a.intersects(b);
    }
}
