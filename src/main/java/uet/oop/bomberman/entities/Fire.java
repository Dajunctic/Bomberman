package uet.oop.bomberman.entities;

import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

import static uet.oop.bomberman.game.Gameplay.tile_map;

public class Fire extends Entity{

    protected DeadAnim ignite = new DeadAnim(SpriteSheet.ignite, 3, 1);
    protected DeadAnim fade = new DeadAnim(SpriteSheet.fire_fade, 3, 1);
    protected DeadAnim burn;
    int tileX ,tileY;
    Point firePoint;
    public Fire(double xUnit, double yUnit) {
        super(xUnit, yUnit);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
    }

    public Fire(double xUnit, double yUnit, double duration){
        super(xUnit, yUnit);
        tileX = (int) xUnit;
        tileY = (int) yUnit;
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        burn = new DeadAnim(SpriteSheet.fire, 8, duration);

        firePoint = new Point(tileX, tileY);
        Gameplay.fires.add(firePoint);
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
        Gameplay.fires.remove(firePoint);
        Gameplay.kill(tileX, tileY);
    }

    @Override
    public void kill() {
        Gameplay.killTask.add(new Point(tileX,tileY));
    }
}
