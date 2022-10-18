package uet.oop.bomberman.music;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import uet.oop.bomberman.others.Menu;


public class Music {
        private  static  double volumm_=0;
        public static  MediaPlayer mediaStage;
       public static MediaPlayer start;
       public static MediaPlayer flash;
      public static  MediaPlayer dead;
      public static  MediaPlayer getitem;
      public static MediaPlayer play;
      public static  MediaPlayer gameover;
      public static MediaPlayer putbomb;


    public Music() {
        //init sound effect

        // For example
        try {
            mediaStage = new MediaPlayer(new Media(getClass().getResource("/music/Bomberman (NES) - Soundtrack Piano Cover.mp3").toURI().toString()));
            dead=new MediaPlayer(new Media(getClass().getResource("/music/dead2.wav").toURI().toString()));
            flash=new MediaPlayer(new Media(getClass().getResource("/music/flash.wav").toURI().toString()));
            getitem=new MediaPlayer(new Media(getClass().getResource("/music/getitem.wav").toURI().toString()));
            start=new MediaPlayer(new Media(getClass().getResource("/music/Stage 1 Starting.mp3").toURI().toString()));
            play =new MediaPlayer(new Media(getClass().getResource("/music/Stage Theme.mp3").toURI().toString()));
            gameover=new MediaPlayer(new Media(getClass().getResource("/music/Game Over .mp3").toURI().toString()));
            putbomb=new MediaPlayer(new Media(getClass().getResource("/music/putbomb2.mp3").toURI().toString()));
            //mediaPlayerList.add(mediaStage);
            Music.setVolumm_(0);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mediaStage.setVolume(volumm_/100);

    }

    public static double getVolumm_() {
        return volumm_;
    }

    public static void setVolumm_(double volumm_) {
        mediaStage.setVolume(volumm_);
        dead.setVolume(volumm_);
        start.setVolume(volumm_);
        dead.setVolume(volumm_);
        putbomb.setVolume(volumm_);
        play.setVolume(volumm_);
        gameover.setVolume(volumm_);
        getitem.setVolume(volumm_);
        flash.setVolume(volumm_);
        Music.volumm_ = volumm_;
    }
}