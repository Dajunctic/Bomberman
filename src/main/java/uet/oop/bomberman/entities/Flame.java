package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.maps.AreaMap;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.others.Physics;

import java.util.HashSet;

import static uet.oop.bomberman.game.BombermanGame.FPS;
import static uet.oop.bomberman.game.Gameplay.*;

public class Flame extends Mobile{
    DeadAnim flame;
    double length;
    protected double duration = 2;
    protected double dir_x;
    protected double dir_y;

    /** Khi nào gặp vật cản như brick hoặc wall thì dừng luôn */
    boolean stop = false;

    HashSet<Integer> floors = new HashSet<>();

    public Flame(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        mode = CENTER_MODE;
    }

    public Flame(double _x, double _y, double length,double dirX,double dirY) {
        super(_x,_y);
        mode = CENTER_MODE;
        //set speed
        speed = length / 10;
        this.length = length;

        // Animation
            if(dirX > 0.5) {
                flame = new DeadAnim(SpriteSheet.flame_right, 5, 0.5);
            }
            else if(dirX < 0.4 && dirX > -0.4) {
                if(dirY > 0) flame = new DeadAnim(SpriteSheet.flame_down,5,0.5);
                    else flame = new DeadAnim(SpriteSheet.flame_up, 5, 0.5);
            }
            else {
                flame = new DeadAnim(SpriteSheet.flame_left, 5, 0.5);
            }
        direction = new Vertex(dirX, dirY);
    }

    public Flame(double _x, double _y, double length,double dirX,double dirY, double timer,double duration) {
        super(_x,_y);
        mode = CENTER_MODE;
        //set speed
        speed = length / (timer * FPS);
        this.length = length;
        // Animation
        if(Math.abs(dirX) > Math.abs(dirY)) {
            if(dirX > 0) flame = new DeadAnim(SpriteSheet.flame_right, 5, timer);
                else flame = new DeadAnim(SpriteSheet.flame_left, 5, timer);
        }
        else {
            if(dirY > 0) flame = new DeadAnim(SpriteSheet.flame_down,5, timer);
            else flame = new DeadAnim(SpriteSheet.flame_up, 5, timer);
        }

        this.duration = duration;
        this.direction = new Vertex(dirX, dirY);
    }
    @Override
    public void update() {
        //moving
        double ref_x = Math.max(0,Math.min(width * Sprite.SCALED_SIZE - this.getWidth(),x  +  speed * direction.getX()));
        double ref_y = Math.max(0,Math.min(height * Sprite.SCALED_SIZE - this.getHeight(),y  +  speed * direction.getY()));
        if(!checkCollision(ref_x,ref_y,15) && !stop) {
            length -= speed;
            if(length <= 0) speed = 0;
            x = ref_x;
            y = ref_y;
        } else {
            stop = true;
        }
        //animation and status update
        flame.update();

    }

    @Override
    public Image getImg() {
        return flame.getImage();
    }

    @Override
    public boolean isExisted() {
        return !flame.isDead();
    }

    @Override
    public void render(GraphicsContext gc,Gameplay gameplay) {
        super.render(gc, gameplay);
    }

    @Override
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        if(ref_x < 0 || ref_y < 0
                || ref_x > width * Sprite.SCALED_SIZE - this.getWidth()
                || ref_y > height * Sprite.SCALED_SIZE - this.getHeight()) return true;

        Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new Rectangle(ref_x - this.getWidth() / 2 + margin, ref_y - this.getHeight() / 2 + margin, this.getWidth() - margin * 2, this.getHeight() - margin * 2);
        else
            rect = new Rectangle(ref_x, ref_y, this.getWidth(), this.getHeight());

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

                Rectangle tileRect = new Rectangle(tileX, tileY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

                // Kiểm tra tilemap[j][i] là loại gì O(1)
                if (Gameplay.get(tile_map[j][i], i, j) == GameMap.WALL)  {
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        return true;
                    }
                }
                else if(Gameplay.get(tile_map[j][i], i, j) == GameMap.BRICK) {
                    //Do something
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        killTask.add(new Point(i,j));
                        return true;
                    }
                }
                else if(Gameplay.get(tile_map[j][i], i, j) == GameMap.FLOOR) {
                    if (Physics.collisionRectToRect(rect, tileRect)) {

                        if (!floors.contains(i * 200 + j)) {
                            floors.add(i * 200 + j);
                            entities.add(new Fire(i, j, Math.max(0.5, duration)));
                        }
                    }
                }

            }
        }

        return false;
    }
}
