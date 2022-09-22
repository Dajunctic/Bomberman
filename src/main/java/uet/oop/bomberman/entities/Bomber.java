package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Anim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;

public class Bomber extends Entity {

    /** Các trạng thái của nhân vật */
    public static final int IDLE = 0;
    public static final int DOWN = 1;
    public static final int UP = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int DEAD = 5;

    private int currentStatus = Bomber.IDLE;
    private Anim[] statusAnims;

    /**
     * Đường dẫn đến folder của Model thôi không cần ảnh.
     * Có đường dẫn bởi vì nhỡ sau này phát triển nhiều nhân vật có thể lựa chọn để chơi.
     * */
    private final String path;


    public Bomber(int x, int y, String path) {
        super( x, y);
        this.path = path;
        load();
    }

    /**
     * Hàm load đã hoàn thiện chỉ có chỉnh sửa statusTime sao cho phù hợp.
     * Muốn hiểu status time thì đọc Anim.time.
     * */
    private void load() {
        String[] statusString = {"idle", "down", "up", "left", "right", "dead"};
        int[] statusNumberFrame = {4, 3, 3, 3, 3, 8};
        int[] statusTime = {60, 10, 10, 10, 10, 10};

        statusAnims = new Anim[6];

        for (int i = 0; i < statusAnims.length; i++) {
            String source = path + "/player_" + statusString[i] + ".png";
            statusAnims[i] = new Anim(new SpriteSheet(source, statusNumberFrame[i]), statusTime[i]);
            statusAnims[i].setScaleFactor(3);
        }
    }

    @Override
    public void update() {
        statusAnims[currentStatus].update();
    }

    /** Hàm render animation nên overload hàm render của Entity. */
    public void render(GraphicsContext gc) {
        gc.drawImage(statusAnims[currentStatus].getFxImage(), x, y);
    }
}
