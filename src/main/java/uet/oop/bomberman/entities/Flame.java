package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Basic;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

import static uet.oop.bomberman.game.BombermanGame.HEIGHT;
import static uet.oop.bomberman.game.BombermanGame.WIDTH;
import static uet.oop.bomberman.game.Gameplay.*;

public class Flame extends Mobile{
    DeadAnim flame;

    protected double duration;
    protected double dir_x;
    protected double dir_y;

    public Flame(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        mode = CENTER_MODE;
    }

    public Flame(double _x, double _y, double length,double dirx,double diry) {
        super(_x,_y);
        mode = CENTER_MODE;
        //set speed
        speed = length/30;
        dir_x =  dirx;
        dir_y =  diry;

        // Animation
            if(dir_x > 0.5) {
                flame = new DeadAnim(SpriteSheet.flame_right, 5, 0.5);
            }
            else if(dir_x < 0.4 && dir_x > -0.4) {
                if(dir_y > 0) flame = new DeadAnim(SpriteSheet.flame_down,5,0.5);
                    else flame = new DeadAnim(SpriteSheet.flame_up, 5, 0.5);
            }
            else {
                flame = new DeadAnim(SpriteSheet.flame_left, 5, 0.5);
            }

    }
    @Override
    public void update() {
        //moving
        double ref_x = Math.max(0,Math.min(width*Sprite.SCALED_SIZE - this.getWidth(),x  +  speed * dir_x));
        double ref_y = Math.max(0,Math.min(height*Sprite.SCALED_SIZE - this.getHeight(),y  +  speed * dir_y));
        if(!checkCollision(ref_x,ref_y,5)) {
            x = ref_x;
            y = ref_y;
        }
        //animation and status update
        flame.update();
    }

    @Override
    public Image getImg() {
        return flame.getFxImage();
    }

    @Override
    public boolean isExisted() {
        return !flame.isDead();
    }

    @Override
    public void render(GraphicsContext gc,Gameplay gameplay) {
//        Rectangle rect = new Rectangle(x - gameplay.translate_x, y - gameplay.translate_y, this.getHeight(), this.getHeight());
//        Basic.drawRectangle(gc, rect);
        super.render(gc, gameplay);
    }
}
