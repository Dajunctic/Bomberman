package uet.oop.bomberman.graphics;

import javafx.scene.image.Image;

/** Class tạo Animation thông qua SpriteSheet. */
public class Anim {

    /**
     * Số lượng khung hình và khung hình hiện tại hiển thị.
     * */
    protected final int numberFrames;
    protected int currentFrame;
    SpriteSheet sheet;
    Sprite[] components;

    /**
     * Thời gian khung hình chạy trong 60FPS
     * VD:
     * time = 3 thì tức là 1 giây sẽ chạy được 20 khung hình.
     * time = 2 thì tức là 1 giây sẽ chạy được 30 khung hình.
     * time = n thì tức là 1 giây chạy được 60 / n khung hình.
     * */
    protected final int frameTime;
    protected int countTime;

    protected int startLoopFrame;


    /**
     * Tạo Animation từ SpriteSheet
     * VD: Anim a = new Anim(new SpriteSheet(path, number_sprites), frameTime)
     * */
    public Anim(SpriteSheet sheet, int frameTime) {
        this.currentFrame = 0;
        this.numberFrames = sheet.getSpriteNumber();
        this.sheet = sheet;
        this.frameTime = frameTime;
        this.countTime = 0;
        this.components = new Sprite[numberFrames];
        this.startLoopFrame = 0;
        this.load();
    }

    private void load() {
        for (int i = 0; i < numberFrames; i++) {
            int w = sheet.getW() / sheet.getSpriteNumber();
            int h = sheet.getH();

            components[i] = new Sprite(sheet, i * w, 0, w, h);
        }
    }

    /** Hàm chỉnh tỉ lệ, thực chất là phóng to ảnh x2, x3 vì scale là số nguyên */
    public void setScaleFactor(int scale) {
        for (Sprite x: components) {
            x.setScaleFactor(scale);
        }
    }

    /** Cài khung hình bắt đầu trong các vòng lặp sau lần đầu.
     * Ví dụ di chuyển có 3 frame thì chỉ cần 2 frame sau vòng lặp là đang bước đi thôi.
     * */
    public void setStartLoopFrame(int startFrame) {
        this.startLoopFrame = startFrame;
    }

    /** Đại khái là cứ time * 1/60s thì mới chạy 1 khung hình */
    public void update() {
        // Ảnh động tĩnh
        if (this.frameTime == 0) {
            return;
        }

        this.countTime ++;

        if (this.countTime % this.frameTime == 0) {
            this.currentFrame ++;
            this.currentFrame %= this.numberFrames;

            if (this.currentFrame == 0) {
                this.currentFrame = this.startLoopFrame;
            }
        }
    }

    /** Hàm dành cho ảnh động tĩnh, tức là khi có lệnh mới đổi khung hình */
    public void staticUpdate() {
        this.currentFrame ++;
        this.currentFrame %= this.numberFrames;
    }

    /** Trả về kiểu dữ liệu ảnh của khung hình hiện tại. */
    public Image getFxImage() {
//        return this.components[currentFrame].getSheetFxImage();
        return this.components[currentFrame].getFxImage();
    }


    private void setCurrentFrame() {
        this.currentFrame = 0;
    }
    /** Hàm làm khung hình hiện thị hiện tại quay về ban đầu */
    public void resetCurrentFrame() {
        this.setCurrentFrame();
    }

    private void setCountTime() {
        this.countTime = 0;
    }
    /** Thời gian tính hiện thị khung hình quay về ban đầu */
    public void resetCountTime() {
        this.setCountTime();
    }

}
