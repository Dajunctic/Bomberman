package uet.oop.bomberman.entities.entities.background;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.*;

import java.util.Objects;


public class Fence extends Entity {

    public static Image horizontalUp = new Image(Objects.requireNonNull(Fence.class.getResourceAsStream("/sprites/Obstacles/Fence/horizontal_up.png")));
    public static Image horizontalDown = new Image(Objects.requireNonNull(Fence.class.getResourceAsStream("/sprites/Obstacles/Fence/horizontal_down.png")));
    public static Image verticalUp = new Image(Objects.requireNonNull(Fence.class.getResourceAsStream("/sprites/Obstacles/Fence/vertical_up.png")));
    public static Image verticalDown = new Image(Objects.requireNonNull(Fence.class.getResourceAsStream("/sprites/Obstacles/Fence/vertical_down.png")));

    public static int HORIZONTAL = 1;
    public static int VERTICAL = 2;
    public static boolean UP = true;
    public static boolean DOWN = false;
    private int posX;
    private int posY;
    private int tileX;
    private int tileY;
    private int type;
    /** up is 1, down is 0 */
    private boolean status;

    private Anim fire;

    public Fence(int xUnit, int yUnit, int type, int fire_type) {
        super(xUnit, yUnit);
        this.tileX = xUnit;
        this.tileY = yUnit;
        this.posX = xUnit * Sprite.SCALED_SIZE;
        this.posY = yUnit * Sprite.SCALED_SIZE;

        this.type = type;
        this.status = true;

        fire = new Anim(SpriteSheet.firePillar.get(fire_type), 4);
    }

    public void setStatus(boolean status) {
        this.status = status;

        char tile;

        if (status == UP) {
            tile = '~';
        } else {
            tile = '.';
        }

        if (type == HORIZONTAL) {
            for (int i = tileX; i < tileX + 5; i++) {
                Gameplay.set(tile, i, tileY, false);
            }
        }else {
            for (int i = tileY; i < tileY + 5; i++) {
                Gameplay.set(tile, tileX, i, false);
            }
        }

    }


    @Override
    public void update() {
        fire.update();
    }

    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        double renderX = posX  - gameplay.translate_x + gameplay.offsetX;
        double renderY = posY - gameplay.translate_y + gameplay.offsetY;

        if (type == HORIZONTAL) {
            if (status == UP) {
                renderY -= 40;
                gc.drawImage(horizontalUp, renderX, renderY);
            } else {
                gc.drawImage(horizontalDown, renderX, renderY);
            }
        }

        if (type == VERTICAL) {
            if (status == UP) {
                renderY -= 30;
                gc.drawImage(verticalUp, renderX, renderY);
            } else {
                gc.drawImage(verticalDown, renderX, renderY);
            }
        }
    }

    @Override
    public void render(GraphicsContext gc, Renderer renderer) {

        if(type == HORIZONTAL) {
            renderer.renderImg(gc, (status == UP ? horizontalUp : horizontalDown),
                    posX + shiftX,
                    posY + shiftY , false);

            renderer.renderImg(gc, fire.getImage(),
                    posX + shiftX - 48,
                    posY + shiftY - 10 , false);
            renderer.renderImg(gc, fire.getImage(),
                    posX + shiftX + 48 * 5,
                    posY + shiftY - 10 , false);

        } else if ( type == VERTICAL) {
            renderer.renderImg(gc, (status == UP ? verticalUp : verticalDown),
                    posX + shiftX,
                    posY + shiftY, false);

            renderer.renderImg(gc, fire.getImage(),
                    posX + shiftX ,
                    posY + shiftY - 10 - 48, false);
            renderer.renderImg(gc, fire.getImage(),
                    posX + shiftX ,
                    posY + shiftY - 10 + 48 * 5, false);
        }
    }
    public void free() {

    }
}
