package uet.oop.bomberman.entities;

import uet.oop.bomberman.graphics.SpriteSheet;

public class Suicider extends Enemy{
    private static SpriteSheet suicider = new SpriteSheet("/sprites/enemy/Suicider/move.png", 12);
    private static SpriteSheet suicider_dead = new SpriteSheet("/sprites/enemt/Suicider/dead.png", 12);
    public Suicider(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }

    @Override
    public void load() {

    }

    @Override
    public void update(Bomber player) {

    }

    @Override
    public void attack(Bomber player) {

    }
}
