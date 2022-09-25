package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

import static uet.oop.bomberman.game.BombermanGame.HEIGHT;
import static uet.oop.bomberman.game.BombermanGame.WIDTH;
import static uet.oop.bomberman.game.Gameplay.tile_map;

public class Flame extends Entity{
    DeadAnim flame;

    protected double speed;
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
        double ref_x = x + speed * dir_x;
        double ref_y = y + speed * dir_y;

        // collision handling
        if(tile_map[Math.max(0,Math.min(Gameplay.height - 1,
                (int) Math.floor((ref_y+(double)20*48/17)/ Sprite.SCALED_SIZE)))]
                [Math.max(0, Math.min(Gameplay.width -1,
                (int) Math.floor((ref_x + 24)/Sprite.SCALED_SIZE ))) ] == '0'
                &&  tile_map[Math.max(0,Math.min(Gameplay.height - 1,
                        (int) Math.floor(ref_y/Sprite.SCALED_SIZE)))]
                             [Math.max(0, Math.min(Gameplay.width -1,
                        (int) Math.floor(ref_x/Sprite.SCALED_SIZE )))] == '0'
                &&  ref_x > 0 && ref_x < (Gameplay.width - 1) * Sprite.SCALED_SIZE
                &&  ref_y > 0 && ref_y < (Gameplay.height - 1) * Sprite.SCALED_SIZE) {
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
}
