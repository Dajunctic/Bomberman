package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Renderer;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Objects;

import static uet.oop.bomberman.game.Gameplay.tileCode;
import static uet.oop.bomberman.graphics.LightProbe.tileCodes;

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

    public Fence(int xUnit, int yUnit, int type) {
        super(xUnit, yUnit);
        this.tileX = xUnit;
        this.tileY = yUnit;
        this.posX = xUnit * Sprite.SCALED_SIZE;
        this.posY = yUnit * Sprite.SCALED_SIZE;

        this.type = type;
        this.status = true;
    }

    public void setStatus(boolean status) {
        this.status = status;

        char tile;

        if (status == UP) {
            tile = '@';
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
        if(!tileCodes.isEmpty()) {
            if(!tileCodes.contains(tileCode(tileX, tileY))) return;
        }
        if(type == HORIZONTAL) {
            renderer.renderImg(gc, (status == UP ? horizontalUp : horizontalDown),
                                        posX + shiftX, posY + shiftY - 40, false);
        } else if ( type == VERTICAL) {
            renderer.renderImg(gc, (status == UP ? verticalUp : verticalDown),
                                        posX + shiftX, posY + shiftY - 30, false);
        }
    }
}
