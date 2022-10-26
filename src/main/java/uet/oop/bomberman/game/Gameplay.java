package uet.oop.bomberman.game;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.EntityPov;
import uet.oop.bomberman.graphics.Renderer;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.AreaMap;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.maps.Minimap;
import uet.oop.bomberman.music.Sound;
import uet.oop.bomberman.others.SkillFrame;
import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;
import java.io.*;
import java.util.*;

import static uet.oop.bomberman.game.BombermanGame.*;
import static uet.oop.bomberman.game.BombermanGame.scene;

/** object handler */
public class Gameplay {

    public static int width;
    public static int height;


    public static List<Entity> entities = new ArrayList<>();

    /** Terminate */
    public static List<Point> killTask = new ArrayList<>();
    public static Bomber player;
    protected List<Enemy> enemies = new ArrayList<>();

    /** Map tổng quan*/
    protected BufferedReader sourceMap;
    public static Entity[][] background;
    public static String[] map;

    public static char[][] tile_map;
    public static boolean[][] checker;
    public  double translate_x = 0;
    public  double translate_y = 0;

    /** Canvas Offset - Chỉnh map cân bằng khi phóng to hay thu nhỏ */
    public double offsetX = 0;
    public double offsetY = 0;
    /** Renderer - Trung gian render lên canvas */
    //Renderer chính
    public static Renderer wholeScene = new Renderer(0.5, 0.5, 0, 0, 0, 0,
                                                BombermanGame.WIDTH* Sprite.SCALED_SIZE,
                                            BombermanGame.HEIGHT  * Sprite.SCALED_SIZE , 1);

    //enemy
    public EntityPov enemyScene = new EntityPov(BombermanGame.WIDTH * Sprite.SCALED_SIZE / 3,
                                                BombermanGame.HEIGHT * Sprite.SCALED_SIZE / 3);
    public static int chosenEnemy = 0;
    public static int bufferMode = 0;
    /** GUI GAME Image */
    Image gameFrame = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/frame.png")));
    Image enemyFrame = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/enemy_frame.png")));
    Image gameBg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/bg.png")));

    /** Minimap cho màn chơi */
    public Minimap minimap;
    public GameMap gameMap;

    /** Map khu vực **/
    public static ArrayList<AreaMap> areaMaps = new ArrayList<>();
    public static int currentArea = 0;

    /** Các tập hợp entity khác */
    public static ArrayList<Fence> fences = new ArrayList<>(); /* * Hàng rào giữa các màn chơi */
    public static Multimap<Integer, Pair<Integer, Boolean>>  fires = ArrayListMultimap.create(); /* * Tọa độ các ô lửa - có thể làm người chơi lẫn enemy bị thương **/
    public static SkillFrame skillFrame = new SkillFrame();

    public static Map<Integer, Buff> buffs = new HashMap<>();
    /** Tiếng */
    public static ArrayList<Sound> sounds = new ArrayList<>();

    public Gameplay() {
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
                map[i] = new String(sourceMap.readLine());
            String ref = new String(sourceMap.readLine());

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
        //testing things
        enemies.add(new Balloon( 14 * 48, 42 * 48));
        enemies.add(new Balloon( 8 * 48, 48 * 48));
        buffs.put(tileCode(9,48), new Buff(9, 48, 1));
        System.out.println(enemies);
        wholeScene.setPov(player);
        enemyScene.setPov(enemies.get(0));
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
        minimap = new Minimap(800, 20 );
        gameMap = new GameMap();

        /* *****  Fence ***** */

        // West
        fences.add(new Fence(22, 45, Fence.VERTICAL));
        fences.add(new Fence(45, 45, Fence.VERTICAL));
        // North
        fences.add(new Fence(54, 35, Fence.HORIZONTAL));
        fences.add(new Fence(54, 14, Fence.HORIZONTAL));
        // East
        fences.add(new Fence(69, 45, Fence.VERTICAL));
        fences.add(new Fence(95, 45, Fence.VERTICAL));
        // South
        fences.add(new Fence(54, 59, Fence.HORIZONTAL));
        fences.add(new Fence(54, 82, Fence.HORIZONTAL));
        // Portal
        fences.add(new Fence(67, 89, Fence.VERTICAL));
        fences.add(new Fence(101, 89, Fence.VERTICAL));

        // Mặc định là hàng rào chặn lại các màn
        for (Fence fence: fences) {
            fence.setStatus(Fence.UP);
        }
    }

