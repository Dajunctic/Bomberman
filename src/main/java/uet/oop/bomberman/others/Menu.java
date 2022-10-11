package uet.oop.bomberman.others;

import javafx.scene.Group;
import javafx.scene.Scene;



public class Menu extends BaseScene{
    Text_Sc Play =new Text_Sc(300,200,"PLAY");
    Text_Sc Highscore=new Text_Sc(300,300,"HIGHSCORE");
    Text_Sc Setting=new Text_Sc(300,400,"SETTING");
    Text_Sc Exit=new Text_Sc(300,500,"EXIT");
   public Menu(Scene scence, Group root) {
        super(scence, root);
    }
    public Menu(){
        this.getScence().getStylesheets().add("/text.css");
        this.getRoot().getChildren().add(Play.getText());
        this.getRoot().getChildren().add(Highscore.getText());
        this.getRoot().getChildren().add(Setting.getText());
        this.getRoot().getChildren().add(Exit.getText());
    }







}
