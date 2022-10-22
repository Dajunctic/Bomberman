package uet.oop.bomberman.entities;

import java.util.*;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import javafx.scene.input.KeyEvent;

import static uet.oop.bomberman.game.BombermanGame.*;
import static uet.oop.bomberman.game.Gameplay.*;

public class Bomber extends Mobile {
    /** Thời gian dùng chiêu **/
    public static final int Q_COOLDOWN = 5;
    public static final int W_COOLDOWN = 10;
    public static final int E_COOLDOWN = 15;
    public static final int R_COOLDOWN = 120;

    public static final int D_COOLDOWN = 20;
    public static final int F_COOLDOWN = 180;

    private long lastQ = 0;
    private long lastW = 0;
    private long lastE = 0;
    private long lastR = 0;
    private long lastD = 0;
    private long lastF = 0;


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

    MotionBlur motionEffect = new MotionBlur();
    ColorAdjust dodgeEffect = new ColorAdjust();

    /**
     * speed projector, properties
     */
    private final double SPEED = 2;
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
    // go incognito
    private boolean invisible = false;
    private double alpha = 1;
    private double fadeInSpeed = 0.05;
    private int icognito = 5;

    /** special skills */
    //fire round
    private int fireCapacity = 99;
    private final double cooldown = 200;
    private long lastAttack = 0;
    //TNT
    private int TNTCapacity = 99;
    Nuke nuke = null;
    //dodges
    private int dodges = 0;
    private int dodgeDistance = 2 * Sprite.SCALED_SIZE;
    private boolean dodging = false;
    private double lightShift = 0;
    private final double shiftSpeed = 0.1;
    private int lighten = 1;
    private double oldX = 0;
    private double oldY = 0;

    private DeadAnim dodgeAnim;

