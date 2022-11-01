package uet.oop.bomberman.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.menu.Menu;
import uet.oop.bomberman.music.Audio;


import java.io.IOException;
import java.util.Objects;

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
    private Menu menu = new Menu();

    public static Audio audio = new Audio();
    public static MediaPlayer menu_bg = Audio.copy(Audio.background_music);
    public static MediaPlayer game_bg = Audio.copy(Audio.gameplay);
    static {
        menu_bg.setCycleCount(MediaPlayer.INDEFINITE);
        game_bg.setCycleCount(MediaPlayer.INDEFINITE);
        game_bg.stop();
        menu_bg.play();
    }

    private final static int MENU = 0;
    private final static int GAMEPLAY = 1;
    private int currentScreen;

    public static boolean gameActive = false;
    @Override
    public void start(Stage stage) throws IOException {
        /* * Tạo canvas */
        gc = canvas.getGraphicsContext2D();
        //Background của game
        gc.setFill(Color.BLACK);
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
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Soul Bomber");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/icon.png"))));
        stage.show();

        currentScreen = MENU;


        AnimationTimer timer = new AnimationTimer() {

            // Control FPS
            public long  time_gap = 1_000_000_000/FPS;
            private long  lastUpdate = 0;

            @Override
            public void handle(long l) {
                /* * Control FPS */
                if( l - lastUpdate >= time_gap){
                    try {
                        update();
                        render();
                        lastUpdate = l;
                        currentFrame++;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        timer.start();

        game.importing("src/main/resources/maps/map.txt", "src/main/resources/maps/area.txt");

    }


    /** Updating */
    public void update() throws IOException {
        if(currentScreen == GAMEPLAY) {

            game.update();

            if (game.ending.getStatus() == Ending.QUIT || game.returnMenu) {
                currentScreen = MENU;
                menu_bg.play();
                game.reset();
            }
        }
        else {
            menu.update();

            if (menu.getPage() == Menu.PLAY) {
                startGame();
                game.ending.setStatus(Ending.NORMAL);
                menu.setPage(Menu.MAIN);
            }
        }
    }


    /** Render objects */
    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (currentScreen == GAMEPLAY) {
            game.render(gc, canvas.getWidth(), canvas.getHeight());
        } else {
            menu.render(gc);
        }
    }

    public void startGame() {
        currentScreen = GAMEPLAY;
        menu_bg.seek(Duration.ZERO);
        menu_bg.stop();
        game_bg.play();
    }


    public static void setBgVolume() {
        menu_bg.setVolume(ratio);
        game_bg.setVolume(ratio * 0.6);
    }

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }
}
