package uet.oop.bomberman.game;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Floor;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** object handler */
public class Play {

    public static int width;
    public static int height;
    protected BufferedReader sourceMap;
    protected List<Entity> entities = new ArrayList<>();
    Entity[][] background;
    public static String[] map;

    public static char[][] tile_map;

    public static double translate_x = 0;
    public static double translate_y = 0;

    public Play() {
    }

    /** make map from file*/
    public void importing (String path) throws IOException {
        try {
            // reading files
            Reader reader = new FileReader(path);
            sourceMap = new BufferedReader(reader);

            //map size intializer
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
            System.out.println(map[height - 1]);
            String ref = new String(sourceMap.readLine());

                String[] info = ref.split(" ");
                switch (info[0]) {
                    case "player":{
                        System.out.println("Bomber in:"  + info[1] + " " + info[2]);
                        entities.add(new Bomber(Integer.parseInt(info[1]) * Sprite.SCALED_SIZE,Integer.parseInt(info[2]) * Sprite.SCALED_SIZE,"/sprites/Player/Model"));
                        break;
                    }
                }

        } finally {
            if(sourceMap != null)
                sourceMap.close();
        }
        createMap();
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
                System.out.print(i + " " + j + " " + background[i][j].getClass() + " ");
                tile_map[i][j] = map[i].charAt(j);
            }

        }
    }

    //debug
    public static void main(String[] args) throws IOException {
        Play test = new Play();
        test.importing("src/main/resources/maps/sandbox_map.txt");
    }

    /** update */
    public void update(){
        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
            if (!entities.get(i).exist()) {
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
        for(int i = low_y; i <= Math.min(BombermanGame.HEIGHT - 1,low_y + BombermanGame.HEIGHT); i ++) {
            for (int j = low_x; j <= Math.min(BombermanGame.WIDTH - 1,low_x + BombermanGame.WIDTH); j++){
                if(background[i][j] != null)
                    background[i][j].render(gc);
            }
        }

        //entities
        entities.forEach(g -> g.render(gc));
    }
}
