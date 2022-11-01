package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.*;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.maps.GameMap;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.others.Basic.mapping;

public class LightProbe {
    private GraphicsContext gc;
    public static List<Stop> gradients = new ArrayList<>();
    static {
        gradients.add(new Stop(0, Color.WHITE));
        gradients.add(new Stop(1, Color.BLACK));
    }
    private Entity src;
    private double width;
    private double height;
    private int radius;
    private double density;
    private double dense;
    private Vertex center;
    private Renderer screen;
    private Layer parent;
    private boolean turned = false;
    private Vertex[] checkPoint;
    public LightProbe(Entity src, Renderer screen, int radius, double density, double dense, Layer parent, GraphicsContext gc) {
        this.src = src;
        this.screen = screen;
        this.radius = radius;
        this.density = density;
        this.gc = gc;
        this.dense = dense;
        this.parent = parent;
        checkPoint = new Vertex[(int) density];
        init();
    }

    //initialize graphics preferences
    public void init() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setLineWidth(360 / density * radius);
        gc.setGlobalAlpha(dense / density);
        reset();
    }

    // Render lighting
    public void renderLight() {
        reset();
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        Vertex origin = screen.getOrigin();
        Vertex starter = new Vertex(src.getCenterX() / Sprite.SCALED_SIZE, src.getCenterY() / Sprite.SCALED_SIZE);
        gc.setStroke(new RadialGradient(0, 0.1, src.getCenterX() + origin.x,
                                        src.getCenterY() + origin.y, radius * Sprite.SCALED_SIZE,
                                        false, CycleMethod.NO_CYCLE, gradients));
        for(int i = 0; i < density; i++) {
            double angle = 2 * PI * i / density;
            if(checkPoint[i] == null) checkPoint[i] = new Vertex(0, 0);
            checkPoint(starter, new Vertex(Math.cos(angle), Math.sin(angle)), checkPoint[i]);
            screen.drawTileLine(parent.gc, starter, checkPoint[i]);
//            screen.drawTileLine(gc, starter, checkPoint[i]);
            //gc.strokeLine(canvas.getWidth() / 2, canvas.getHeight() / 2, lightRay.x * Sprite.SCALED_SIZE, lightRay.y * Sprite.SCALED_SIZE);
        }
    }

    // Find collision
    public void checkPoint(Vertex starter, Vertex dir, Vertex output) {
        Vertex rayUnitStepSize =  new Vertex(Math.sqrt(1 + (dir.getY() / dir.getX()) * (dir.getY() / dir.getX()))
                                            , Math.sqrt(1 + (dir.getX() / dir.getY()) * (dir.getX() / dir.getY())));
        Point tileCheck = new Point((int) Math.floor(starter.getX()), (int) Math.floor(starter.getY()));
        Vertex rayLength = new Vertex(0, 0);
        Point stepDir = new Point(1, 1);
        if(dir.getX() < 0) {
            stepDir.setX(-1);
            rayLength.x = (starter.x - tileCheck.x) * rayUnitStepSize.x;
        } else rayLength.x = -(starter.x - (tileCheck.x + 1)) * rayUnitStepSize.x;

        if(dir.getY() < 0) {
            stepDir.setY(-1);
            rayLength.y = (starter.y - tileCheck.y) * rayUnitStepSize.y;
        } else rayLength.y = -(starter.y - (tileCheck.y + 1)) * rayUnitStepSize.y;

        boolean stopped = false;
        double distance = 0;
        while(!stopped && distance < radius) {
            //check out of bound
//            if(areaMaps.get(currentArea).checkInArea(tileCheck.x, tileCheck.y) || !screen.onScreen(tileCheck.x * Sprite.SCALED_SIZE, tileCheck.y * Sprite.SCALED_SIZE)){
//                stopped = true;
//                break;
//            }

            if(rayLength.x < rayLength.y) {
                tileCheck.x += stepDir.x;
                distance = rayLength.x;
                rayLength.x += rayUnitStepSize.x;
            } else {
                tileCheck.y += stepDir.y;
                distance = rayLength.y;
                rayLength.y += rayUnitStepSize.y;
            }

            if(Gameplay.get(tile_map[tileCheck.y][tileCheck.x], tileCheck.x, tileCheck.y) != GameMap.FLOOR){
                stopped = true;
                break;
            }
        }
        output.set(starter.x + dir.x * distance, starter.y + dir.y * distance);
    }
    public void reset() {
        gc.fillRect(0,0, width, height);
    }
    public void setPov(Mobile src) {
        this.src = src;
    }
}
