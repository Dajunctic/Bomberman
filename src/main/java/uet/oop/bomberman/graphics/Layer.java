package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Triplets;

import java.awt.*;

public class Layer {
    WritableImage img;
    public Canvas canvas;
    public Renderer renderer;
    public GraphicsContext gc;
    private double width;
    private double height;
    private double bufferX;
    private double bufferY;
    private double scale;
    public LightProbe lighter = null;
    private boolean shadow = false;
    public Layer(double bufferX, double bufferY, double width, double height, double scale) {
        this.bufferX = bufferX;
        this.bufferY = bufferY;
        this.scale = scale;
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        img = new WritableImage((int) width, (int) height);
        renderer = new Renderer(0.5, 0.5, 0, 0, 0, 0, width, height, 1);
    }
    public void update() {
        renderer.update();
    }
    public void render(Gameplay gameplay) {
        gameplay.render(this);
        if(lighter != null) {
//            gc.setGlobalBlendMode(BlendMode.MULTIPLY);
            lighter.renderLight();
            renderer.renderImg(this.gc, lighter.getImg(), 0, 0, false);
//            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        }

    }
    public void setPov(Mobile pov) {
        renderer.setPov(pov);
        if(lighter == null && pov != null) {
            lighter = new LightProbe(pov, renderer, canvas.getWidth(), canvas.getHeight(), 8, 200, this);
        } else {
            assert lighter != null;
            lighter.setPov(pov);
        }
    }
    public Image getImg() {
        return new ImageView(canvas.snapshot(null, img)).getImage();
    }

    public Triplets details() {
        return new Triplets(bufferX, bufferY, scale);
    }
    public void switchShadow() {
        shadow = !shadow;
        System.out.println("Shader turned " + shadow);
    }
}
