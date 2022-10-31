package uet.oop.bomberman.music;

import java.net.URISyntaxException;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;


public class Music {
        private  static  double volumm_=1;
        public static  MediaPlayer mediaStage;
       public static MediaPlayer start;
       public static MediaPlayer flash;
      public static  MediaPlayer dead;
      public static  MediaPlayer getitem;
      public static MediaPlayer play;
      public static  MediaPlayer gameover;
      public static MediaPlayer putbomb;
      private double volumeRate = 1;
    public Music() {
        //init sound effect

        // For example
        try {
            mediaStage = new MediaPlayer(new Media( getClass().getResource("/sound/Background/background.mp3").toURI().toString()));
            dead=new MediaPlayer(new Media( getClass().getResource("/sound/Background/background.mp3").toURI().toString()));
            flash=new MediaPlayer(new Media(getClass().getResource("/sound/Background/background.mp3").toURI().toString()));
            getitem=new MediaPlayer(new Media(getClass().getResource("/sound/Player/Status/dead.wav").toURI().toString()));
            start=new MediaPlayer(new Media(getClass().getResource("/sound/Gameplay/1/Stage 1 Starting.mp3").toURI().toString()));
            play =new MediaPlayer(new Media(getClass().getResource("/sound/Player/Status/dead.wav").toURI().toString()));
            gameover=new MediaPlayer(new Media(getClass().getResource("/sound/Player/Status/dead.wav").toURI().toString()));
            putbomb=new MediaPlayer(new Media(getClass().getResource("/sound/Player/Status/dead.wav").toURI().toString()));
            //mediaPlayerList.add(mediaStage);
            Music.setVolumm_(1);
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