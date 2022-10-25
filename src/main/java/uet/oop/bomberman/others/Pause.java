package uet.oop.bomberman.others;

import uet.oop.bomberman.game.BombermanGame;

public class Pause extends BaseScene{
    Text_Sc Back =new Text_Sc((int) (BombermanGame.canvas.getWidth()/10*7),(int) (BombermanGame.canvas.getHeight()/10*7),"BACK");

    public Pause() {
        try
        {this.getScene().getStylesheets().add(getClass().getResource("/css/text.css").toURI().toString());}
        catch (Exception e){
            System.out.println("KKk");
        }
        this.getRoot().getChildren().add(Back);
    }
}
