package uet.oop.bomberman.explosive.special;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.*;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.LargeSound;
import uet.oop.bomberman.music.Sound;

import static uet.oop.bomberman.game.Gameplay.*;

//The nuke
public class Nuke extends Entity {

    public DeadAnim nuke;
    private double nukeThreshold = 0.01;
    private DeadAnim explosion = new DeadAnim(SpriteSheet.nuke_explosion, 5, 1);
    private double expThreshold = 0.01;
    private int radius = 3;
    private boolean exploded = false;
    private boolean friendly = true;
    private double duration = 2;
    private int damage = 20;
    public Nuke(double xUnit, double yUnit, double timer) {
        super(xUnit, yUnit);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        x += (double) Sprite.SCALED_SIZE / 2;
        y +=  Sprite.SCALED_SIZE;
        tileX = (int) xUnit;
        tileY = (int) yUnit;
        nuke = new DeadAnim(SpriteSheet.nuke, 6, timer * (timer + 1) / 2);
        System.out.println("NUKE placed!!!");
        setMode(BOTTOM_MODE);
        explosion.setScaleFactor(2);
        Gameplay.sounds.add(new LargeSound(this.x, this.y, Audio.copy(Audio.nuke), timer));
    }

    public Nuke(double xUnit, double yUnit, double timer, int radius) {
        super(xUnit, yUnit);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        x += (double) Sprite.SCALED_SIZE / 2;
        y +=  Sprite.SCALED_SIZE;
        tileX = (int) xUnit;
        tileY = (int) yUnit;
        nuke = new DeadAnim(SpriteSheet.nuke, 6, timer * (timer + 1) / 2);
        System.out.println("NUKE placed!!!");
        setMode(BOTTOM_MODE);
        explosion.setScaleFactor(1 + radius / 4);
        Gameplay.sounds.add(new LargeSound(this.x, this.y, Audio.copy(Audio.nuke), timer));
        this.radius = radius;
    }

    @Override
    public void update() {
        if (nuke.isDead()) {
            explosion.update();
            expThreshold += 0.005;
            effect = new Bloom(expThreshold);
        } else {
            nuke.update(2);
            nukeThreshold += 0.04;
            effect = new Glow(nukeThreshold);
            if(nuke.isDead()){
                explode();
                setMode(BOTTOM_MODE);
            }
        }
    }


    public Image getImg() {
        if (nuke.isDead()) {
            return explosion.getImage();
        } else {
            return nuke.getImage();
        }
    }
    @Override
    public boolean isExisted() {
        return !explosion.isDead();
    }
    @Override
    public void deadAct(Gameplay gameplay) {
        //destroy all tiles including walls
        //implement method to find areaMap grid for bordering
    }

    public void explode() {
        if(exploded) return;
        exploded = true;
        nukes.add(new ShockWave(x, y, friendly, radius, damage, duration, true));
        sounds.add(new Sound(x ,y, Audio.copy(Audio.fire), duration, radius * 3));
        Gameplay.sounds.add(new LargeSound(this.x + shiftX, this.y + shiftY, Audio.copy(Audio.nuke_explosion), -1));
    }
    @Override
    public double getWidth() {
        if (nuke.isDead())
            return explosion.getImage().getWidth();
        return nuke.getImage().getWidth();
    }

    @Override
    public double getHeight() {
        if (nuke.isDead())
            return explosion.getImage().getHeight();
        return nuke.getImage().getHeight();
    }
    @Override
    public boolean onScreen(Gameplay gameplay) {
        return  true;
    }
    @Override
    public void render(GraphicsContext gc, Renderer renderer) {
        gc.setEffect(effect);
        renderer.renderDirectImg(gc, getImg(), x + shiftX, y + shiftY, false);
        gc.setEffect(null);
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    @Override
    public void render(Layer layer) {
        if(layer.shaderEnable && layer.shade && !exploded){
            if(!layer.lighter.tileCodes.contains(tileCode(tileX, tileY))) return;
        }
        render(layer.gc, layer.renderer);
    }
}
