package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Entity {
    /** Tọa độ của thực thể. */
    protected int x;
    protected int y;

    protected Image img;
    /** Dành cho thực thể chuyển động theo tọa độ Pixel như Bomber, Enemy */
    public Entity( int xPixel, int yPixel) {
        this.x = xPixel;
        this.y = yPixel;
    }

    public Entity( int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }
    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }
    public abstract void update();
}
