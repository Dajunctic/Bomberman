package uet.oop.bomberman.menu;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.menu.items.Character;
import uet.oop.bomberman.menu.items.Episodes;
import uet.oop.bomberman.menu.items.Options;

import java.util.Objects;

import static uet.oop.bomberman.game.BombermanGame.scene;

public class Menu{
    Image bg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/bg.png")));
    Image items = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/items.png")));
    Image activeHover = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menu/active_hover.png")));

    public final static int MAIN = 0;
    public final static int EPISODES = 1;
    public final static int OPTIONS = 2;
    public final static int QUIT = 3;
    public final static int CHARACTER = 4;
    public final static int PLAY = 5;

    public int page = MAIN;
    public int currentActive = MAIN;

    public Episodes episodes = new Episodes();
    public Options options = new Options();
    public Character character = new Character();


    public Menu() {}

    public void update() {
        if (page == MAIN) {
            interact();
        } else if (page == EPISODES) {
            episodes.update(this);
        } else if (page == OPTIONS) {
            options.update(this);
        } else if (page == CHARACTER) {
            character.update(this);
        }

    }

    public void render(GraphicsContext gc) {
        if (page == MAIN) {
            gc.drawImage(bg, 0, 0);
            gc.drawImage(activeHover, 53, 381 + currentActive * 82);
            gc.drawImage(items, 0 , 0);

        } else if (page == CHARACTER) {
            character.render(gc);
        } else if (page == EPISODES) {
            episodes.render(gc);
        } else if (page == OPTIONS) {
            options.render(gc);
        }
    }

    public void interact() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                this.handleEvent(keyEvent);
            }

            private void handleEvent(KeyEvent keyEvent) {

                switch (keyEvent.getCode()) {
                    case UP -> {
                        currentActive += 3;
                        currentActive %= 4;
                    }
                    case DOWN -> {
                        currentActive += 1;
                        currentActive %= 4;
                    }
                    case ENTER -> {

                        if (currentActive == MAIN) {
                            page = CHARACTER;
                        } else {
                            page = currentActive;
                        }

                        if (page == QUIT) {
                            Platform.exit();
                            System.exit(0);
                        }
                    }
                }
            }
        });
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
