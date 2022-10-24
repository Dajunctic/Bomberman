package uet.oop.bomberman.entities;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
//The nuke
public class Nuke extends Entity{

    public DeadAnim nuke;
    private double nukeThreshold = 0.01;
    private DeadAnim explosion = new DeadAnim(SpriteSheet.nuke_explosion, 5, 1);
    private double expThreshold = 0.01;
    private int tileX;
    private int tileY;
    private int radius = 5;
    private boolean exploded = false;
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
            if(nuke.isDead()) setMode(BOTTOM_MODE);
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
        if(exploded) return;
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
}
