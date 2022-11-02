package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.Layer;
import uet.oop.bomberman.graphics.Renderer;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.others.Physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;

import static uet.oop.bomberman.game.Gameplay.tileCode;
import static uet.oop.bomberman.game.Gameplay.tile_map;

public abstract class Entity {
    /** Tọa độ của thực thể. */
    protected double x;
    protected double y;
    protected boolean existed;
    protected Image img;
    protected Effect effect;
    /** Các mode in tọa render hình ảnh */
    public static final int NORMAL_MODE = 0;
    public static final int CENTER_MODE = 1;
    public static final int BOTTOM_MODE = 2;
    protected int mode = NORMAL_MODE;
    //image shifting
    protected double shiftX = 0;
    protected double shiftY = 0;

    protected int tileX;
    protected int tileY;

    /** Dành cho thực thể chuyển động theo tọa độ Pixel như Bomber, Enemy */
    public Entity(double xPixel, double yPixel) {
        this.x = xPixel;
        this.y = yPixel;
        this.existed = true;
    }

    /** Dành cho thực thể bình thường như các Tiles hoặc Effect,v.v. */
    public Entity(double xUnit, double yUnit, Image img) {
        this.tileX =(int) xUnit;
        this.tileY =(int) yUnit;
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        this.existed = true;
    }

    /** Kiểm tra thực thể có nằm trong khoảng hiện trên màn hình không */
    public boolean onScreen(Gameplay gameplay) {
        return  !(x < gameplay.translate_x - this.getWidth())
                && !(x > gameplay.translate_x + BombermanGame.WIDTH * Sprite.SCALED_SIZE)
                && !(y < gameplay.translate_y - this.getHeight())
                && !(y > gameplay.translate_y + BombermanGame.HEIGHT * Sprite.SCALED_SIZE);
    }

    /** Hiển thị màn hình dựa theo map của gameplay */
    public void render(GraphicsContext gc, Gameplay gameplay) {

        gc.setEffect(effect);
        // Whether object is on screen
        if(!onScreen(gameplay)) return;
        gc.drawImage(getImg(), x + shiftX - gameplay.translate_x + gameplay.offsetX,
                                y + shiftY - gameplay.translate_y + gameplay.offsetY);
        gc.setEffect(null);
    }

    public void render(GraphicsContext gc, Renderer renderer) {
        gc.setEffect(effect);
        renderer.renderImg(gc, getImg(), x + shiftX, y + shiftY, false);
        gc.setEffect(null);
    }
    /** Hiển thị trên màn hình cố định **/
    public void render(GraphicsContext gc, double x, double y) {
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

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
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
        if(this.getImg() == null) return Sprite.SCALED_SIZE;
        return this.getImg().getWidth();
    }

    public double getHeight() {
        if(this.getImg() == null) return Sprite.SCALED_SIZE;
        return this.getImg().getHeight();
    }

    public Image getImg() {
        if(img == null) return null;
        return img;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setImg(Image img) {
        this.img = img;
    }
    public void deadAct(Gameplay gameplay) {
        return ;
    }
    public void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case NORMAL_MODE -> {
                shiftX = 0;
                shiftY = 0;
            }
            case CENTER_MODE -> {
                shiftX = - getWidth() / 2;
                shiftY = - getHeight() / 2;
            }
            case BOTTOM_MODE -> {
                shiftX = - getWidth() / 2;
                shiftY = - getHeight();
            }
        }
    }

    public void kill(){

    }
    public Vertex getCenter() {
        return new Vertex(getCenterX(), getCenterY());
    }
    public void render(Layer layer) {
        if(layer.shaderEnable && layer.shade){
            if(!layer.lighter.tileCodes.contains(tileCode(tileX, tileY))) return;
        }
        render(layer.gc, layer.renderer);
    }
}
