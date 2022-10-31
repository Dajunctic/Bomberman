package uet.oop.bomberman.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.others.Basic;

import java.net.URISyntaxException;

public class Sound {

    private long createTime;
    private long duration;
    //sources
    public static String _background_music = "/sound/Background/background.mp3";
    public static String _button = "/sound/Background/button.mp3";
    public static String _gameplay = "/sound/Background/sound_game.mp3";
    //player status audio
    public static String _fatal = "/sound/Player/Status/fatal.mp3";
    public static String _dead = "/sound/Player/Status/dead.wav";
    //player abilities audio
    public static String _buff = "/sound/Player/Abilities/buff.mp3";
    public static String _dodge = "/sound/Player/Abilities/dodge.wav";
    public static String _place_bomb = "/sound/Player/Abilities/bombing.mp3";
    public static String _heal = "/sound/Player/Abilities/heal.mp3";
    public static String _invisible = "/sound/Player/Abilities/invisible.mp3";
    public static String _shooting_fire = "/sound/Player/Abilities/shooting_fire.mp3";
    public static String _nuke = "/sound/Player/Entities/nuke.mp3";
    public static String _nuke_explosion = "/sound/Player/Entities/nuke_explosion_final.mp3";
    //bomb related audio
    public static String _bomb = "/sound/Player/Entities/bomb.mp3";
    public static String _bomb_explosion = "/sound/Player/Entities/bomb_explosion.mp3";
    public static String _flame = "/sound/Player/Entities/flame.mp3";
    public static String _fire = "/sound/Player/Entities/fire.mp3";
    //enemy
    public static String _enemy_dead = "/sound/Enemy/dead.mp3";
    public static String _balloon_attack = "/sound/Enemy/balloon_attack.mp3";



    //properties
    public static double ratio = 1;
    public static double threshold = 8 * Sprite.SCALED_SIZE;
    private double impact = threshold;
    protected MediaPlayer audio;
    protected Vertex position;
    protected Vertex balance = new Vertex(0,1);
    protected boolean isPlaying = true;
    protected boolean replayable = false;
    public Sound(double x, double y, String path, double durationm, double impact) throws URISyntaxException {
        position = new Vertex(x, y);
        audio = new MediaPlayer(new Media( getClass().getResource(path).toURI().toString()));
        audio.setAutoPlay(true);
        this.duration =  (long)(duration * 1000);
        if(duration < 0) this.duration = (long) audio.getStopTime().toMillis();
        createTime = System.currentTimeMillis();
        audio.setCycleCount(MediaPlayer.INDEFINITE);
        this.impact = impact * Sprite.SCALED_SIZE;
    }
    public Sound(double x, double y, MediaPlayer audio, double duration, double impact)  {
        position = new Vertex(x, y);
        this.audio = audio;
        audio.setAutoPlay(true);
        this.duration =  (long)(duration * 1000);
        if(duration < 0) this.duration = (long) audio.getStopTime().toMillis();
        createTime = System.currentTimeMillis();
        audio.setCycleCount(MediaPlayer.INDEFINITE);
        this.impact = impact;
    }

    public Sound(double x, double y, MediaPlayer audio, double duration, double impact, boolean replayable)  {
        position = new Vertex(x, y);
        this.audio = audio;
        audio.setAutoPlay(true);
        this.duration =  (long)(duration * 1000);
        if(duration < 0) this.duration = (long) audio.getStopTime().toMillis();
        createTime = System.currentTimeMillis();
        audio.setCycleCount(MediaPlayer.INDEFINITE);
        this.impact = impact;
        this.replayable = replayable;
    }
    public void update(Bomber player) {
        balance.set(player.getX() - position.getX(), player.getY() - position.getY());
        if(balance.abs() > threshold ) {
            if(isPlaying) {
                audio.pause();
                isPlaying = false;
            }
            return;
        }
        if(!isPlaying) {
            audio.play();
            isPlaying = true;
        }
        double dis = balance.abs();
        double volume = Basic.mapping(0, impact, 1, 0, balance.abs()) * ratio;
        balance.normalize();
        audio.setBalance(-balance.getX() * (dis / threshold));
        audio.setVolume(volume);
    }
    public boolean exists() {
        return System.currentTimeMillis() - createTime <= duration;
    }

    public MediaPlayer getAudio() {
        return audio;
    }
    public void stop() {
        audio.stop();
    }

    public void start() {
        Audio.start(audio);
    }
}
