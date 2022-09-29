package uet.oop.bomberman.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

import java.io.IOException;

public class BombermanGame extends Application {

    /** 960 x 720 */
    public static final int WIDTH = 20;
    public static final int HEIGHT = 14;

    private GraphicsContext gc;
    private Canvas canvas;
    public static Scene scene;
    private Gameplay level = new Gameplay();
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }
    //number of terrain styles
    public static int floor_styles = 2;
    public static int brick_styles = 1;
    public static int wall_styles = 1;
    @Override
    public void start(Stage stage) throws IOException {
        //Loads tiles properties
        load_tiles();

        //Canvas generates
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        scene = new Scene(root);

        // Thêm scene vao stage
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Bomberman");

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

        load_level();
    }

    /** updating */
    public void update() {
        level.update();
    }

    public void load_tiles() {
        Sprite.load_tiles(floor_styles, wall_styles);
        SpriteSheet.load_tiles(brick_styles);
    }

    public void load_level() throws IOException {
        //importing
        level.importing("src/main/resources/maps/sandbox_map.txt");
    }
    /** render objects */
    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        level.render(gc);
    }
}
