package uet.oop.bomberman.entities.enemy.balloon;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.enemy.balloon.Balloon;
import uet.oop.bomberman.graphics.*;

public class Ghost extends Balloon {
    private static final SpriteSheet ghost = new SpriteSheet("/sprites/enemy/Ghost/move.png", 12);
    private static final SpriteSheet ghost_dead = new SpriteSheet("/sprites/enemy/Ghost/dead.png", 12);
    private static final SpriteSheet ghost_attack = new SpriteSheet("/sprites/enemy/Ghost/attack.png", 7);
    public Ghost(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }
    public void load() {
        enemy = new Anim(ghost, 10, 0);
        killed = new DeadAnim(ghost_dead, 5, 1);
        attack = new DeadAnim(ghost_attack, 6, 1);
        attackRange = (double) Sprite.SCALED_SIZE / 2;
        margin = 5;
        setHP(1000);
        standingTile();
        margin = 5;
    }
    public void render(GraphicsContext gc, Renderer renderer) {
        if(!visible(renderer)) return;
        super.render(gc, renderer);
    }
    public boolean visible(Renderer renderer) {
        return (isDead || isAttacking) || (!appear.isDead()) || (renderer.getPov().isAlly() == isAlly) || !renderer.getPov().vulnerable();
    }

    public void free() {

    }
}
