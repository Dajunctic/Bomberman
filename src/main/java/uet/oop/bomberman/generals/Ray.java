package uet.oop.bomberman.generals;

public class Ray {
    private Vertex p0;
    private Vertex dir;

    public Ray(Vertex p0, Vertex p1) {
        this.p0 = p0;
        this.dir = new Vertex(p0, p1);
        this.dir.normalize();
    }
}
