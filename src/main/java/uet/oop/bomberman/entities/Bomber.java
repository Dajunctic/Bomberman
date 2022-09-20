package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Bomber extends Entity {

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
    }

    @Override
    public void update() {

        x = x + 8;
        y = y + 5;
    }
}
