package Map.generator.Generators;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.game.Gameplay;

import java.io.IOException;

public class Proccessor extends Gameplay {

    public static int zoneX = Main.width - 3;
    public static int zoneY = Main.height - 3;
    public Proccessor(int width, int height) {
         tile_map = new int[height][width];
         background = new Entity[height][width];
    }
    public Proccessor(String path) {
        try{
            importing(path);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}