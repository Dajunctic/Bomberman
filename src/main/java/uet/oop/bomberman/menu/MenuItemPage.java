package uet.oop.bomberman.menu;

import javafx.scene.canvas.GraphicsContext;

public abstract class MenuItemPage {
    public abstract void update(Menu menu);
    public abstract void render(GraphicsContext gc);
    public abstract void interact(Menu menu);
}
