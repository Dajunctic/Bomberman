package uet.oop.bomberman.explosive.bomb;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.explosive.Flame;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.Sound;

import static uet.oop.bomberman.game.Gameplay.player;

public class Bomb extends Entity {

    //animation
    DeadAnim explosion = new DeadAnim(SpriteSheet.explosion, 6, 1);
    public DeadAnim bomb = new DeadAnim(SpriteSheet.bomb, 12, 2.5);
    private Sound audio;
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
        audio = new Sound(x, y, Audio.copy(Audio.bomb), timer, 6 * Sprite.SCALED_SIZE);
    }

    public Bomb(double xPixel, double yPixel, double timer, int radius, double duration,int damage, boolean friendly) {
        super(xPixel, yPixel);
        tileX =(int) xPixel;
        tileY =(int) yPixel;
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
        audio = new Sound(x, y, Audio.copy(Audio.bomb), timer, 6 * Sprite.SCALED_SIZE);
    }
    @Override
    public void update() {
        if (bomb.isDead()) {
            explosion.update();

        } else {
            bomb.update();
            audio.update(player);
            if(bomb.isDead()) {
                setMode(CENTER_MODE);
                explode();
            }
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
        exploded = true;
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 1,0, 5, duration, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 0, 1, 5, duration, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 0,-1, 5, duration, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, -1,0, 5, duration, damage, friendly));
        super.deadAct(gameplay);
    }

    public void explode() {
        audio.stop();
        audio = null;
        Gameplay.sounds.add(new Sound(x, y, Audio.copy(Audio.bomb_explosion), -1, 8 * Sprite.SCALED_SIZE));
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

    public void free() {
        explosion = null;
        bomb = null;
        audio.free();
        audio = null;
    }
}
