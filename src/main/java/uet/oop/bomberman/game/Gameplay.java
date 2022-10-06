package uet.oop.bomberman.game;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.maps.Stage1;
import uet.oop.bomberman.maps.Minimap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** object handler */
public class Gameplay {

    public static int width;
    public static int height;
    protected BufferedReader sourceMap;
    public static List<Entity> entities = new ArrayList<>();
    //terminate
    public static List<Point> killTask = new ArrayList<>();
    protected Bomber player;
    protected List<Enemy> enemies = new ArrayList<>();
    Entity[][] background;
    public static String[] map;

    public static char[][] tile_map;
    public static boolean[][] checker;
    public  double translate_x = 0;
    public  double translate_y = 0;

    /** Minimap cho màn chơi */
    public Minimap minimap;
    public GameMap gameMap;

    public Stage1 stage1;

    public Gameplay() {

    }

    /** Load map from file */
    public void importing (String path) throws IOException {
        try {
            // reading files
            Reader reader = new FileReader(path);
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
                    break;
                }
                case "balloon" -> {
                    System.out.println("Enemy in:" + info[1] + " " + info[2]);
                    enemies.add(new Balloon(Integer.parseInt(info[1]) * Sprite.SCALED_SIZE, Integer.parseInt(info[2]) * Sprite.SCALED_SIZE));
                }
            }

        } finally {
            if(sourceMap != null)
                sourceMap.close();
        }
        createMap();

//        enemies.add(new Balloon(100,200));
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
        minimap = new Minimap(900, 20);
        gameMap = new GameMap();
        stage1 = new Stage1();
    }

    //debug
    public static void main(String[] args) throws IOException {
        Gameplay test = new Gameplay();
        test.importing("src/main/resources/stages/Stage1.txt");
    }

    /** update */
    public void update(){
        player.update(this);
        minimap.update(player);

        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
            if (!entities.get(i).isExisted()) {
                entities.get(i).deadAct(this);
                entities.remove(i);
                i --;
            }
        }
        //enemies
        for(int i = 0; i < enemies.size(); i++){
            enemies.get(i).update(player);
            if(!enemies.get(i).isExisted()) {
                enemies.get(i).deadAct(this);
                enemies.remove(i);
                i--;
            }
        }
        kill();
    }
    /** render objects */
    public void render(GraphicsContext gc) {

        //background rendering
        int low_x =(int) Math.floor(translate_x/Sprite.SCALED_SIZE);
        int low_y = (int) Math.floor(translate_y/Sprite.SCALED_SIZE);
        for(int i = low_y; i <= Math.min(height - 1,low_y + BombermanGame.HEIGHT); i ++) {
            for (int j = low_x; j <= Math.min(width - 1,low_x + BombermanGame.WIDTH); j++){
                    background[i][j].render(gc, this);
                    String temp = new String("");
                    temp += tile_map[i][j];
//                    gc.fillText(temp,j * Sprite.SCALED_SIZE - translate_x, i * Sprite.SCALED_SIZE - translate_y);
            }
        }
        //entities
        entities.forEach(g -> g.render(gc, this));

        //player
        player.render(gc, this);
        enemies.forEach(g -> g.render(gc, this));

        minimap.render(gc);
        stage1.render(gc, this);
    }


    //destroy tiles
    public void kill() {
        while(!killTask.isEmpty())
        {

            Point ref = killTask.get(0);
            //handle
            if(tile_map[ref.getX()][ref.getY()] == '*') {
                tile_map[ref.getX()][ref.getY()] = '0';
                return;
            }
            background[ref.getY()][ref.getX()].kill();
            killTask.remove(0);
        }

    }


    public void generate(Entity obj) {
        entities.add(obj);
    }

    //background fire image
}
