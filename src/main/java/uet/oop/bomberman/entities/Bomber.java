package uet.oop.bomberman.entities;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.Generals.Point;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.others.Physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uet.oop.bomberman.entities.Bomb.length_boom;
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

    Entity superSayan = new Floor(0, 0, Sprite.superSayan.getFxImage());
    MotionBlur effect = new MotionBlur();
    /**
     * speed projector, properties
     */
    private double SPEED = 2;
    private double speed_x;
    private double speed_y;
    // gia tốc
    double acceleration = 0.1;
    double brakeAcceleration = -2; // gia tốc phanh
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
    List<Bomb> bombs = new ArrayList<>();

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
        effect.setRadius(0);
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
        if (speed_x >= movingEffectSpeed || speed_y >= movingEffectSpeed) {
            movingEffect = true;
        }
        else if(effect.getRadius() != 0) effect.setRadius(0);
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
                if( bombs.size() < capacity) {
                    placeBomb(x, y, 0);
                }
//                System.out.println(bombs.size());
            }
        });
    }

    //inheritances
    @Override
    public void update() {
        effect.setRadius(Math.sqrt(Math.abs(speed_x*speed_x*dirX) + Math.abs(speed_y*speed_y*dirY)) * 2);
        double angle = Math.toDegrees(Math.atan(speed_x / speed_y)) - 90;
        effect.setAngle(angle);
    }

    /** updates */
    public void update(Gameplay gameplay) {
        //interacting
        interaction();

        //movement
        move(gameplay);

        //animations
        statusAnims[currentStatus].update();

        // Cài vị trí của Super Sayan.
        superSayan.setPosition(x - 30, y - 80);

        // attributes handling
        attribute_update(gameplay);

        //interior changes
        if(movingEffect) update();
   }

    /** Hàm render animation nên overload hàm render của Entity. */
    @Override
    public void render(GraphicsContext gc,Gameplay gameplay) {
        //render bomb
        bombs.forEach(g -> g.render(gc, gameplay));



        gc.setEffect(effect);
        // Hiện thị các effect của nhân vật
//        if (movingEffect) {
//            superSayan.render(gc, gameplay);
//        }
        // Hiển thị nhân vật
        gc.drawImage(this.getImg(), x - gameplay.translate_x, y - gameplay.translate_y);


        gc.setEffect(null);
    }


    @Override
    public Image getImg() {
        return statusAnims[currentStatus].getImage();
    }

    public void move(Gameplay gameplay) {
        if (moving) moveUpdate();

        double ref_x = Math.max(0,Math.min(width*Sprite.SCALED_SIZE - this.getWidth(),x  +  speed_x * dirX));
        double ref_y = Math.max(0,Math.min(height*Sprite.SCALED_SIZE - this.getHeight(),y  +  speed_y * dirY));

        if(!checkCollision(ref_x,ref_y,5)) {
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

    public void placeBomb(double ref_x, double ref_y, int margin) {
        // có đấy bạn ạ
        if(ref_x < 0 || ref_y < 0
                || ref_x > width * Sprite.SCALED_SIZE - this.getWidth()
                || ref_y > height * Sprite.SCALED_SIZE - this.getHeight()) return;

        Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new Rectangle(ref_x - this.getWidth() / 2 + margin, ref_y - this.getHeight() / 2 + margin, this.getWidth() - margin, this.getHeight() - margin);
        else
            rect = new Rectangle(ref_x, ref_y, this.getWidth(), this.getHeight());

        // Không cần check ra khỏi map vì trong update BOMBER hoặc ENEMY sẽ giới hạn speed.

        // Thay vì ngồi debug code Hưng fake thì tôi kiểm tra tất cả các tiles xung quanh thực thể luôn.
        int tileStartX = (int) Math.max(0, Math.floor(rect.getX() / Sprite.SCALED_SIZE) - 1);
        int tileStartY = (int) Math.max(0, Math.floor(rect.getY() / Sprite.SCALED_SIZE) - 1);
        int tileEndX = (int) Math.ceil((rect.getX() + rect.getWidth()) / Sprite.SCALED_SIZE);
        int tileEndY = (int) Math.ceil((rect.getY() + rect.getHeight()) / Sprite.SCALED_SIZE);
        tileEndX = Math.min(tileEndX, Gameplay.width - 1);
        tileEndY = Math.min(tileEndY, Gameplay.height - 1);
        for (int i = tileStartX; i <= tileEndX; i++) {
            for (int j = tileStartY; j <= tileEndY; j++) {

                int tileX = i * Sprite.SCALED_SIZE;
                int tileY = j * Sprite.SCALED_SIZE;

                Rectangle tileRect = new Rectangle(tileX, tileY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

                if (Gameplay.tile_map[j][i] == '0') {
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        bombs.add(new Bomb(i, j, timer));
                        return;
                    }
                }

            }
        }

        return;
    }
    @Override
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        // có đấy bạn ạ
        if(ref_x < 0 || ref_y < 0
                || ref_x > width * Sprite.SCALED_SIZE - this.getWidth()
                || ref_y > height * Sprite.SCALED_SIZE - this.getHeight()) return true;

        Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new Rectangle(ref_x - this.getWidth() / 2 + margin, ref_y - this.getHeight() / 2 + margin, this.getWidth() - margin, this.getHeight() - margin);
        else
            rect = new Rectangle(ref_x, ref_y, this.getWidth(), this.getHeight());

        // Không cần check ra khỏi map vì trong update BOMBER hoặc ENEMY sẽ giới hạn speed.

        // Thay vì ngồi debug code Hưng fake thì tôi kiểm tra tất cả các tiles xung quanh thực thể luôn.
        int tileStartX = (int) Math.max(0, Math.floor(rect.getX() / Sprite.SCALED_SIZE));
        int tileStartY = (int) Math.max(0, Math.floor(rect.getY() / Sprite.SCALED_SIZE));
        int tileEndX = (int) Math.ceil((rect.getX() + rect.getWidth()) / Sprite.SCALED_SIZE);
        int tileEndY = (int) Math.ceil((rect.getY() + rect.getHeight()) / Sprite.SCALED_SIZE);
        tileEndX = Math.min(tileEndX, Gameplay.width - 1);
        tileEndY = Math.min(tileEndY, Gameplay.height - 1);
        for (int i = tileStartX; i <= tileEndX; i++) {
            for (int j = tileStartY; j <= tileEndY; j++) {

                int tileX = i * Sprite.SCALED_SIZE;
                int tileY = j * Sprite.SCALED_SIZE;

                Rectangle tileRect = new Rectangle(tileX, tileY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

                if (Gameplay.tile_map[j][i] > '0'&&Gameplay.tile_map[j][i] < '3') {
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        return true;
                    }
                }
                //speed
                if (Gameplay.tile_map[j][i] ==  '3') {
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        killTask.add(new Point(i,j));
                        this.SPEED=6;
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                SPEED=2;
                            }
                        };
                        long delay = 10000L;
                        Timer timer = new Timer("Timer");
                        timer.schedule(timerTask, delay);

                        return true;
                    }
                }
                //buff boom
                if (Gameplay.tile_map[j][i] == '4') {
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        length_boom=5;
                        killTask.add(new Point(i,j));

                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                length_boom=2;
                            }
                        };
                        long delay = 10000L;
                        Timer timer = new Timer("Timer");
                        timer.schedule(timerTask, delay);
                        System.out.println("buff boom");
                        return true;
                    }
                }
                if (Gameplay.tile_map[j][i] == '5') {
                    if (Physics.collisionRectToRect(rect, tileRect)) {

                        killTask.add(new Point(i,j));
                        return true;
                    }
                }

            }
        }

        return false;
    }
}
