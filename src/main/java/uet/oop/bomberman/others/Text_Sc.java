package uet.oop.bomberman.others;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import uet.oop.bomberman.entities.MenuSprite;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;

public class Text_Sc{
    public static void test(){
    Text text= new Text();


    //Setting font to the text
    text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

    //setting the position of the text
    text.setX(50);
    text.setY(130);

    //Setting the text to be added.
    text.setText("Hi how are you");

    //Creating a Group object
    BombermanGame.root.getChildren().add(text);}
}