package uet.oop.bomberman.game;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.entities.enemy.balloon.Balloon;
import uet.oop.bomberman.entities.enemy.balloon.Ghost;
import uet.oop.bomberman.entities.enemy.balloon.Jumper;
import uet.oop.bomberman.entities.enemy.special.Mage;
import uet.oop.bomberman.entities.enemy.special.Suicider;
import uet.oop.bomberman.entities.entities.background.Fence;
import uet.oop.bomberman.entities.functional.Buff;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.explosive.Fire;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.generals.Triplets;
import uet.oop.bomberman.graphics.Layer;
import uet.oop.bomberman.graphics.LightProbe;
import uet.oop.bomberman.graphics.Renderer;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.AreaMap;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.maps.Minimap;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.Sound;
import uet.oop.bomberman.others.SkillFrame;

import java.io.*;
import java.util.*;

import static uet.oop.bomberman.game.BombermanGame.*;
import static uet.oop.bomberman.game.BombermanGame.scene;

/** object handler */
public class Gameplay {

    public static int width;
    public static int height;


    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> nukes = new ArrayList<>();

    /** Terminate */
    public static List<Point> killTask = new ArrayList<>();
    public static Bomber player;
    protected List<Enemy> enemies = new ArrayList<>();
    protected static List<Stack<Enemy>> enemyStack = new ArrayList<>();
    /** Map tổng quan*/
    protected BufferedReader sourceMap;
    public static Entity[][] background;
    public static String[] map;

    public static char[][] tile_map;
    public static boolean[][] checker;
    public double translate_x = 0;
    public double translate_y = 0;

    public boolean lose = false;
    public boolean returnMenu = false;
    public boolean loading = false;

    /** Canvas Offset - Chỉnh map cân bằng khi phóng to hay thu nhỏ */
    public double offsetX = 0;
    public double offsetY = 0;
    /** Renderer - Trung gian render lên canvas */
    //Renderer chính
    public Renderer wholeScene = new Renderer(0.5, 0.5, 0, 0, 0, 0,
                                                BombermanGame.WIDTH* Sprite.SCALED_SIZE,
                                            BombermanGame.HEIGHT  * Sprite.SCALED_SIZE , 0.95);
    LightProbe playerPov;

    //enemy
    public static Layer playerScene = new Layer(0, 0, BombermanGame.WIDTH * Sprite.SCALED_SIZE,
                                                BombermanGame.HEIGHT * Sprite.SCALED_SIZE, 1, true);
    public static Layer enemyScene = new Layer(0.75, 0.1, BombermanGame.WIDTH * Sprite.SCALED_SIZE / 2.0,
                                        BombermanGame.HEIGHT * Sprite.SCALED_SIZE / 2.0, 0.2, true);
    public static int chosenEnemy = 0;
    public static int bufferMode = 0;
    /** GUI GAME Image */
    Image gameFrame = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/frame.png")));
    Image enemyFrame = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/enemy_frame.png")));
    Image badEnding = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/bg/bad.png")));
    Image loadingImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/loading.png")));

    /** Minimap cho màn chơi */
    public Minimap minimap;
    public GameMap gameMap;
    public boolean openMinimap = false;

    /** Map khu vực **/
    public static ArrayList<AreaMap> areaMaps = new ArrayList<>();
    public static int currentArea = 0;
    public static int wonArea = -1;
    public static boolean checkWon = true;

    /** Các tập hợp entity khác */
    public static ArrayList<Fence> fences = new ArrayList<>(); /* * Hàng rào giữa các màn chơi */
    public static HashMap<Integer, Fire>  fires = new HashMap<>(); /* * Tọa độ các ô lửa - có thể làm người chơi lẫn enemy bị thương **/
    public static SkillFrame skillFrame = new SkillFrame();

    public static Map<Integer, Buff> buffs = new HashMap<>();
    /** Âm thanh */
    public static ArrayList<Sound> sounds = new ArrayList<>();

    /** Ending */
    public Ending ending;

    public static MediaPlayer openFence = Audio.copy(Audio.open_fence);
    public static MediaPlayer closeFence = Audio.copy(Audio.close_fence);

    boolean reset = false;

