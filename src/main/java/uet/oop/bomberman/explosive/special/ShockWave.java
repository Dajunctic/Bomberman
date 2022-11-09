package uet.oop.bomberman.explosive.special;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.*;
import uet.oop.bomberman.maps.GameMap;

import static java.lang.Math.*;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.others.Basic.mapping;
/** Nổ theo hình tròn và ignore obstacles */
public class ShockWave extends Entity {
    private static SpriteSheet shock_wave = new SpriteSheet("/sprites/Player/Abilities/shockwave.png", 10);
    private int tileX;
    private int tileY;
    private DeadAnim shockwave;
    private boolean friendly;
    private double radius;
    private int damage;
    private double duration;

    private double zoom;
    private int stage = 0;
    private double speed;
    private double span = 0;
    private boolean destructive = false;

    public ShockWave(double xPixel, double yPixel, boolean friendly, double radius, int damage, double duration, boolean destructive) {
        super(xPixel, yPixel);
        this.friendly = friendly;
        this.radius = radius;
        this.damage = damage;
        this.duration = duration;
        shockwave = new DeadAnim(shock_wave, 3, 1);
        speed = radius * Sprite.SCALED_SIZE / 28;
        setMode(CENTER_MODE);
        tileX =(int) x / Sprite.SCALED_SIZE;
        tileY =(int)y / Sprite.SCALED_SIZE;
        this.destructive = destructive;
    }

    /** Tăng bán kính vành đai */
    @Override
    public void update() {
        span += speed;
        if(Math.floor(span / Sprite.SCALED_SIZE) > stage) {
            stage ++;
            impact();
            System.out.println("Stage updated");
        }
        shockwave.update();
    }

    /** Ảnh hưởng vành đai lên các tile */
    public void impact() {
        double R = stage * Sprite.SCALED_SIZE;
        int div = (int) Math.round(2 * PI * R);
        double angle = 2 * PI / div;
        for(int i = 0; i < div ; i ++) {
            //calculate
            int tileX = (int) ((x + R * cos(angle * i)) / Sprite.SCALED_SIZE);
            int tileY = (int) ((y + R * sin(angle * i)) / Sprite.SCALED_SIZE);

            //check legit
            if(!areaMaps.get(currentArea).checkInArea(tileX, tileY)) {
                System.out.println("Out of bound");
                continue;
            }

            if(fires.containsKey(tileCode(tileX, tileY))) continue;
            //destroy
            if(destructive) {
                Gameplay.set('.', tileX, tileY, true);
                Gameplay.sqawnFire(tileX, tileY, duration, damage, friendly, true, false);
            } else if (Gameplay.get(tile_map[tileY][tileX], tileX, tileY) != GameMap.WALL) {
                Gameplay.set('.', tileX, tileY, true);
                Gameplay.sqawnFire(tileX, tileY, duration, damage, friendly, true, true);
            }

        }
    }
    @Override
    public Image getImg() {
        return shockwave.getImage();
    }

    @Override
    public boolean isExisted() {
        return !shockwave.isDead();
    }
    @Override
    public void render(GraphicsContext gc, Renderer renderer) {
        double scale = mapping(0, radius * Sprite.SCALED_SIZE, 0.5, radius, span);
        renderer.renderCenterImg(gc, this.getImg(), x, y, false, scale);
    }

    @Override
    public void render(Layer layer) {
        render(layer.gc, layer.renderer);
    }
}
