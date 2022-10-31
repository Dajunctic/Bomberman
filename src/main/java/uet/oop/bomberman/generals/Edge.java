package uet.oop.bomberman.generals;

import uet.oop.bomberman.entities.Mobile;

public class Edge {
    private Vertex start;
    private Vertex stop;
    private Vertex dir;
    private double tileX;
    private double tileY;
    private int tileLength;
    private Vertex stepUnitSize;
    private Vertex tileCheck;
    private Vertex startingPoint = new Vertex(0, 0);
    private Point step =new Point(1, 1);

    public Edge(Vertex start, Vertex dir, int length) {
        this.start = start;
        this.dir = dir;
        this.tileLength = length;
        this.dir.normalize();
        stepUnitSize = new Vertex(Math.sqrt(1 + Math.pow(dir.getY() / dir.getX(), 2)),
                                    Math.sqrt(1 + Math.pow(dir.getX()/dir.getY(),2)));
        tileCheck = new Vertex(start.getX(), start.getY());
        if(dir.getX() < 0) {
            step.x = -1;
            startingPoint.setX((start.getX() - tileCheck.getX()) * stepUnitSize.getX());
        } else startingPoint.setX(-(start.getX() - (tileCheck.getX() + 1)) * stepUnitSize.getX());

        if(dir.getY() < 0) {
            step.y = -1;
            startingPoint.setX((start.getY() - tileCheck.getY()) * stepUnitSize.getY());
        } else startingPoint.setX(-(start.getY() - (tileCheck.getY() + 1)) * stepUnitSize.getY());
    }

}
