package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.others.Basic;
import uet.oop.bomberman.others.HealthBar;
import uet.oop.bomberman.others.Physics;

import java.util.ArrayList;
import java.util.List;

import static uet.oop.bomberman.game.Gameplay.*;

/** Everything that moves */
public class Mobile extends Entity{
    protected double speed;
    protected double dir_x = 0;
    protected double dir_y = 0;

    /** Heath Point * */
    public static final int DEFAULT_HP = 200;
    public static final int FIRE_SUBTRACT_HP = 1;
    public static final int EXPLOSION_SUBTRACT_HP = 100;
    public int maxHP;
    public int currentHP;
    public HealthBar HPBar;
    public double barX;
    public double barY;

    public static List<Mobile> mobiles = new ArrayList<>();

    // inheritance
    public Mobile(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        HPBar = new HealthBar(xPixel, yPixel - 10, DEFAULT_HP);
        setHP(DEFAULT_HP);
        mobiles.add(this);
    }

    public Mobile(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        HPBar.update();

        subtractHP(getInjured());

    }

    /** Trả về số máu bị mất */
    public int getInjured() {

        int subtractHP = 0;


        /* *********** FIRE ************ */
        boolean checkFire = false;

        for (Point p: fires) {

            Rectangle a = new Rectangle(p.getX() * Sprite.SCALED_SIZE, p.getY() * Sprite.SCALED_SIZE,
                    Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

            Rectangle b = getRect(x, y, getWidth(), getHeight());

            if (Physics.collisionRectToRect(a, b)){
                checkFire = true;
            }
        }
        if (checkFire) {
            subtractHP += FIRE_SUBTRACT_HP;
        }

        return subtractHP;
    }

    //extension
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

            }
        }

        return false;
    }

    public void move() {
        double ref_x = Math.max(0,Math.min(width*Sprite.SCALED_SIZE - this.getWidth(),x  +  speed * dir_x));
        double ref_y = Math.max(0,Math.min(height*Sprite.SCALED_SIZE - this.getHeight(),y  +  speed * dir_y));
        if(!checkCollision(ref_x,ref_y,5)) {
            x = ref_x;
            y = ref_y;
        }
    }

    public Rectangle getRect(double x, double y, double w, double h) {
        return new Rectangle(x, y, w, h);
    }

    public Rectangle getRectCollision() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    /** *** Tất cả hàm liên quan tới Health Point */
    public void setHP(int HP) {
        maxHP = HP;
        currentHP = HP;

        HPBar = new HealthBar(x, y - 10, HP);
        HPBar.setCurrentImg(maxHP);
        HPBar.setTempImg(0);
    }

    void setCurrentHP(int HP) {
        currentHP = HP;
        HPBar.setCurrentImg(currentHP);
    }

    public void addHP(int inc) {
        currentHP += Math.min(maxHP - currentHP, inc);
        HPBar.setCurrentImg(currentHP);
    }

    public void subtractHP(int dec) {
        HPBar.setTempImg(currentHP);
        currentHP -= Math.min(dec, currentHP);
        HPBar.setCurrentImg(currentHP);

    }

    public void renderHP(GraphicsContext gc, Gameplay gameplay) {
        double renderX = x - gameplay.translate_x + gameplay.offsetX;
        double renderY = y - gameplay.translate_y + gameplay.offsetY;

        this.barX = x + (this.getImg().getWidth() - 38) / 2;
        this.barY = y - 20;

        HPBar.setPosition(barX, barY);

        HPBar.render(gc, gameplay);
    }
}
