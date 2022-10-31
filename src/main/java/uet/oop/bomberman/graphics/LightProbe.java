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
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.maps.GameMap;

import static uet.oop.bomberman.game.Gameplay.*;

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
        double div = 360 / density;
        Vertex screenBound = screen.getOrigin();
        Vertex starter = new Vertex(src.getCenterX() / Sprite.SCALED_SIZE, src.getCenterY() / Sprite.SCALED_SIZE);
        for(double step = 0; step < 360; step += div) {
            double angle = Math.toRadians(step);
            Vertex end = checkPoint(starter, new Vertex(Math.cos(angle), Math.sin(angle)));
            screen.drawTileLine(parent.gc, starter, end);
            screen.drawTileLine(gc, starter, end);
        }
    }


    public Vertex checkPoint(Vertex starter, Vertex dir) {
        double boundX;
        double boundY;
        if(Math.abs(dir.getX()) <= 0.0005) {
            boundX = 0;
            boundY = 1;
        }
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
        return new Vertex(starter.x + dir.x * distance, starter.y + dir.y * distance);
    }
    public Image getImg() {
        return new ImageView(canvas.snapshot(null, img)).getImage();
    }
    public void reset() {
        gc.fillRect(0,0, width, height);
    }
    public void setPov(Mobile src) {
        this.src = src;
    }
}
