package uet.oop.bomberman.others;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Renderer;

public class ManaBar extends Bar{
    public ManaBar(double xPixel, double yPixel, int maxMana) {
        super(xPixel, yPixel, maxMana);

        currentBoard = new Color[2][36];
        tempBoard = new Color[2][36];
        this.img = MANA_BAR;
    }

    @Override
    public void setCurrentImg(int point) {
        int n = point * 40 / maxValue;

        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[0].length; j++) {
                if (j < n) {
                    currentBoard[i][j] = MANA_COLOR;
                } else {
                    currentBoard[i][j] = new Color(0, 0, 0, 0);
                }
            }
        }

        currentImg = getMatrixImage(currentBoard);
    }

    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        double renderX = x - gameplay.translate_x + gameplay.offsetX;
        double renderY = y - gameplay.translate_y + gameplay.offsetY;

        gc.drawImage(this.getImg(), renderX, renderY);
        gc.drawImage(this.tempImg, renderX + 1 , renderY);
        gc.drawImage(this.currentImg, renderX + 1, renderY);
    }

    @Override
    public void render(GraphicsContext gc, Renderer renderer) {
        renderer.renderImg(gc, this.getImg(), x + shiftX, y + shiftY, false);
        renderer.renderImg(gc, this.tempImg, x + shiftX + 1, y + shiftY, false);
        renderer.renderImg(gc,this.currentImg, x + shiftX + 1, y + shiftY, false);
    }
}
