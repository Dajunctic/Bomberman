package uet.oop.bomberman.others;

import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Physics {
    public static boolean collisionRectToRect(Rectangle a, Rectangle b) {
        return a.getX() + a.getWidth() > b.getX() &&
                a.getX() < b.getX() + b.getWidth() &&
                a.getY() + a.getHeight() > b.getY() &&
                a.getY() < b.getY() + b.getHeight();
    }
    public static boolean collisionPointToRect(Point a, Rectangle b) {
        return a.getX()  > b.getX() &&
                a.getX() < b.getX() + b.getWidth() &&
                a.getY() > b.getY() &&
                a.getY() < b.getY() + b.getHeight();
    }
}
