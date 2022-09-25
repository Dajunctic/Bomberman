package uet.oop.bomberman.game;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** object handler */
public class Gameplay {

    public static int width;
    public static int height;
    protected BufferedReader sourceMap;
    protected List<Entity> entities = new ArrayList<>();

    protected Bomber player;
    Entity[][] background;
    public static String[] map;

    public static char[][] tile_map;

    public  double translate_x = 0;
    public  double translate_y = 0;

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
            //init
            for(int i = 0;i < height; i++ )
                map[i] = new String(sourceMap.readLine());
            String ref = new String(sourceMap.readLine());

                String[] info = ref.split(" ");
                switch (info[0]) {
                    case "player":{
                        System.out.println("Bomber in:"  + info[1] + " " + info[2]);
                       player = new Bomber(Integer.parseInt(info[1]) * Sprite.SCALED_SIZE,Integer.parseInt(info[2]) * Sprite.SCALED_SIZE,"/sprites/Player/Model");
                        break;
                    }
                }

        } finally {
            if(sourceMap != null)
                sourceMap.close();
        }
        createMap();

        Bomb test = new Bomb(10 * 48, 10 * 48);
        test.setMode(Entity.CENTER_MODE);
        entities.add(test);
    }

    /** init background */
    private void createMap() {
        background = new Entity[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                switch (map[i].charAt(j)) {
                    case '0': {
                        background[i][j] = new Floor(j, i, Sprite.floor.getFxImage());
                        break;
                    }
                    case '1': {
                        background[i][j] = new Floor(j, i, Sprite.floor.getFxImage());
                    }
                    case '2': {
                        background[i][j] = new Wall(j, i, Sprite.wall.getFxImage());
                    }

                }
            //    System.out.print(i + " " + j + " " + background[i][j].getClass() + " ");
                tile_map[i][j] = map[i].charAt(j);
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

    }
    /** render objects */
    public void render(GraphicsContext gc) {
        //background rendering
        int low_x =(int) Math.floor(translate_x/Sprite.SCALED_SIZE);
        int low_y = (int) Math.floor(translate_y/Sprite.SCALED_SIZE);
        for(int i = low_y; i <= Math.min(height - 1,low_y + BombermanGame.HEIGHT); i ++) {
            for (int j = low_x; j <= Math.min(width - 1,low_x + BombermanGame.WIDTH); j++){
                    background[i][j].render(gc, this);
            }
        }

        //player
        player.render(gc, this);
        //entities
        entities.forEach(g -> g.render(gc, this));
    }

    public void generate(Entity obj) {
        entities.add(obj);
    }
}
