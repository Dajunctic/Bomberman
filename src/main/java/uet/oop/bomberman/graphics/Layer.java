package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Triplets;
import uet.oop.bomberman.generals.Vertex;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static uet.oop.bomberman.game.Gameplay.decodeTile;

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
    public boolean shaderEnable = false;
    public boolean shade = true;
    public Canvas shader;
    public GraphicsContext shaderGc;
    public LightProbe lighter = null;
    private int radius = 5;
    private HashMap<Integer, Integer> staticLightSource = new HashMap<>();
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
        if(shaderEnable && shade) {
            shade();
        }

    }
    public void setPov(Mobile pov) {
        renderer.setPov(pov);


        if(shaderEnable && pov != null)
            if(lighter == null) lighter = new LightProbe(pov, radius, 50, this);
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
    public void shadeDynamic() {
        shaderGc.setFill(Color.BLACK);
        shaderGc.fillRect(0, 0, shader.getWidth(), shader.getWidth());
        //bake polygon
        lighter.renderLight();
        Vertex origin = renderer.getPov().getCenter();
        origin.shift(-lighter.center.x, -lighter.center.y);
        renderer.renderImg(shaderGc, lighter.getImg(), origin.x, origin.y, false, 1);
    }
    public void shadeStatic() {
        if(staticLightSource.isEmpty()) return;
        for(Map.Entry<Integer, Integer> entry : staticLightSource.entrySet()) {
            renderer.clearTile(shaderGc, decodeTile(entry.getKey()),
                                Math.abs(entry.getValue()), Math.abs(entry.getValue()),
                                entry.getValue() < 0);
            lighter.tileCodes.add(entry.getKey());
        }
    }
    public void shade() {
        shadeDynamic();
        shadeStatic();
        gc.setGlobalBlendMode(BlendMode.MULTIPLY);
        gc.drawImage(this.getShade(), 0, 0);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
    }
    public void turnShader() {
        if(shaderEnable) shade = !shade;
    }
}
