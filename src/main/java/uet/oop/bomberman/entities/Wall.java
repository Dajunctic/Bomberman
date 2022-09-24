package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

/**
 * Class dành cho vật thể không bị phá hủy bởi bomb.
 * +, Wall
 * +, Stone
 * */

public class Wall extends Entity {

    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }
}
