package zelda_like;

import java.util.List;

/**
 * Loads maps from /maps/{name}.txt (CSV or whitespace). Falls back to tiny embedded maps.
 * Seeds enemies/items for a quick demo scene.
 */
public class MapLoader {
    private final GamePanel gp;

    public MapLoader(GamePanel gp) { this.gp = gp; }

    public void load(String name) {
        int[][] map = gp.tileM.loadMapFromResource("/maps/overworld/" + name + ".txt");
        if (map == null) map = fallbackMap(name);
        gp.tileM.setMap(map);
    }

    public void spawnFromMap(List<Enemy> enemies, List<Item> items) {
        enemies.clear();
        items.clear();

        if ("overworld".equals(gp.currentMap)) {
            Enemy e1 = new Enemy(gp); e1.setWorldPosition(gp.tileSize * 5, gp.tileSize * 5);
            Enemy e2 = new Enemy(gp); e2.setWorldPosition(gp.tileSize * 12, gp.tileSize * 7);
            enemies.add(e1); enemies.add(e2);
            // no key in overworld; transition to dungeon via door
        } else if ("dungeon".equals(gp.currentMap)) {
            Enemy e1 = new Enemy(gp); e1.setWorldPosition(gp.tileSize * 7, gp.tileSize * 7);
            enemies.add(e1);
            // Place a key in the dungeon
            items.add(new KeyItem(gp, gp.tileSize * 10, gp.tileSize * 8));
        }
    }

    // Default tiny maps: 0=grass, 1=wall, 2=water, 3=door, 4=floor, 5=tree
    private int[][] fallbackMap(String name) {
        if ("overworld".equals(name)) {
            String[] rows = {
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 5 5 5 5 0 0 0 0 0 0 0",
                "0 0 0 0 0 5 0 0 5 0 0 0 0 0 0 0",
                "0 0 0 0 0 5 0 0 5 0 0 0 0 0 0 0",
                "0 0 0 0 0 5 0 3 5 0 0 0 0 0 0 0", // door at (7,5)
                "0 0 0 0 0 5 0 0 5 0 0 0 0 0 0 0",
                "0 0 0 0 0 5 5 5 5 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 2 2 2 2 0 0 0 0 0 0",
                "0 0 0 0 0 0 2 2 2 2 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0"
            };
            return parseRows(rows);
        } else { // dungeon
            String[] rows = {
                "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1",
                "1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 1",
                "1 4 1 1 1 4 4 4 4 4 1 1 1 4 4 1",
                "1 4 1 4 1 4 4 4 4 4 1 4 1 4 4 1",
                "1 4 1 4 1 4 4 4 4 4 1 4 1 4 4 1",
                "1 4 1 4 1 4 4 4 3 4 1 4 1 4 4 1", // door at (8,5) - exit (requires key)
                "1 4 1 4 1 4 4 4 4 4 1 4 1 4 4 1",
                "1 4 1 4 1 4 4 4 4 4 1 4 1 4 4 1",
                "1 4 1 1 1 4 4 4 4 4 1 1 1 4 4 1",
                "1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 1",
                "1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 1",
                "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1"
            };
            return parseRows(rows);
        }
    }

    private int[][] parseRows(String[] rows) {
        int rCount = rows.length;
        int cCount = rows[0].trim().split("\\s+").length;
        int[][] map = new int[rCount][cCount];
        for (int r = 0; r < rCount; r++) {
            String[] parts = rows[r].trim().split("\\s+");
            for (int c = 0; c < cCount; c++) {
                map[r][c] = Integer.parseInt(parts[c]);
            }
        }
        return map;
    }
}
