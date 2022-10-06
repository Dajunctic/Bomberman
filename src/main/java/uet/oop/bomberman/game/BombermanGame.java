package uet.oop.bomberman.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;

import java.io.IOException;

public class BombermanGame extends Application {

    /** 960 x 720 */
    public static final int WIDTH = 24;
    public static final int HEIGHT = 14;

    private GraphicsContext gc;
    private Canvas canvas;
    public static Scene scene;
    private Gameplay level = new Gameplay();
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Tao Canvas
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
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                render();
            }
        };
        timer.start();
        System.out.println(timer);
        level.importing("src/main/resources/stages/Stage1.txt");
    }

    /** updating */
    public void update() {
        level.update();
    }



    /** render objects */
    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        level.render(gc);
    }
}
