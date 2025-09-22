package zelda_like;

import java.awt.image.BufferedImage;

public class KeyItem extends Item {
    public KeyItem(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY);
        BufferedImage keyImg = SpriteLoader.tryLoadImage("/items/key.png", gp.tileSize, gp.tileSize);
        if (keyImg != null) sprite = keyImg;
        else sprite = SpriteLoader.keyGlyph(gp.tileSize, gp.tileSize);
    }

    @Override
    public void onPickup(Player player) {
        player.keys++;
        // TODO: play sound here if you add audio later
    }
}
