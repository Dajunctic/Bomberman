package uet.oop.bomberman.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;

import static uet.oop.bomberman.music.Sound.*;

public class Audio implements Runnable {
    //srcs Media
    public static Media background_music;
    public static Media button;
    public static Media gameplay;
    public static Media fatal;
    public static Media dead;
    public static Media buff;
    public static Media dodge;
    public static Media place_bomb;
    public static Media heal;
    public static Media invisible;
    public static Media shooting_fire;
    public static Media nuke;
    public static Media nuke_explosion;
    public static Media bomb;
    public static Media bomb_explosion;
    public static Media flame;
    public static Media fire;
    public static Media enemy_dead;
    public static Media balloon_attack;

    public Audio() {
        try {
            balloon_attack = new Media(getClass().getResource(_balloon_attack).toURI().toString());
            background_music = new Media(getClass().getResource(_background_music).toURI().toString());
            button = new Media(getClass().getResource(_button).toURI().toString());
            gameplay = new Media(getClass().getResource(_gameplay).toURI().toString());
            fatal =  new Media(getClass().getResource(_fatal).toURI().toString());
            dead =  new Media(getClass().getResource(_dead).toURI().toString());
            buff =  new Media(getClass().getResource(_buff).toURI().toString());
            dodge =  new Media(getClass().getResource(_dodge).toURI().toString());
            place_bomb =  new Media(getClass().getResource(_place_bomb).toURI().toString());
            heal =  new Media(getClass().getResource(_heal).toURI().toString());
            invisible =  new Media(getClass().getResource(_invisible).toURI().toString());
            shooting_fire =  new Media(getClass().getResource(_shooting_fire).toURI().toString());
            nuke =  new Media(getClass().getResource(_nuke).toURI().toString());
            nuke_explosion =  new Media(getClass().getResource(_nuke_explosion).toURI().toString());
            bomb =  new Media(getClass().getResource(_bomb).toURI().toString());
            bomb_explosion =  new Media(getClass().getResource(_bomb_explosion).toURI().toString());
            flame =  new Media(getClass().getResource(_flame).toURI().toString());
            fire =  new Media(getClass().getResource(_fire).toURI().toString());
            enemy_dead =  new Media(getClass().getResource(_enemy_dead).toURI().toString());
        } catch (URISyntaxException e) {
            System.err.println("Load sound failed");
        }
        finally {
            System.out.println("Sound loaded");
        }
    }
    public static MediaPlayer copy(Media x) {
        return new MediaPlayer(x);
    }

    public static void start(MediaPlayer inp) {
                inp.setVolume(ratio);
                inp.seek(Duration.millis(100));
                inp.play();
    }

    @Override
    public void run() {

    }
}
