package uet.oop.bomberman.entities;

import java.util.*;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.*;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.music.Audio;

import static uet.oop.bomberman.game.BombermanGame.*;
import static uet.oop.bomberman.game.Gameplay.*;

public class Bomber extends Mobile {
    /** Thời gian dùng chiêu **/
    public static double Q_COOLDOWN = 5;
    public static double W_COOLDOWN = 10;
    public static double W_INVISIBLE_COOLDOWN = 2;
    public static double E_COOLDOWN = 10;
    public static double R_COOLDOWN = 0;

    public static int D_COOLDOWN = 0;
    public static int F_COOLDOWN = 0;

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
     * Đường dẫn đến folder của Model thôi không cần ảnh.
     * Có đường dẫn bởi vì nhỡ sau này phát triển nhiều nhân vật có thể lựa chọn để chơi.
     */
    private final String path;

    /** abilities */
    MediaPlayer buffed = Audio.copy(Audio.buff);
    // go incognito
    private boolean invisible = false;
    private double alpha = 1;
    private double invisibleDuration = 3;
    private double fadeInSpeed = 0.8 / (3 *FPS);
    private int Elevel = 1;
    private MediaPlayer Eaudio = Audio.copy(Audio.invisible);

    /** special skills */
    //bomb
    private int radius = 20;
    private int damage = 4;
    public double timer = 2.5;

    private double bombDuration = 0;
    public int Qlevel = 1;

    List<Bomb> bombs = new ArrayList<>();
    private static MediaPlayer Qaudio = Audio.copy(Audio.place_bomb);
    //firewaves
    private int Wdivider = 3;
    private int Wradius = 10;
    private int Wdamage = 8;
    public int Wlevel = 1;
    private MediaPlayer Waudio = Audio.copy(Audio.shooting_fire);
    //TNT
    Nuke nuke = null;
    private int Rdamage = 10;
    private int Rradius = 4;
    public int Rlevel = 1;
    //dodges
    private int dodgeDistance = 2 * Sprite.SCALED_SIZE;
    private boolean dodging = false;
    private double lightShift = 0;
    private final double shiftSpeed = 0.1;
    private int lighten = 1;
    private double oldX = 0;
    private double oldY = 0;
    private MediaPlayer Daudio = Audio.copy(Audio.dodge);
    private DeadAnim dodgeAnim;
    //heal
    private MediaPlayer Faudio = Audio.copy(Audio.heal);
    //Rep
    private MediaPlayer fatality = Audio.copy(Audio.fatal);
    private  boolean isFatal = false;
    //Constructor
    public Bomber(double x, double y, String path) {
        super(x, y);
        this.path = path;
        speed_x = SPEED;
        speed_y = SPEED;
        motionEffect.setRadius(0);
        isAlly = true;
        load();
        standingTile();
        setMana(10000);
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

    public void moves(int dir) {
        currentStatus = dir;
        moving = true;
        moveSet.add(dir);
    }
    public void stopMoving(int dir) {
        moveSet.remove(dir);
        resetSpeed();
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
                lightShift = 1;
                Audio.start(Daudio);
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
        if(alpha >= 1 && invisible) {
            invisible = false;
            Eaudio.setVolume(1);
            Eaudio.seek(Duration.millis(Eaudio.getStopTime().toMillis() - 1500));
        }

        //immense audio
        if(currentHP <= maxHP / 4  ) {
            if(!isFatal) {
                isFatal = true;
                Audio.start(fatality);
            }
            fatality.setVolume((double)(currentHP / maxHP + 0.3));
        }   else if(isFatal) {
            isFatal = false;
            fatality.stop();
        }

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
    public void render(GraphicsContext gc, Renderer renderer) {
        /* * Render bombs */
        bombs.forEach(g -> g.render(gc, renderer));
        gc.setEffect(effect);
        if(invisible)  gc.setGlobalAlpha(alpha);
        /* * Apply invisibility */

        /* * Hiển thị nhân vật */
        renderer.renderImg(gc, this.getImg(), x + shiftX, y + shiftY, false);
        gc.setGlobalAlpha(1);
        gc.setEffect(null);

        if(nuke != null) nuke.render(gc, renderer);
        if(!dodgeAnim.isDead()) renderer.renderImg(gc, dodgeAnim.getImage(), oldX + shiftX, oldY + shiftY, false);

        /* * Hiển thị máu và mana */
        renderHP(gc, renderer);
        renderMana(gc, renderer);
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

            //update tiles
            standingTile();
            checkBuff();
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


    public void setDodge() {
        if (System.currentTimeMillis() - lastD < D_COOLDOWN * 1000) return;
        dodging = true;
        lastD = System.currentTimeMillis();
    }

    public void recover() {
        if (System.currentTimeMillis() - lastF < F_COOLDOWN * 1000) return;

        addHP(HP_RECOVER);
        lastF = System.currentTimeMillis();
        Audio.start(Faudio);
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

        if (currentMana < E_MANA_CONSUMING) return;
        Audio.start(Eaudio);
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
        radius++;
    }


    /**************************** UNIQUE SKILL ********************************/
    //bombing
    public void placeBomb() {
        /* * Điều kiện để có thể đặt được bomb * */
        if ((currentMana < Q_MANA_CONSUMING) ||
                (System.currentTimeMillis() - lastQ < Q_COOLDOWN * 1000L)) return;

        int i = (int) Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE));
        int j = (int) Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE));

