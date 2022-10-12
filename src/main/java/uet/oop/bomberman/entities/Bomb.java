package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

public class Bomb extends Entity{

    //animation
    DeadAnim explosion = new DeadAnim(SpriteSheet.explosion, 6, 1);
    public DeadAnim bomb = new DeadAnim(SpriteSheet.bomb, 12, 2.5);
    boolean exploded = false;

    //tí sửa sau :))) 
    public void relocate() {

    }
    public Bomb(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        mode = CENTER_MODE;
    }
    public Bomb(double xPixel, double yPixel, double timer) {
        super(xPixel, yPixel);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        x += (double) Sprite.SCALED_SIZE / 2;
        y += (double) Sprite.SCALED_SIZE / 2;
        bomb = new DeadAnim(SpriteSheet.bomb, 15, timer);
        mode = CENTER_MODE;
        explosion.setScaleFactor(2);
        effect = new Bloom(0.2);
    }
    @Override
    public void update() {
        if (bomb.isDead()) {
            explosion.update();
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
        exploded = true;
        gameplay.generate(new Flame(x, y, 3 * Sprite.SCALED_SIZE, 1,0));
        gameplay.generate(new Flame(x, y, 3 * Sprite.SCALED_SIZE, 0,1));
        gameplay.generate(new Flame(x, y, 3 * Sprite.SCALED_SIZE, 0,-1));
        gameplay.generate(new Flame(x, y, 3 * Sprite.SCALED_SIZE, -1,0));

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