    //Constructor
    public Bomber(double x, double y, String path) {
        super(x, y);
        this.path = path;
        speed_x = SPEED;
        speed_y = SPEED;
        motionEffect.setRadius(0);
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

        //init buff animation
        dodgeAnim = new DeadAnim(new SpriteSheet("/sprites/Player/Abilities/dodge.png", 9), 5, 1);
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
        else if(motionEffect.getRadius() != 0) motionEffect.setRadius(0);
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
        /* * released */
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

                    case Q -> placeBomb();
                    case W -> shootFireball();
                    case E -> goInvisible(5);
                    case R -> placeNuke();
                    case D -> setDodge();
                    case F -> recover();
                }
            }});

        /* * Toggle bomb */
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

        //update splash

    }

    /** Handling special */
    private void visualUpdate() {
        if(dodging) {
            if(!(effect instanceof ColorAdjust)) effect = dodgeEffect;
            dodgeEffect.setBrightness(lightShift);
            if(lightShift >= 1) {
                dodge();
                lighten = -lighten;
            }
            if (lightShift < 0) {
                lighten = 1;
                lightShift = 0.5;
                dodging = false;
            }
            lightShift += lighten * shiftSpeed;
        } else if (movingEffect) {
            if(!(effect instanceof MotionBlur)) effect = motionEffect;
            motionEffect.setRadius(Math.sqrt(Math.abs(speed_x*speed_x*dirX) + Math.abs(speed_y*speed_y*dirY)) * 2);
            double angle = Math.toDegrees(Math.atan(speed_x / speed_y)) - 90;
            motionEffect.setAngle(angle);
        }

    }

    /** Animation updating */
    private void animationUpdate() {
        /* * Moving */
        statusAnims[currentStatus].update();
        /* * Buffs */
        dodgeAnim.update();
    }
    /** Updates */
    public void update(Gameplay gameplay) {
        super.update();

        //interacting
        interaction();

        //movement
        move(gameplay);
        //animations
        animationUpdate();

        // attributes handling
        attribute_update(gameplay);

        //interior changes
        visualUpdate();

        //handling vulnerabilities
        if(invisible) alpha += fadeInSpeed;
        if(alpha >= 1) invisible = false;
   }

    /** Hàm render animation nên overload hàm render của Entity. */
    @Override
    public void render(GraphicsContext gc,Gameplay gameplay) {
        /* * Render bombs */
        bombs.forEach(g -> g.render(gc, gameplay));
        gc.setEffect(effect);
        if(invisible)  gc.setGlobalAlpha(alpha);
        /* * Apply invisibility */

        /* * Hiển thị nhân vật */
        gc.drawImage(this.getImg(), x - gameplay.translate_x + gameplay.offsetX
                , y - gameplay.translate_y + gameplay.offsetY);

        gc.setGlobalAlpha(1);
        gc.setEffect(null);

        if(nuke != null) nuke.render(gc, gameplay);
        if(!dodgeAnim.isDead()) gc.drawImage(dodgeAnim.getImage(), oldX - gameplay.translate_x + gameplay.offsetX
                , oldY - gameplay.translate_y + gameplay.offsetY);

        /* * Hiển thị máu và mana */
        renderHP(gc, gameplay);
        renderMana(gc, gameplay);
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

    /** Attributes handler */
    public void attribute_update(Gameplay gameplay) {
        /* * Bomb updates */
        for(int i = 0; i < bombs.size(); i++) {
            bombs.get(i).update();
            if(bombs.get(i).bomb.isDead())
                bombs.get(i).deadAct(gameplay);
            if(!bombs.get(i).isExisted()){
                bombs.remove(i);
                i--;
            }
        }
        if(nuke != null) {
            nuke.update();
            if(nuke.nuke.isDead()) nuke.deadAct(gameplay);
            if(!nuke.isExisted()) nuke = null;
        }
    }

    /** Find facing direction */
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
        /* * Điều kiện để có thể đặt được bomb * */
        if (currentMana < Q_MANA_CONSUMING) return;
        if (System.currentTimeMillis() - lastQ < Q_COOLDOWN * 1000) return;

        int i = (int) Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE));
        int j = (int) Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE));

        bombs.add(new Bomb(i, j, timer, true));
        subtractMana(Q_MANA_CONSUMING);
        lastQ = System.currentTimeMillis();

    }

    public void setDodge() {
        if (System.currentTimeMillis() - lastD < F_COOLDOWN * 1000) return;
        dodging = true;
        lastD = System.currentTimeMillis();
    }

    public void recover() {
        if (System.currentTimeMillis() - lastF < F_COOLDOWN * 1000) return;

        addHP(HP_RECOVER);

        lastF = System.currentTimeMillis();
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
        if (icognito <= 0) return;
        if (currentMana < E_MANA_CONSUMING) return;
        if (System.currentTimeMillis() - lastE < E_COOLDOWN * 1000);

        subtractMana(E_MANA_CONSUMING);

        fadeInSpeed = 0.8 / (time * (double) FPS );
        alpha = 0.2;
        invisible = true;

        lastE = System.currentTimeMillis();
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


    /**************************** UNIQUE SKILL ********************************/
    /* * Shoot 3 parallel fire */
    public void shootFireball() {
        /* * Điều kiện để bắn */
        if (System.currentTimeMillis() - lastW <= cooldown) return;
        if (currentMana < W_MANA_CONSUMING) return;


        if(invisible) {
            entities.add(new Flame(this.x, this.y, 1, facing.getX(), facing.getY(), true));
        } else if(fireCapacity > 0) {
            fireCapacity --;
            double startX =  Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE) + 0.5) * Sprite.SCALED_SIZE;
            double startY =  Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE) + 0.5) * Sprite.SCALED_SIZE;
            /* * Spawn flames */
            entities.add(new Flame(startX, startY, HEIGHT * Sprite.SCALED_SIZE, facing.getX()
                                                                                    , facing.getY()
                                                                                    , 1, 0.5, true));
            entities.add(new Flame(startX, startY, HEIGHT * Sprite.SCALED_SIZE,  (double) 5 / HEIGHT * facing.getY() + facing.getX()
                                                                                    ,  (double) 5 / HEIGHT * facing.getX() + facing.getY()
                                                                                    , 1, 0.5, true));
            entities.add(new Flame(startX, startY, HEIGHT * Sprite.SCALED_SIZE, -(double) 5 / HEIGHT * facing.getY() + facing.getX()
                                                                                    , -(double) 5 / HEIGHT * facing.getX() + facing.getY()
                                                                                    , 1, 0.5, true));
        }
        subtractMana(W_MANA_CONSUMING);
        lastW = System.currentTimeMillis();
    }

    /** Nuke placing, need to install impact on tile_map */
    public void placeNuke() {
        if (currentMana < R_MANA_CONSUMING) return;
        if (System.currentTimeMillis() - lastR < R_COOLDOWN * 1000) return;

        if(TNTCapacity > 0) {
            int i = (int) Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE));
            int j = (int) Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE));
            nuke = new Nuke(i, j, timer);

            subtractMana(R_MANA_CONSUMING);
            lastR = System.currentTimeMillis();
        }
    }
    
    /** Jump 2 tiles, need to reinstall checkCollision for out of Map bug */
    public void dodge() {
        dodgeAnim.reset();
        oldX = this.x;
        oldY = this.y;
        double refX = this.x + facing.getX() * dodgeDistance;
        double refY = this.y + facing.getY() * dodgeDistance;
        if(!checkCollision(refX, refY, 5)) {
            placeBomb();

            /* * Kiểm tra xem dodge có bị ra ngoài area map không */
            int pX = (int) (refX + getWidth() / 2) / Sprite.SCALED_SIZE;
            int pY = (int) (refY + getHeight() / 2) / Sprite.SCALED_SIZE;

            if (areaMaps.get(currentArea).checkInArea(pX, pY)) {
                this.x = refX;
                this.y = refY;
            }
        }

        /* * reset speed */
        speed_x = 0;
        speed_y = 0;
    }

    public long getCoolDownTime(char skill) {
        switch (skill) {
            case 'Q' -> {
                return (System.currentTimeMillis() - lastQ) / 1000 - Q_COOLDOWN;
            }
            case 'W' -> {
                return (System.currentTimeMillis() - lastW) / 1000 - W_COOLDOWN;
            }
            case 'E' -> {
                return (System.currentTimeMillis() - lastE) / 1000 - E_COOLDOWN;
            }
            case 'R' -> {
                return (System.currentTimeMillis() - lastR) / 1000 - R_COOLDOWN;
            }
            case 'D' -> {
                return (System.currentTimeMillis() - lastD) / 1000 - D_COOLDOWN;
            }
            case 'F' -> {
                return (System.currentTimeMillis() - lastF) / 1000 - F_COOLDOWN;
            }
        }

        return 0;
    }
}
