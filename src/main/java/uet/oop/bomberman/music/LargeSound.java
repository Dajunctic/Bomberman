package uet.oop.bomberman.music;

import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.entities.Bomber;

import java.net.URISyntaxException;

public class LargeSound extends Sound{
    public LargeSound(double x, double y, String path, double duration) throws URISyntaxException {
        super(x, y, path, duration);
        audio.setVolume(ratio);
    }
    public LargeSound(double x, double y, MediaPlayer audio, double duration) {
        super(x, y, audio, duration);
    }
    @Override
    public void update(Bomber player) {
        balance.set(player.getX() - position.getX(), player.getY() - position.getX());
        double dis = balance.abs();
        balance.normalize();
        audio.setBalance(-balance.getX() * (dis / threshold));
        if(audio.getCurrentTime().toMillis() == audio.getStopTime().toMillis()) Audio.start(audio);
    }
}
