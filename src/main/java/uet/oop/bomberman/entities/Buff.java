package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;

//Buff placing, haven't implemented interface and renderer
public class Buff extends Entity{

    private double floatingSpeed = 1;
    private double floating = 0;
    private double floatingThreshold = 15;
    public static final int INVISIBILITY = 0;
    public static final int ATTACK = 1;
    public static final int FIRE = 2;
    public static final int TNT = 3;
    private int type;
    private int tilex;
    private int tiley;
    private final double sizeX = (double) Sprite.SCALED_SIZE / 2;
    private final double sizeY = (double) Sprite.SCALED_SIZE / 2;

    /** initializing */
    public Buff(double xUnit, double yUnit, Image img, int type) {
        super(xUnit, yUnit, img);
        this.x += sizeX;
        this.y += sizeY;
        this.type = type;
        mode = CENTER_MODE;
    }

    @Override
    public void update() {
        floating += floatingSpeed;
        if(Math.abs(floating) > floatingThreshold) floatingSpeed *= -1;
    }

    //shrink into 1/2 tile
    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        renderCenter(gc, gameplay);
    }
    @Override
    public void renderCenter(GraphicsContext gc, Gameplay gameplay) {
        gc.setEffect(effect);
         double renderX = x - sizeX / 2;
         double renderY = y - sizeY / 2 + floating;
         gc.drawImage(this.getImg(),renderX - gameplay.translate_x + gameplay.offsetX
                       ,renderY - gameplay.translate_y + gameplay.offsetY, sizeX, sizeY);
        gc.setEffect(null);
    }

    //for searching
    @Override
    public int hashCode() {
        return tiley* Gameplay.width + tilex;
    }

    /** Appling buff */
    public void applyEffect(Bomber player) {

    }
}
