package uet.oop.bomberman.generals;

public class Vertex {
    private double x,y;

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
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
    public void normalize() {
        double abs = abs();
        x /= abs;
        y /= abs;
    }
}