        bombs.add(new Bomb(i, j, timer, radius, bombDuration, damage, false));
        subtractMana(Q_MANA_CONSUMING);
        lastQ = System.currentTimeMillis();
        Audio.start(Qaudio);
    }
    /* * Shoot 3 parallel fire */
    public void shootFireball() {
        /* * Điều kiện để bắn */

        //if invisible
        if(invisible) {
            if (System.currentTimeMillis() - lastW <= W_INVISIBLE_COOLDOWN * 1000L) return;
            //sqawn fires
            double startX =  Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE) + 0.5) * Sprite.SCALED_SIZE;
            double startY =  Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE) + 0.5) * Sprite.SCALED_SIZE;
            entities.add(new Flame(startX, startY, Wradius * Sprite.SCALED_SIZE, facing.getX()
                    , facing.getY()
                    , 1, 4,
                    Wdamage,  true));
        } else {
            //if not invisible
            if ((currentMana < W_MANA_CONSUMING) ||
                    (System.currentTimeMillis() - lastW <= W_COOLDOWN * 1000L))       return;
            /* * Spawn flames */
            double startX =  Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE) + 0.5) * Sprite.SCALED_SIZE;
            double startY =  Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE) + 0.5) * Sprite.SCALED_SIZE;
            entities.add(new Flame(startX, startY, Wradius * Sprite.SCALED_SIZE, facing.getX()
                                                                                    , facing.getY()
                                                                                    , 1, bombDuration,
                                                                                    Wdamage,  true));
            for(int i = 1; i <= Wdivider; i++) {
                entities.add(new Flame(startX, startY, Wradius * Sprite.SCALED_SIZE,  (double) 2 * i / Wradius * facing.getY() + facing.getX()
                                                     ,  (double) 2 * i / Wradius * facing.getX() + facing.getY()
                                                        , 1, bombDuration
                                                        , Wdamage, true));
                entities.add(new Flame(startX, startY, Wradius * Sprite.SCALED_SIZE, -(double) 2 * i / Wradius * facing.getY() + facing.getX()
                                                     , -(double) 2 * i / Wradius * facing.getX() + facing.getY()
                                                      , 1, bombDuration
                                                        , Wdamage, true));
            }

            subtractMana(W_MANA_CONSUMING);
        }
        Audio.start(Waudio);
        lastW = System.currentTimeMillis();
    }

    /** Nuke placing, need to install impact on tile_map */
    public void placeNuke() {
        if ((currentMana < R_MANA_CONSUMING) ||
            (System.currentTimeMillis() - lastR < R_COOLDOWN * 1000L)) return;
        int i = (int) Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE));
        int j = (int) Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE));
        nukes.add(new Nuke(i, j, timer));
        subtractMana(R_MANA_CONSUMING);
        lastR = System.currentTimeMillis();
        Audio.start(Qaudio);
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
                return (long) ((System.currentTimeMillis() - lastQ) / 1000 - Q_COOLDOWN);
            }
            case 'W' -> {
                //if invisible
                if(invisible) return (long) ((System.currentTimeMillis() - lastW) / 1000 - W_INVISIBLE_COOLDOWN);
                //if not
                return (long) ((System.currentTimeMillis() - lastW) / 1000 - W_COOLDOWN);
            }
            case 'E' -> {
                return (long) ((System.currentTimeMillis() - lastE) / 1000 - E_COOLDOWN);
            }
            case 'R' -> {
                return (long) ((System.currentTimeMillis() - lastR) / 1000 - R_COOLDOWN);
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
    /********************** BUFFS **********************************/
    public void buffBomb() {
            Qlevel ++;
            Q_COOLDOWN -= 0.5;
            damage ++;
            bombDuration += 0.5;
            if(Qlevel % 2 == 0) {
                radius++;
            }
            System.out.println("Q upgraded");
            lvlUp();
    }
    public void buffFire() {
            Wlevel ++;
            Wdamage += 2;
            Wradius ++;
            System.out.println("W upgraded");
            lvlUp();
    }

    public void buffInvisible() {
            Elevel ++;
            invisibleDuration += 0.4;
            fadeInSpeed = 0.8 / invisibleDuration / FPS;
            if(Elevel % 2 == 0) {
                W_INVISIBLE_COOLDOWN -= 0.4;
            }
            System.out.println("E upgraded");
            lvlUp();
    }

    public void buffNuke() {
            Rlevel ++;
            Rdamage += 5;
            if(Rlevel % 2 == 0) {
                Rradius ++;
            }
            System.out.println("R upgraded");
            lvlUp();
    }
    public void lvlUp() {
        int hpGap = maxHP - currentHP;
        int mpGap = maxMana - currentMana;
        setHP(maxHP + 50);
        subtractHP(hpGap);
        setMana(maxMana + 50);
        subtractMana(mpGap);
        Audio.start(buffed);
    }
    //checks hitting buffs
    public void checkBuff() {
        Integer index = Gameplay.tileCode(tileX, tileY);
        if(!buffs.containsKey(index)) return;
        buffs.get(index).applyEffect(this);
        buffs.remove(index);
    }

}
