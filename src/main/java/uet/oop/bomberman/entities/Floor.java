package uet.oop.bomberman.entities;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.GameMap;

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
        switch (Gameplay.currentArea) {
            case 0 -> {
                img = Sprite.iceDestroyFloor.getFxImage();
            }
            case 1 -> {
                img = Sprite.destroyFloor.getFxImage();
            }
            case 2 -> {
                img = Sprite.tombDestroyFloor.getFxImage();
            }
            case 3 -> {
                img = Sprite.springDestroyFloor.getFxImage();
            }
            case 4 -> {
                img = Sprite.castleDestroyFloor.getFxImage();
            }
        }

    }
    @Override
    public void update() {

    }
}
