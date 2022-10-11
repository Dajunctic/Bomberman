package uet.oop.bomberman.others;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class Text_Sc extends ScrollPane {
    protected Button text;


    public Text_Sc(int X, int Y, String string_) {
        text= new Button();
        text.setLayoutX(X);
        text.setLayoutY(Y);
        text.setText(string_);

        text.getStyleClass().add("button");
        try {Font font = Font.loadFont(getClass().getResource("/PhoenixGaming-nRJj0.ttf").toURI().toString(), 40);
            text.setFont(font);
        }
        catch(Exception e){
            System.out.println("JHj");
        }



    }

    public Button getText() {
        return text;
    }

    public void setText(Button text) {
        this.text = text;
    }


}