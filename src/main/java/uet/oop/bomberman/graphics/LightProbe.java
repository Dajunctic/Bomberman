package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.maps.GameMap;
import java.util.ArrayList;
import static java.lang.Math.PI;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.others.Basic.inf;

public class LightProbe {
    private Canvas canvas;
    private GraphicsContext gc;
    private WritableImage img;
    private Mobile src;
    private Point srcTile = new Point(-1,-1);
    private int radius;
    private double density;
    private double[] xPoints = new double[50];
    private double[] yPoints = new double[50];
    private int nPoints = 0;
    public ArrayList<Integer> tileCodes = new ArrayList();
    public static ArrayList<Stop> gradients = new ArrayList<>();
    static {
        gradients.add(new Stop(0, Color.WHITE));
        gradients.add(new Stop(0.8, Color.rgb(90, 90, 200)));
        gradients.add(new Stop(0.9, Color.BLACK));
    }
    public Vertex center;
    private RadialGradient texture;
    private GaussianBlur blur;
    public LightProbe(Mobile src, int radius, double density) {
        this.src = src;
        this.radius = radius;
        this.density = density;
        canvas = new Canvas(radius * Sprite.SCALED_SIZE * 2, radius * Sprite.SCALED_SIZE * 2);
        gc = canvas.getGraphicsContext2D();
        img = new WritableImage((int) canvas.getWidth(),(int) canvas.getHeight());
        center = new Vertex(canvas.getWidth() / 2, canvas.getHeight() / 2);
        reset();
        init();
    }
    /** Cài đặt hiệu ứng */
    public void init() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        //fading
        texture = new RadialGradient(0, 0, center.x, center.y, radius * Sprite.SCALED_SIZE,
                                    false, CycleMethod.NO_CYCLE, gradients);
        blur = new GaussianBlur(0.2);
    }
    /** Vẽ vùng chiếu sáng lên local canvas*/
    public void renderLight() {
        reset();
        createPolygon();
        drawPolygon(1);
    }
    /** Khởi tạo vùng chiếu sáng*/
    public void createPolygon() {
        tileCodes.clear();
        nPoints = 0;
        double div = 2 * PI / density;
        srcTile.set(src.getTileX(), src.getTileY());
//        System.out.println("Spin step " + div);
        Vertex starter = new Vertex(src.getCenterX() / Sprite.SCALED_SIZE, src.getCenterY() / Sprite.SCALED_SIZE);
        double angle = 0;
        Vertex tempDir = new Vertex(inf,inf);
        /** Vector trung gian*/
        Vertex temp = new Vertex(-1, -1);
        while (angle  <= 2 * PI){
            Vertex dir = new Vertex(Math.cos(angle), Math.sin(angle));
            dir.normalize();
            addCheckPoint(starter, dir, temp, tempDir);
            angle += div;
//            System.out.println("Angle: " + angle);
        }
        addPoint(temp);
//        System.out.println(tileCodes);
    }

    /** Lưu điểm dừng */
    public void addCheckPoint(Vertex starter, Vertex dir, Vertex temp, Vertex verDir) {
        /** Thuật toán DDA cơ bản*/
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
            if(!tileCodes.contains(tileCode) &&
                    Math.abs(tileCheck.x - srcTile.x) < radius &&
                    Math.abs(tileCheck.y - srcTile.y) < radius ) tileCodes.add(tileCode);
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

        if(temp.x == -1) {
            temp.set(tileX, tileY);
        } else {
           double tempDir = (tileX - temp.x) * verDir.y - (tileY - temp.y) * verDir.x;
            if(Math.abs(tempDir) > 0.0015) {
                addPoint(temp);
                verDir.set(tileX - temp.x, tileY - temp.y);
                verDir.normalize();
                temp.set(tileX, tileY);
            }   else {
                temp.set(tileX, tileY);
            }
        }
    }
    /** Xuất ảnh phơi sáng */
    public Image getImg() {
        return new ImageView(canvas.snapshot(null, img)).getImage();
    }
    /** Đặt lại local canvas*/
    public void reset() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
    }
    /** Đặt điểm sáng */
    public void setPov(Mobile src) {
        this.src = src;
//        texture = new RadialGradient(0, 0.1, src.getCenterX(), src.getCenterY(), radius, false, CycleMethod.NO_CYCLE, gradients);
    }
    /** Thêm điểm vào vùng sáng*/
    private void addPoint(Vertex temp) {
        xPoints[nPoints] = temp.x;
        yPoints[nPoints] = temp.y;
        nPoints = (nPoints + 1) % 200;
    }
    /** Chuyển đổi tọa độ tile thành tọa độ Descartes nhìn vào điểm sáng*/
    private void normalizeArray(double scale) {
        double avgX = 0;
        Vertex pov = src.getCenter();
        for(int i = 0; i < nPoints; i++) {
            xPoints[i] = (xPoints[i] * Sprite.SCALED_SIZE - pov.x) * scale + center.x;
            yPoints[i] = (yPoints[i] * Sprite.SCALED_SIZE - pov.y) * scale + center.y;
            avgX += xPoints[i];
        }
        avgX /= nPoints;
        System.out.println("Average x coordinates: " + avgX);
    }
    /** Render vùng sáng lên local canvas*/
    private void drawPolygon(double scale) {
        normalizeArray(scale);
//        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        reset();
        gc.setFill(texture);
        gc.fillPolygon(xPoints, yPoints, nPoints);
//        gc.setEffect(null);
    }

    public void shade(GraphicsContext gc, Renderer renderer) {
        gc.setEffect(blur);
        gc.setGlobalBlendMode(BlendMode.MULTIPLY);
        Vertex origin = renderer.getPov().getCenter();
//        origin.shift(-lighter.center.x, -lighter.center.y);
        renderer.renderCenterImg(gc, getImg(), origin.x, origin.y, false, 1);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.setEffect(null);
    }
}
