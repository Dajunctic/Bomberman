package uet.oop.bomberman.entities.enemy.special;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.explosive.special.ShockWave;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.Sound;

import static java.lang.Math.PI;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.graphics.SpriteSheet.explosion;

public class Suicider extends Enemy {
    private static final SpriteSheet suicider = new SpriteSheet("/sprites/enemy/Suicider/move.png", 12);
    private static final SpriteSheet suicider_attack = new SpriteSheet("/sprites/enemy/Suicider/attack.png", 6);
    private static final double acceleration = 0.25;
    private static final double baseSpeed = 1.5;
    private boolean exploded = false;
    private static final int damage = 10;
    public Suicider(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }
    @Override
    public void load() {
        enemy = new Anim(suicider, 10);
        killed = new DeadAnim(explosion, 12, 1);
        killed.setScaleFactor(2);
        attack = new DeadAnim(suicider_attack, 12, 1);
        attackRange = (double) Sprite.SCALED_SIZE * 5;
        speed = baseSpeed;
        sight_depth = 10;
        sight_angle = PI / 3;
        margin = 0;
        setHP(1000);
        standingTile();
    }


    @Override
    public Image getImg()
    {
        if(!appear.isDead()) return  appear.getImage();
        if(!isDead) {
            if(isAttacking) return attack.getImage();
            return enemy.getImage();
        }   else return killed.getImage();
    }

    @Override
    public void update(Bomber player) {
        //Make appearance
        if(!appear.isDead()){
            appear.update();
            return;
        }
        //hp update
        super.update();
        //status
        if(!isDead){
                if(isAttacking) {
                    attack.update();
                    status = SERIOUS;
                } else enemy.update();
                move();
                if(player.vulnerable()) {
                    distance.set(player.getPosition().x - x, player.getPosition().y - y);

                    if( distance.abs() <= 60 * speed + 3600 * acceleration && !isAttacking && status == SERIOUS) {
                        isAttacking = true;
                        margin = 10;
                        return ;
                    }
                }
                //60 * speed + 3600 * acceleration
                //if its attacking

                if(attack.isDead() || distance.abs() <= margin * 2) explode();
                if(status == SERIOUS) speed += acceleration;
                if(isDead) explode();
        }
        else killed.update();

        //search for player
        if(enemy.getTime() % frequency == 0) {
            search(player);
        }
    }
    public void explode() {
        if(exploded) return;
        exploded = true;
        subtractHP(1000);
        isDead = true;
        sounds.add(new Sound(x, y, Audio.copy(Audio.bomb_explosion), -1, 10 * Sprite.SCALED_SIZE));
        entities.add(new ShockWave(x, y, false, 1.5, damage, 2, false));
        setMode(CENTER_MODE);
    }

    @Override
    public void deadAct(Gameplay gameplay) {

    }
    public boolean isExisted() {
        return !killed.isDead();
    }
    @Override
    public void attack(Bomber player) {
    }

    @Override
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        boolean checker = super.checkCollision(ref_x, ref_y, margin);
        if(checker && isAttacking) explode();
        return checker;
    }
}
