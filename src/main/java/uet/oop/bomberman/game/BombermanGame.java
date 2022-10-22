package uet.oop.bomberman.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;

import java.io.IOException;

public class BombermanGame extends Application {

    /** 1056 x 576 */
    public static final int WIDTH = 22;
    public static final int HEIGHT = 12;

    /** Frame control */
    public static final int FPS = 80;
    private GraphicsContext gc;
    private Canvas canvas;
    public static Scene scene;
    private Gameplay game = new Gameplay();

    @Override
    public void start(Stage stage) throws IOException {
        /* * Tạo canvas */
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(Sprite.SCALED_SIZE * WIDTH + 200, Sprite.SCALED_SIZE * HEIGHT + 200);
        stackPane.getChildren().add(canvas);

        /* * Tạo scene * */
        scene = new Scene(stackPane);

        /* * Liên kết canvas với kích thước của Pane * */
        canvas.widthProperty().bind(
                stackPane.widthProperty());
        canvas.heightProperty().bind(
                stackPane.heightProperty());

        /* * Thêm scene vao stage */
        stage.setScene(scene);

        stage.show();
        stage.setTitle("League of Bomberman");
        AnimationTimer timer = new AnimationTimer() {

            private long  lastUpdate = 0;
            @Override
            public void handle(long l) {
                /* * Control FPS */
                long time_gap = 1_000_000_000 / FPS;
                if( l - lastUpdate >= time_gap){
                    update();
                    render();
                    lastUpdate = l;
                }
            }
        };
        timer.start();

        System.out.println(timer);
        game.importing("src/main/resources/maps/map.txt", "src/main/resources/maps/area.txt");

    }


    /** Updating */
    public void update() {
        game.update();
    }


    /** Render objects */
    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        game.render(gc, canvas.getWidth(), canvas.getHeight());
    }

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }
}
