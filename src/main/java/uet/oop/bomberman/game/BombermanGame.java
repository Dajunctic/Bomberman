package uet.oop.bomberman.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.Music;
import uet.oop.bomberman.others.TotalScene;



import java.io.IOException;

import static uet.oop.bomberman.music.Sound.ratio;

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
    public static StackPane stackPane;
    private Gameplay game = new Gameplay();
    public static Audio audio = new Audio();
    private TotalScene totalScene = new TotalScene();
    public static MediaPlayer menu_bg = Audio.copy(Audio.background_music);
    public static MediaPlayer game_bg = Audio.copy(Audio.gameplay);
    static {
        menu_bg.setCycleCount(MediaPlayer.INDEFINITE);
        game_bg.setCycleCount(MediaPlayer.INDEFINITE);
        game_bg.stop();
        menu_bg.play();
    }
    @Override
    public void start(Stage stage) throws IOException {
        /* * Tạo canvas */
        gc = canvas.getGraphicsContext2D();

        stackPane = new StackPane();
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
        stage.setTitle("League of Bomberman");
        AnimationTimer timer = new AnimationTimer() {

            //Control FPS
            public long  time_gap = 1_000_000_000/FPS;
            private long  lastUpdate = 0;
            @Override
            public void handle(long l) {
                /* * Control FPS */
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

    public static void startGame() {
        menu_bg.seek(Duration.ZERO);
        menu_bg.stop();
        game_bg.play();
    }
    public static void setBgVolume() {
        menu_bg.setVolume(ratio);
        game_bg.setVolume(ratio * 0.6);
//        System.out.println("Volume set to: " + ratio);
    }
    public static void main(String[] args) {

        Application.launch(BombermanGame.class);
    }
}
