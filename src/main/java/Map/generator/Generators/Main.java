package Map.generator.Generators;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

import java.io.IOException;

public class Main {
    public final static int width = 40;
    public final static int height = 30;

    private GraphicsContext gc;
    private Canvas canvas;
    public static Scene scene;
    public static final int floor_styles = 2;
    public static final int brick_styles = 1;
    public static final int wall_styles = 1;


    public void start(Stage stage) throws IOException {
        //Loads tiles properties
        load_tiles();

        //Canvas generates
        canvas = new Canvas(Sprite.SCALED_SIZE * width, Sprite.SCALED_SIZE * height);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        scene = new Scene(root);

        // Thêm scene vao stage
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Tile Editor");

        //frame update
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                render();
            }
        };
        timer.start();
        System.out.println(timer);
    }


    /** updating */
    public void update() {

    }

    public void load_tiles() {
        Sprite.load_tiles(floor_styles, wall_styles);
        SpriteSheet.load_tiles(brick_styles);
    }

    public void load_level() throws IOException {
        //importing
    }
    /** render objects */
    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
