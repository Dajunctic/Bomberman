package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.*;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.Sound;
import uet.oop.bomberman.others.Physics;

import java.util.*;

import static uet.oop.bomberman.game.BombermanGame.FPS;
import static uet.oop.bomberman.game.BombermanGame.currentFrame;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.graphics.Sprite.spot;
import static uet.oop.bomberman.others.Basic.inf;

public abstract class Enemy extends Mobile{

    protected static SpriteSheet enemy_appear = new SpriteSheet("/sprites/enemy/appear.png", 9);
    protected Anim enemy;
    protected DeadAnim appear = new DeadAnim(enemy_appear, 3, 1);
    protected DeadAnim killed;
    protected Point focus;

    /** State of game */
    protected final int[] dir = {0, 1, -1};
    protected Random switcher = new Random();
    protected static final int WANDERING = 0;
    protected static final int SERIOUS = 1;

    /** Detect player */
    protected double sight_depth = 5;
    protected int frequency = 10;
    protected double sight_angle = Math.PI / 4;
    protected int status = WANDERING;
    protected int strict = 5;

    protected Vertex destination = new Vertex(0,0);
    protected Vertex distance = new Vertex(inf, inf);
    /** Attack */
    protected double attackRange = (double) Sprite.SCALED_SIZE / 3;

    protected DeadAnim attack;
    protected boolean isAttacking = false;
    /** Moving */
    protected double speed = 1;
    protected Vertex direction = new Vertex(0,1);

    protected boolean reversed = false;
    /** Life status */
    protected boolean isDead = false;
    protected int margin = 5;
    protected long lastCheck = 0;
    public int stuckTime = 0;
    private double distanceCheck = 0;
    public Enemy(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        load();
    }
    public abstract void load();

    public Enemy(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    //reverse the sprite
    private void switchSprite() {
        reversed =  (direction.getX() < 0);
    }
    /** random setter */
    public void switchDirection() {
        if(isAttacking) return;
        if(Math.abs(direction.getX()) > Math.abs(direction.getY())) {
            direction.setX(0);
            direction.setY(dir[(int) Math.round(Math.random()) + 1]);
        }
        else {
            direction.setY(0);
            direction.setX(dir[(int) Math.round(Math.random()) + 1]);
        }
        switchSprite();
    }

    //also need to be adjusted to fit in new map
    /** Tracking player*/
    protected void search(Bomber player) {
        if(!player.vulnerable()) return;

        Vertex line = new Vertex(player.x - x, player.y - y);
        if(Math.abs(direction.angle(line)) <= sight_angle
            &&  line.abs() <= sight_depth * Sprite.SCALED_SIZE){

            if(!checkSight(line)) return;
            direction.set(player.x - x, player.y - y);
            direction.normalize();
            status = SERIOUS;
            destination = new Vertex( player.x, player.y);
            switchSprite();
        }
    }
    //check whether path is blocked
    protected boolean checkSight(Vertex line) {
        for(double i = 0; i <= 1; i += 1 / (double) sight_depth) {
            int tileX = (int) (x + line.getX()*i) / Sprite.SCALED_SIZE;
            int tileY = (int) (y + line.getY()*i) / Sprite.SCALED_SIZE;
            if(Gameplay.get(tile_map[tileY][tileX], tileX, tileY) != GameMap.FLOOR) return false;
        }
        return true;
    }

    @Override
    public void move() {
        double ref_x = x +  speed * direction.getX();
        double ref_y = y +  speed * direction.getY();
        if(!checkCollision(ref_x, ref_y, (status == SERIOUS ? margin + 3 : margin))) {
            distanceCheck += Vertex.dis(ref_x - x, ref_y - y);
            x = ref_x;
            y = ref_y;
            //check standing tile
            standingTile();
            //check if it reached its destination
            if(destination != null)
                if(status == SERIOUS && destination.distance(x, y) <= margin && !isAttacking) {
                    destination = null;
                    status = WANDERING;
                    switchDirection();
                }
        }
        else {
            if(status == SERIOUS) status = WANDERING;
            switchDirection();
        }

    }

    //need to be reinstalled
    @Override
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        int check = 0;
        /* * Kiểm tra border map */
        if(ref_x < 0 || ref_y < 0
                || ref_x > width * Sprite.SCALED_SIZE - this.getWidth()
                || ref_y > height * Sprite.SCALED_SIZE - this.getHeight()) return true;

        Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new Rectangle(ref_x - this.getWidth() / 2 , ref_y - this.getHeight() / 2, this.getWidth() , this.getHeight());
        else
            rect = this.getRect(ref_x, ref_y, this.getWidth(), this.getHeight());

        /* * Kiểm tra tất cả các tiles xung quanh thực thể. */

        int tileStartX = (int) Math.max(0, Math.floor(rect.getX() / Sprite.SCALED_SIZE));
        int tileStartY = (int) Math.max(0, Math.floor(rect.getY() / Sprite.SCALED_SIZE));

        if(!areaMaps.get(currentArea).checkInArea(tileStartX, tileStartY)) return true;

        int tileEndX = (int) Math.ceil((rect.getX() + rect.getWidth()) / Sprite.SCALED_SIZE);
        int tileEndY = (int) Math.ceil((rect.getY() + rect.getHeight()) / Sprite.SCALED_SIZE);
        tileEndX = Math.min(tileEndX, Gameplay.width - 1);
        tileEndY = Math.min(tileEndY, Gameplay.height - 1);
        for (int i = tileStartX; i <= tileEndX; i++) {
            for (int j = tileStartY; j <= tileEndY; j++) {

                int tileX = i * Sprite.SCALED_SIZE;
                int tileY = j * Sprite.SCALED_SIZE;

                Rectangle tileRect = new Rectangle(tileX + margin, tileY + margin, Sprite.SCALED_SIZE - 2 * margin, Sprite.SCALED_SIZE - 2 * margin);

                /* * Kiểm tra tile có phải kiểu WALL hoặc BRICK không! */

                if (Gameplay.get(tile_map[j][i], i, j) == GameMap.WALL
                        || Gameplay.get(tile_map[j][i], i, j) == GameMap.BRICK) {

                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        check ++;
                    }
                }
            }
        }
        if(check >= 2) {
            escape();
        }
        if(check > 0) return true;
        return false;
    }

    @Override
    public Image getImg() {
        if(!appear.isDead()) return  appear.getImage();
        if(!isDead) return enemy.getImage();
            else return killed.getImage();
    }


    @Override
    public void update() {
        if(isDead) return;
        super.update();
        if(currentHP <= 0) {
            isDead = true;
            sounds.add(new Sound(x, y, Audio.copy(Audio.enemy_dead), -1, 8));
        }
    }
    public abstract void update(Bomber player);

    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        double dim = (reversed ? -1 : 1);
        /* * Hiển thị máu */
        renderHP(gc, gameplay);

        gc.setEffect(effect);
        // Whether object is on screen
