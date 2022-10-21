package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.maps.GameMap;

import static uet.oop.bomberman.game.Gameplay.tile_map;

public class Fire extends Entity{

    protected DeadAnim ignite = new DeadAnim(SpriteSheet.ignite, 3, 1);
    protected DeadAnim fade = new DeadAnim(SpriteSheet.fire_fade, 3, 1);
    protected DeadAnim burn;
    boolean friendly;
    int tileX ,tileY;
    ColorAdjust effect;
    public Fire(double xUnit, double yUnit) {
        super(xUnit, yUnit);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
    }

    public Fire(double xUnit, double yUnit, double duration, boolean friendly){
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
        //Burning ground
//        Gameplay.set('!', tileX, tileY, false);
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
        Gameplay.set('.', tileX, tileY, false);
        Gameplay.kill(tileX, tileY);
    }

    @Override
    public void kill() {
        Gameplay.killTask.add(new Point(tileX,tileY));
    }
    //re-apply effects
    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {

        gc.setEffect(effect);
        // Whether object is on screen
        if(!onScreen(gameplay)) return;

        if (mode == Entity.CENTER_MODE) {
            renderCenter(gc, gameplay);
        } else if (mode == Entity.BOTTOM_MODE) {
            renderBottom(gc, gameplay);
        } else {
            gc.drawImage(this.getImg(), x - gameplay.translate_x + gameplay.offsetX
                    , y - gameplay.translate_y + gameplay.offsetY);
        }

        gc.setEffect(null);
    }
}
