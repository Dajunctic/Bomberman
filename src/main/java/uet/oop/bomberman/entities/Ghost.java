package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.graphics.*;

import static uet.oop.bomberman.game.Gameplay.tileCode;

public class Ghost extends Balloon{
    private static SpriteSheet ghost = new SpriteSheet("/sprites/enemy/Ghost/move.png", 12);
    private static SpriteSheet ghost_dead = new SpriteSheet("/sprites/enemy/Ghost/dead.png", 12);
    private static SpriteSheet ghost_attack = new SpriteSheet("/sprites/enemy/Ghost/attack.png", 7);
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
        if((isDead || isAttacking) || (!appear.isDead()) || (renderer.getPov().isAlly == isAlly) || !renderer.getPov().vulnerable()) return  true;
        return false;
    }
}
