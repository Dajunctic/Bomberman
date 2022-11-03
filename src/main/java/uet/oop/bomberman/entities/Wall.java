package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;

/**
 * Class dành cho vật thể không bị phá hủy bởi bomb.
 * +, Wall
 * +, Stone
 * */

public class Wall extends Entity {

    protected boolean isDead = false;
    int theme;
    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }
    public Wall(int x, int y, Image img, int theme)
    {
        super(x, y, img);
        this.theme = theme;
    }
    //reduced to atom
    public void kill() {
        Gameplay.set('.', (int)this.x/ Sprite.SCALED_SIZE, (int) this.y / Sprite.SCALED_SIZE, true);
    }

    @Override
    public void update() {
    }
    public void free() {

    }
}
