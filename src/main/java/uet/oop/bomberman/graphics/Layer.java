package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Triplets;
import uet.oop.bomberman.generals.Vertex;

public class Layer {
    public static final int SHADE_CENTER = -1;
    public static final int SHADE_NORMAL = 1;
    WritableImage img;
    public Canvas canvas;

    public Renderer renderer;
    public GraphicsContext gc;

    private double width;
    private double height;
    private double bufferX;
    private double bufferY;
    private double scale;

    /** Attributes của shader */
    public boolean shaderEnable = false;
    public boolean shade = false;
    public boolean lookAtPlayer = false;

    /** Canvas của shader, nhưng vì chậm vãi beep nên sẽ không dùng*/
    public Canvas shader;
    public GraphicsContext shaderGc;

    /** chuyển đổi trạng thái*/
    public LightProbe lighter = null;
    private int radius = 5;
    private final static GaussianBlur blur = new GaussianBlur(Sprite.SCALED_SIZE / 3);
    public Layer(double bufferX, double bufferY, double width, double height, double scale, boolean shaderEnable) {
        this.bufferX = bufferX;
        this.bufferY = bufferY;
        this.scale = scale;
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        this.shaderEnable = shaderEnable;
        if(shaderEnable) {
            shader = new Canvas(width, height);
            shaderGc = shader.getGraphicsContext2D();
            shaderGc.setFill(Color.BLACK);
            shaderGc.setEffect(blur);
        }

        img = new WritableImage((int) width, (int) height);
        renderer = new Renderer(0.5, 0.5, 0, 0, 0, 0, width, height, 1);
    }
    public void update() {
        renderer.update();
    }
    public void render(Gameplay gameplay) {
        gc.clearRect(0, 0, width, height);
        shaderGc.fillRect(0, 0, width, height);
        gameplay.render(this);

    }
    public void setPov(Mobile pov) {
        lookAtPlayer = pov instanceof Bomber;
        renderer.setPov(pov);

        if(shaderEnable && pov != null)
            if(lighter == null) lighter = new LightProbe(pov, radius, 50);
                else lighter.setPov(pov);
    }
    public Image getImg() {
        return new ImageView(canvas.snapshot(null, img)).getImage();
    }
    public Image getShade() {
        return new ImageView(shader.snapshot(null, img)).getImage();
    }

    public Triplets details() {
        return new Triplets(bufferX, bufferY, scale);
    }

    /** Shade từ điểm sáng*/
    public void shadeDynamic() {
//        shaderGc.setFill(Color.BLACK);
//        shaderGc.fillRect(0, 0, shader.getWidth(), shader.getWidth());
        //bake polygon
        lighter.renderLight();
        gc.setEffect(blur);
        gc.setGlobalBlendMode(BlendMode.MULTIPLY);
        Vertex origin = renderer.getPov().getCenter();
//        origin.shift(-lighter.center.x, -lighter.center.y);
        renderer.renderCenterImg(gc, lighter.getImg(), origin.x, origin.y, false, 1.1);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.setEffect(null);
    }
    /** Đổ bóng */
    public void shade() {

        if((shaderEnable && !renderer.getPov().vulnerable() && lookAtPlayer) ||
                (shaderEnable && !lookAtPlayer && shade)) {
            shade = true;
            shadeDynamic();
        }   else shade = false;
    }
    public void turnShader() {
        if(shaderEnable) shade = !shade;
    }

}
