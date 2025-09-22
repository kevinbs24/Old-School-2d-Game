package zelda_like;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public enum Dir { UP, DOWN, LEFT, RIGHT }

    public int worldX, worldY, screenX, screenY;
    public int speed = 2;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public Dir direction = Dir.DOWN;

    public int spriteNum = 1;
    public int spriteCounter = 0;

    public Rectangle solidArea = new Rectangle(8, 8, 32, 32);
    public boolean collisionOn = false;
    public boolean alive = true;
    public int hp = 1;

    protected final GamePanel gp;

    public Entity(GamePanel gp) { this.gp = gp; }

    public void setWorldPosition(int wx, int wy) { worldX = wx; worldY = wy; }

    public void animate() {
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public BufferedImage currentSprite() {
        switch (direction) {
            case UP:    return (spriteNum == 1) ? up1 : up2;
            case DOWN:  return (spriteNum == 1) ? down1 : down2;
            case LEFT:  return (spriteNum == 1) ? left1 : left2;
            case RIGHT: return (spriteNum == 1) ? right1 : right2;
            default:    return down1;
        }
    }

    public void draw(Graphics2D g2, int camX, int camY) {
        BufferedImage img = currentSprite();
        int sx = worldX - camX;
        int sy = worldY - camY;
        if (img != null) g2.drawImage(img, sx, sy, gp.tileSize, gp.tileSize, null);
        else {
            g2.setColor(Color.MAGENTA);
            g2.fillRect(sx, sy, gp.tileSize, gp.tileSize);
        }
    }
}
