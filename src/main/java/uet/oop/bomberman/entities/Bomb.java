package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

public class Bomb extends Entity{

    //animation
    DeadAnim explosion = new DeadAnim(SpriteSheet.explosion, 10, 1);
    public DeadAnim bomb = new DeadAnim(SpriteSheet.bomb, 15, 2.5);
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
        bomb = new DeadAnim(SpriteSheet.bomb, 15, timer);
        mode = CENTER_MODE;
    }
    @Override
    public void update() {
        if (bomb.isDead()) {
            explosion.update();
        } else {
            bomb.update();
        }
    }


    @Override
    public Image getImg() {
        if (bomb.isDead()) {
            return explosion.getFxImage();
        } else {
            return bomb.getFxImage();
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
        gameplay.generate(new Flame(x, y, 2 * Sprite.SCALED_SIZE, 1,0));
        gameplay.generate(new Flame(x, y, 2 * Sprite.SCALED_SIZE, 0,1));
        gameplay.generate(new Flame(x, y, 2 * Sprite.SCALED_SIZE, 0,-1));
        gameplay.generate(new Flame(x, y, 2 * Sprite.SCALED_SIZE, -1,0));

    }
}
