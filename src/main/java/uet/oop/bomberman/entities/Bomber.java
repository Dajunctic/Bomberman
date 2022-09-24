package uet.oop.bomberman.entities;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import javafx.scene.input.KeyEvent;

import static uet.oop.bomberman.game.BombermanGame.scene;
import static uet.oop.bomberman.game.Gameplay.tile_map;

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

    /** Các effect Animation của nhân vật */
    boolean movingEffect = false;
    double movingEffectSpeed = 8;

    Entity movingLeftEffect = new Floor(0, 0, Sprite.movingLeft.getFxImage());
    Entity movingRightEffect = new Floor(0, 0, Sprite.movingRight.getFxImage());

    /** speed projector, properties*/

    final private double SPEED_X = 4;
    final private double SPEED_Y = 4;
    private double speed_x;
    private double speed_y;
    final private double acceleration = 0.2; // gia tốc
    /** Direction */
    private double dir_x = 0;
    private double dir_y = 0;
    private int currentIdleDirection;

    /**
     * Đường dẫn đến folder của Model thôi không cần ảnh.
     * Có đường dẫn bởi vì nhỡ sau này phát triển nhiều nhân vật có thể lựa chọn để chơi.
     * */
    private final String path;


    public Bomber(double x, double y, String path) {
        super( x, y);
        this.path = path;

        speed_x = SPEED_X;
        speed_y = SPEED_Y;
        load();
    }

    /**
     * Hàm load đã hoàn thiện chỉ có chỉnh sửa statusTime sao cho phù hợp.
     * Muốn hiểu status time thì đọc Anim.time.
     * */
    private void load() {
        String[] statusString = {"idle", "down", "up", "left", "right", "dead"};
        int[] statusNumberFrame = {2, 3, 3, 3, 3, 8};
        int[] statusTime = {0, 6, 6, 8, 8, 10};

        statusAnims = new Anim[6];

        for (int i = 0; i < statusAnims.length; i++) {
            String source = path + "/player_" + statusString[i] + ".png";
            statusAnims[i] = new Anim(new SpriteSheet(source, statusNumberFrame[i]), statusTime[i]);
            statusAnims[i].setScaleFactor(3);

            if (Bomber.DOWN <= i && i <= Bomber.RIGHT) {
                statusAnims[i].setStartLoopFrame(0);
            }
        }

        currentIdleDirection = Bomber.RIGHT;
        statusAnims[Bomber.IDLE].staticUpdate();
    }

    private void move() {
        // read input form keyboard
        // pressed
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                this.handleEvent(keyEvent);
            }

            private void handleEvent(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case UP -> {
                        currentStatus = Bomber.UP;
                        dir_x = 0;
                        dir_y = -1;
                    }
                    case DOWN -> {
                        currentStatus = Bomber.DOWN;
                        dir_x = 0;
                        dir_y = 1;
                    }
                    case LEFT -> {
                        currentStatus = Bomber.LEFT;
                        dir_x = -1;
                        dir_y = 0;
                    }
                    case RIGHT -> {
                        currentStatus = Bomber.RIGHT;
                        dir_x = 1;
                        dir_y = 0;
                    }
                }
                speed_x += Math.abs(dir_x) * acceleration;
                speed_y += Math.abs(dir_y) * acceleration;

                // Đổi hướng trạng thái IDLE từ bước di chuyển trước
                if (currentStatus >= Bomber.LEFT && currentStatus != currentIdleDirection) {
                    statusAnims[Bomber.IDLE].staticUpdate();
                    currentIdleDirection = currentStatus;
                }

                // Điều kiện để có moving Effect
                if (speed_x >= movingEffectSpeed) {
                    movingEffect = true;
                }
            }
        });
        // released
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                currentStatus = Bomber.IDLE;
                movingEffect = false;
                dir_x = 0;
                dir_y = 0;
                speed_x = SPEED_X;
                speed_y = SPEED_Y;
            }});

    }
    @Override
    public void update() {
        move();
        double ref_x = x + speed_x * dir_x;
        double ref_y = y + speed_y * dir_y;

        // collision handling
        if(tile_map[Math.max(0,Math.min(Gameplay.height - 1,(int) Math.floor((ref_y+(double)20*48/17)/Sprite.SCALED_SIZE)))][Math.max(0, Math.min(Gameplay.width -1,(int) Math.floor((ref_x + 24)/Sprite.SCALED_SIZE ))) ] == '0') {
            x = ref_x;
            y = ref_y;
        }
        statusAnims[currentStatus].update();


        movingLeftEffect.setPosition(x - 124, y + 18);
        movingRightEffect.setPosition(x + 18, y + 18);
    }

    /** Hàm render animation nên overload hàm render của Entity. */
    @Override
    public void render(GraphicsContext gc) {
        // Hiện thị các effect của nhân vật
        if (movingEffect) {
            if (currentIdleDirection == Bomber.RIGHT) {
                movingLeftEffect.render(gc);
            } else {
                movingRightEffect.render(gc);
            }
        }

        // Hiển thị nhân vật
        gc.drawImage(this.getImg(), x, y);
    }

    @Override
    public Image getImg() {
        return statusAnims[currentStatus].getFxImage();
    }

}
