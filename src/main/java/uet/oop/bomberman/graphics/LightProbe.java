package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.maps.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.PI;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.others.Basic.inf;

public class LightProbe {
    private Canvas canvas;
    private Canvas subCanvas;
    private GraphicsContext gc;
    private WritableImage img;
    private Mobile src;
    private double width;
    private double height;
    private int radius;
    private double density;
    private Vertex center;
    private Renderer screen;
    private Layer parent;
    private boolean turned = false;
    private ArrayList<Vertex> vertexes = new ArrayList<>();
    public static ArrayList<Integer> tileCodes = new ArrayList();
    public static ArrayList<Stop> gradients = new ArrayList<>();
    static {
        gradients.add(new Stop(0, Color.TRANSPARENT));
        gradients.add(new Stop(0.8, Color.rgb(50, 0, 0)));
        gradients.add(new Stop(1, Color.BLACK));
    }
    private RadialGradient texture;
    public LightProbe(Mobile src, Renderer screen, double width, double height, int radius, double density, Layer parent) {
        this.src = src;
        this.screen = screen;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.density = density;
        canvas = new Canvas(width, height);
        subCanvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        img = new WritableImage((int) width,(int) height);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(360 / density * 2);
        gc.setGlobalAlpha(1 / density * 1.5);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        reset();
        this.parent = parent;
    }

    public void renderLight() {
        reset();
        createPolygon();
        Vertex starter = new Vertex(src.getCenterX() / Sprite.SCALED_SIZE,
                                    src.getCenterY() / Sprite.SCALED_SIZE);
//        screen.drawPolygon(parent.gc, vertexes, radius);
        for(int i = 0; i < vertexes.size(); i++) screen.drawTileLine(parent.gc, vertexes.get(i), vertexes.get((i+1) % vertexes.size()));

    }

    public void createPolygon() {
        tileCodes.clear();
        vertexes.clear();
        double div = 2 * PI / density;
//        System.out.println("Spin step " + div);
        Vertex starter = new Vertex(src.getCenterX() / Sprite.SCALED_SIZE, src.getCenterY() / Sprite.SCALED_SIZE);
        double angle = 0;
        Vertex tempDir = new Vertex(inf,inf);
        while (angle  <= 2 * PI){
            Vertex dir = new Vertex(Math.cos(angle), Math.sin(angle));
            dir.normalize();
            addCheckPoint(starter, dir, tempDir);
            angle += div;
//            System.out.println("Angle: " + angle);
        }
//        System.out.println(tileCodes);
    }

    public void addCheckPoint(Vertex starter, Vertex dir, Vertex verDir) {
        Vertex rayUnitStepSize =  new Vertex(Math.sqrt(1 + (dir.getY() / dir.getX()) * (dir.getY() / dir.getX()))
                                            , Math.sqrt(1 + (dir.getX() / dir.getY()) * (dir.getX() / dir.getY())));
        Point tileCheck = new Point((int) Math.floor(starter.getX()), (int) Math.floor(starter.getY()));
        Vertex rayLength = new Vertex(0, 0);
        Point stepDir = new Point(1, 1);
        tileCodes.add(tileCode(tileCheck.x, tileCheck.y));
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
        boolean type = false;
        while(!stopped && distance < radius) {
//            if(areaMaps.get(currentArea).checkInArea(tileCheck.x, tileCheck.y) || !screen.onScreen(tileCheck.x * Sprite.SCALED_SIZE, tileCheck.y * Sprite.SCALED_SIZE)){
//                stopped = true;
//                break;
//            }

            if(rayLength.x < rayLength.y) {
                tileCheck.x += stepDir.x;
                distance = rayLength.x;
                type = false;
                rayLength.x += rayUnitStepSize.x;
            } else {
                tileCheck.y += stepDir.y;
                distance = rayLength.y;
                type = true;
                rayLength.y += rayUnitStepSize.y;
            }
            int tileCode = Gameplay.tileCode(tileCheck.x, tileCheck.y);
            if(!tileCodes.contains(tileCode)) tileCodes.add(tileCode);
            if(distance >= radius) break;
            if(Gameplay.get(tile_map[tileCheck.y][tileCheck.x], tileCheck.x, tileCheck.y) != GameMap.FLOOR){
                stopped = true;
                break;
            }
        }
        double tileX;
        double tileY;
        if(stopped) {
            tileX = starter.x + distance * dir.x;
            tileY = starter.y + distance * dir.y;
        } else {
            tileX = starter.x + radius * dir.x;
            tileY = starter.y + radius * dir.y;
        }
        if(vertexes.isEmpty()) {
            vertexes.add(new Vertex(tileX, tileY));
        } else {
            Vertex temp = vertexes.get(vertexes.size() - 1);
            double tempDir = (tileX - temp.x) * verDir.y - (tileY - temp.y) * verDir.x;
            if(Math.abs(tempDir) > 0.0015) {
                vertexes.add(new Vertex(tileX, tileY));
                verDir.set(tileX - temp.x, tileY - temp.y);
            }
        }
    }
    public Image getImg() {
        return new ImageView(canvas.snapshot(null, img)).getImage();
    }
    public void reset() {
        gc.fillRect(0,0, width, height);
    }
    public void setPov(Mobile src) {
        this.src = src;
//        texture = new RadialGradient(0, 0.1, src.getCenterX(), src.getCenterY(), radius, false, CycleMethod.NO_CYCLE, gradients);
    }
}
