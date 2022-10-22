package uet.oop.bomberman.entities;

import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

import static uet.oop.bomberman.game.Gameplay.entities;
import static uet.oop.bomberman.game.Gameplay.killTask;

public class Balloon extends Enemy{

    public Balloon(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        super.enemy = new Anim(SpriteSheet.balloon, 10, 0);
        super.killed = new DeadAnim(SpriteSheet.balloon_die, 5, 1);
    }

    @Override
    public void deadAct(Gameplay gameplay){
        killTask.add(recent_tile);
        entities.add(new Bomb( x / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE,0));
    }

    @Override
    public boolean isExisted() {
        return !killed.isDead();
    }

    @Override
    public void update(Bomber player) {
        super.update();

        if(enemy.getTime() % frequency == 0) {
            search(player);
        }
        move();
        if(!isDead) enemy.update();
            else killed.update();
    }
}
