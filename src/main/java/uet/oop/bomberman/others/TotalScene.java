package uet.oop.bomberman.others;

import javafx.application.Platform;
import javafx.stage.Stage;
import uet.oop.bomberman.game.BombermanGame;
//khoi tao cac sence de thuc hien chuyen scene
public class TotalScene {
    private Menu menu ;
    private Pause pause ;
    private Setting setting ;
    private Highscore highscore ;

    public TotalScene() {
        menu =new Menu();
        pause =new Pause();
        setting =new Setting();
        highscore =new Highscore();
    }
    //xu ly cac su kien bam chuot
    public void update(Stage stage){
        menu.Play.getText().setOnMouseClicked(event -> {
            stage.setScene(BombermanGame.scene);
            System.out.println("KK");
        });
        menu.Highscore.getText().setOnMouseClicked(event -> {
            stage.setScene(highscore.getScence());

        });
        menu.Setting.getText().setOnMouseClicked(event -> {
            stage.setScene(setting.getScence());

        });
        menu.Exit.getText().setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);

        });
        highscore.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(menu.getScence());

        });
        setting.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(menu.getScence());

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
