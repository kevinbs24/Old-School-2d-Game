package zelda_like;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    public final int originalTileSize = 32; // 32x32 tile
    public final int scale = 2;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // World logical limits (not strict; map sizes come from TileManager/MapLoader)
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // Systems
    public final KeyHandler input = new KeyHandler();
    public final CollisionChecker cChecker = new CollisionChecker(this);
    public final TileManager tileM = new TileManager(this);
    public final MapLoader mapLoader = new MapLoader(this);

    public final Player player = new Player(input, this);
    public final List<Enemy> enemies = new ArrayList<>();
    public final List<Item> items = new ArrayList<>();

    // Map state
    public String currentMap = "worldmap01"; // "overworld" or "dungeon"

    // Thread
    private Thread thread;

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(new Color(10, 30, 60));
        setDoubleBuffered(true);
        setFocusable(true);
        addKeyListener(input);

        // Load first map + content
        mapLoader.load(currentMap);
        mapLoader.spawnFromMap(enemies, items);

        // Player initial center-on-screen
        player.screenX = screenWidth / 2 - (tileSize / 2);
        player.screenY = screenHeight / 2 - (tileSize / 2);
        player.setWorldPosition(37 * tileSize, 37 * tileSize);
    }

    public void startGameThread() {
        thread = new Thread(this, "GameLoop");
        thread.start();
    }

    @Override
    public void run() {
        final double fps = 60.0;
        final double nsPerFrame = 1_000_000_000.0 / fps;
        long last = System.nanoTime();
        double acc = 0;

        while (thread != null) {
            long now = System.nanoTime();
            acc += (now - last) / nsPerFrame;
            last = now;

            while (acc >= 1.0) {
                update();
                repaint();
                acc -= 1.0;
            }
            // simple sleep to reduce CPU
            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        }
    }

    private void update() {
        // Update player (movement + collision handled inside Player)
        player.update();

        // Enemy AI + movement
        for (Enemy e : enemies) {
            if (!e.alive) continue;
            e.updateAI(player);
            e.update();
        }

        // Item pickups
        for (Item it : items) {
            if (it.collected) continue;
            if (cChecker.entityIntersectsItem(player, it)) {
                it.collected = true;
                it.onPickup(player);
            }
        }

        // Door checks (only when player is not moving to avoid rapid bounce)
        checkDoorAtPlayer();
    }

    private void checkDoorAtPlayer() {
        int col = (player.worldX + player.solidArea.x + player.solidArea.width / 2) / tileSize;
        int row = (player.worldY + player.solidArea.y + player.solidArea.height / 2) / tileSize;
        Tile t = tileM.getTileAt(col, row);
        if (t != null && t.isDoor) {
            tryDoorTransition(); // handle key requirements inside
        }
    }

    /** Handles map transitions when on a door tile. */
    public void tryDoorTransition() {
        if ("overworld".equals(currentMap)) {
            // Enter dungeon at spawn
            currentMap = "dungeon";
            mapLoader.load(currentMap);
            enemies.clear();
            items.clear();
            mapLoader.spawnFromMap(enemies, items);
            player.setWorldPosition(2 * tileSize, 2 * tileSize);
        } else {
            // Leaving dungeon requires a key
            if (player.keys > 0) {
                player.keys--;
                currentMap = "overworld";
                mapLoader.load(currentMap);
                enemies.clear();
                items.clear();
                mapLoader.spawnFromMap(enemies, items);
                player.setWorldPosition(8 * tileSize, 6 * tileSize); // near the overworld door
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int camX = player.worldX - player.screenX;
        int camY = player.worldY - player.screenY;

        // world
        tileM.draw(g2, camX, camY, screenWidth, screenHeight);

        // items
        for (Item it : items) it.draw(g2, camX, camY);

        // enemies
        for (Enemy e : enemies) e.draw(g2, camX, camY);

        // player
        player.draw(g2);

        // HUD
        drawHud(g2);
    }

    private void drawHud(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(8, 8, 140, 40, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawString("Map: " + currentMap, 16, 28);
        g2.drawString("Keys: " + player.keys, 90, 28);
    }
}
