package uet.oop.bomberman.others;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import uet.oop.bomberman.game.BombermanGame;


import java.net.URISyntaxException;
//1 scene co 1 scene 1 root
public class BaseScene {
    private Scene scene;
    private Group root = new Group();

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
        this.root.getChildren().add(background_);
        this.root.getChildren().add(logo);

        scene =new Scene(root,BombermanGame.canvas.getWidth(),BombermanGame.canvas.getHeight());


    }

    public BaseScene(Scene scene, Group root) {
        this.scene = scene;
        this.root = root;
    }

    public Scene getScene() {

        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }
}
