package uet.oop.bomberman.others;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import uet.oop.bomberman.game.BombermanGame;

//khoi tao cac sence de thuc hien chuyen scene
public class TotalScene {
    private ImageView pausebt=new ImageView();
    private Group test= new Group(pausebt);
    private Menu menu ;
    private Pause pause ;
    private Setting setting ;
    private Highscore highscore ;
    private ChoosePlayer choosePlayer;

    public TotalScene() {

        menu =new Menu();
        pause =new Pause();
        setting =new Setting();
        highscore =new Highscore();
        choosePlayer =new ChoosePlayer();

    }
    private void setscenceplay(Stage stage){

        try {Image pauseImg=new Image(getClass().getResource("/sprites/menu/Square Buttons/Colored Square Buttons/Pause col_Square Button.png").toURI().toString());
            pausebt =new ImageView(pauseImg);
            pausebt.setX(BombermanGame.canvas.getWidth()-200);
            pausebt.setY(100);
            pausebt.toFront();
            BombermanGame.root.getChildren().add(pausebt);
            System.out.println("KKKKO");
        }
        catch (Exception e){
            System.out.println("KKKK");
        }


    }
    //xu ly cac su kien bam chuot
    public void update(Stage stage) {
        setscenceplay(stage);
        // set cho menu
        pausebt.setOnMouseClicked(event -> {
                        stage.setScene(pause.getScene());
            System.out.println("KKK");
                    });
        menu.Play.getText().setOnMouseClicked(event -> {
            stage.setScene(choosePlayer.getScene());

        });
        menu.Highscore.getText().setOnMouseClicked(event -> {
            stage.setScene(highscore.getScene());

        });
        menu.Setting.getText().setOnMouseClicked(event -> {
            stage.setScene(setting.getScene());

        });
        menu.Exit.getText().setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);

        });
        //set cho highscore
        highscore.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(menu.getScene());

        });
        //set cho setting
        setting.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(menu.getScene());

        });
        //set cho choose player
        choosePlayer.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(menu.getScene());

        });
        choosePlayer.playerBt1.getText().setOnMouseClicked(event -> {
            stage.setScene(BombermanGame.scene);
            BombermanGame.startGame();

        });
        choosePlayer.playerBt2.getText().setOnMouseClicked(event -> {
            stage.setScene(BombermanGame.scene);
            BombermanGame.startGame();
        });

        //set cho pause
        pause.settingpause.getText().setOnMouseClicked(event -> {
            stage.setScene(setting.getScene());
        });
        pause.continuepause.getText().setOnMouseClicked(event -> {
            stage.setScene(BombermanGame.scene);
        });
        pause.Back.getText().setOnMouseClicked(event -> {
            stage.setScene(BombermanGame.scene);
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
