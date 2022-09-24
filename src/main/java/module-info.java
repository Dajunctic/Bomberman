module uet.oop.bomberman {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports uet.oop.bomberman.game;
    opens uet.oop.bomberman.game to javafx.fxml;
}