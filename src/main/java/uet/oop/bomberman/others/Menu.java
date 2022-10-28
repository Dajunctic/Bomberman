package uet.oop.bomberman.others;

import javafx.scene.Group;
import javafx.scene.Scene;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.music.Music;


public class Menu extends BaseScene{
    Text_Sc Play =new Text_Sc(BombermanGame.canvas.getWidth()/2-100,BombermanGame.canvas.getHeight()/10*3,"PLAY");
    Text_Sc Highscore=new Text_Sc(BombermanGame.canvas.getWidth()/2-100,BombermanGame.canvas.getHeight()/10*4.5,"HIGHSCORE");
    Text_Sc Setting=new Text_Sc(BombermanGame.canvas.getWidth()/2-100,BombermanGame.canvas.getHeight()/10*6,"SETTING");
    Text_Sc Exit=new Text_Sc(BombermanGame.canvas.getWidth()/2-100,BombermanGame.canvas.getHeight()/10*7.5,"EXIT");
   public Menu(Scene scence, Group root) {
        super(scence, root);
    }
    public Menu(){
        try
        {this.getScene().getStylesheets().add(getClass().getResource("/css/text.css").toURI().toString());}
        catch (Exception e){
            System.out.println("KKk");
        }
        this.getRoot().getChildren().add(Play.getText());
        this.getRoot().getChildren().add(Highscore.getText());
        this.getRoot().getChildren().add(Setting.getText());
        this.getRoot().getChildren().add(Exit.getText());
    }

    @Override
    public Scene getScene() {
        return super.getScene();
    }
}
