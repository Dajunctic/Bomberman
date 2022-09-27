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

    final private double SPEED = 4;
    private double speed_x;
    private double speed_y;
    final private double acceleration = 0.2; // gia tốc
    /** Direction */
    private double dirX = 0;
    private double dirY = 0;
    private int currentIdleDirection;

    Set<Integer> moveSet = new HashSet<Integer>();
    boolean moving = false;

    /** power properties */
    public int capacity = 15;
    public int power = 2;
    public double timer = 2.5;
    private List<Bomb> bomb = new ArrayList<>() ;

    /**
     * Đường dẫn đến folder của Model thôi không cần ảnh.
     * Có đường dẫn bởi vì nhỡ sau này phát triển nhiều nhân vật có thể lựa chọn để chơi.
     * */
    private final String path;


    public Bomber(double x, double y, String path) {
        super( x, y);
        this.path = path;

        speed_x = SPEED;
        speed_y = SPEED;
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

    private void setDir(int status) {
        switch (status) {
            case Bomber.UP -> {
                dirX = 0;
                dirY = -1;
            }
            case Bomber.DOWN -> {
                dirY = 1;
                dirX = 0;
            }
            case Bomber.LEFT -> {
                dirX = -1;
                dirY = 0;
            }
            case Bomber.RIGHT -> {
                dirX = 1;
                dirY = 0;
            }
            case Bomber.IDLE -> {
                dirX = 0;
                dirY = 0;
                speed_x = SPEED;
                speed_y = SPEED;
            }

        }
    }

    public void moveUpdate() {
        speed_x += Math.abs(dirX) * acceleration;
        speed_y += Math.abs(dirY) * acceleration;

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
                    }
                    case DOWN -> {
                        currentStatus = Bomber.DOWN;
                        moving = true;
                    }
                    case LEFT -> {
                        currentStatus = Bomber.LEFT;
                        moving = true;
                    }
                    case RIGHT -> {
                        currentStatus = Bomber.RIGHT;
                        moving = true;
                    }
                }

                moveSet.add(currentStatus);
                setDir(currentStatus);
            }
        });
        // released
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case UP -> {
                        moveSet.remove(Bomber.UP);
                    }
                    case DOWN -> {
                        moveSet.remove(Bomber.DOWN);
                    }
                    case LEFT -> {
                        moveSet.remove(Bomber.LEFT);
                    }
                    case RIGHT -> {
                        moveSet.remove(Bomber.RIGHT);
                    }
                }

                // Chỉnh chuyển động là cái Phím Key Down gần nhất với cái phím vừa Key Up hiện tại
                for (Integer x: moveSet) {
                    setDir(x);
                    currentStatus = x;
                }

                movingEffect = false;

                if (moveSet.isEmpty()) {
                    currentStatus = Bomber.IDLE;
                    dirX = 0;
                    dirY = 0;
                    moving = false;
                    speed_x = SPEED;
                    speed_y = SPEED;
                }
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
        movingLeftEffect.setPosition(x - 100, y + 10);
        movingRightEffect.setPosition(x + 18, y + 10);

        // attributes handling
        attribute_update(gameplay);

        //interior changes
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

//        // Hiện thị border nhân vật
//        Rectangle rect = new Rectangle(x, y, this.getHeight(), this.getHeight());
//        Basic.drawRectangle(gc, rect);

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
        for(int i = 0; i < bomb.size(); i++) {
            bomb.get(i).update();
            if(bomb.get(i).bomb.isDead())
                bomb.get(i).deadAct(gameplay);
            if(!bomb.get(i).isExisted()){
                bomb.remove(i);
                i--;
            }
        }
    }
}
