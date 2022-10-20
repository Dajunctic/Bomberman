module uet.oop.bomberman {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;

    exports uet.oop.bomberman.game;
    opens uet.oop.bomberman.game to javafx.fxml;
    exports uet.oop.bomberman.maps;
    opens uet.oop.bomberman.maps to javafx.fxml;
}