package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
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
    Image destroyImg;
    boolean isKilled = false;
    public Floor(double x, double y, Image img) {
        super(x, y, img);
    }
    private boolean burned = false;
    @Override
    public void kill() {
        if(burned) return;
        burned = true;
        isKilled = true;
        switch (Gameplay.currentArea) {
            case 0 -> {
                destroyImg = Sprite.iceDestroyFloor.getFxImage();
            }
            case 1 -> {
                destroyImg = Sprite.destroyFloor.getFxImage();
            }
            case 2 -> {
                destroyImg = Sprite.tombDestroyFloor.getFxImage();
            }
            case 3 -> {
                destroyImg = Sprite.springDestroyFloor.getFxImage();
            }
            case 4 -> {
                destroyImg = Sprite.castleDestroyFloor.getFxImage();
            }
        }

    }

    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        super.render(gc, gameplay);

        if (isKilled) {
            gc.drawImage(this.destroyImg, x - gameplay.translate_x + gameplay.offsetX
                    , y - gameplay.translate_y + gameplay.offsetY);
        }
    }

    @Override
    public void update() {

    }
}
