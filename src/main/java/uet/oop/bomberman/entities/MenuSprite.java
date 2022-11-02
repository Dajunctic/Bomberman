package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class MenuSprite extends Entity {

    protected Rectangle rect= new Rectangle();
    public MenuSprite(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }

    public MenuSprite(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
        rect=new Rectangle(this.x,this.y,this.getWidth(),this.getHeight());
    }

    @Override
    public void update() {
        return;
    }
    public Rectangle getRect(){
        return this.rect;
    }
    public void free() {

    }
}
