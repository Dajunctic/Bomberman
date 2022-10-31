package uet.oop.bomberman.others;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uet.oop.bomberman.game.BombermanGame;

public class Pause extends BaseScene{
    Text_Sc Back =new Text_Sc((int) (BombermanGame.canvas.getWidth()/10*7),(int) (BombermanGame.canvas.getHeight()/10*7),"BACK");

    Text_Sc continuepause =new Text_Sc(BombermanGame.canvas.getWidth()/2-100,BombermanGame.canvas.getHeight()/10*3,"CONTINUE");;
    Text_Sc settingpause =new Text_Sc(BombermanGame.canvas.getWidth()/2-100,BombermanGame.canvas.getHeight()/10*4.5,"SETTING");;

    public Pause() {
        try
        {   this.getRoot().getChildren().add(Back.getText());
            this.getRoot().getChildren().add(continuepause.getText());
            this.getRoot().getChildren().add(settingpause.getText());
            this.getScene().getStylesheets().add(getClass().getResource("/css/text.css").toURI().toString());
        }
            catch (Exception e){
            System.out.println("KKk");
        }

    }
}
