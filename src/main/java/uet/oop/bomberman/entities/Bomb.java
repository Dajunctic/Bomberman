package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.SpriteSheet;

public class Bomb extends Entity{

    //animation
    DeadAnim explosion = new DeadAnim(SpriteSheet.explosion, 10, 1);
    DeadAnim bomb = new DeadAnim(SpriteSheet.bomb, 15, 2.5);


    public Bomb(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }
    public Bomb(double xPixel, double yPixel, double timer) {
        super(xPixel, yPixel);
        bomb = new DeadAnim(SpriteSheet.bomb, 15, timer);
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

    public void render(GraphicsContext gc) {
        gc.drawImage(this.getImg(),x,y);
    }
    @Override
    public boolean isExisted() {
        return !explosion.isDead();
    }
}
