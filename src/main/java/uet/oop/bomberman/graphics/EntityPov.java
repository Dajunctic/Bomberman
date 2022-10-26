package uet.oop.bomberman.graphics;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;

public class EntityPov {
    WritableImage img;
    Canvas canvas;
    private Renderer renderer;
    GraphicsContext gc;
    private double width;
    private double height;
    public EntityPov(double width, double height) {
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        img = new WritableImage((int) width, (int) height);
        renderer = new Renderer(0.5, 0.5, 0, 0, 0, 0, width, height, 1);
    }
    public void update() {
        renderer.update();
    }
    public void render(Gameplay gameplay) {
        gameplay.render(this.gc, this.renderer);
    }
    public void setPov(Mobile pov) {
        renderer.setPov(pov);
    }
    public Image getImg() {
        return new ImageView(canvas.snapshot(null, img)).getImage();
    }
}
