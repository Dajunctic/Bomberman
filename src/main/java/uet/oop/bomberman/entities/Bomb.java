package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.Sound;
import uet.oop.bomberman.others.Physics;

public class Bomb extends Entity{

    //animation
    DeadAnim explosion = new DeadAnim(SpriteSheet.explosion, 6, 1);
    public DeadAnim bomb = new DeadAnim(SpriteSheet.bomb, 12, 2.5);
    boolean exploded = false;
    boolean friendly = false;
    //tí sửa sau :)))
    private int radius = 3;
    private int damage = 4;
    private double duration = 2;
    public void relocate() {

    }
    public Bomb(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        setMode(CENTER_MODE);
    }
    public Bomb(double xPixel, double yPixel, double timer) {
        super(xPixel, yPixel);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        x += (double) Sprite.SCALED_SIZE / 2;
        y += (double) Sprite.SCALED_SIZE / 2;
        bomb = new DeadAnim(SpriteSheet.bomb, 15, timer);
        setMode(CENTER_MODE);
        explosion.setScaleFactor(2);
        Gameplay.sounds.add(new Sound(x, y, Audio.copy(Audio.bomb), timer));
    }

    public Bomb(double xPixel, double yPixel, double timer, int radius, double duration,int damage, boolean friendly) {
        super(xPixel, yPixel);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        x += (double) Sprite.SCALED_SIZE / 2;
        y += (double) Sprite.SCALED_SIZE / 2;
        bomb = new DeadAnim(SpriteSheet.bomb, 15, timer);
        explosion.setScaleFactor(2);
        this.radius = radius;
        this.duration = duration;
        this.damage = damage;
        this.friendly = friendly;
        setMode(CENTER_MODE);
    }
    @Override
    public void update() {
        if (bomb.isDead()) {
            explosion.update();
            setMode(CENTER_MODE);
            explode();
        } else {
            bomb.update();
        }
    }


    public Image getImg() {
        if (bomb.isDead()) {
            return explosion.getImage();
        } else {
            return bomb.getImage();
        }
    }
    @Override
    public boolean isExisted() {
        return !explosion.isDead();
    }

    @Override
    public void deadAct(Gameplay gameplay) {
        if(exploded) return;
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 1,0, 0.5, duration, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 0, 1, 0.5, duration, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 0,-1, 0.5, duration, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, -1,0, 0.5, duration, damage, friendly));

    }

    public void explode() {
        if(exploded) return;
        exploded = true;
        Gameplay.sounds.add(new Sound(x, y, Audio.copy(Audio.bomb_explosion), -1));
    }
    @Override
    public double getWidth() {
        if (bomb.isDead())
            return explosion.getImage().getWidth();
        return bomb.getImage().getWidth();
    }

    @Override
    public double getHeight() {
        if (bomb.isDead())
            return explosion.getImage().getHeight();
        return bomb.getImage().getHeight();
    }
}
