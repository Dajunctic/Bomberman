package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

/** Class cho các thực thể đi xuyên qua được và không ảnh hưởng các thực thể khác.
 * VD: Floor, Effect
 */
public class Floor extends Entity {
    public Floor(double x, double y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }
}
