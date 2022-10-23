package uet.oop.bomberman.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;

import javax.swing.text.html.HTMLDocument;

import static uet.oop.bomberman.game.BombermanGame.FPS;

public class Renderer {
    //canvas overall
    private double offsetX;
    private double offsetY;
    //display position in canvas
    private double shiftX;
    private double shiftY;
    //display position in map
    private double translateX;
    private double translateY;
    //size of display
    private double width;
    private double height;
    //camera movements
    private Vertex goal;
    private Vertex speed;
    private double interval = 0.5;
    private double margin = 50;
    private boolean stable = true;

    private double scaleX = 1;
    private double scaleY = 1;
    //window center
    public static final double centerX = BombermanGame.WIDTH * Sprite.SCALED_SIZE / 2;
    public static final double centerY = BombermanGame.HEIGHT * Sprite.SCALED_SIZE / 2;
    public Renderer(double offsetX, double offsetY, double shiftX, double shiftY, double translateX, double translateY, double width, double height) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.translateX = translateX;
        this.translateY = translateY;
        this.width = width;
        this.height = height;
        goal = new Vertex(translateX, translateY);
        speed = new Vertex(0,0);
    }
    //relocate

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    //check on screen
    public boolean onScreen(double x, double y, double marginX, double marginY) {
        return (Math.abs(x - translateX - centerX) <= centerX + marginX) && (Math.abs(y - translateY - centerY) <= centerY + marginY);
    }
    //render images
    public void renderImg(GraphicsContext gc, Image img, double x, double y) {
        if(!onScreen(x, y, img.getWidth(), img.getHeight())) return;
        double renderX = offsetX - translateX + shiftX;
        double renderY = offsetY - translateY + shiftY;
        gc.drawImage(img, x + renderX, y + renderY, scaleX * img.getWidth(), scaleY * img.getHeight());
    }
    //move camera to somewhere
    public void setGoal(double x, double y) {
        if(Math.abs(translateX - goal.getX()) <= margin &&
                Math.abs(translateY - goal.getY()) <= margin) {
            return;
        }
        goal.set(x, y);
        speed.set((x-translateX) / (interval * FPS), (y - translateY) / (interval * FPS));
        stable = false;
    }
    //update camera positions
    public void update() {
        if(stable) return ;
        translateX += speed.getX();
        translateY += speed.getY();
        if(Math.abs(translateX - goal.getX()) <= margin &&
                Math.abs(translateY - goal.getY()) <= margin) {
                stable = true;
        }
    }
    //directly set camera positions
    public void setTranslate(double x, double y) {
        if(translateX == x && translateY == y) return;
        System.out.println("Camera set to"+ " " + x + " " + y);
        translateX = x;
        translateY = y;
    }
}
