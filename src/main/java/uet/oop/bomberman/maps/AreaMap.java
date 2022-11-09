package uet.oop.bomberman.maps;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Layer;
import uet.oop.bomberman.graphics.Renderer;

import static uet.oop.bomberman.game.Gameplay.tileCode;

/** Map này là máp chính thức mỗi khu vực trong GameMap
 * Map gồm brick và wall mỗi màn (dùng mảng 2 chiều)
 * Xử lí AI các thứ trong map này.
 * */
public class AreaMap {

    private char[][] tiles;
    private Entity[][] obstacles;

    /* Tọa độ so với GameMap */
    private final int posX;
    private final int posY;
    private final int height;
    private final int width;

    public AreaMap(char[][] tiles, int posX, int posY, int width, int height) {
        this.tiles = tiles;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.init();
    }

    private void init() {
        obstacles = new Entity[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                obstacles[i][j] = GameMap.getTile(tiles[i][j], i + posY, j + posX);
            }
        }
    }

    public void render(GraphicsContext gc, Gameplay gp) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (tiles[i][j] != '.' ) {
                    obstacles[i][j].render(gc, gp);
                }
            }
        }
    }

    public void render(GraphicsContext gc, Renderer renderer) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] != '.') {
                    obstacles[i][j].render(gc, renderer);
                }
            }
        }
    }

    public void render(Layer layer) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] != '.') {
                    if(layer.lighter.tileCodes.contains(tileCode(j + posX,i + posY)) || !layer.shaderEnable || !layer.shade) obstacles[i][j].render(layer.gc, layer.renderer);
                }
            }
        }
    }
    public void setChar(int i, int j, char x) {
        tiles[j - posY][i - posX] =  x;
        obstacles[j - posY][i - posX] = GameMap.getTile(tiles[j - posY][i - posX], j, i);
    }

    public char getChar(int i, int j) {
        return tiles[j - posY][i - posX];
    }

    public boolean checkInArea(int i, int j) {
        return posX <= i && i <= posX + width - 1 && posY <= j && j <= posY + height - 1;
    }

    public Entity getEntity(int i, int j) {
        return obstacles[j - posY][i - posX];
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
