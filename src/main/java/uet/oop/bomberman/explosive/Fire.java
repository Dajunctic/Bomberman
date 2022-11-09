package uet.oop.bomberman.explosive;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import static uet.oop.bomberman.graphics.SpriteSheet.explosion;

public class Fire extends Entity {

    protected DeadAnim ignite = new DeadAnim(SpriteSheet.ignite, 3, 1);
    protected DeadAnim fade = new DeadAnim(SpriteSheet.fire_fade, 3, 1);
    protected DeadAnim burn;
    boolean friendly;
    static ColorAdjust fireEffect = new ColorAdjust(0.3, 1, 0, 0);
    static ColorAdjust mixedFire = new ColorAdjust(-0.3, 1, 0, 0);
    private Integer index = 0;
    private int damage = 3;
    public Fire(double xUnit, double yUnit) {
        super(xUnit, yUnit);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
    }

    public Fire(double xUnit, double yUnit, double duration, int damage, boolean friendly){
        super(xUnit, yUnit);
        tileX = (int) xUnit;
        tileY = (int) yUnit;
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        burn = new DeadAnim(SpriteSheet.fire, 8, duration);
        this.friendly = friendly;
        if(!friendly) {
            super.effect = mixedFire;
        }   else if(damage < 0) super.effect = fireEffect;

        index = Gameplay.tileCode(tileX, tileY);
        this.damage = damage;
        Gameplay.fires.put(index, this);
//        System.out.println(String.format("Fire in: %d %d, %d", tileX, tileY, index) + fires.get(index));
//        System.out.println("________________________________________________");
    }

    public Fire(double xUnit, double yUnit, double duration, int damage, boolean friendly, boolean special){
        super(xUnit, yUnit);
        tileX = (int) xUnit;
        tileY = (int) yUnit;
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        burn = new DeadAnim(SpriteSheet.fire, 8, duration);
        this.friendly = friendly;
        if(!friendly) {
            super.effect = mixedFire;
        }   else if(damage < 0) super.effect = fireEffect;

        index = Gameplay.tileCode(tileX, tileY);
        this.damage = damage;
        Gameplay.fires.put(index, this);
        if(special){
            ignite = new DeadAnim(explosion, 5, 1);
            ignite.setScaleFactor(1);
//            if(!friendly) sounds.add(new Sound(x, y, Audio.copy(Audio.bomb_explosion), -1, 5 * Sprite.SCALED_SIZE));
        }
//        System.out.println(String.format("Fire in: %d %d, %d", tileX, tileY, index) + fires.get(index));
//        System.out.println("________________________________________________");
    }

    @Override
    public void update() {
        if(!ignite.isDead()) ignite.update();
            else if(!burn.isDead()) burn.update();
                else fade.update();
    }

    @Override
    public Image getImg() {
        if(!ignite.isDead()) return ignite.getImage();
            else if(!burn.isDead()) return burn.getImage();
                else return fade.getImage();
    }

    @Override
    public boolean isExisted() {
        return !fade.isDead();
    }
    @Override
    public void deadAct(Gameplay gameplay) {
        super.effect = null;
        burn.free();
        ignite.free();
        fade.free();
        Gameplay.fires.remove(index);
        Gameplay.kill(tileX, tileY);
    }

    @Override
    public void kill() {
        Gameplay.killTask.add(new Point(tileX,tileY));
    }
    //re-apply effects

    public  void addDamage(int bonus) {
        if(!friendly) {
            damage += Math.abs(bonus);
            return;
        }
        if(damage * bonus <= 0) {
            damage = Math.abs(damage + bonus);
            friendly = false;
            super.effect = mixedFire;
        }
        else damage += bonus;

    }
    public int getDamage() {
        return  damage;
    }
    public boolean isFriendly() {
        return friendly;
    }
}