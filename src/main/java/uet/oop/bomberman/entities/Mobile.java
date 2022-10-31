package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.Renderer;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.others.Basic;
import uet.oop.bomberman.others.HealthBar;
import uet.oop.bomberman.others.ManaBar;
import uet.oop.bomberman.others.Physics;

import java.util.ArrayList;
import java.util.List;

import static uet.oop.bomberman.game.BombermanGame.HEIGHT;
import static uet.oop.bomberman.game.BombermanGame.WIDTH;
import static uet.oop.bomberman.game.Gameplay.*;

/** Everything that moves */
public class Mobile extends Entity{
    protected double speed;
    protected Vertex direction;

    /** Health Point * */
    public static final int DEFAULT_HP = 1000;
    public static final int HP_RECOVER_PER_SECOND = 20;
    public static final int HP_RECOVER = 200 ; // Skill F

    public static final int FIRE_SUBTRACT_HP = 5;
    public static final int EXPLOSION_SUBTRACT_HP = 400;
    public int maxHP;
    public int currentHP;
    public HealthBar HPBar;
    public double barX;
    public double barY;

    /** Mana Point * */
    public static final int DEFAULT_MANA = 400;
    public static final int MANA_RECOVER_PER_SECOND = 1;
    public static final int Q_MANA_CONSUMING = 10;
    public static final int W_MANA_CONSUMING = 50;
    public static final int E_MANA_CONSUMING = 20;
    public static final int R_MANA_CONSUMING = 20;
    public int maxMana;
    public int currentMana;
    public ManaBar manaBar;

    long lastTime = 0;
    //fire properties
    protected int tileX;
    protected int tileY;

    protected boolean isAlly = false;
    protected int margin = 0;

    public static List<Mobile> mobiles = new ArrayList<>();
    // inheritance
    public Mobile(double xPixel, double yPixel) {
        super(xPixel, yPixel);

        /* * HP Constructor */
        HPBar = new HealthBar(xPixel, yPixel - 10, DEFAULT_HP);
        setHP(DEFAULT_HP);

        /* * Mana Constructor */
        manaBar = new ManaBar(xPixel, yPixel - 10, DEFAULT_MANA);
        setMana(DEFAULT_MANA);
        mobiles.add(this);
    }

    public Mobile(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        /* * Hồi máu và mana mỗi giây */
        if (System.currentTimeMillis() - lastTime > 1000) {
            addHP(HP_RECOVER_PER_SECOND);
            addMana(MANA_RECOVER_PER_SECOND);

            lastTime = System.currentTimeMillis();
        }

        HPBar.update();
        subtractHP(getInjured());

        manaBar.update();

    }

    /** Trả về số máu bị mất */
    public int getInjured() {
        //extract hashcode
        standingTile();
        Integer tileCode = Gameplay.tileCode(tileX, tileY);
        //processor
        int subtractHP = 0;
        /* *********** FIRE ************ */
        if(!fires.containsKey(tileCode)) return 0;
//        System.out.println(getClass() + ", Caught fire in:" + tileX + " " + tileY + ", " + tileCode + " " + fires.get(tileCode) );
        for(Pair<Integer, Boolean> fire : fires.get(tileCode)) {
            //friendly fire
            if(fire.getValue() == isAlly) continue;
            //damaging
            subtractHP += fire.getKey();
//            System.out.println(String.format("-%d HP", fire.getKey()) );
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
        double ref_x = Math.max(0,Math.min(width*Sprite.SCALED_SIZE - this.getWidth(),x  + speed * direction.getX()));
        double ref_y = Math.max(0,Math.min(height*Sprite.SCALED_SIZE - this.getHeight(),y  +  speed * direction.getY()));
        if(!checkCollision(ref_x,ref_y,margin)) {
            x = ref_x;
            y = ref_y;
            standingTile();
        }
    }

    public Rectangle getRect(double x, double y, double w, double h) {
        return new Rectangle(x, y, w, h);
    }

    public Rectangle getRectCollision() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    //update standing tiles
    public void standingTile() {
        switch (mode) {
            case NORMAL_MODE -> {
                tileX = (int) Math.floor((x + getWidth() / 2) / Sprite.SCALED_SIZE);
                tileY = (int) Math.floor((y + getHeight() - 5) / Sprite.SCALED_SIZE);
            }
            case CENTER_MODE -> {
                tileX = (int) Math.floor((x) / Sprite.SCALED_SIZE);
                tileY = (int) Math.floor((y + getHeight()/2 - 5) / Sprite.SCALED_SIZE);
            }
            case BOTTOM_MODE -> {
                tileX = (int) Math.floor((x) / Sprite.SCALED_SIZE);
                tileY = (int) Math.floor((y - 5) / Sprite.SCALED_SIZE);
            }
            default -> throw new IllegalStateException("Unexpected value: " + mode);
        }
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

    public void renderHP(GraphicsContext gc, Renderer renderer) {
        this.barX = x + (this.getImg().getWidth() - 38) / 2;
        this.barY = y - 20;

        HPBar.setPosition(barX, barY);
        HPBar.render(gc, renderer);
    }
    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    /** *** Tất cả hàm liên quan tới Mana Point */
    public void setMana(int mana) {
        maxMana = mana;
        currentMana = mana;

        manaBar = new ManaBar(x, y - 10, mana);
        manaBar.setCurrentImg(maxMana);
        manaBar.setTempImg(0);
    }

    void setCurrentMana(int mana) {
        currentMana = mana;
        manaBar.setCurrentImg(mana);
    }

    public void addMana(int inc) {
        currentMana += Math.min(maxMana - currentMana, inc);
        manaBar.setCurrentImg(currentMana);
    }

    public void subtractMana(int dec) {
        manaBar.setTempImg(currentMana);
        currentMana -= Math.min(dec, currentMana);
        manaBar.setCurrentImg(currentMana);

    }

    public void renderMana(GraphicsContext gc, Gameplay gameplay) {
        double renderX = x - gameplay.translate_x + gameplay.offsetX;
        double renderY = y - gameplay.translate_y + gameplay.offsetY;

        this.barX = x + (this.getImg().getWidth() - 38) / 2;
        this.barY = y - 13;

        manaBar.setPosition(barX, barY);

        manaBar.render(gc, gameplay);
    }

    public void renderMana(GraphicsContext gc, Renderer renderer) {
        this.barX = x + (this.getImg().getWidth() - 38) / 2;
        this.barY = y - 13;

        manaBar.setPosition(barX, barY);

        manaBar.render(gc, renderer);
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public Vertex translation(double dW, double dH) {
        double translate_x = Math.max(0, Math.min( x - dW / 2,
                Gameplay.width * Sprite.SCALED_SIZE - dW));

        double translate_y = Math.max(0, Math.min( y - dH / 2,
                Gameplay.height * Sprite.SCALED_SIZE - dH ));
        return new Vertex(translate_x, translate_y);
    }
    public boolean isAlly() {
        return isAlly;
    }

    public boolean visible(Renderer renderer) {
        return true;
    }

    protected boolean vulnerable() {
        return true;
    }
}
