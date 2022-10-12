package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.SpriteSheet;

import static uet.oop.bomberman.game.Gameplay.*;

public class Brick extends  Entity{

    protected DeadAnim brick = new DeadAnim(SpriteSheet.brick,4,1);
    protected Image passive;
    protected boolean isDead = false;
    private int tileX, tileY;
    public Brick(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }

    public Brick(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
        tileX = (int) xUnit;
        tileY = (int) yUnit;
        passive = brick.getImage();

    }

    public void kill() {
        isDead = true;
    }


    @Override
    public void update() {
    }

    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        double renderX = x - gameplay.translate_x + gameplay.offsetX;
        double renderY = y - gameplay.translate_y + gameplay.offsetY;


        if(!isDead) gc.drawImage(img,renderX, renderY);
        else {
            gc.drawImage(passive, renderX, renderY);

            //death trigger
            if(brick != null){
                if(exists()) {
                    gc.drawImage(brick.getImage(), renderX, renderY);
                    brick.update();
                } else {
                    deadAct();
                }
            }
        }

    }

    public boolean  exists() {
        return !brick.isDead();
    }
    @Override
    public Image getImg() {
        return brick.getImage();
    }


    public void deadAct() {
        if( brick == null) return ;
        brick = null;
        Gameplay.set('.', tileX, tileY, true);
    }
}
