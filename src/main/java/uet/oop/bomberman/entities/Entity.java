package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Entity {
    /** Tọa độ của thực thể. */
    protected double x;
    protected double y;
    protected boolean existed;
    protected Image img;

    /** Dành cho thực thể chuyển động theo tọa độ Pixel như Bomber, Enemy */
    public Entity(double xPixel, double yPixel) {
        this.x = xPixel;
        this.y = yPixel;
        this.existed = true;
    }

    /** Dành cho thực thể bình thường như các Tiles hoặc Effect,v.v. */
    public Entity(double xUnit, double yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        this.existed = true;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(this.getImg(), x, y);
    }
    public abstract void update();

    public void setExisted(boolean existed) {
        this.existed = existed;
    }

    public boolean isExisted() {
        return this.existed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /** Không sửa this.getImg thành img hoặc this.img
     * Vì mỗi thằng con sẽ có getImg khác nhau.
     */
    public double getCenterX() {
        return (x + this.getImg().getWidth()) / 2;
    }

    public double getCenterY() {
        return (y + this.getImg().getHeight()) / 2;
    }

    public double getWidth() {
        return this.getImg().getWidth();
    }

    public double getHeight() {
        return this.getImg().getHeight();
    }

    public Image getImg() {
        return img;
    }

    void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