    public Gameplay() {
        for (int i = 0 ; i < 5; i++) {
            Stack<Enemy> stack = new Stack<>();
            enemyStack.add(stack);
        }

        ending = new Ending();
    }

    /** Load map from file */
    public void importing (String generalMap, String areaMap) throws IOException {

        /* Map rộng */
        try {
            // reading files
            Reader reader = new FileReader(generalMap);
            sourceMap = new BufferedReader(reader);

            //map size initializer
            String map_size = sourceMap.readLine();
            String[] op = map_size.split(" ");
            width = Integer.parseInt(op[0]);
            height = Integer.parseInt(op[1]);
            System.out.println(width + " " + height);
            map = new String[height];
            tile_map = new char[height][width];
            checker = new boolean[height][width];
            //init
            for(int i = 0;i < height; i++ )
                map[i] = sourceMap.readLine();
            String ref = sourceMap.readLine();

            String[] info = ref.split(" ");
            switch (info[0]) {
                case "player" -> {
                    System.out.println("Bomber in:" + info[1] + " " + info[2]);
                    player = new Bomber(Integer.parseInt(info[1]) * Sprite.SCALED_SIZE, Integer.parseInt(info[2]) * Sprite.SCALED_SIZE, "/sprites/Player/Model");
                }
                case "balloon" -> {
                    System.out.println("Enemy in:" + info[1] + " " + info[2]);
                    enemies.add(new Balloon(Integer.parseInt(info[1]) * Sprite.SCALED_SIZE, Integer.parseInt(info[2]) * Sprite.SCALED_SIZE));
                }
            }

        } finally {
            if(sourceMap != null) {
                sourceMap.close();
            }
        }


        /* *** Đọc map từng khu vực - AREA MAP *** */
        try {
            Reader reader = new FileReader(areaMap);
            sourceMap = new BufferedReader(reader);

            int numArea = Integer.parseInt(sourceMap.readLine());
            System.out.println(numArea);
            for (int k = 0; k < numArea; k++) {
                String areaInfo = sourceMap.readLine();
                String[] detail = areaInfo.split(" ");

                int posY = Integer.parseInt(detail[0]);
                int posX = Integer.parseInt(detail[1]);
                int height = Integer.parseInt(detail[2]);
                int width = Integer.parseInt(detail[3]);

                char[][] tiles = new char[height][width];

                for (int i = 0; i < height; i++) {
                    String rows = sourceMap.readLine();

                    for (int j = 0; j < width; j++) {
                        tiles[i][j] = rows.charAt(j);
                    }
                }

                areaMaps.add(new AreaMap(tiles, posX, posY, width, height));
            }

        } finally {
            if (sourceMap != null) {
                sourceMap.close();
            }
        }

        createMap();

//        createTestEnemy();
        createEnemy();


        wholeScene.setPov(player);
        enemyScene.setPov(player);
        playerScene.setPov(player);
        playerPov = new LightProbe(player, 5, 50);
    }

    /** Tạo map hoàn chỉnh */
    private void createMap() {
        background = new Entity[height][width];
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                background[i][j] = GameMap.getTile(map[i].charAt(j), i, j);

                tile_map[i][j] = map[i].charAt(j);
            }

        }
        /* * Minimap */
//        if (!reset) {
        minimap = new Minimap(800, 300);
        gameMap = new GameMap();
//        }

        /* *****  Fence ***** */

        // West
        fences.add(new Fence(22, 45, Fence.VERTICAL, 1)); // 0
        fences.add(new Fence(45, 45, Fence.VERTICAL, 2)); // 1
        // North
        fences.add(new Fence(54, 35, Fence.HORIZONTAL, 2)); // 2
        fences.add(new Fence(54, 14, Fence.HORIZONTAL, 0)); // 3
        // East
        fences.add(new Fence(69, 45, Fence.VERTICAL, 2)); // 4
        fences.add(new Fence(95, 45, Fence.VERTICAL,3)); // 5
        // South
        fences.add(new Fence(54, 59, Fence.HORIZONTAL, 2)); // 6
        fences.add(new Fence(54, 82, Fence.HORIZONTAL, 3)); // 7
        // Portal
        fences.add(new Fence(67, 89, Fence.VERTICAL, 0)); // 8
