package uet.oop.bomberman.others;

import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.music.Audio;

//khoi tao cac sence de thuc hien chuyen scene
public class TotalScene {

    private Menu menu ;
    private Pause pause ;
    private Setting setting ;
    private Highscore highscore ;
    private ChoosePlayer choosePlayer;
    private final static MediaPlayer button = new MediaPlayer(Audio.button);

    public TotalScene() {

        menu =new Menu();
        pause =new Pause();
        setting =new Setting();
        highscore =new Highscore();
        choosePlayer =new ChoosePlayer();

    }
    //xu ly cac su kien bam chuot
    public void update(Stage stage) {
        menu.Play.getText().setOnMouseClicked(event -> {
            stage.setScene(choosePlayer.getScene());
            Audio.start(button);
        });
        menu.Highscore.getText().setOnMouseClicked(event -> {
            stage.setScene(highscore.getScene());
            Audio.start(button);
        });
        menu.Setting.getText().setOnMouseClicked(event -> {
            stage.setScene(setting.getScene());
            Audio.start(button);
        });
        menu.Exit.getText().setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        highscore.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(menu.getScene());
            Audio.start(button);
        });
        setting.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(menu.getScene());
            Audio.start(button);
        });
        choosePlayer.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(menu.getScene());
            Audio.start(button);
        });
        choosePlayer.playerBt1.getText().setOnMouseClicked(event -> {
            stage.setScene(BombermanGame.scene);
            BombermanGame.startGame();
            Audio.start(button);
        });
        choosePlayer.playerBt2.getText().setOnMouseClicked(event -> {
            stage.setScene(BombermanGame.scene);
            BombermanGame.startGame();
        });
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Pause getPause() {
        return pause;
    }

    public void setPause(Pause pause) {
        this.pause = pause;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public Highscore getHighscore() {
        return highscore;
    }

    public void setHighscore(Highscore highscore) {
        this.highscore = highscore;
    }
}
