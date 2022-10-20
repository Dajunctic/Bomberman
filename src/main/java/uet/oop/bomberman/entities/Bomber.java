package uet.oop.bomberman.entities;

import java.util.*;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.others.Physics;

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

    MotionBlur effect = new MotionBlur();
    /**
     * speed projector, properties
     */
    private double SPEED = 2;
    private double speed_x;
    private double speed_y;
    // gia tốc
    double acceleration = 0.15;
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

    //toggle attacking in future
    private Vertex facing = new Vertex(0,0);
    /**
     * power properties
     */
    public int capacity = 1;
    public int power = 2;
    public double timer = 2.5;
    List<Bomb> bombs = new ArrayList<>();

    /**
     * Đường dẫn đến folder của Model thôi không cần ảnh.
     * Có đường dẫn bởi vì nhỡ sau này phát triển nhiều nhân vật có thể lựa chọn để chơi.
     */
    private final String path;

    /** abilities */
    // go icognito
    private boolean invisible = false;
    private double alpha = 1;
    private double fadeInSpeed = 0.05;
    private int icognito = 5;
    //Constructor
    public Bomber(double x, double y, String path) {
        super(x, y);
        this.path = path;

        speed_x = SPEED;
        speed_y = SPEED;
        effect.setRadius(0);
        load();
    }
    /** special skills */
    //fireround
    private int fireCapacity = 99;
    private final double cooldown = 200;
    private long lastAttack = 0;
    //TNT
    private int TNTCapacity = 99;
    private List<Nuke> nuke = new ArrayList<>();
    //dodges
    private int dodges = 0;
    private int dodgeDistance = 2 * Sprite.SCALED_SIZE;


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
            //determine facing direction
            findFace();
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

                    /** Interaction hotkeys.*/
                    case Q -> placeBomb();
                    case W -> shootFireball();
                    case E -> goInvisible(5);
                    case R -> placeNuke();
                    case D -> dodge();
                }
            }});

        //toggle bomb
        scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case SPACE -> placeBomb();
                    case Q -> shootFireball();
                    case W -> goInvisible(5);
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

        // attributes handling
        attribute_update(gameplay);

        //interior changes
        if(movingEffect) update();

        //handling vulnerabilities
        if(invisible) alpha += fadeInSpeed;
        if(alpha >= 1) invisible = false;

   }

    /** Hàm render animation nên overload hàm render của Entity. */
    @Override
    public void render(GraphicsContext gc,Gameplay gameplay) {
        // Render bombs.
        bombs.forEach(g -> g.render(gc, gameplay));
        gc.setEffect(effect);
        if(invisible)  gc.setGlobalAlpha(alpha);
        //apply invisiblity
        gc.setGlobalAlpha(alpha);
        // Hiển thị nhân vật
        gc.drawImage(this.getImg(), x - gameplay.translate_x + gameplay.offsetX
                , y - gameplay.translate_y + gameplay.offsetY);

        gc.setGlobalAlpha(1);
        gc.setEffect(null);

        if(!nuke.isEmpty()) nuke.forEach(g -> g.render(gc, gameplay));
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
        if(!nuke.isEmpty()) {
            nuke.forEach(g -> {
                g.update();
                if(g.nuke.isDead()) g.deadAct(gameplay);
                if(!g.isExisted()) nuke.remove(g);
            });
        }
    }

    //find facing direction
    public void findFace() {
        if(speed_x*dirX >= 0) {
            if(speed_y*dirY >= 0) {
                if (speed_x >= speed_y) facing.set(1,0);
                    else facing.set(0,1);
            }
            else {
                if (speed_x >= speed_y) facing.set(1,0);
                else facing.set(0,-1);
            }
        }
        else {
            if(speed_y*dirY >= 0) {
                if (speed_x >= speed_y) facing.set(-1,0);
                else facing.set(0,1);
            }
            else {
                if (speed_x >= speed_y) facing.set(-1, 0);
                else facing.set(0, -1);
            }
        }
    }
    public void placeBomb() {

        int i = (int) Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE));
        int j = (int) Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE));

        bombs.add(new Bomb(i, j, timer));

    }

    /** Override RECT COLLISION cho Nhân vật */
    @Override
    public Rectangle getRect(double x, double y, double w, double h) {
        return super.getRect(x + w / 6, y + h / 3, w * 2 / 3, h * 2 / 3);
    }

    @Override
    public double getWidth() {
        return statusAnims[IDLE].getImage().getWidth();
    }

    @Override
    public double getHeight() {
        return statusAnims[IDLE].getImage().getHeight();
    }

    /**************************** BUFF AND BUGS ********************************/
    //invisible
    public void goInvisible(double time) {
        if(icognito <= 0) return ;
        fadeInSpeed = 0.8 / (time * (double) FPS );
        alpha = 0.2;
        invisible = true;
    }

    public boolean vulnerable() {
        return !invisible;
    }
    //bomb upgrade
    public void radiusUpgrade() {
        power ++;
    }
    public void capacityUpgrade() {
        capacity ++;
    }

    //gaining items
    public void addFireball () {
        fireCapacity ++;
    }

    public void addTNT() {
        TNTCapacity ++;
    }

    //unique skills
    //shoot 3 parallel fire
    public void shootFireball() {
        if(System.currentTimeMillis() - lastAttack <= cooldown) return ;

        if(invisible) {

            entities.add(new Flame(this.x, this.y, 1, facing.getX(), facing.getY()));

        }
        else if(fireCapacity > 0) {
            fireCapacity --;
            double startX =  Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE) + 0.5) * Sprite.SCALED_SIZE;
            double startY =  Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE) + 0.5) * Sprite.SCALED_SIZE;
            entities.add(new Flame(startX, startY, HEIGHT * Sprite.SCALED_SIZE, facing.getX()
                                                                                    , facing.getY()
                                                                                    , 1, 0.5));
            entities.add(new Flame(startX, startY, HEIGHT * Sprite.SCALED_SIZE,  (double) 5 / HEIGHT * facing.getY() + facing.getX()
                                                                                    ,  (double) 5 / HEIGHT * facing.getX() + facing.getY()
                                                                                    , 1, 0.5));
            entities.add(new Flame(startX, startY, HEIGHT * Sprite.SCALED_SIZE, -(double) 5 / HEIGHT * facing.getY() + facing.getX()
                                                                                    , -(double) 5 / HEIGHT * facing.getX() + facing.getY()
                                                                                    , 1, 0.5));
        }

        lastAttack = System.currentTimeMillis();
    }

    //Nuke placing, need to install impact on tile_map
    public void placeNuke() {
        if(TNTCapacity > 0) {
        int i = (int) Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE));
        int j = (int) Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE));
        nuke.add(new Nuke(i, j, timer));
        }
    }
    //jump 2 tiles, need to reinstall checkCollision for out of Map bug
    public void dodge() {
        effect.setRadius(10);
        double refx = this.x + facing.getX() * dodgeDistance;
        double refy = this.y + facing.getY() * dodgeDistance;
        if(!checkCollision(refx, refy, 5)) {
            placeBomb();
            this.x = refx;
            this.y = refy;
        }
    }
}
