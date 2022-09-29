package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

/**
 * Class dành cho vật thể không bị phá hủy bởi bomb.
 * +, Wall
 * +, Stone
 * */

public class Wall extends Entity {
    int theme;
    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }
    public Wall(int x, int y, Image img, int theme)
    {
        super(x, y, img);
        this.theme = theme;
    }
    @Override
    public void update() {

    }
}
