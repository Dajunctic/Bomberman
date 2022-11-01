package uet.oop.bomberman.generals;

import static java.lang.Math.PI;

public class Vertex {
    public double x,y;

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vertex(Vertex origin, Vertex goal) {
        x = goal.getX() - origin.getX();
        y = goal.getY() - origin.getY();
    }

    public void setX(double x) {
        this.x = x;
    }
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vertex other) {
        x += other.x;
        y += other.y;
    }

    public void copy(Vertex other) {
        x = other.x;
        y = other.y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double abs() {
        return Math.sqrt(x*x + y*y);
    }
    public double angle(Vertex other) {
        return Math.acos((x * other.x + y * other.y)/(abs() * other.abs()));
    }
    public double distance(Vertex other) {
        double x = this.x - other.x;
        double y = this.y - other.y;
        return Math.sqrt(x*x + y*y);
    }
    public void divide(double factor) {
        x /= factor;
        y /= factor;
    }
    public void divide(double factorX, double factorY) {
        x /= factorX;
        y /= factorY;
    }
    public void normalize() {
        double abs = abs();
        x /= abs;
        y /= abs;
    }
    public double distance(double x, double y) {
        double px = this.x - x;
        double py = this.y - y;
        return Math.sqrt(px*px + py*py);
    }
    public static double dis(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
    public double angleToOrigin(Vertex origin) {
        double x = this.x - origin.x;
        double y = this.y - origin.y;
        double bound = (x < 0 ? PI : 0) + (y < 0 && x < 0 ? PI : 0);
        return bound + Math.atan(x / y);
    }
}
