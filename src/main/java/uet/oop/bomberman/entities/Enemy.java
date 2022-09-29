package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.VertexFormat;
import uet.oop.bomberman.Generals.Point;
import uet.oop.bomberman.Generals.Vertex;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.others.Physics;

import java.util.Random;

import static uet.oop.bomberman.game.Gameplay.*;

public class Enemy extends Mobile{

    protected Anim enemy;
    protected DeadAnim killed;
    protected Point focus;
    //state of game
    protected final int[] dir = {1, -1};
    protected Random switcher = new Random();
    protected static final int WANDERING = 0;
    protected static final int SERIOUS = 1;
    //detect player
    private int sight_depth = 3;
    protected int frequency = 10;
    protected double sight_angle = Math.PI / 2;
    protected int status = WANDERING;
    protected int strict = 5;
    //moving
    protected int speed = 1;
    protected Vertex direction = new Vertex(1,0);
    protected Point recent_tile;
    //life status
    protected boolean isDead = false;
    public Enemy(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        recent_tile = new Point((int) xPixel / Sprite.SCALED_SIZE, (int) yPixel / Sprite.SCALED_SIZE);
    }

    public Enemy(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
        recent_tile = new Point((int) xUnit / Sprite.SCALED_SIZE, (int) yUnit / Sprite.SCALED_SIZE);
    }

    /** random setter */
    public void switchDirection() {
        if(dir_x == 1 || dir_x == -1) {
            dir_x = 0;
            dir_y = dir[switcher.nextInt(dir.length)];
        }
        else {
            dir_y = 0;
            dir_x = dir[switcher.nextInt(dir.length)];
        }
        direction.setX(dir_x);
        direction.setY(dir_y);
    }

    /** detect player */
    private double distance(Bomber player) {
        return Math.sqrt((player.x - x)*(player.x - x) + (player.y - y)*(player.y - y));
    }

    protected void search(Bomber player) {
        if(distance(player) < sight_depth * Sprite.SCALED_SIZE) {
            Vertex line = new Vertex(player.x - x, player.y - y);
            Vertex dir = new Vertex(dir_x, dir_y);
            if(Math.abs(dir.angle(line)) <= sight_angle){
                direction.set(player.x - x, player.y - y);
                direction.normalize();
                status = SERIOUS;
            }
        }
    }
    @Override
    public void move() {
        double ref_x = x +  speed * direction.getX();
        double ref_y = y +  speed * direction.getY();
        if(!checkCollision(ref_x, ref_y, 0)){
            if(focus != null)
                if(ref_x - focus.getX() <= strict && ref_y - focus.getY() <= strict) {
                    focus = null;
                    status = WANDERING;
                    switchDirection();
                }
            x = ref_x;
            y = ref_y;

            //capturing the tile, kills player
            if(tile_map[(int) Math.floor(getCenterX() / Sprite.SCALED_SIZE)][(int) Math.floor(getCenterY() / Sprite.SCALED_SIZE)] == 0 &&
                tile_map[recent_tile.getX()][recent_tile.getY()] < 0){

                tile_map[(int) Math.floor(x / Sprite.SCALED_SIZE)][(int) Math.floor(y / Sprite.SCALED_SIZE)] = -1;
                killTask.add(recent_tile);
                recent_tile.setX((int) Math.floor(x / Sprite.SCALED_SIZE));
                recent_tile.setY((int) Math.floor(y / Sprite.SCALED_SIZE));
            }
        }
        else{
            if(status == SERIOUS) status = WANDERING;
            switchDirection();
        }

    }

    @Override
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        // có đấy bạn ạ
        if(ref_x < 0 || ref_y < 0
                || ref_x > width * Sprite.SCALED_SIZE - this.getWidth()
                || ref_y > height * Sprite.SCALED_SIZE - this.getHeight()) return true;

        javafx.scene.shape.Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new javafx.scene.shape.Rectangle(ref_x - this.getWidth() / 2 + margin, ref_y - this.getHeight() / 2 + margin, this.getWidth() - margin, this.getHeight() - margin);
        else
            rect = new javafx.scene.shape.Rectangle(ref_x, ref_y, this.getWidth(), this.getHeight());

        // Không cần check ra khỏi map vì trong update BOMBER hoặc ENEMY sẽ giới hạn speed.

        // Thay vì ngồi debug code Hưng fake thì tôi kiểm tra tất cả các tiles xung quanh thực thể luôn.
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

                javafx.scene.shape.Rectangle tileRect = new Rectangle(tileX, tileY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

                if (tile_map[j][i] > 0) {
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        return true;
                    }
                }
                else if (tile_map[j][i]  < 0) {
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

    public void update(Bomber player) {

    }
}
