package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

import static uet.oop.bomberman.game.Gameplay.tile_map;

public class Brick extends  Entity{

    protected DeadAnim brick = new DeadAnim(SpriteSheet.brick,15,1);
    protected Image passive;
    protected boolean isDead = false;
    private int tilex,tiley;
    public Brick(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }

    public Brick(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
        tilex = (int) xUnit;
        tiley = (int) yUnit;
        passive = brick.getFxImage();

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
            if(!exists() && brick != null){
                gc.drawImage(brick.getFxImage(), x - gameplay.translate_x, y - gameplay.translate_y);
                brick.update();
            }

        }

    }
    public boolean exists() {
        return !brick.isDead();
    }
    @Override
    public Image getImg() {
        return brick.getFxImage();
    }

    @Override
    public void deadAct(Gameplay gameplay) {
        brick = null;
        tile_map[tiley][tilex] = '0';
    }
}
