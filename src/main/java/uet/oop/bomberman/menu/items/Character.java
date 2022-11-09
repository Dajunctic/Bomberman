package uet.oop.bomberman.menu.items;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.menu.Menu;
import uet.oop.bomberman.menu.MenuItemPage;
import uet.oop.bomberman.music.Sound;

import java.util.Objects;

import static uet.oop.bomberman.game.BombermanGame.scene;

public class Character extends MenuItemPage {
    Image bg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/character.png")));

    private Anim[][] anims = Bomber.anims;
    int currentChoice = 6;
    int scale = 4;

    public Character() {

    }

    @Override
    public void update(Menu menu) {
        interact(menu);
        anims[currentChoice][Bomber.IDLE].update();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(bg, 0, 0);

        int width = (int) anims[currentChoice][Bomber.IDLE].getImage().getWidth();
        int height = (int) anims[currentChoice][Bomber.IDLE].getImage().getHeight();
        int x = (1256 - width * scale) / 2;
        int y = (480 - height * scale);

        gc.drawImage(anims[currentChoice][Bomber.IDLE].getImage(), x, y, scale * width, scale * height);
    }

    @Override
    public void interact(Menu menu) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                this.handleEvent(keyEvent);
            }

            private void handleEvent(KeyEvent keyEvent) {

                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        menu.setPage(Menu.PLAY);
                        Bomber.currentCharacter = currentChoice;
                    }

                    case SPACE -> {
                        menu.setPage(Menu.MAIN);
                    }

                    case RIGHT -> {
                        currentChoice ++;
                        currentChoice %= Bomber.NUMCHAR;
                    }

                    case LEFT -> {
                        currentChoice += Bomber.NUMCHAR - 1;
                        currentChoice %= Bomber.NUMCHAR;
                    }
                }
            }
        });
    }
}
