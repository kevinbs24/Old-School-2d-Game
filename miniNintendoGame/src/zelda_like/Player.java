package zelda_like;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends Entity {
    private final KeyHandler keyH;

    public int keys = 0;

    public Player(KeyHandler keyH, GamePanel gp) {
        super(gp);
        this.keyH = keyH;
        // Adjust hitbox to tile size
        this.solidArea = new Rectangle(6, 10, gp.tileSize - 12, gp.tileSize - 12);
        loadSprites();
        this.speed = 5;
    }

    private void loadSprites() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/player_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/player_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/player_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/player_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/player_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/player_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/player_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/player_right_2.png"));
        } catch (Exception e) {
            // Fallback placeholders
            BufferedImage stand = SpriteLoader.striped(80, 200, 90, gp.tileSize, gp.tileSize);
            up1 = up2 = down1 = down2 = left1 = left2 = right1 = right2 = stand;
        }
    }

    public void update() {
        int nx = worldX, ny = worldY;

        boolean moving = false;
        if (keyH.upPressed)    { ny -= speed; direction = Dir.UP; moving = true; }
        if (keyH.downPressed)  { ny += speed; direction = Dir.DOWN; moving = true; }
        if (keyH.leftPressed)  { nx -= speed; direction = Dir.LEFT; moving = true; }
        if (keyH.rightPressed) { nx += speed; direction = Dir.RIGHT; moving = true; }

        if (moving) {
            boolean blocked = gp.cChecker.checkTileCollision(this, nx, ny);
            if (!blocked) {
                worldX = nx; worldY = ny;
            }
            animate();
        } else {
            spriteCounter = 0; // idle
        }
    }

    public void draw(Graphics2D g2) {
        int camX = worldX - screenX;
        int camY = worldY - screenY;
        super.draw(g2, camX, camY);
    }
}
