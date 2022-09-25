package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.SpriteSheet;

public class Bomb extends Entity{

    DeadAnim explosion = new DeadAnim(SpriteSheet.explosion, 10, 1);
    DeadAnim bomb = new DeadAnim(SpriteSheet.bomb, 15, 2.5);

    public Bomb(double xPixel, double yPixel) {
        super(xPixel, yPixel);
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
}
