package uet.oop.bomberman.entities;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uet.oop.bomberman.game.BombermanGame.*;
import static uet.oop.bomberman.game.Gameplay.*;

public class Bomber extends Mobile {
    /**
     * Các trạng thái của nhân vật
     */
    public static final int IDLE = 0;
    public static final int DOWN = 1;
    public static final int UP = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int DEAD = 5;
    private int currentStatus = Bomber.IDLE;
    private Anim[] statusAnims;

    /**
     * Các effect Animation của nhân vật
     */
    boolean movingEffect = false;
    double movingEffectSpeed = 8;

    Entity movingLeftEffect = new Floor(0, 0, Sprite.movingLeft.getFxImage());
    Entity movingRightEffect = new Floor(0, 0, Sprite.movingRight.getFxImage());

    /**
     * speed projector, properties
     */

    final private double SPEED = 3;
    private double speed_x;
    private double speed_y;
    final private double acceleration = 0.15; // gia tốc
    final private double brakeAcceleration = -2; // gia tốc phanh
    /**
     * Direction
     */
    private double dirX = 0;
    private double dirY = 0;
    private int currentIdleDirection;

    Set<Integer> moveSet = new HashSet<Integer>();
    private boolean moving = false;
    private int firstStatus = 0;
    private int secondStatus = 0;

    /**
     * power properties
     */
    public int capacity = 15;
    public int power = 2;
    public double timer = 2.5;
    private List<Bomb> bombs = new ArrayList<>();
    private List<Flame> flames = new ArrayList<>();

    /**
     * Đường dẫn đến folder của Model thôi không cần ảnh.
     * Có đường dẫn bởi vì nhỡ sau này phát triển nhiều nhân vật có thể lựa chọn để chơi.
     */
    private final String path;


    public Bomber(double x, double y, String path) {
        super(x, y);
        this.path = path;

        speed_x = SPEED;
        speed_y = SPEED;
        load();
    }

    /**
     * Hàm load đã hoàn thiện chỉ có chỉnh sửa statusTime sao cho phù hợp.
     * Muốn hiểu status time thì đọc Anim.time.
     */
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

    private void setDir(int status, boolean single) {
        switch (status) {

            case Bomber.UP -> {
                dirY = -1;

                if (single) dirX = 0;
            }
            case Bomber.DOWN -> {
                dirY = 1;

                if (single) dirX= 0;
            }
            case Bomber.LEFT -> {
                dirX = -1;

                if (single) dirY = 0;
            }
            case Bomber.RIGHT -> {
                dirX = 1;

                if (single) dirY = 0;
            }
        }

    }

    private void resetSpeed() {
        speed_x = SPEED;
        speed_y = SPEED;
    }

