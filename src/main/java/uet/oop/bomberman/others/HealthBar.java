package uet.oop.bomberman.others;

import javafx.scene.paint.Color;

public class HealthBar extends Bar {

    public HealthBar(double xPixel, double yPixel, int maxHP) {
        super(xPixel, yPixel, maxHP);
        this.img = HP_BAR;
    }

    public void setCurrentImg(int point) {
        int n = point * 40 / maxValue;

        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[0].length; j++) {
                if (j < n) {
                    currentBoard[i][j] = HP_COLOR;
                } else {
                    currentBoard[i][j] = new Color(0, 0, 0, 0);
                }
            }
        }

        currentImg = getMatrixImage(currentBoard);
    }
}
