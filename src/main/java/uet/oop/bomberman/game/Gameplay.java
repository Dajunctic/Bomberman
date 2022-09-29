package uet.oop.bomberman.game;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.Generals.Point;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static int[][] tile_map;
    public static boolean[][] checker;
    public  double translate_x = 0;
    public  double translate_y = 0;

    //differentiates tiles
    public static int divisors = 10;
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
            tile_map = new int[height][width];
            checker = new boolean[height][width];

            for(int i = 0;i < height;i ++){
                map[i] = sourceMap.readLine();
            }
            //read entities positions
            String ref = sourceMap.readLine();
            while(ref != null){
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
                ref = sourceMap.readLine();
            }


        } finally {
            if(sourceMap != null)
                sourceMap.close();
        }

        //init map
        createMap();

        enemies.add(new Balloon(100,200));
    }

    /** init background */
    private void createMap() throws IOException {
        background = new Entity[height][width];
        for(int i = 0; i < height; i++) {
            String[] components = map[i].split(" ");
            for (int j = 0; j < width; j++) {
                int ref = Integer.parseInt(components[j]);
                switch (ref / divisors) {
                    case 0: {
                        background[i][j] = new Floor(j, i, Sprite.floors.get(ref % divisors).getFxImage(), ref % divisors);

                        break;
                    }
                    case 1: {
                        background[i][j] = new Brick(j, i, Sprite.destroyed.get(ref % divisors).getFxImage(), ref % divisors);
                        break;
                    }
                    case 2: {
                        background[i][j] = new Wall(j, i, Sprite.walls.get(ref % divisors).getFxImage(), ref % divisors);
                        break;
                    }

                }
                tile_map[i][j] = ref / divisors;
                System.out.println(i + " " + j + " " + background[i][j].getClass() + " " + ref + " tiles type " + tile_map[i][j]);

            }
        }
    }

    //debug
    public static void main(String[] args) throws IOException {
        Gameplay test = new Gameplay();
        test.importing("src/main/resources/maps/sandbox_map.txt");
    }

    /** update */
    public void update(){
        player.update(this);

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
                    gc.fillText(temp,j * Sprite.SCALED_SIZE - translate_x + 10 , i * Sprite.SCALED_SIZE - translate_y + 10);
            }
        }
        //entities
        entities.forEach(g -> g.render(gc, this));

        //player
        player.render(gc, this);
        enemies.forEach(g -> g.render(gc, this));

    }


    //destroy tiles
    public void kill() {
        while(!killTask.isEmpty())
        {

            Point ref = killTask.get(0);
            //handle
            if(tile_map[ref.getX()][ref.getY()] < 0) {
                tile_map[ref.getX()][ref.getY()] = 0;
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
