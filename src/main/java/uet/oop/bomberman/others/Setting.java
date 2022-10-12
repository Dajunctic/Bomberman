package uet.oop.bomberman.others;

public class Setting extends BaseScene{
    Text_Sc Back =new Text_Sc(700,550,"BACK");
    public Setting() {this.getScence().getStylesheets().add("/text.css");
        this.getRoot().getChildren().add(Back.getText());
    }
}
