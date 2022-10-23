package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.others.Basic;
import uet.oop.bomberman.others.Physics;

import java.util.Random;

import static java.lang.Math.random;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.graphics.Sprite.spot;

public abstract class Enemy extends Mobile{

    protected Anim enemy;
    protected DeadAnim killed;
    protected Point focus;

    /** State of game */
    protected final int[] dir = {0, 1, -1};
    protected Random switcher = new Random();
    protected static final int WANDERING = 0;
    protected static final int SERIOUS = 1;

    /** Detect player */
    private final double sight_depth = 5;
    protected int frequency = 10;
    protected double sight_angle = Math.PI / 4;
    protected int status = WANDERING;
    protected int strict = 5;

    protected Point destination = new Point(0,0);

    /** Moving */
    protected int speed = 1;
    protected Vertex direction = new Vertex(0,1);

    protected int reversed = 1;
    /** Life status */
    protected boolean isDead = false;
    public Enemy(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }

    public Enemy(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    //reverse the sprite
    private void switchSprite() {
        reversed =  (direction.getX() < 0 ? -1 : 1);
    }
    /** random setter */
    public void switchDirection() {
        if(direction.getX() == 1 || direction.getX() == -1) {
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
        if(!player.vulnerable()) {
            return;
        }

        Vertex line = new Vertex(player.x - x, player.y - y);
        if(Math.abs(direction.angle(line)) <= sight_angle
            &&  line.abs() <= sight_depth * Sprite.SCALED_SIZE){

            if(!checkSight(line)) return;
            direction.set(player.x - x, player.y - y);
            direction.normalize();
            status = SERIOUS;
            destination.set((int) player.x, (int) player.y);
            switchSprite();
        }
    }
    //check whether path is blocked
    private boolean checkSight(Vertex line) {
        for(double i = 0; i <= 1; i += 1 / (double) sight_depth) {
            int tileX = (int) ((x + line.getX()*i) / Sprite.SCALED_SIZE);
            int tileY = (int) ((y + line.getY()*i)/ Sprite.SCALED_SIZE);
            if(Gameplay.get(tile_map[tileY][tileX], tileX, tileY) != GameMap.FLOOR) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void move() {
        double ref_x = x +  speed * direction.getX();
        double ref_y = y +  speed * direction.getY();
        if(!checkCollision(ref_x, ref_y, 5)){
            if(focus != null)
                if(ref_x - focus.getX() <= strict && ref_y - focus.getY() <= strict) {
                    focus = null;
                    status = WANDERING;
                    switchDirection();
                }
            x = ref_x;
            y = ref_y;
            //check standing tile
            standingTile();
            //check if it reached its destination
            if(status == SERIOUS && destination.equals(new Point((int)x, (int)y))) {
                status = WANDERING;
                int dirx = dir[(int) Math.floor(Math.random() * 2.9)];
                direction.set(dirx, 1 - Math.abs(dirx));
            }
        }
        else{
            if(status == SERIOUS) status = WANDERING;
            switchDirection();
        }

    }

    //need to be reinstalled
    @Override
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        /* * Kiểm tra border map */
        if(ref_x < 0 || ref_y < 0
                || ref_x > width * Sprite.SCALED_SIZE - this.getWidth()
                || ref_y > height * Sprite.SCALED_SIZE - this.getHeight()) return true;

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

                if (Gameplay.get(tile_map[j][i], i, j) == GameMap.WALL
                        || Gameplay.get(tile_map[j][i], i, j) == GameMap.BRICK) {

                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        return true;
                    }
                }
                //will need reimplemented
                if (tile_map[j][i] == '!') {
                    isDead = true;
                    return false;
                }
            }
        }

        return false;
    }

    @Override
    public Image getImg() {
        if(!isDead) return enemy.getImage();
            else return killed.getImage();
    }


    @Override
    public void update() {
        if(isDead) return;
        super.update();
        if(currentHP <= 0) isDead = true;
    }
    public abstract void update(Bomber player);

    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        /* * Hiển thị máu */
        renderHP(gc, gameplay);

        gc.setEffect(effect);
        // Whether object is on screen
//        if(!onScreen(gameplay)) return;

        //  If spotted bomber
        if(status == SERIOUS) gc.drawImage(spot.getFxImage(), x - gameplay.translate_x + gameplay.offsetX + this.getWidth() / 2
                                                            , y - gameplay.translate_y + gameplay.offsetY - 18 );

        // If it is going backward
        gc.drawImage(this.getImg(), x - gameplay.translate_x + gameplay.offsetX + ((Sprite.SCALED_SIZE * (1 - reversed)) / 2)
                , y - gameplay.translate_y + gameplay.offsetY
                , Sprite.SCALED_SIZE * reversed
                , Sprite.SCALED_SIZE);
        gc.setEffect(null);
    }

}
