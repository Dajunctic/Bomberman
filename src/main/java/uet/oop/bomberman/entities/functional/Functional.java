package uet.oop.bomberman.entities.functional;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
public class Functional extends Entity {

    private int tileX, tileY;
    public Functional(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        tileX = (int) xUnit;
        tileY = (int) yUnit;
    }

    @Override
    public void update() {
    }

    public void free() {

    }
}
