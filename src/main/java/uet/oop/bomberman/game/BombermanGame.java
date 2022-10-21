package uet.oop.bomberman.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.music.Sound;
import uet.oop.bomberman.others.TotalScene;



import java.io.IOException;

public class BombermanGame extends Application {

    /** 1056 x 576 */
    public static final int WIDTH = 22;
    public static final int HEIGHT = 12;

    /** Frame control */
    public static final int FPS = 80;
    public static int currentFrame = 0;
    private GraphicsContext gc;
    public static Canvas canvas = new Canvas(WIDTH * Sprite.SCALED_SIZE, HEIGHT * Sprite.SCALED_SIZE);;
    public static Scene scene;
    private Gameplay game = new Gameplay();
    private Sound sound = new Sound();
    private TotalScene totalScene = new TotalScene();
    @Override
    public void start(Stage stage) throws IOException {
        /* * Tạo canvas */
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
        stage.setScene(totalScene.getMenu().getScene());

        stage.show();
        stage.setTitle("Bomberman Super X");
        AnimationTimer timer = new AnimationTimer() {

            //Control FPS
            public long  time_gap = 1_000_000_000/FPS;
            private long  lastUpdate = 0;
            @Override
            public void handle(long l) {
                if( l - lastUpdate >= time_gap){
                    update(stage);
                    render();
                    lastUpdate = l;
                    currentFrame++;
                }
            }
        };
        timer.start();

        System.out.println(timer);
        game.importing("src/main/resources/maps/map.txt", "src/main/resources/maps/area.txt");

    }


    /** Updating */
    public void update(Stage stage) {
        totalScene.update(stage);
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
