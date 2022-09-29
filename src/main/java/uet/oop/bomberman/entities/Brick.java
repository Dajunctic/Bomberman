package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.SpriteSheet;

import static uet.oop.bomberman.game.Gameplay.tile_map;

public class Brick extends  Entity{

    protected DeadAnim brick;
    protected Image passive;
    protected int theme;
    protected boolean isDead = false;
    private int tileX, tileY;
    public Brick(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }

    //blind caller
    public Brick(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
        tileX = (int) xUnit;
        tileY = (int) yUnit;
    }

    public Brick(double xUnit, double yUnit, Image img, int theme) {
        super(xUnit, yUnit, img);
        tileX = (int) xUnit;
        tileY = (int) yUnit;
        this.theme = theme;
        brick = new DeadAnim(SpriteSheet.bricks.get(theme), 10, 1);
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
        if(!isDead) gc.drawImage(passive,x - gameplay.translate_x, y - gameplay.translate_y);
        else {
            gc.drawImage(img, x - gameplay.translate_x, y - gameplay.translate_y);

            //death trigger
            if(brick != null){
                if(exists()) {
                    gc.drawImage(brick.getImage(), x - gameplay.translate_x, y - gameplay.translate_y);
                    brick.update();
                    }
                else deadAct();
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
        tile_map[tileY][tileX] = 0;
    }
}
