package zelda_like;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends Entity {
    private final Random rng = new Random();

    private enum State { WANDER, CHASE }
    private State state = State.WANDER;

    private int wanderTimer = 0;
    private int detectionRadiusTiles = 6;

    public Enemy(GamePanel gp) {
        super(gp);
        this.speed = 2;
        this.hp = 2;
        this.solidArea = new java.awt.Rectangle(6, 10, gp.tileSize - 12, gp.tileSize - 12);
        loadSprites();
        pickRandomDirection();
        wanderTimer = 45 + rng.nextInt(60);
    }

    private void loadSprites() {
        BufferedImage stand = SpriteLoader.solidColor(200, 60, 60, gp.tileSize, gp.tileSize);
        up1 = up2 = down1 = down2 = left1 = left2 = right1 = right2 = stand;

        BufferedImage d1 = SpriteLoader.tryLoadImage("/enemy/slime_down1.png", gp.tileSize, gp.tileSize);
        BufferedImage d2 = SpriteLoader.tryLoadImage("/enemy/slime_down2.png", gp.tileSize, gp.tileSize);
        if (d1 != null) down1 = d1;
        if (d2 != null) down2 = d2;
        BufferedImage r1 = SpriteLoader.tryLoadImage("/enemy/slime_right1.png", gp.tileSize, gp.tileSize);
        BufferedImage r2 = SpriteLoader.tryLoadImage("/enemy/slime_right2.png", gp.tileSize, gp.tileSize);
        if (r1 != null) right1 = r1;
        if (r2 != null) right2 = r2;
        BufferedImage l1 = SpriteLoader.tryLoadImage("/enemy/slime_left1.png", gp.tileSize, gp.tileSize);
        BufferedImage l2 = SpriteLoader.tryLoadImage("/enemy/slime_left2.png", gp.tileSize, gp.tileSize);
        if (l1 != null) left1 = l1;
        if (l2 != null) left2 = l2;
        BufferedImage u1 = SpriteLoader.tryLoadImage("/enemy/slime_up1.png", gp.tileSize, gp.tileSize);
        BufferedImage u2 = SpriteLoader.tryLoadImage("/enemy/slime_up2.png", gp.tileSize, gp.tileSize);
        if (u1 != null) up1 = u1;
        if (u2 != null) up2 = u2;
    }

    public void updateAI(Player player) {
        int dx = (player.worldX - worldX);
        int dy = (player.worldY - worldY);
        double distTiles = Math.hypot(dx, dy) / gp.tileSize;

        state = (distTiles <= detectionRadiusTiles) ? State.CHASE : State.WANDER;

        if (state == State.WANDER) {
            if (wanderTimer-- <= 0) {
                pickRandomDirection();
                wanderTimer = 45 + rng.nextInt(60);
            }
        } else {
            if (Math.abs(dx) > Math.abs(dy)) {
                direction = (dx < 0) ? Dir.LEFT : Dir.RIGHT;
            } else {
                direction = (dy < 0) ? Dir.UP : Dir.DOWN;
            }
        }
    }

    private void pickRandomDirection() {
        int r = rng.nextInt(4);
        direction = Dir.values()[r];
    }

   // @Override
    public void update() {
        int nextX = worldX, nextY = worldY;
        switch (direction) {
            case UP: nextY -= speed; break;
            case DOWN: nextY += speed; break;
            case LEFT: nextX -= speed; break;
            case RIGHT: nextX += speed; break;
        }

        boolean blocked = gp.cChecker.checkTileCollision(this, nextX, nextY);
        if (!blocked) {
            worldX = nextX; worldY = nextY;
        } else {
            pickRandomDirection();
        }
        animate();
    }

    public void draw(java.awt.Graphics2D g2, int camX, int camY) {
        super.draw(g2, camX, camY);
    }
}