//        if(!onScreen(gameplay)) return;

        //  If spotted bomber
        if(status == SERIOUS) gc.drawImage(spot.getFxImage(), x - gameplay.translate_x + gameplay.offsetX + this.getWidth() / 2
                                                            , y - gameplay.translate_y + gameplay.offsetY - 18 );

        // If it is going backward
        gc.drawImage(this.getImg(), x - gameplay.translate_x + gameplay.offsetX + ((Sprite.SCALED_SIZE * (1 - dim)) / 2)
                , y - gameplay.translate_y + gameplay.offsetY
                , Sprite.SCALED_SIZE * dim
                , Sprite.SCALED_SIZE);
        gc.setEffect(null);
    }

    @Override
    public void render(GraphicsContext gc, Renderer renderer) {

        /* * Hiển thị máu */
        renderHP(gc, renderer);

        gc.setEffect(effect);
        // Whether object is on screen
//        if(!onScreen(gameplay)) return;

        //  If spotted bomber
        if(status == SERIOUS) renderer.renderImg(gc, spot.getFxImage(), x + shiftX + this.getWidth() / 2
                , y + shiftY - 18, false);
        // If it is going backward
        renderer.renderImg(gc, this.getImg(), x + shiftX, y + shiftY, reversed);
        gc.setEffect(null);
    }
    public abstract void attack(Bomber player);

    protected void checkStuck() {
        //check whether stepsister is stuck
        if(isAttacking || !appear.isDead()) return;
        if(distanceCheck <= 2 * Sprite.SCALED_SIZE) {
                stuckTime++;
                System.out.println("Stucked in " + stuckTime);
                if (stuckTime >= 5) escape();
        }
        else stuckTime = 0;
        distanceCheck = 0;
    }
    public void escape() {
        stuckTime = 0;
        status = WANDERING;
        switchDirection();
        Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new Rectangle(x - this.getWidth() / 2 , y - this.getHeight() / 2, this.getWidth() , this.getHeight());
        else
            rect = this.getRect(x, y, this.getWidth(), this.getHeight());

        /* * Kiểm tra tất cả các tiles xung quanh thực thể. */

        int tileStartX = (int) Math.max(1, Math.floor(rect.getX() / Sprite.SCALED_SIZE)) - 1;
        int tileStartY = (int) Math.max(1, Math.floor(rect.getY() / Sprite.SCALED_SIZE)) - 1;
        if(!areaMaps.get(currentArea).checkInArea(tileStartX, tileStartY)) return;

        int tileEndX = (int) Math.ceil((rect.getX() + rect.getWidth()) / Sprite.SCALED_SIZE);
        int tileEndY = (int) Math.ceil((rect.getY() + rect.getHeight()) / Sprite.SCALED_SIZE);
        tileEndX = Math.min(tileEndX, Gameplay.width - 1);
        tileEndY = Math.min(tileEndY, Gameplay.height - 1);
        for (int i = tileStartX; i <= tileEndX; i++) {
            for (int j = tileStartY; j <= tileEndY; j++) {

                int tileX = i * Sprite.SCALED_SIZE;
                int tileY = j * Sprite.SCALED_SIZE;

                Rectangle tileRect = new Rectangle(tileX + margin, tileY + margin, Sprite.SCALED_SIZE - 2 * margin, Sprite.SCALED_SIZE - 2 * margin);

                /* * Kiểm tra tile có phải kiểu WALL hoặc BRICK không! */

                if (Gameplay.get(tile_map[j][i], i, j) == GameMap.FLOOR) {
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        x = i * Sprite.SCALED_SIZE;
                        y = j * Sprite.SCALED_SIZE;
                        appear.reset();
                    }
                }
            }
        }
    }
    public void free() {
        enemy = null;
        killed = null;
        appear = null;
        attack = null;
        focus = null;
        distance = null;
        destination = null;
    }

}
