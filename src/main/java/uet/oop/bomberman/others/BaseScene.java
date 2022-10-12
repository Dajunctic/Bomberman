package uet.oop.bomberman.others;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.graphics.Sprite;

import java.net.URISyntaxException;
//1 scene co 1 scene 1 root
public class BaseScene {
    private Scene scence;
    private Group root = new Group();
    private Image background;
    private ImageView background_;
    public BaseScene() {
        try {

            background = new Image(getClass().getResource("/sprites/menu/menubackground.png").toURI().toString());
            background_=new ImageView(background);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.root.getChildren().add(background_);
        scence=new Scene(root,960,672);
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