//        fences.add(new Fence(101, 89, Fence.VERTICAL)); // 9

        // Mặc định là hàng rào chặn lại các màn
        for (Fence fence: fences) {
            fence.setStatus(Fence.UP);
        }
    }

    public void createTestEnemy() {
        /* * Area 0 */
        int areaX = areaMaps.get(0).getPosX();
        int areaY = areaMaps.get(0).getPosY();

//        enemyStack.get(0).add(new Ghost(8 * 48, 46 * 48));
        enemyStack.get(0).add(new Mage( 17 * 48, 52 * 48));
//        enemyStack.get(0).add(new Mage( 13 * 48, 42 * 48));
//        enemyStack.get(0).add(new Jumper(15 * 48, 42 * 48));
//        enemyStack.get(0).add(new Jumper(15 * 48, 40 * 48));
//        enemyStack.get(0).add(new Suicider(15 * 48, 52 * 48));
//        enemyStack.get(0).add(new Suicider(15 * 48, 52 * 48));
//        enemyStack.get(0).add(new Suicider(15 * 48, 52 * 48));
//        enemyStack.get(0).add(new Balloon(12 * 48, 48 * 48));

        buffs.put(tileCode(9,48), new Buff(9, 48, 1));

        /* * Area 1 */
        areaX = areaMaps.get(1).getPosX();
        areaY = areaMaps.get(1).getPosY();

        enemyStack.get(1).add(new Mage( (2 + areaX) * 48, (2 + areaY) * 48));

        /* * Area 2 */
        areaX = areaMaps.get(2).getPosX();
        areaY = areaMaps.get(2).getPosY();

        enemyStack.get(2).add(new Mage( (2 + areaX) * 48, (2 + areaY) * 48));

        /* * Area 3 */
        areaX = areaMaps.get(3).getPosX();
        areaY = areaMaps.get(3).getPosY();

        enemyStack.get(3).add(new Mage( (2 + areaX) * 48, (2 + areaY) * 48));

        /* * Area 4 */
        areaX = areaMaps.get(4).getPosX();
        areaY = areaMaps.get(4).getPosY();

        enemyStack.get(4).add(new Mage( (2 + areaX) * 48, (2 + areaY) * 48));
    }

    public void createEnemy() {
        /* * Area 0 */
        int areaX = areaMaps.get(0).getPosX();
        int areaY = areaMaps.get(0).getPosY();

        enemyStack.get(0).add(new Ghost( (14 + areaX) * 48, (9 + areaY) * 48));
        enemyStack.get(0).add(new Jumper( (14 + areaX) * 48, (8 + areaY) * 48));
        enemyStack.get(0).add(new Mage( (14 + areaX) * 48, (4 + areaY) * 48));
        enemyStack.get(0).add(new Jumper( (10 + areaX) * 48, (9 + areaY) * 48));
        enemyStack.get(0).add(new Jumper( (10 + areaX) * 48, (7 + areaY) * 48));
        enemyStack.get(0).add(new Jumper( (2 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(0).add(new Jumper( (15 + areaX) * 48, (0 + areaY) * 48));
        enemyStack.get(0).add(new Suicider( (10 + areaX) * 48, (0 + areaY) * 48));
        enemyStack.get(0).add(new Suicider( (9 + areaX) * 48, (8 + areaY) * 48));
        enemyStack.get(0).add(new Ghost( (10 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(0).add(new Balloon( (2 + areaX) * 48, (3 + areaY) * 48));
        enemyStack.get(0).add(new Balloon( (14 + areaX) * 48, (2 + areaY) * 48));
        enemyStack.get(0).add(new Suicider( (4 + areaX) * 48, (0 + areaY) * 48));
        enemyStack.get(0).add(new Mage( (4 + areaX) * 48, (9 + areaY) * 48));
        enemyStack.get(0).add(new Suicider( (6 + areaX) * 48, (14 + areaY) * 48));

        buffs.put(tileCode(9,48), new Buff(9, 48, 1));

        /* * Area 1 */
        areaX = areaMaps.get(1).getPosX();
        areaY = areaMaps.get(1).getPosY();

        enemyStack.get(1).add(new Jumper( (16 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(1).add(new Ghost( (21 + areaX) * 48, (6 + areaY) * 48));
        enemyStack.get(1).add(new Jumper( (12 + areaX) * 48, (21 + areaY) * 48));
        enemyStack.get(1).add(new Ghost( (22 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(1).add(new Suicider( (19 + areaX) * 48, (22 + areaY) * 48));
        enemyStack.get(1).add(new Mage( (10 + areaX) * 48, (17 + areaY) * 48));
        enemyStack.get(1).add(new Jumper( (18 + areaX) * 48, (8 + areaY) * 48));
        enemyStack.get(1).add(new Jumper( (12 + areaX) * 48, (7 + areaY) * 48));
        enemyStack.get(1).add(new Jumper( (22 + areaX) * 48, (2 + areaY) * 48));
        enemyStack.get(1).add(new Jumper( (8 + areaX) * 48, (1 + areaY) * 48));
        enemyStack.get(1).add(new Balloon( (16 + areaX) * 48, (18 + areaY) * 48));
        enemyStack.get(1).add(new Balloon( (10 + areaX) * 48, (0 + areaY) * 48));
        enemyStack.get(1).add(new Mage( (20 + areaX) * 48, (2 + areaY) * 48));
        enemyStack.get(1).add(new Balloon( (18 + areaX) * 48, (20 + areaY) * 48));
        enemyStack.get(1).add(new Mage( (6 + areaX) * 48, (12 + areaY) * 48));


        /* * Area 2 */
        areaX = areaMaps.get(2).getPosX();
        areaY = areaMaps.get(2).getPosY();

        enemyStack.get(2).add(new Mage( (11 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(2).add(new Ghost( (12 + areaX) * 48, (8 + areaY) * 48));
        enemyStack.get(2).add(new Suicider( (10 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(2).add(new Suicider( (8 + areaX) * 48, (10 + areaY) * 48));
        enemyStack.get(2).add(new Ghost( (10 + areaX) * 48, (10 + areaY) * 48));
        enemyStack.get(2).add(new Mage( (12 + areaX) * 48, (0 + areaY) * 48));
        enemyStack.get(2).add(new Jumper( (10 + areaX) * 48, (11 + areaY) * 48));
        enemyStack.get(2).add(new Ghost( (10 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(2).add(new Ghost( (9 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(2).add(new Balloon( (9 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(2).add(new Balloon( (0 + areaX) * 48, (2 + areaY) * 48));
        enemyStack.get(2).add(new Jumper( (10 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(2).add(new Suicider( (2 + areaX) * 48, (8 + areaY) * 48));
        enemyStack.get(2).add(new Suicider( (2 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(2).add(new Ghost( (9 + areaX) * 48, (4 + areaY) * 48));


        /* * Area 3 */
        areaX = areaMaps.get(3).getPosX();
        areaY = areaMaps.get(3).getPosY();

        enemyStack.get(3).add(new Mage( (11 + areaX) * 48, (10 + areaY) * 48));
        enemyStack.get(3).add(new Mage( (11 + areaX) * 48, (4 + areaY) * 48));
        enemyStack.get(3).add(new Suicider( (14 + areaX) * 48, (6 + areaY) * 48));
        enemyStack.get(3).add(new Balloon( (8 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(3).add(new Suicider( (8 + areaX) * 48, (8 + areaY) * 48));
        enemyStack.get(3).add(new Balloon( (12 + areaX) * 48, (2 + areaY) * 48));
        enemyStack.get(3).add(new Mage( (11 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(3).add(new Balloon( (13 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(3).add(new Suicider( (7 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(3).add(new Balloon( (8 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(3).add(new Suicider( (13 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(3).add(new Suicider( (10 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(3).add(new Ghost( (2 + areaX) * 48, (10 + areaY) * 48));
        enemyStack.get(3).add(new Suicider( (2 + areaX) * 48, (14 + areaY) * 48));
        enemyStack.get(3).add(new Jumper( (6 + areaX) * 48, (2 + areaY) * 48));

        /* * Area 4 */
        areaX = areaMaps.get(4).getPosX();
        areaY = areaMaps.get(4).getPosY();

        enemyStack.get(4).add(new Balloon( (16 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(4).add(new Jumper( (10 + areaX) * 48, (12 + areaY) * 48));
        enemyStack.get(4).add(new Ghost( (12 + areaX) * 48, (16 + areaY) * 48));
        enemyStack.get(4).add(new Ghost( (7 + areaX) * 48, (16 + areaY) * 48));
        enemyStack.get(4).add(new Suicider( (7 + areaX) * 48, (16 + areaY) * 48));
        enemyStack.get(4).add(new Suicider( (10 + areaX) * 48, (16 + areaY) * 48));
        enemyStack.get(4).add(new Suicider( (14 + areaX) * 48, (16 + areaY) * 48));
        enemyStack.get(4).add(new Jumper( (12 + areaX) * 48, (6 + areaY) * 48));
        enemyStack.get(4).add(new Balloon( (14 + areaX) * 48, (16 + areaY) * 48));
        enemyStack.get(4).add(new Suicider( (8 + areaX) * 48, (4 + areaY) * 48));
        enemyStack.get(4).add(new Suicider( (16 + areaX) * 48, (0 + areaY) * 48));
        enemyStack.get(4).add(new Ghost( (10 + areaX) * 48, (4 + areaY) * 48));
        enemyStack.get(4).add(new Suicider( (2 + areaX) * 48, (6 + areaY) * 48));
        enemyStack.get(4).add(new Suicider( (16 + areaX) * 48, (6 + areaY) * 48));
        enemyStack.get(4).add(new Ghost( (6 + areaX) * 48, (10 + areaY) * 48));

    }

    /** update */
    public void update(){

        if((currentFrame % FPS == 0 || enemies.isEmpty())
                && !enemyStack.get(currentArea).isEmpty()) {
            enemies.add(enemyStack.get(currentArea).pop());
        }

        /* * interaction */
        interaction();

        /* * update renderer */
        wholeScene.setOffSet(canvas);
        wholeScene.update();


        /* * Cập nhật map khu vực hiện tại * */
        updateAreaMaps();

        /* * other */
        player.update(this);
        ending.update(player);

        if (player.getCurrentHP() == 0) {
            lose = true;
        }

        if (ending.getStatus() > Ending.NORMAL) return;

        skillFrame.update(player);
        for (Fence fence: fences) fence.update();



        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
            if (!entities.get(i).isExisted()) {
                entities.get(i).deadAct(this);
                entities.remove(i);
                i --;
            }
        }
        for(int i = 0; i < nukes.size(); i++) {
            nukes.get(i).update();
            if (!nukes.get(i).isExisted()) {
                nukes.get(i).deadAct(this);
                nukes.remove(i);
                i --;
            }
        }

        /* * Sound **/
        if(BombermanGame.currentFrame % (FPS / 5) == 0) {
            for (int i = 0; i < sounds.size(); i++) {
                sounds.get(i).update(player);
                if (!sounds.get(i).exists()) {
                    sounds.get(i).free();
                    sounds.remove(i);
                    i--;
                }
            }
        }

        /* * Enemies */
        for(int i = 0; i < enemies.size(); i++){
            enemies.get(i).update(player);
            if(!enemies.get(i).isExisted()) {
                enemies.get(i).deadAct(this);
                enemies.remove(i);
                if(i == chosenEnemy){
                    chosenEnemy --;
                    switchPov();
                }
                i--;
            }
        }
        kill();
        playerScene.update();

        if (bufferMode == 1) {
            enemyScene.update();
        }

        minimap.update(player);
    }
    /* * Render objects.
     * Thứ tự render/ layering:
     * Tiles -> Buffs -> Mobile -> Bomb/Items -> Player -> Nuke -> Fx images */

    /** Render theo viewport*/
    public void render(GraphicsContext gc, Renderer renderer) {
        int low_x =(int) Math.floor(renderer.getTranslateX() / Sprite.SCALED_SIZE);
        int low_y = (int) Math.floor(renderer.getTranslateY() / Sprite.SCALED_SIZE);
        int bound_x = (int) Math.round(renderer.getWidth() / Sprite.SCALED_SIZE);
        int bound_y = (int) Math.round(renderer.getHeight() / Sprite.SCALED_SIZE);
        for(int i = low_y; i <= Math.min(height - 1,low_y + bound_y); i ++) {
            for (int j = low_x; j <= Math.min(width - 1,low_x + bound_x); j++){
                background[i][j].render(gc, renderer);
            }
        }


        /* * Render map khu vực * */
        for (AreaMap areaMap: areaMaps) {
            areaMap.render(gc, renderer);
        }

        /* * Hàng rào * */
        for (Fence fence: fences) {
            fence.render(gc, renderer);
        }
        /* * Buffs * */
        for(Buff i : buffs.values()) {
            i.render(gc, renderer);
        }

        entities.forEach(g -> g.render(gc, renderer));

        /* * Player * */
        player.render(gc, renderer);

        /* * Enemies * */
        enemies.forEach(g -> g.render(gc,renderer));

        /* * Nukes * */
        nukes.forEach(g -> g.render(gc,renderer));
    }

    /** Render theo layer có ảnh hưởng của shader*/
    public void render(Layer layer) {
        if(layer.lighter == null){
            render(layer.gc, layer.renderer);
            return;
        }
        GraphicsContext gc = layer.gc;
        Renderer renderer = layer.renderer;
        //Render >:D
        gc.fillRect(0,0, layer.canvas.getWidth(), layer.canvas.getHeight());
        int low_x =(int) Math.floor(renderer.getTranslateX() / Sprite.SCALED_SIZE);
        int low_y = (int) Math.floor(renderer.getTranslateY() / Sprite.SCALED_SIZE);
        int bound_x = (int) Math.round(renderer.getWidth() / Sprite.SCALED_SIZE);
        int bound_y = (int) Math.round(renderer.getHeight() / Sprite.SCALED_SIZE);
        for(int i = low_y; i <= Math.min(height - 1,low_y + bound_y); i ++) {
            for (int j = low_x; j <= Math.min(width - 1,low_x + bound_x); j++){
                background[i][j].render(layer);
            }
        }

        /* * Render map khu vực * */
        for (AreaMap areaMap: areaMaps) {
            areaMap.render(layer);
        }

        /* * Hàng rào * */
        fences.forEach(fence -> fence.render(layer));

        /* * Buffs * */
        for(Buff i : buffs.values()) {
            i.render(layer);
        }

        entities.forEach(g -> g.render(layer));
        /* * Player * */
        player.render(layer);

        /* * Enemies * */
        enemies.forEach(g -> g.render(layer));

        /* * Shader * */
        layer.shade();


        /* ****** Not affected by shader **/
        /* * Nukes * */
        nukes.forEach(g -> g.render(layer));


        /* * Ending Renderer */
        ending.render(layer);
    }

    /** Render tổng và ghi lên cửa sổ game*/
    public void render(GraphicsContext gc, double canvasWidth, double canvasHeight) {

        offsetX = Math.max(0, (canvasWidth - BombermanGame.WIDTH * Sprite.SCALED_SIZE) / 2);
        offsetY = Math.max(0, (canvasHeight - BombermanGame.HEIGHT * Sprite.SCALED_SIZE) / 2);

        if (loading) {
            gc.drawImage(loadingImg, 0, 0);

            if (!returnMenu) {
                try {
                    reset();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return;
        }

        if (lose) {

            gc.drawImage(badEnding, 0, 0);
            return;
        }


        /* Game Background */
        playerScene.render(this);
        renderLayer(gc, playerScene, gameFrame, 10, 10);


        /* **/
        ending.render(gc);

        if (ending.getStatus() > Ending.NORMAL) return;


        /* * Buffer * */
        if (bufferMode == 1) {
            enemyScene.render(this);
            renderLayer(gc, enemyScene, enemyFrame, 8, 13);
        }

        /* * Map * */
        if (openMinimap) {
            minimap.render(gc, WIDTH * Sprite.SCALED_SIZE * 0.8 + offsetX, HEIGHT * Sprite.SCALED_SIZE * 0.5 + offsetY);
        }

        /* * Khung Skill * */
        skillFrame.render(gc, this, player);


    }

    /** Destroy tiles */
    public void kill() {
        while(!killTask.isEmpty()) {

            Point ref = killTask.get(0);
            //handle
            int i = ref.getX();
            int j = ref.getY();

            boolean checkBrick = Gameplay.get(tile_map[j][i], i, j) == GameMap.BRICK;

            if(checkBrick) {
                areaMaps.get(currentArea).getEntity(i, j).kill();
                return;
            }

            killTask.remove(0);
        }

    }


    public void generate(Entity obj) {
        entities.add(obj);
    }

    /** Hàm trả về kiểu của Tile đang xét tới. */
    public static int get(char x, int i, int j) {
        AreaMap cur = areaMaps.get(currentArea);

        if (cur.checkInArea(i, j)) {
            return GameMap.get(cur.getChar(i, j));
        } else {
            return GameMap.get(x);
        }
    }

    /** Hàm cài kiểu Tile */
    public static void set(char x, int i, int j, boolean area) {

        if (area) {
            AreaMap cur = areaMaps.get(currentArea);
            if (cur.checkInArea(i, j)) {
                cur.setChar(i, j, x);
            }
            return;
        }

        tile_map[j][i] = x;
        background[j][i] = GameMap.getTile(tile_map[j][i], j, i);
    }

    /** Floor or Wall bị phá hủy */
    public static void kill(int i, int j) {
        background[j][i].kill();
    }

    void updateAreaMaps() {

        if (enemies.isEmpty() && checkWon) {
            wonArea ++;
            checkWon = false;
            openFence();
        }


        for (int i = 0; i < areaMaps.size(); i++) {
            int p0X = (int) Math.floor((player.getX() + player.getHeight() / 4) / Sprite.SCALED_SIZE );
            int p0Y = (int) Math.floor(player.getY()/ Sprite.SCALED_SIZE );
            int p1X = (int) Math.floor((player.getX() + player.getWidth()) / Sprite.SCALED_SIZE );
            int p1Y = (int) Math.floor((player.getY() + player.getHeight()) / Sprite.SCALED_SIZE );

            if (areaMaps.get(i).checkInArea(p0X, p0Y) && areaMaps.get(i).checkInArea(p1X, p1Y)) {

                if (i != currentArea) {
                    currentArea = i;

                    /* To play new area*/
                    if (currentArea > wonArea) {
                        closeFence();
                        checkWon = true;
                    }

                    break;
                }
            }
        }
    }


    public void openFence() {

        Audio.start(openFence);

        if (wonArea == 0) {
            fences.get(0).setStatus(Fence.DOWN);
            fences.get(1).setStatus(Fence.DOWN);
        } else if (wonArea == 1) {
            fences.get(1).setStatus(Fence.DOWN);
            fences.get(2).setStatus(Fence.DOWN);
            fences.get(3).setStatus(Fence.DOWN);
        } else if (wonArea == 2) {
            fences.get(3).setStatus(Fence.DOWN);
            fences.get(4).setStatus(Fence.DOWN);
            fences.get(5).setStatus(Fence.DOWN);
        } else if (wonArea == 3) {
            fences.get(5).setStatus(Fence.DOWN);
            fences.get(6).setStatus(Fence.DOWN);
            fences.get(7).setStatus(Fence.DOWN);
        } else if (wonArea == 4) {
            fences.get(7).setStatus(Fence.DOWN);
            fences.get(8).setStatus(Fence.DOWN);
        }

    }

    public void closeFence() {
        Audio.start(closeFence);

        if (currentArea == 1) {
            fences.get(1).setStatus(Fence.UP);
        } else if (currentArea == 2) {
            fences.get(3).setStatus(Fence.UP);
        } else if (currentArea == 3) {
            fences.get(5).setStatus(Fence.UP);
        } else if (currentArea == 4) {
            fences.get(7).setStatus(Fence.UP);
        }
    }

    /** Trả về hash code cho ô */
    public static Integer tileCode(int x, int y) {
        if(width >= height) return y * width + x;
        else return x * height + y;
    }
    /** Trả về tọa độ từ hash code*/
    public static Point decodeTile(int tileCode) {
        if(width >= height) return new Point(tileCode % width, tileCode / width);
            else return new Point(tileCode / height, tileCode % height);
    }
    /** Đổi góc nhìn của màn hình nhỏ*/
    public void switchPov() {
        if(enemies.size() == 0) {
            enemyScene.setPov(player);
            return;
        }
        chosenEnemy = (chosenEnemy + 1) % enemies.size();
        enemyScene.setPov(enemies.get(chosenEnemy));
    }


    //transported to gameplay
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
                        player.moves(Bomber.UP);
                    }
                    case DOWN -> {
                        player.moves(Bomber.DOWN);
                    }
                    case LEFT -> {
                        player.moves(Bomber.LEFT);
                    }
                    case RIGHT -> {
                        player.moves(Bomber.RIGHT);
                    }
                }
            }
        });
        /* * released */
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!lose) {
                    switch (keyEvent.getCode()) {
                        case UP -> {
                            player.stopMoving(Bomber.UP);
                        }
                        case DOWN -> {
                            player.stopMoving(Bomber.DOWN);
                        }
                        case LEFT -> {
                            player.stopMoving(Bomber.LEFT);
                        }
                        case RIGHT -> {
                            player.stopMoving(Bomber.RIGHT);
                        }
                        case Q -> player.placeBomb();
                        case W -> player.shootFireball();
                        case E -> player.goInvisible(5);
                        case R -> player.placeNuke();
                        case D -> player.setDodge();
                        case F -> player.recover();
                        case TAB -> bufferMode = (bufferMode + 1) % 2;
                        case T -> switchPov();
                        case P -> enemyScene.turnShader();
                        case M -> openMinimap = !openMinimap;


                        /* * Hack game */
                        case END -> {
//                        wonArea ++;
//                        openFence();
                            enemies.clear();
                            enemyStack.get(currentArea).clear();
                        }
                    }
                } else {
                    switch (keyEvent.getCode()) {
                        case SPACE -> {
                            returnMenu = true;
                            loading = true;
                        }
                        case ENTER -> {
//                          reset();
                            loading = true;

                        }

                    }
                }

            }});
    }
    public void renderLayer(GraphicsContext gc, Layer input, Image cover, double coverThicknessX, double coverThicknessY){
        Triplets v = input.details();
        gc.drawImage(input.getImg(),wholeScene.getWidth() * v.v1, wholeScene.getHeight() * v.v2
                , wholeScene.getWidth() * v.v3, wholeScene.getHeight() * v.v3);
        if(cover != null) {
            gc.drawImage(cover,wholeScene.getWidth() * v.v1 - coverThicknessX, wholeScene.getHeight() * v.v2 - coverThicknessY
                    , wholeScene.getWidth() * v.v3 + coverThicknessX * 2, wholeScene.getHeight() * v.v3 + coverThicknessY * 2);
        }
    }

    public void reset() throws IOException {
        enemyStack.clear();
        for (int i = 0 ; i < 5; i++) {
            Stack<Enemy> stack = new Stack<>();
            enemyStack.add(stack);
        }

        areaMaps.clear();
        enemies.clear();
        fires.clear();
        fences.clear();
        skillFrame = new SkillFrame();
        buffs.clear();

        resetSound();

        entities.clear();
        nukes.clear();

        killTask.clear();

        reset = true;
        currentArea = 0;
        wonArea = -1;
        checkWon = true;
        openMinimap = false;
        chosenEnemy = 0;
        bufferMode = 0;
        lose = false;
        returnMenu = false;
        loading = false;

        importing("src/main/resources/maps/map.txt", "src/main/resources/maps/area.txt");

        reset = false;
    }

    public static void resetSound() {
        for (Sound sound : sounds) {
            sound.stop();
            sound.free();
        }

        sounds.clear();
    }
    public static void sqawnFire(double xUnit, double yUnit, double duration, int damage, boolean friendly, boolean special, boolean mixed) {
        int tileCode = tileCode((int) xUnit, (int) yUnit);
        if(fires.containsKey(tileCode)) fires.get(tileCode).addDamage(damage * (friendly ? 1: -1));
            else entities.add(new Fire(xUnit, yUnit, duration, damage * (friendly ? 1: -1), mixed,  special));
    }

    public static void addEnemy(Enemy enemy) {
        enemyStack.get(currentArea).add(enemy);
    }
}
