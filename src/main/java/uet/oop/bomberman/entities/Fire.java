package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.util.Pair;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.maps.GameMap;

import static uet.oop.bomberman.game.Gameplay.fires;
import static uet.oop.bomberman.game.Gameplay.tile_map;

public class Fire extends Entity{

    protected DeadAnim ignite = new DeadAnim(SpriteSheet.ignite, 3, 1);
    protected DeadAnim fade = new DeadAnim(SpriteSheet.fire_fade, 3, 1);
    protected DeadAnim burn;
    boolean friendly;
    int tileX ,tileY;
    Point firePoint;
    ColorAdjust effect;
    private Integer index = 0;
    private int damage = 3;
    private Pair<Integer, Boolean> effector;
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
            effect = new ColorAdjust();
            effect.setHue(-0.3);
            effect.setBrightness(-1);
            effect.setContrast(1.0);
        }
        index = Gameplay.tileCode(tileX, tileY);
        this.damage = damage;
        effector = new Pair<>(damage, friendly);
        Gameplay.fires.put(index, effector);
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
        Gameplay.fires.get(index).remove(effector);
        Gameplay.kill(tileX, tileY);
    }

    @Override
    public void kill() {
        Gameplay.killTask.add(new Point(tileX,tileY));
    }
    //re-apply effects
}
