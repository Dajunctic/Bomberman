package uet.oop.bomberman.others;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.game.BombermanGame;


import java.io.File;
import java.net.URISyntaxException;
//1 scene co 1 scene 1 root
public class BaseScene {
    private Scene scence;
    private Group root = new Group();
    private int x1=0;
    private int w=5;
    private int x2=w;
    private GraphicsContext gc;
    private Canvas cs=new Canvas(BombermanGame.canvas.getWidth(),BombermanGame.canvas.getHeight());
    private ImageView background_;
    private Image background;
    private ImageView logo;
    public BaseScene() {
        try {

            background=new Image(getClass().getResource("/sprites/menu/bg.png").toURI().toString());
            background_=new ImageView(background);
            background_.setFitWidth(BombermanGame.canvas.getWidth());
            background_.setFitHeight(BombermanGame.canvas.getHeight());
            Image logo_= new Image(getClass().getResource("/sprites/menu/Logo1.png").toURI().toString());
            logo=new ImageView(logo_);


            logo.setX(BombermanGame.canvas.getWidth()/2-logo_.getWidth()/2);
            logo.setY(BombermanGame.canvas.getHeight()/8);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("");
        gc= cs.getGraphicsContext2D();
        this.root.getChildren().add(cs);
        this.root.getChildren().add(background_);
        this.root.getChildren().add(logo);

        scence=new Scene(root,BombermanGame.canvas.getWidth(),BombermanGame.canvas.getHeight());


    }

    public BaseScene(Scene scence, Group root) {
        this.scence = scence;
        this.root = root;
    }

    public Scene getScence() {

        return scence;
    }

    public void setScence(Scene scence) {
        this.scence = scence;
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }
}
