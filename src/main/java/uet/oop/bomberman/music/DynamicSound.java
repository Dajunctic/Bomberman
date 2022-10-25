package uet.oop.bomberman.music;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.generals.Vertex;

import java.net.URISyntaxException;

import static uet.oop.bomberman.game.BombermanGame.FPS;

public class DynamicSound extends Sound{
    private Vertex speed;
    private double interval = 1;
    private Vertex goal = new Vertex(0,0);
    private Mobile src = null;
    public DynamicSound(double x, double y, String path, double duration) throws URISyntaxException {
        super(x, y, path, duration);
        speed = new Vertex(0, 0);
    }

    public DynamicSound(double x, double y, String path, double duration, Mobile src) throws URISyntaxException {
        super(x, y, path, duration);
        speed = new Vertex(0, 0);
        this.src = src;
    }

    public DynamicSound(double x, double y, MediaPlayer audio, double duration, Mobile src) {
        super(x, y, audio, duration);
        this.src = src;
    }
    public DynamicSound(double x, double y, MediaPlayer audio, double duration, Mobile src, double interval) {
        super(x, y, audio, duration);
        this.src = src;
        this.interval = interval;
    }
    public void move() {
        if(src != null) {
            position.set(src.getX(), src.getY());
            return ;
        }
        if(goal.abs() == 0 || position.distance(goal) <= 10) return;
        position.add(speed);
    }
    public void setGoal(double x, double y) {
        goal.set(x, y);
        speed = new Vertex(position, goal);
        speed.divide(interval * FPS);
    }

    @Override
    public void update(Bomber player) {
        move();
        super.update(player);
    }
}
