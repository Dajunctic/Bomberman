package uet.oop.bomberman.entities.enemy.balloon;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.*;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.others.Physics;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.others.Basic.inf;

public class Jumper extends Balloon {

    private static Sprite jump = new Sprite("/sprites/enemy/Jumper/jump.png", Sprite.NORMAL);
    private static SpriteSheet jumper = new SpriteSheet("/sprites/enemy/Jumper/move.png", 12);
    private static SpriteSheet jumper_dead = new SpriteSheet( "/sprites/enemy/Jumper/dead.png", 12);
    private static SpriteSheet jumper_attack = new SpriteSheet("/sprites/enemy/Jumper/attack.png", 7);

    private static final double jumpThreshold = 20;
    private static final double jumpSpeed = 2;

    private double offSetY = 0;
    private boolean isJumping = false;
    public Jumper(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        margin = 0;
    }
    @Override
    public void load() {
        enemy = new Anim(jumper, 10, 0);
        killed = new DeadAnim(jumper_dead, 5, 1);
        attack = new DeadAnim(jumper_attack, 6, 1);
        attackRange = (double) Sprite.SCALED_SIZE / 2;
        setHP(1000);
        standingTile();
    }
    @Override
    protected boolean checkSight(Vertex line) {
        for(double i = 0; i <= 1; i += 1 / (double) sight_depth) {
            int tileX = (int) ((x + line.getX()*i) / Sprite.SCALED_SIZE);
            int tileY = (int) ((y + line.getY()*i)/ Sprite.SCALED_SIZE);
            if(Gameplay.get(tile_map[tileY][tileX], tileX, tileY) == GameMap.WALL) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        /* * Kiểm tra border map */
        int pX = (int) (ref_x + getWidth() / 2) / Sprite.SCALED_SIZE;
        int pY = (int) (ref_y + getHeight() / 2) / Sprite.SCALED_SIZE;
        if (!areaMaps.get(currentArea).checkInArea(pX, pY)) return true;

        // Collision
        Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new Rectangle(ref_x - this.getWidth() / 2 + margin, ref_y - this.getHeight() / 2 + margin, this.getWidth() - margin, this.getHeight() - margin);
        else
            rect = this.getRect(ref_x, ref_y, this.getWidth(), this.getHeight());

        /* * Kiểm tra tất cả các tiles xung quanh thực thể. */

        int tileStartX = (int) Math.max(0, Math.floor(rect.getX() / Sprite.SCALED_SIZE));
        int tileStartY = (int) Math.max(0, Math.floor(rect.getY() / Sprite.SCALED_SIZE));
        int tileEndX = (int) Math.ceil((rect.getX() + rect.getWidth()) / Sprite.SCALED_SIZE);
        int tileEndY = (int) Math.ceil((rect.getY() + rect.getHeight()) / Sprite.SCALED_SIZE);
        tileEndX = Math.min(tileEndX, Gameplay.width - 1);
        tileEndY = Math.min(tileEndY, Gameplay.height - 1);
        for (int i = tileStartX; i <= tileEndX; i++) {
            for (int j = tileStartY; j <= tileEndY; j++) {

                int tileX = i * Sprite.SCALED_SIZE;
                int tileY = j * Sprite.SCALED_SIZE;

                Rectangle tileRect = new Rectangle(tileX, tileY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

                /* * Kiểm tra tile có phải kiểu WALL hoặc BRICK không! */
                if (Physics.collisionRectToRect(rect, tileRect)) {
                    if (Gameplay.get(tile_map[j][i], i, j) == GameMap.WALL) {
                            return true;
                    }
                    if (Gameplay.get(tile_map[j][i], i, j) == GameMap.BRICK) {
                        if (!isJumping) isJumping = true;
                        return false;
                    }
                    if (Gameplay.get(tile_map[j][i], i, j) == GameMap.FLOOR) {
                        if (isJumping) isJumping = false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Image getImg() {
        if(!appear.isDead()) return  appear.getImage();
        if(isDead) return killed.getImage();
        else  {
            if(isAttacking) return attack.getImage();
            if(offSetY > 0 && offSetY < jumpThreshold) return jump.getFxImage();
            return enemy.getImage();
        }
    }

    @Override
    public void update(Bomber player) {
        //Make appearance
        if(!appear.isDead()){
            appear.update();
            return;
        }
        //hp update
        update();
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
                offSetY = Math.max(0, Math.min(jumpThreshold, offSetY + jumpSpeed * (isJumping ? 1 : -1)));
                shiftY = -offSetY;
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



}
