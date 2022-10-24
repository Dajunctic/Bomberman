package uet.oop.bomberman.others;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Renderer;

import java.util.Objects;

public class Bar extends Entity {
    Image HP_BAR = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/hp_bar.png")));
    Image MANA_BAR = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/mana_bar.png")));

    public static Color HP_COLOR =  new Color(0, 204 / 255.0, 153 / 255.0, 1);
    public static Color MANA_COLOR = new Color(51 / 255.0, 204 / 255.0, 1, 1);

    static int DELAY_TIME = 2;

    int timeCount = 0;
    int timer = 0;

    protected int maxValue;

    Color[][] currentBoard = new Color[4][36];
    Color[][] tempBoard = new Color[4][36];

    Image currentImg;
    Image tempImg;

    public Bar(double xPixel, double yPixel, int maxValue) {
        super(xPixel, yPixel);
        this.maxValue = maxValue;

    }

    @Override
    public void update() {
        timeCount ++;
        if (timeCount % 60 == 0) {
            timer --;
            if (timer == 0) {
                for (int i = 0; i < tempBoard.length; i++) {
                    for (int j = 0; j < tempBoard[0].length; j++) {
                        tempBoard[i][j] = new Color(0, 0, 0, 0);
                    }
                }

                tempImg = getMatrixImage(tempBoard);
            }
        }
    }

    public void setCurrentImg(int point) {
        int n = point * 40 / maxValue;

        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[0].length; j++) {
                if (j < n) {
                    currentBoard[i][j] = new Color(0, 204 / 255.0, 153 / 255.0, 1);
                } else {
                    currentBoard[i][j] = new Color(0, 0, 0, 0);
                }
            }
        }

        currentImg = getMatrixImage(currentBoard);
    }

    public void setTempImg(int point) {
        if (timer > 0) {
            return;
        }

        timer = DELAY_TIME;
        timeCount = 0;

        int n = point * 40 / maxValue;

        for (int i = 0; i < tempBoard.length; i++) {
            for (int j = 0; j < tempBoard[0].length; j++) {
                if (j < n) {
                    tempBoard[i][j] = new Color(238 / 255.0, 71 / 255.0, 78 / 255.0, 1);
                } else {
                    tempBoard[i][j] = new Color(0, 0, 0, 0);
                }
            }
        }

        tempImg = getMatrixImage(tempBoard);
    }


    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        double renderX = x - gameplay.translate_x + gameplay.offsetX;
        double renderY = y - gameplay.translate_y + gameplay.offsetY;

        gc.drawImage(this.getImg(), renderX, renderY);
        gc.drawImage(this.tempImg, renderX + 1, renderY + 1);
        gc.drawImage(this.currentImg, renderX + 1, renderY + 1);
    }

    public void render(GraphicsContext gc, Renderer renderer) {
        renderer.renderImg(gc, this.getImg(), x + shiftX, y + shiftY, false);
        renderer.renderImg(gc, this.tempImg, x + shiftX + 1, y + shiftY + 1, false);
        renderer.renderImg(gc,this.currentImg, x + shiftX + 1, y + shiftY + 1, false);
    }
    public static Image getMatrixImage(Color[][] pixels) {
        int h = pixels.length;
        int w = pixels[0].length;

        WritableImage wr = new WritableImage(w, h);
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                pw.setColor(x, y, pixels[y][x]);
            }
        }

        return new ImageView(wr).getImage();
    }
}
