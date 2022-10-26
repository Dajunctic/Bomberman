package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Renderer;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

import static java.lang.Math.*;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.others.Basic.mapping;

public class ShockWave extends Entity{
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

    public ShockWave(double xPixel, double yPixel, boolean friendly, double radius, int damage, double duration) {
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
    }

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
            if(!fires.get(tileCode(tileX, tileY)).isEmpty()) continue;

            //destroy
            Gameplay.set('.', tileX, tileY, true);
            entities.add(new Fire(tileX, tileY, duration, damage, friendly));
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
}
