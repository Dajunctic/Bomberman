package uet.oop.bomberman.others;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

abstract public class Basic {
    public static void drawRectangle(GraphicsContext gc, Rectangle rect){
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(rect.getX(),
                rect.getY(),
                rect.getWidth(),
                rect.getHeight());
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
    }

    public static boolean contain(char[] arr, char x) {
        for (char c : arr) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

    public static boolean contain(int[] arr, int x) {
        for (int j : arr) {
            if (x == j) {
                return true;
            }
        }
        return false;
    }

}
