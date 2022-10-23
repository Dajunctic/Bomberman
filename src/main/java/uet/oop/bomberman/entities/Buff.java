package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

//Buff placing, haven't implemented interface and renderer
public class Buff extends Entity{

    private double floatingSpeed = 0.5;
    private double floating = 0;
    private double floatingThreshold = 5;
    public static final int INVISIBILITY = 0;
    public static final int BOMB = 1;
    public static final int FIRE = 2;
    public static final int TNT = 3;
    public static final int MP = 4;
    public static final int HP = 5;
    private static final List<Sprite> imgs = new ArrayList<>();
    static {
        imgs.add(new Sprite("/sprites/Buffs/invisible.png",Sprite.NORMAL));
        imgs.add(new Sprite("/sprites/Buffs/bomb.png",Sprite.NORMAL));
        imgs.add(new Sprite("/sprites/Buffs/fire.png",Sprite.NORMAL));
        imgs.add(new Sprite("/sprites/Buffs/nuke.png",Sprite.NORMAL));
        imgs.add(new Sprite("/sprites/Buffs/mp.png",Sprite.NORMAL));
        imgs.add(new Sprite("/sprites/Buffs/heal.png",Sprite.NORMAL));
    }
    private int type;
    private int tilex;
    private int tiley;
    private final double sizeX = (double) Sprite.SCALED_SIZE / 2;
    private final double sizeY = (double) Sprite.SCALED_SIZE / 2;

    /** initializing */
    public Buff(double xUnit, double yUnit, int type) {
        super(xUnit, yUnit);
        this.x *= Sprite.SCALED_SIZE;
        this.y *= Sprite.SCALED_SIZE;
        this.x += sizeX;
        this.y += sizeY;
        this.img = imgs.get(type).getFxImage();
        this.type = type;
        mode = CENTER_MODE;
    }

    @Override
    public void update() {
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
        //update animation
        floating += floatingSpeed;
        if(Math.abs(floating) > floatingThreshold) floatingSpeed *= -1;
    }
    /** Appling buff */
    public void applyEffect(Bomber player) {
        switch (type) {
            default -> {
            }
            case INVISIBILITY -> {
                player.buffInvisible();
            }
            case BOMB -> {
                player.buffBomb();
            }
            case FIRE -> {
                player.buffFire();
            }
            case TNT -> {
                player.buffNuke();
            }
            case MP -> {
                player.addMana(player.getMaxMana() / 3);
            }
            case HP -> {
                player.addHP(player.getMaxHP() / 4);

            }
        }
        System.out.println("Buff applied");
    }
}