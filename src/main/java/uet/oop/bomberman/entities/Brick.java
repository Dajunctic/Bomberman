package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.SpriteSheet;

public class Brick extends  Entity{

    protected DeadAnim brick = new DeadAnim(SpriteSheet.brick,15,1);
    protected boolean isDead = false;
    public Brick(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }
    public void kill() {
        isDead = true;
    }

    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        super.render(gc, gameplay);
    }

    @Override
    public void update() {

    }

}
