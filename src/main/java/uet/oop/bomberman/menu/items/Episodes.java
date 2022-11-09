package uet.oop.bomberman.menu.items;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.menu.Menu;
import uet.oop.bomberman.menu.MenuItemPage;

import java.util.Objects;

import static uet.oop.bomberman.game.BombermanGame.scene;

public class Episodes extends MenuItemPage {

    Image bg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/episodes.png")));

    public Episodes() {}

    @Override
    public void update(Menu menu) {
        interact(menu);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(bg, 0, 0);
    }

    @Override
    public void interact(Menu menu) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                this.handleEvent(keyEvent);
            }

            private void handleEvent(KeyEvent keyEvent) {

                if (keyEvent.getCode() == KeyCode.SPACE) {
                    menu.setPage(Menu.MAIN);
                }
            }
        });
    }

}