    /** update */
    public void update(){
        //interaction
        interaction();

        //update renderer
        wholeScene.setOffSet(canvas);
        wholeScene.update();
        //other
        player.update(this);
        skillFrame.update(player);

        /* * Cập nhật map khu vực hiện tại * */
        updateAreaMaps();

        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
            if (!entities.get(i).isExisted()) {
                entities.get(i).deadAct(this);
                entities.remove(i);
                i --;
            }
        }

        /** Sound **/
        if(BombermanGame.currentFrame % (FPS / 5) == 0) {
            for (int i = 0; i < sounds.size(); i++) {
                sounds.get(i).update(player);
                if (!sounds.get(i).exists()) {
                    sounds.get(i).stop();
                    sounds.remove(i);
                    i--;
                    System.out.println(sounds);
                }
                System.out.println("Sound update");
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
        switch (bufferMode) {
            default -> {}
            case 1 -> enemyScene.update();
            case 2 -> minimap.update(player);
        }
    }
    /** Render objects.
     * Thứ tự render/ layering:
     * Tiles -> Buffs -> Mobile -> Bomb/Items -> Player -> Nuke -> Fx images */

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

        /* * Enemies * */
        enemies.forEach(g -> g.render(gc,renderer));

        /* * Player * */
        if(renderer.getPov().isAlly() == player.isAlly() || player.vulnerable()) player.render(gc, renderer);
    }

    public void render(GraphicsContext gc, double canvasWidth, double canvasHeight) {

        offsetX = Math.max(0, (canvasWidth - BombermanGame.WIDTH * Sprite.SCALED_SIZE) / 2);
        offsetY = Math.max(0, (canvasHeight - BombermanGame.HEIGHT * Sprite.SCALED_SIZE) / 2);
        /* Game Background */
        gc.drawImage(gameBg, 0, 0);

        render(gc, wholeScene);

        /* * Buffer * */
        switch (bufferMode) {
            default -> {}
            case 1 -> {
                enemyScene.render(this);
                gc.drawImage(enemyScene.getImg(),minimap.getX() + offsetX - 20, minimap.getY() + offsetY
                                                , wholeScene.centerX / 2, wholeScene.centerY / 2 );
                gc.drawImage(enemyFrame, minimap.getX() + offsetX - 30, minimap.getY() + offsetY - 10
                                                ,wholeScene.centerX / 2 + 20, wholeScene.centerY / 2 + 30);
            }
            case 2 -> minimap.render(gc, minimap.getX() + offsetX, minimap.getY() + offsetY);
        }
        /* * Khung Skill * */
        skillFrame.render(gc, this, player);

        /* * Khung màn hình game */
        gc.drawImage(gameFrame, this.offsetX - 320, this.offsetY - 255);
    }

    /** Destroy tiles */
    public void kill() {
        while(!killTask.isEmpty())
        {

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
        for (int i = 0; i < areaMaps.size(); i++) {
            int pX = (int) Math.floor(player.getCenterX() / Sprite.SCALED_SIZE );
            int pY = (int) Math.floor(player.getCenterY() / Sprite.SCALED_SIZE );

            if (areaMaps.get(i).checkInArea(pX, pY)) {

                if (i != currentArea) {
                    currentArea = i;
                    break;
                }
            }
        }
    }
    public static Integer tileCode(int x, int y) {
        if(width >= height) return y * width + x;
        else return x * height + y;
    }

    public void switchPov() {
        if(enemies.size() == 0) {
            enemyScene.setPov(null);
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
                    case TAB -> bufferMode = (bufferMode + 1) % 3;
                    case T -> switchPov();
                }
            }});
    }
}
