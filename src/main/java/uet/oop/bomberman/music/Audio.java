package uet.oop.bomberman.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;

import static uet.oop.bomberman.music.Sound.*;

public class Audio {
    //srcs Media
    public static MediaPlayer background_music;
    public static MediaPlayer gameplay;
    public static MediaPlayer fatal;
    public static MediaPlayer dead;
    public static MediaPlayer buff;
    public static MediaPlayer dodge;
    public static MediaPlayer place_bomb;
    public static MediaPlayer heal;
    public static MediaPlayer invisible;
    public static MediaPlayer shooting_fire;
    public static MediaPlayer nuke;
    public static MediaPlayer nuke_explosion;
    public static MediaPlayer bomb;
    public static MediaPlayer bomb_explosion;
    public static MediaPlayer flame;
    public static MediaPlayer fire;
    public static MediaPlayer enemy_dead;

    public Audio() {
        try {
            background_music = new MediaPlayer(new Media(getClass().getResource(_background_music).toURI().toString()));
            gameplay = new MediaPlayer(new Media(getClass().getResource(_gameplay).toURI().toString()));
            fatal =  new MediaPlayer(new Media(getClass().getResource(_fatal).toURI().toString()));
            dead =  new MediaPlayer(new Media(getClass().getResource(_dead).toURI().toString()));
            buff =  new MediaPlayer(new Media(getClass().getResource(_buff).toURI().toString()));
            dodge =  new MediaPlayer(new Media(getClass().getResource(_dodge).toURI().toString()));
            place_bomb =  new MediaPlayer(new Media(getClass().getResource(_place_bomb).toURI().toString()));
            heal =  new MediaPlayer(new Media(getClass().getResource(_heal).toURI().toString()));
            invisible =  new MediaPlayer(new Media(getClass().getResource(_invisible).toURI().toString()));
            shooting_fire =  new MediaPlayer(new Media(getClass().getResource(_shooting_fire).toURI().toString()));
            nuke =  new MediaPlayer(new Media(getClass().getResource(_nuke).toURI().toString()));
            nuke_explosion =  new MediaPlayer(new Media(getClass().getResource(_nuke_explosion).toURI().toString()));
            bomb =  new MediaPlayer(new Media(getClass().getResource(_bomb).toURI().toString()));
            bomb_explosion =  new MediaPlayer(new Media(getClass().getResource(_bomb_explosion).toURI().toString()));
            flame =  new MediaPlayer(new Media(getClass().getResource(_flame).toURI().toString()));
            fire =  new MediaPlayer(new Media(getClass().getResource(_fire).toURI().toString()));
            enemy_dead =  new MediaPlayer(new Media(getClass().getResource(_enemy_dead).toURI().toString()));
        } catch (URISyntaxException e) {
            System.err.println("Load sound failed");
        }
        finally {
            System.out.println("Sound loaded");
        }
    }
    public static MediaPlayer copy(MediaPlayer x) {
        return new MediaPlayer(x.getMedia());
    }

    public static void start(MediaPlayer inp) {
                inp.setVolume(ratio);
                inp.seek(Duration.millis(100));
                inp.play();
    }
}
