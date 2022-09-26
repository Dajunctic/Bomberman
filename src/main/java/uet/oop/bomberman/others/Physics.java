package uet.oop.bomberman.others;

import javafx.scene.shape.Rectangle;

public class Physics {
    public static boolean collisionRectToRect(Rectangle a, Rectangle b) {
        return a.getX() + a.getWidth() >= b.getX() &&
                a.getX() <= b.getX() + b.getWidth() &&
                a.getY() + a.getHeight() >= b.getY() &&
                a.getY() <= b.getY() + b.getHeight();
    }
}
