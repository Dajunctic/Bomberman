package uet.oop.bomberman.maps;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Floor;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Stage1 {
    public List<Entity> decoration = new ArrayList<Entity>();

    public static Entity portal;
    public Stage1() {
        this.load();
        portal = new Portal(89, 99, Sprite.portal.getFxImage());
    }

    private void load() {


        /* ******** Ice Scene *******/
        decoration.add(new Floor(2, 34, Sprite.bigFrozen.getFxImage()));
        decoration.add(new Floor(3, 36, Sprite.frozen.getFxImage()));
        decoration.add(new Floor(2, 34, Sprite.frozen.getFxImage()));
        decoration.add(new Floor(2, 34, Sprite.frozen.getFxImage()));

    }

    public void render(GraphicsContext gc, Gameplay gameplay) {
        for (Entity x : decoration) {
            x.render(gc, gameplay);
        }

        portal.render(gc, gameplay);
    }
}
