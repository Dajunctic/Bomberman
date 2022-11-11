package uet.oop.bomberman.game;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.Renderer;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.others.Physics;

import java.util.Objects;

import static uet.oop.bomberman.game.BombermanGame.game_bg;
import static uet.oop.bomberman.game.BombermanGame.scene;
import static uet.oop.bomberman.game.Gameplay.resetSound;

public class Ending extends Entity {

    private Anim npc = new Anim(new SpriteSheet("/sprites/Player/NPC/princess.png", 8), 2);
    private Anim request = new Anim(new SpriteSheet("/sprites/bg/request.png", 3), 2);
    private Anim[][] anims = Bomber.anims;
    private Anim bg = new Anim(new SpriteSheet("/sprites/bg/ending.png", 6), 2);
    private Anim heart = new Anim(new SpriteSheet("/sprites/bg/heart.png", 6), 4);
    private final Image textbox = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/textbox.png")));
    private final Image theme = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/theme.png")));
    private final Image portal1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/portal1.png")));
    private final Image portal2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/portal2.png")));
    private final Image happy = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/bg/happy.png")));

    public static MediaPlayer stage_theme = Audio.copy(Audio.stage_theme);
    static {
        stage_theme.setCycleCount(MediaPlayer.INDEFINITE);
    }

    int NPCX = 106 * 48;
    int NPCY = 90 * 48 + 24;
//    int NPCX = 10 * 48;
//    int NPCY = 48 * 48;

    int npcX = NPCX;
    int npcY = NPCY;

    int speedX = 3;
    int speedY = 0;

    int portalX = npcX + 120 + 487 / 3 + 40;

    public static int NORMAL = 0;
    public static int CONVERSATION = 1;
    public static int PORTAL = 2;
    public static int AUTO = 3;
    public static int END = 4;
    public static int QUIT = 5;

    public static int status = NORMAL;

    int nameX = 393;
    int nameY = 607;

    int textX = 330;
    int textY = 670;

    String current_text = "";
    int current_char = -1;
    int current_line = 0;
    boolean fullLine = false;

    long lastTime = System.currentTimeMillis();
    long textTime = 40; // mili second

    String[] conversation = new String[] {
            "Finally, you reach here!",
            "I have been waiting you for a long time!",
            "You have proved yourself to be a good UET-er!",
            "You do not look like fuckboiz or sadboiz as rumor!",
            "That make me admire you more!",
            "So, you need to follow me right now!",
            "I have an other duty for you!",
            "Come on my SIMP LORD!"
    };

    public Ending() {
        super(0, 0);
    }

    public void update() {npc.update();}

    public void update(Bomber player) {
        npc.update();
        request.update();


        if (status == NORMAL) {
            Rectangle npcRect = new Rectangle(npcX, npcY,
                    npc.getImage().getWidth() / 4.0,
                    npc.getImage().getHeight() / 4.0);

            Rectangle playerRect = new Rectangle(player.getX(), player.getY(),
                    player.getWidth(),
                    player.getHeight());


            if (Physics.collisionRectToRect(npcRect, playerRect)) {
                status = CONVERSATION;

                game_bg.stop();
                stage_theme.play();
                Gameplay.resetSound();
                player.stopSound();
            }
        }

        if (status == CONVERSATION) {
            if (System.currentTimeMillis() - lastTime > textTime) {
                current_char ++;
                if (current_char == conversation[current_line].length()) {
                    fullLine = true;
                }

                if (!fullLine) {

                    char addChar = conversation[current_line].charAt(current_char);

                    current_text = current_text.concat(String.valueOf(addChar));

                    lastTime = System.currentTimeMillis();
                }
            }

        }

        if (status == PORTAL) {
            if (System.currentTimeMillis() - lastTime > 2000) {
                status = AUTO;
                lastTime = System.currentTimeMillis();
            }
        }

        if (status == AUTO) {
            if (System.currentTimeMillis() - lastTime > 1500) {
                anims[Bomber.currentCharacter][Bomber.IDLE].update();

                npcX += speedX;
                npcY += speedY;

                if (npcX > portalX) {
                    status = END;
                }
            }
        }

        if (status == END) {
            bg.update();
            heart.update();
        }

        if (status > NORMAL) interact();
    }

    public void render(GraphicsContext gc, Renderer renderer) {
        if (status == AUTO || status == PORTAL) {

            renderer.renderImg(gc, portal1, portalX - 40 - 487/3.0, npcY - 180, false, 1/3.0);

            renderer.renderImg(gc, npc.getImage(), npcX + shiftX, npcY + shiftY, status != AUTO, 1 / 4.0);

            renderer.renderImg(gc, anims[Bomber.currentCharacter][Bomber.IDLE].getImage(),
                    npcX + shiftX - 50,
                    npcY + shiftY + 16,
                    false, 1.2);

            renderer.renderImg(gc, portal2, portalX - 40, npcY - 180, false, 1/3.0);

        } else {

            renderer.renderImg(gc, npc.getImage(), npcX + shiftX, npcY + shiftY, true, 1 / 4.0);
            renderer.renderImg(gc, request.getImage(), npcX + shiftX + 20, npcY + shiftY - 5, false, 1 / 30.0);
        }

    }


     public void render(GraphicsContext gc) {
        if (status == CONVERSATION) {
            gc.drawImage(theme, 0, 0);
            gc.drawImage(npc.getImage(), 0 , 100 ,
                    npc.getImage().getWidth() * 2, npc.getImage().getHeight() * 2);
            gc.drawImage(textbox, 100, 205);

            gc.setFont(Font.font ("Segoe UI",28));
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);

            gc.fillText("Miranda", nameX, nameY);

            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText(current_text, textX, textY);
        }

        if (status == END) {
            gc.drawImage(bg.getImage(), 0, 0,
                    1256, 776);

            gc.drawImage(npc.getImage(), 800 , 500,
                     - npc.getImage().getWidth() / 2.0,
                    npc.getImage().getHeight() / 2.0);

            gc.drawImage(anims[Bomber.currentCharacter][Bomber.IDLE].getImage(), 560, 530,
                    anims[Bomber.currentCharacter][Bomber.IDLE].getImage().getWidth() * 2.4,
                    anims[Bomber.currentCharacter][Bomber.IDLE].getImage().getHeight() * 2.4);


            gc.drawImage(heart.getImage(), 630, 450, 75, 75);
            gc.drawImage(happy, 0, 0);
        }
    }

    public void interact() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                this.handleEvent(keyEvent);
            }

            private void handleEvent(KeyEvent keyEvent) {

                if (keyEvent.getCode() == KeyCode.SPACE) {

                    if (status == CONVERSATION) {
                        if (fullLine) {
                            current_line++;
                            current_char = -1;
                            current_text = "";
                            fullLine = false;

                            if (current_line == conversation.length) {
                                status = PORTAL;
                                lastTime = System.currentTimeMillis();
                            }
                        } else {
                            int endChar = conversation[current_line].length() - 1;
                            current_text = current_text.concat(conversation[current_line].substring(current_char + 1, endChar + 1));
                            current_char = endChar;
                        }
                    }

                    if (status == END) {
                        status = QUIT;
                        stage_theme.stop();
                    }
                }
            }
        });
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        Ending.status = status;

        if (status == NORMAL) {
            reset();
        }
    }

    public void reset() {
        npcX = NPCX;
        npcY = NPCY;
        current_text = "";
        current_char = -1;
        current_line = 0;
        fullLine = false;
        //up git:))
    }
}
