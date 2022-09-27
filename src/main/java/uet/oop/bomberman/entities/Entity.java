package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.others.Physics;

import java.util.Arrays;
import java.util.stream.IntStream;

import static uet.oop.bomberman.game.Gameplay.tile_map;

public abstract class Entity {
    /** Tọa độ của thực thể. */
    protected double x;
    protected double y;
    protected boolean existed;
    protected Image img;

    /** Các mode in tọa render hình ảnh */
    public static int NORMAL_MODE = 0;
    public static int CENTER_MODE = 1;
    protected int mode = NORMAL_MODE;

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

    public boolean onScreen(Gameplay gameplay) {
        return  !(x < gameplay.translate_x - Sprite.SCALED_SIZE)
                && !(x > gameplay.translate_x + BombermanGame.WIDTH * Sprite.SCALED_SIZE)
                && !(y < gameplay.translate_y - Sprite.SCALED_SIZE)
                && !(y > gameplay.translate_y + BombermanGame.HEIGHT * Sprite.SCALED_SIZE);
    }
    public void render(GraphicsContext gc, Gameplay gameplay) {
        // Whether object is on screen
        if(!onScreen(gameplay)) return ;

        if (mode == Entity.CENTER_MODE) {
            renderCenter(gc, gameplay);
        } else {
            gc.drawImage(this.getImg(), x - gameplay.translate_x, y - gameplay.translate_y);
        }
    }

    /** Hàm này coi x, y là tọa độ trung tâm **/
    private void renderCenter(GraphicsContext gc, Gameplay gameplay) {
        double renderX = x - this.getWidth() / 2;
        double renderY = y - this.getHeight() / 2;
        gc.drawImage(this.getImg(),renderX - gameplay.translate_x,renderY - gameplay.translate_y);
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
        return x + Sprite.SCALED_SIZE / 2;
    }

    public double getCenterY() {
        return y + Sprite.SCALED_SIZE / 2;
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

    public void deadAct(Gameplay gameplay) {
        return ;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }

    public void kill(){

    }
}
