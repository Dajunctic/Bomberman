package uet.oop.bomberman.others;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.music.Music;

public class Setting extends BaseScene{
    Text_Sc Back =new Text_Sc((int) (BombermanGame.canvas.getWidth()/10*7),(int) (BombermanGame.canvas.getHeight()/10*7),"BACK");

    Text_Sc volum = new Text_Sc((int) (BombermanGame.canvas.getWidth()/10*2),(int) (BombermanGame.canvas.getHeight()/10*3),"VOLUM");
    Slider volum_= new Slider(0,100,100);

    public Setting() {
        try
        {this.getScence().getStylesheets().add(getClass().getResource("/css/text.css").toURI().toString());}
        catch (Exception e){
            System.out.println("KKk");
        }
        this.getRoot().getChildren().add(Back.getText());
        this.getRoot().getChildren().add(volum.getText());
        volum_.setLayoutX((int) (BombermanGame.canvas.getWidth()/10*5));
        volum_.setLayoutY((int) (BombermanGame.canvas.getHeight()/10*3.3));

        this.getRoot().getChildren().add(volum_);
        volum_.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                Music.setVolumm_((Double) newValue);

            }
        });

    }
}
