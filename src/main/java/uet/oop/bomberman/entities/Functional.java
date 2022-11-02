package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.game.Gameplay.tile_map;


public class Functional extends Entity{

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
