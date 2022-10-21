package uet.oop.bomberman.music;

import java.net.URISyntaxException;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;


public class Sound {
        private  static  double volumm_=0;
        public static  MediaPlayer mediaStage = new MediaPlayer(new Media(Sound.class.getResource("/sound/Background/background.mp3").toURI().toString()));
       public static MediaPlayer start;
       public static MediaPlayer flash;
      public static  MediaPlayer dead;
      public static  MediaPlayer getitem;
      public static MediaPlayer play;
      public static  MediaPlayer gameover;
      public static MediaPlayer putbomb;
      private double volumeRate = 1;
    public Sound() {
        //init sound effect

        // For example
        try {
            mediaStage = new MediaPlayer(new Media(getClass().getResource("/sound/Background/background.mp3").toURI().toString()));
            dead=new MediaPlayer(new Media(getClass().getResource("/sound/dead2.wav").toURI().toString()));
            flash=new MediaPlayer(new Media(getClass().getResource("/sound/flash.wav").toURI().toString()));
            getitem=new MediaPlayer(new Media(getClass().getResource("/sound/getitem.wav").toURI().toString()));
            start=new MediaPlayer(new Media(getClass().getResource("/sound/Stage 1 Starting.mp3").toURI().toString()));
            play =new MediaPlayer(new Media(getClass().getResource("/sound/Stage Theme.mp3").toURI().toString()));
            gameover=new MediaPlayer(new Media(getClass().getResource("/sound/Game Over .mp3").toURI().toString()));
            putbomb=new MediaPlayer(new Media(getClass().getResource("/sound/putbomb2.mp3").toURI().toString()));
            //mediaPlayerList.add(mediaStage);
            Sound.setVolumm_(0);
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
        Sound.volumm_ = volumm_;
    }
}