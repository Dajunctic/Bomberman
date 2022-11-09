package uet.oop.bomberman.menu.items;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.menu.Menu;
import uet.oop.bomberman.menu.MenuItemPage;
import uet.oop.bomberman.music.Sound;

import java.util.Objects;

import static uet.oop.bomberman.game.BombermanGame.scene;

public class Options extends MenuItemPage {
    Image bg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/options.png")));
    Image mark = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/mark.png")));
    Image opt = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/opt.png")));

    public static final int SOUND = 0;
    public static final int MUSIC = 1;


    int soundValue = 50;
    int musicValue = 50;

    int currentOptions = 0;

    @Override
    public void update(Menu menu) {
        interact(menu);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(bg, 0, 0);
        gc.drawImage(opt, 180, 302 + currentOptions * 85);

        gc.drawImage(mark, 671 + soundValue * 4.9, 307);
        gc.drawImage(mark, 671 + musicValue * 4.9, 386);
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
                    case SPACE -> {
                        menu.setPage(Menu.MAIN);
                    }
                    case UP, DOWN -> {
                        currentOptions ++;
                        currentOptions %= 2;
                    }

                    case RIGHT -> {
                        if (currentOptions == SOUND) {
                            soundValue += Math.min(100 - soundValue, 2);
                            Sound.ratio = soundValue / 100.0;
                        } else {
                            musicValue += Math.min(100 - musicValue, 2);
                        }
                    }

                    case LEFT -> {
                        if (currentOptions == SOUND) {
                            soundValue -= Math.min(soundValue, 2);
                            Sound.ratio = soundValue / 100.0;
                        } else {
                            musicValue -= Math.min(musicValue, 2);
                        }
                    }
                }
            }
        });
    }
}
