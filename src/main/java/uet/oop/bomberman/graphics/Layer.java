package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Triplets;
import uet.oop.bomberman.generals.Vertex;

import java.awt.*;

public class Layer {
    WritableImage img;
    Canvas canvas;
    private Renderer renderer;
    GraphicsContext gc;
    private double width;
    private double height;
    private double bufferX;
    private double bufferY;
    private double scale;
    private Mobile focus;
    private Vertex center;
    public Layer(double bufferX, double bufferY, double width, double height, double scale) {
        this.bufferX = bufferX;
        this.bufferY = bufferY;
        this.scale = scale;
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        img = new WritableImage((int) width, (int) height);
        renderer = new Renderer(0.5, 0.5, 0, 0, 0, 0, width, height, 1);
        center = new Vertex(width / 2, height / 2);
    }
    public void update() {
        renderer.update();
    }
    public void render(Gameplay gameplay) {
        gameplay.render(this.gc, this.renderer);
    }
    public void setPov(Mobile pov) {
        focus = pov;
        renderer.setPov(pov);
    }
    public Image getImg() {
        return new ImageView(canvas.snapshot(null, img)).getImage();
    }

    public Triplets details() {
        return new Triplets(bufferX, bufferY, scale);
    }
    public void renderImg(GraphicsContext gc, Renderer renderer) {
        renderer.renderDirectImg(gc, this.getImg(), this.focus.getX() - center.x, this.focus.getY() - center.y, false);
    }
}
