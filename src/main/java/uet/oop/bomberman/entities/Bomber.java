package uet.oop.bomberman.entities;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.Basic;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import static uet.oop.bomberman.game.BombermanGame.*;
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

    /** power properties */
    public int capacity = 15;
    public int power = 2;
    public double timer = 2.5;
    private List<Bomb> bomb = new ArrayList<>() ;
    private List<Flame> flame = new ArrayList<>();

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
            statusAnims[i].setScaleFactor(2);

            if (Bomber.DOWN <= i && i <= Bomber.RIGHT) {
                statusAnims[i].setStartLoopFrame(0);
            }
        }

        currentIdleDirection = Bomber.RIGHT;
        statusAnims[Bomber.IDLE].staticUpdate();
    }

    private void interaction() {
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

        //toggle bomb
        scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if( bomb.size() < capacity)
                    bomb.add(new Bomb(getCenterX(), getCenterY(), timer));
                System.out.println(bomb.size());
            }
        });
    }

    //handle attributes
    @Override
    public void update() {

    }

    public void update(Gameplay gameplay) {
        interaction();

        // Không để nhân vật chạy ra khỏi map
        if (dir_x == -1) speed_x = Math.min(speed_x, x);
        if (dir_x == 1) speed_x = Math.min(speed_x, Gameplay.width * Sprite.SCALED_SIZE  - x - this.getWidth());
        if (dir_y == -1) speed_y = Math.min(speed_y, y);
        if (dir_y == 1) speed_y = Math.min(speed_y, Gameplay.height * Sprite.SCALED_SIZE  - y - this.getHeight());

        double ref_x = x + speed_x * dir_x;
        double ref_y = y + speed_y * dir_y;

        // Xử lí va chạm
        if(!checkCollision(ref_x,ref_y)) {
            x = ref_x;
            y = ref_y;
            gameplay.translate_x = Math.max(0, Math.min( x - (double) WIDTH * Sprite.SCALED_SIZE / 2,
                                                    (Gameplay.width - WIDTH) * Sprite.SCALED_SIZE ));

            gameplay.translate_y = Math.max(0, Math.min( y - (double) HEIGHT * Sprite.SCALED_SIZE / 2,
                                                    (Gameplay.height - HEIGHT) * Sprite.SCALED_SIZE ));
        }


        statusAnims[currentStatus].update();

        // Cài vị trí của Moving Effect.
        movingLeftEffect.setPosition(x - 100, y + 10);
        movingRightEffect.setPosition(x + 18, y + 10);

        //bomb updates
        for(int i = 0; i < bomb.size(); i++) {
            bomb.get(i).update();
            if(bomb.get(i).bomb.isDead())
                bomb.get(i).deadAct(gameplay);
            if(!bomb.get(i).isExisted()){
                bomb.remove(i);
            }
        }

        // attributes handling
        update();
    }

    /** Hàm render animation nên overload hàm render của Entity. */
    @Override
    public void render(GraphicsContext gc,Gameplay gameplay) {
        //render bomb
        bomb.forEach(g -> g.render(gc, gameplay));

        // Hiện thị các effect của nhân vật
        if (movingEffect) {
            if (currentIdleDirection == Bomber.RIGHT) {
                movingLeftEffect.render(gc, gameplay);
            } else {
                movingRightEffect.render(gc, gameplay);
            }
        }

        // Hiện thị border nhân vật
        Rectangle rect = new Rectangle(x, y, this.getHeight(), this.getHeight());
        Basic.drawRectangle(gc, rect);

        // Hiển thị nhân vật
        gc.drawImage(this.getImg(), x - gameplay.translate_x, y - gameplay.translate_y);
    }



    @Override
    public Image getImg() {
        return statusAnims[currentStatus].getFxImage();
    }

    public void fire(Flame obj) {
        flame.add(obj);
    }

}