    public void moveUpdate() {
        boolean brake = false;

        if (moveSet.size() <= 1) {
            for (int x: moveSet) {
                currentStatus = x;
                setDir(currentStatus, true);
            }
        } else {
            int firstStatus, secondStatus;
            ArrayList<Integer> statusKeys = new ArrayList<Integer>(moveSet);

            int fId = statusKeys.size() - 1;
            int sId = statusKeys.size() - 2;

            firstStatus = statusKeys.get(fId);
            secondStatus = statusKeys.get(sId);

            if ((firstStatus >= Bomber.LEFT && secondStatus >= Bomber.LEFT)
                    || (firstStatus < Bomber.LEFT && secondStatus < Bomber.LEFT)) {
                brake = true;
            } else {
                if (this.firstStatus + this.secondStatus != firstStatus + secondStatus
                        || Math.abs(this.firstStatus - this.secondStatus) != Math.abs(firstStatus - secondStatus)) {
                    resetSpeed();
                }

                currentStatus = firstStatus;
                this.firstStatus = firstStatus;
                this.secondStatus = secondStatus;
                setDir(firstStatus, false);
                setDir(secondStatus, false);
            }
        }


        if (brake) {
            speed_x += Math.abs(dirX) * brakeAcceleration;
            speed_y += Math.abs(dirY) * brakeAcceleration;
        } else {
            speed_x += Math.abs(dirX) * acceleration;
            speed_y += Math.abs(dirY) * acceleration;
        }

        if (speed_x < 0  || speed_y < 0) {
            speed_x = 0;
            speed_y = 0;
            currentStatus = Bomber.IDLE;
        }


        if (moveSet.isEmpty()) {
            movingEffect = false;
            currentStatus = Bomber.IDLE;
            moving = false;
            dirX = 0;
            dirY = 0;
            resetSpeed();
        }

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

    /** input reader */
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
                        moving = true;
                        moveSet.add(currentStatus);
                    }
                    case DOWN -> {
                        currentStatus = Bomber.DOWN;
                        moving = true;
                        moveSet.add(currentStatus);
                    }
                    case LEFT -> {
                        currentStatus = Bomber.LEFT;
                        moving = true;
                        moveSet.add(currentStatus);
                    }
                    case RIGHT -> {
                        currentStatus = Bomber.RIGHT;
                        moving = true;
                        moveSet.add(currentStatus);
                    }
                }
            }
        });
        // released
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case UP -> {
                        moveSet.remove(Bomber.UP);
                        resetSpeed();
                    }
                    case DOWN -> {
                        moveSet.remove(Bomber.DOWN);
                        resetSpeed();
                    }
                    case LEFT -> {
                        moveSet.remove(Bomber.LEFT);
                        resetSpeed();
                    }
                    case RIGHT -> {
                        moveSet.remove(Bomber.RIGHT);
                        resetSpeed();
                    }
                }
            }});

        //toggle bomb
        scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if( bombs.size() < capacity)
                    bombs.add(new Bomb(getCenterX(), getCenterY(), timer));
                System.out.println(bombs.size());
            }
        });
    }

    //inheritances
    @Override
    public void update() {

    }

    /** updates */
    public void update(Gameplay gameplay) {
        //interacting
        interaction();

        //movement
        move(gameplay);

        //animations
        statusAnims[currentStatus].update();
        // Cài vị trí của Moving Effect.
//        movingLeftEffect.setPosition(x - 100, y + 10);
//        movingRightEffect.setPosition(x + 18, y + 10);

        // attributes handling
        attribute_update(gameplay);

        //interior changes
        update();
    }

    /** Hàm render animation nên overload hàm render của Entity. */
    @Override
    public void render(GraphicsContext gc,Gameplay gameplay) {
        //render bomb
        bombs.forEach(g -> g.render(gc, gameplay));

        // Hiện thị các effect của nhân vật
//        if (movingEffect) {
//            if (currentIdleDirection == Bomber.RIGHT) {
//                movingLeftEffect.render(gc, gameplay);
//            } else {
//                movingRightEffect.render(gc, gameplay);
//            }
//        }

        // Hiển thị nhân vật
        gc.drawImage(this.getImg(), x - gameplay.translate_x, y - gameplay.translate_y);
    }


    @Override
    public Image getImg() {
        return statusAnims[currentStatus].getImage();
    }

    public void move(Gameplay gameplay) {
        if (moving) moveUpdate();

        double ref_x = Math.max(0,Math.min(width*Sprite.SCALED_SIZE - this.getWidth(),x  +  speed_x * dirX));
        double ref_y = Math.max(0,Math.min(height*Sprite.SCALED_SIZE - this.getHeight(),y  +  speed_y * dirY));

        if(!checkCollision(ref_x,ref_y,0)) {
            x = ref_x;
            y = ref_y;
            gameplay.translate_x = Math.max(0, Math.min( x - (double) WIDTH * Sprite.SCALED_SIZE / 2,
                    (Gameplay.width - WIDTH) * Sprite.SCALED_SIZE ));

            gameplay.translate_y = Math.max(0, Math.min( y - (double) HEIGHT * Sprite.SCALED_SIZE / 2,
                    (Gameplay.height - HEIGHT) * Sprite.SCALED_SIZE ));
        }
    }

    //attributes handler
    public void attribute_update(Gameplay gameplay) {
        //bomb updates
        for(int i = 0; i < bombs.size(); i++) {
            bombs.get(i).update();
            if(bombs.get(i).bomb.isDead())
                bombs.get(i).deadAct(gameplay);
            if(!bombs.get(i).isExisted()){
                bombs.remove(i);
                i--;
            }
        }
    }
}
