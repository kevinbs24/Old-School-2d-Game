package zelda_like;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    private final GamePanel gp;

    public Tile[] tile;        // tile types
    private int[][] map;       // map[row][col]
    private int mapRows, mapCols;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        setupTiles();
        // default empty map until MapLoader loads one
        this.map = new int[12][16];
        this.mapRows = 12;
        this.mapCols = 16;
    }

    private void setupTiles() {
        try {
            // 0 grass
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
        } catch (Exception e) {
            tile[0] = new Tile();
            tile[0].image = SpriteLoader.solidColor(30, 140, 30, gp.tileSize, gp.tileSize);
        }
        try {
            // 1 wall
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            tile[1].collision = true;
        } catch (Exception e) {
            tile[1] = new Tile();
            tile[1].image = SpriteLoader.striped(100, 100, 100, gp.tileSize, gp.tileSize);
            tile[1].collision = true;
        }
        try {
            // 2 water
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            tile[2].collision = true;
        } catch (Exception e) {
            tile[2] = new Tile();
            tile[2].image = SpriteLoader.striped(40, 90, 200, gp.tileSize, gp.tileSize);
            tile[2].collision = true;
        }
        try {
            // 3 door (non-colliding; transition handled in GamePanel)
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
            //tile[3].isDoor = true;
        } catch (Exception e) {
            tile[3] = new Tile();
            tile[3].image = SpriteLoader.solidColor(200, 180, 40, gp.tileSize, gp.tileSize);
            //tile[3].isDoor = true;
        }
        try {
            // 4 dungeon floor
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[4].collision = true;
        } catch (Exception e) {
            tile[4] = new Tile();
            tile[4].image = SpriteLoader.solidColor(120, 80, 50, gp.tileSize, gp.tileSize);
            tile[4].collision = true;
        }
        try {
            // 5 tree (blocking)
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/dirt.png"));
        } catch (Exception e) {
            tile[5] = new Tile();
            tile[5].image = SpriteLoader.striped(20, 90, 20, gp.tileSize, gp.tileSize);
        }
    }

    public void setMap(int[][] newMap) {
        this.map = newMap;
        this.mapRows = newMap.length;
        this.mapCols = newMap[0].length;
    }

    public int getMapRows() { return mapRows; }
    public int getMapCols() { return mapCols; }

    public Tile getTileAt(int col, int row) {
        if (row < 0 || row >= mapRows || col < 0 || col >= mapCols) return null;
        int id = map[row][col];
        if (id < 0 || id >= tile.length) return null;
        return tile[id];
    }

    /** Utility: load map from a resource text (rows of ints) */
    public int[][] loadMapFromResource(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            java.util.List<int[]> rows = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("[,\\s]+");
                int[] r = new int[parts.length];
                for (int i = 0; i < parts.length; i++) r[i] = Integer.parseInt(parts[i]);
                rows.add(r);
            }
            if (rows.isEmpty()) return null;
            int[][] map = new int[rows.size()][rows.get(0).length];
            for (int r = 0; r < rows.size(); r++) map[r] = rows.get(r);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public void draw(Graphics2D g2, int camX, int camY, int screenW, int screenH) {
        int tileSize = gp.tileSize;

        int firstCol = Math.max(0, camX / tileSize);
        int firstRow = Math.max(0, camY / tileSize);
        int lastCol = Math.min(mapCols - 1, (camX + screenW) / tileSize + 1);
        int lastRow = Math.min(mapRows - 1, (camY + screenH) / tileSize + 1);

        for (int row = firstRow; row <= lastRow; row++) {
            for (int col = firstCol; col <= lastCol; col++) {
                int id = map[row][col];
                BufferedImage img = (id >= 0 && id < tile.length && tile[id] != null) ? tile[id].image : null;

                int worldX = col * tileSize;
                int worldY = row * tileSize;
                int screenX = worldX - camX;
                int screenY = worldY - camY;

                if (img != null) g2.drawImage(img, screenX, screenY, tileSize, tileSize, null);
                else {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(screenX, screenY, tileSize, tileSize);
                }
            }
        }
    }
}
