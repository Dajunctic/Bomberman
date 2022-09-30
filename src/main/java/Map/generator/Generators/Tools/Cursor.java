package Map.generator.Generators.Tools;

import Map.generator.Generators.Main;
import Map.generator.Generators.Proccessor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.Generals.Point;
import uet.oop.bomberman.graphics.Sprite;

public class Cursor {
    public static final int mouseClicked = 0;
    public static final int mouseDragged = 1;
    public static final int mousePressed = 2;
    public static final int DRAW = 0;
    public static final int SELECT = 1;
    public int mode;
    private int tile_code;
    public Rectangle mapZone;
    public Rectangle tileZone;
    public Rectangle functionalZone;
    public Cursor() {
        mapZone = new javafx.scene.shape.Rectangle(0,
                                                  0,
                                                  Proccessor.zoneX * Sprite.SCALED_SIZE,
                                                  Proccessor.zoneY * Sprite.SCALED_SIZE);
        tileZone = new Rectangle(Proccessor.zoneX * Sprite.SCALED_SIZE,
                                    0,
                                    Main.width - Proccessor.zoneX,
                                    Main.height );
        mode = DRAW;
    }


    public void interact(int mouseX, int mouseY, Proccessor proccessor, int mouseEvent) {

    }
    public void render(GraphicsContext gc) {

    }
}