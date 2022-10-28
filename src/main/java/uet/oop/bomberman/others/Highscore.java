package uet.oop.bomberman.others;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.music.Music;

import java.util.ArrayList;
import java.util.List;

public class Highscore extends BaseScene{
    Text_Sc Back =new Text_Sc((int) (BombermanGame.canvas.getWidth()/10*8),(int) (BombermanGame.canvas.getHeight()/10*8),"BACK");
    Text highscoretext =new Text((int) (BombermanGame.canvas.getWidth()/10*4),(int) (BombermanGame.canvas.getHeight()/10*3),"HIGHSCORE");

    //khoi taoj list text de css
    List<Text> listText =new ArrayList<>();
    public Highscore() {
        try
        {this.getScene().getStylesheets().add(getClass().getResource("/css/text.css").toURI().toString());
            for(int i=1;i<=5;i++){
                Text Topi= new Text((int)( BombermanGame.canvas.getWidth()/10*1.5),(int) (BombermanGame.canvas.getHeight()/10*(3+i)),
                        String.format("TOP %d",i));

                Topi.getStyleClass().add("Text");
                listText.add(Topi);
            }
        }
        catch (Exception e){
            System.out.println("KKk");

        }
        this.getRoot().getChildren().add(Back.getText());
        highscoretext.getStyleClass().clear();

        highscoretext.getStyleClass().add("Text");
        listText.add(highscoretext);
        for (Text i:
             listText) {
            try{Font font = Font.loadFont(getClass().getResource("/PhoenixGaming-nRJj0.ttf").toURI().toString(), 40);
            i.setFont(font);
            this.getRoot().getChildren().add(i);}
            catch (Exception e){
                System.out.println("bug doc font");
            }
        }
    }
}
