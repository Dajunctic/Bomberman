package uet.oop.bomberman.others;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uet.oop.bomberman.game.BombermanGame;

public class ChoosePlayer extends BaseScene{
    Text_Sc Back =new Text_Sc((int) (BombermanGame.canvas.getWidth()/10*7),(int) (BombermanGame.canvas.getHeight()/10*7),"BACK");

    private ImageView player1;
    private ImageView player2;

    protected ImageView loadingscreen;
    Text_Sc playerBt1 =new Text_Sc((int) (BombermanGame.canvas.getWidth()/10*2.2),(int) (BombermanGame.canvas.getHeight()/10*5.5),"PLAY ");
    Text_Sc playerBt2 =new Text_Sc((int) (BombermanGame.canvas.getWidth()/10*6.2),(int) (BombermanGame.canvas.getHeight()/10*5.5),"PLAY");
    public ChoosePlayer() {
        try
        {this.getScene().getStylesheets().add(getClass().getResource("/css/text.css").toURI().toString());
            loadingscreen=new ImageView(new Image(getClass().getResource("/sprites/menu/loading_desktop_by_brianmccumber-d41z4h6.gif").toURI().toString()));
            loadingscreen.setFitWidth(BombermanGame.canvas.getWidth());
            loadingscreen.setFitHeight(BombermanGame.canvas.getHeight());

            player1=new ImageView(new Image(getClass().getResource("/sprites/menu/player1.png").toURI().toString()));
            player2=new ImageView(new Image(getClass().getResource("/sprites/menu/player2.png").toURI().toString()));
            player1.setFitHeight(48);
            player1.setFitWidth(48);
            player2.setFitHeight(48);
            player2.setFitWidth(48);
            player1.setX(BombermanGame.canvas.getWidth()/10*3);
            player1.setY(BombermanGame.canvas.getHeight()/10*4);
            player2.setX(BombermanGame.canvas.getWidth()/10*7);
            player2.setY(BombermanGame.canvas.getHeight()/10*4);

        }
        catch (Exception e){
            System.out.println("KKk");
        }
        //add button
        this.getRoot().getChildren().add(Back.getText());
        this.getRoot().getChildren().add(playerBt1.getText());
        this.getRoot().getChildren().add(playerBt2.getText());
        //add 2 imange
        this.getRoot().getChildren().add(player1);
        this.getRoot().getChildren().add(player2);
        //add loading screen
        this.getRoot().getChildren().add(loadingscreen);
        loadingscreen.toBack();
    }
}
