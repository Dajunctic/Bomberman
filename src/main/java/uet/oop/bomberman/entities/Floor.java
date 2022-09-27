package uet.oop.bomberman.entities;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

/** Class cho các thực thể đi xuyên qua được và không ảnh hưởng các thực thể khác.
 * VD: Floor, Effect
 */
public class Floor extends Entity {
    public Floor(double x, double y, Image img) {
        super(x, y, img);
    }
    private boolean burned = false;
    @Override
    public void kill() {
        if(burned) return;
        burned = true;
        img = Sprite.lava_floor.getFxImage();
    }
    @Override
    public void update() {

    }
}
