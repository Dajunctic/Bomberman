package uet.oop.bomberman.entities;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static uet.oop.bomberman.BombermanGame.scene;

public class Bomber extends Entity {
    /** Các trạng thái của nhân vật */
    public static final int IDLE = 0;
    public static final int DOWN = 1;
    public static final int UP = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int DEAD = 5;
    private int currentStatus = Bomber.IDLE;
    private Anim[] statusAnims;
    /** speed projector, properties*/
    private double speed_x = 2;
    private double speed_y = 2;
    final private double acc = 0.25;
    /** direction */
    private double dir_x = 0;
    private double dir_y = 0;

    /**
     * Đường dẫn đến folder của Model thôi không cần ảnh.
     * Có đường dẫn bởi vì nhỡ sau này phát triển nhiều nhân vật có thể lựa chọn để chơi.
     * */
    private final String path;


    public Bomber(double x, double y, String path) {
        super( x, y);
        this.path = path;
        load();
    }

    /**
     * Hàm load đã hoàn thiện chỉ có chỉnh sửa statusTime sao cho phù hợp.
     * Muốn hiểu status time thì đọc Anim.time.
     * */
    private void load() {
        String[] statusString = {"idle", "down", "up", "left", "right", "dead"};
        int[] statusNumberFrame = {4, 3, 3, 3, 3, 8};
        int[] statusTime = {60, 10, 10, 10, 10, 10};

        statusAnims = new Anim[6];

        for (int i = 0; i < statusAnims.length; i++) {
            String source = path + "/player_" + statusString[i] + ".png";
            statusAnims[i] = new Anim(new SpriteSheet(source, statusNumberFrame[i]), statusTime[i]);
            statusAnims[i].setScaleFactor(3);
        }
    }

    private void move() {
        //read input form key board
        //pressed
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                this.handleEvent(keyEvent);
            }

            private void handleEvent(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP: {
                        currentStatus=Bomber.UP;
                        dir_x = 0;
                        dir_y = -1;
                        break;
                    }
                    case DOWN: {
                        currentStatus=Bomber.DOWN;
                        dir_x = 0;
                        dir_y = 1;
                        break;
                    }
                    case LEFT: {
                        currentStatus=Bomber.LEFT;
                        dir_x = -1;
                        dir_y = 0;
                        break;
                    }
                    case RIGHT: {
                        currentStatus=Bomber.RIGHT;
                        dir_x = 1;
                        dir_y = 0;
                        break;
                    }
                }
                speed_x += Math.abs(dir_x) * acc;
                speed_y += Math.abs(dir_y) * acc;
            }
        });
        //released
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                currentStatus=Bomber.IDLE;
                dir_x = 0;
                dir_y = 0;
                speed_x = 2;
                speed_y = 2;
            }});

    }
    @Override
    public void update() {

        move();
        x += speed_x * dir_x;
        y += speed_y * dir_y;
        statusAnims[currentStatus].update();
    }

    /** Hàm render animation nên overload hàm render của Entity. */
    public void render(GraphicsContext gc) {
        gc.drawImage(statusAnims[currentStatus].getFxImage(), x, y);
    }
}
