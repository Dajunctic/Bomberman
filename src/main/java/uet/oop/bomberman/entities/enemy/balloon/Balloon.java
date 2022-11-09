package uet.oop.bomberman.entities.enemy.balloon;

import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.graphics.*;
import uet.oop.bomberman.music.Audio;

import static uet.oop.bomberman.game.Gameplay.killTask;
import static uet.oop.bomberman.others.Basic.inf;

public class Balloon extends Enemy {

    public static SpriteSheet balloonAttack = new SpriteSheet("/sprites/enemy/Balloon/attack.png", 7);

    private int damage = 100;
    protected MediaPlayer attackAudio = Audio.copy(Audio.balloon_attack);

    public Balloon(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }

    @Override
    public void load() {
        enemy = new Anim(SpriteSheet.balloon, 10, 0);
        killed = new DeadAnim(SpriteSheet.balloon_die, 5, 1);
        attack = new DeadAnim(balloonAttack, 6, 1);
        attackRange = (double) Sprite.SCALED_SIZE / 2;
        margin = 0;
        setHP(1000);
        standingTile();
    }

    @Override
    public void deadAct(Gameplay gameplay){
        killTask.add(new Point(tileX, tileY));
        super.deadAct(gameplay);
    }



    @Override
    public boolean isExisted() {
        return !killed.isDead();
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
                if(attack.isDead()) {
                    attack(player);
                    attack.reset();
                    isAttacking = false;
                }
            } else {
                move();
                if(player.vulnerable()) distance.set(player.getPosition().x - x, player.getPosition().y - y);
                    else distance.set(inf, inf);
                enemy.update();

                //if its attacking
                if( distance.abs() <= attackRange && !isAttacking) {
                    Audio.start(attackAudio);
                    isAttacking = true;
                    return ;
                }
            }

        }
            else killed.update();

        //search for player
        if(enemy.getTime() % frequency == 0) {
            search(player);
        }
    }

    @Override
    public Image getImg() {
        if(!appear.isDead()) return  appear.getImage();
        if(isDead) return killed.getImage();
            else  {
                if(isAttacking) return attack.getImage();
                return enemy.getImage();
        }
    }
    @Override
    public void attack(Bomber player) {
        player.subtractHP(damage);
    }

}
