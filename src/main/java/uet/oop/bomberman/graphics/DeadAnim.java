package uet.oop.bomberman.graphics;


/** Animation chạy numLoop lần xong biến mất */
public class DeadAnim extends Anim{

    /**
     * Tạo Animation từ SpriteSheet.
     * VD: Anim a = new Anim(new SpriteSheet(path, number_sprites), frameTime);
     */

    private long numLoop = 0; // Số vòng lặp của Anim
    private int currentLoop = 0; // Vòng lặp hiện tại
    private boolean dead; // Trạng thái của Anim

    /** Hàm khởi tạo dựa theo số vòng lặp cài sẵn. */
    public DeadAnim(SpriteSheet sheet, int frameTime, int numLoop) {
        super(sheet, frameTime);
        this.numLoop = numLoop;
        this.dead = false;
    }

    /** Hàm khởi tạo dựa theo thời gian cài sẵn. */
    public DeadAnim(SpriteSheet sheet, int frameTime, double timer) {
        super(sheet, frameTime);
        this.numLoop = Math.round(timer * 60 / (frameTime * sheet.getSpriteNumber()));
        this.dead = false;
    }

    /** Hàm update của Dead Anim */
    @Override
    public void update() {
        this.countTime ++;

        if (this.countTime % this.frameTime == 0) {
            this.currentFrame ++;
            this.currentFrame %= this.numberFrames;

            if (this.currentFrame == 0) {
                this.currentFrame = this.startLoopFrame;

                this.currentLoop ++;
            }
        }

        if (this.currentLoop == this.numLoop) {
            dead = true;
        }
    }

    /** Update with speed up motherfucker. **/
    public void update(int speed_up) {
        this.countTime ++;

        if (this.countTime % this.frameTime == 0) {
            this.currentFrame ++;
            this.currentFrame %= this.numberFrames;

            if (this.currentFrame == 0) {
                this.currentFrame = this.startLoopFrame;
                //speeding up
                frameTime = Math.max(1, frameTime - speed_up);

                this.currentLoop ++;
            }
        }

        if (this.currentLoop == this.numLoop) {
            dead = true;
        }
    }

    /** Reset lại Animation của DeadAnim */
    public void reset() {
         currentLoop = 0;
         currentFrame = 0;
         countTime = 0;
         dead = false;
    }
    public boolean isDead() {
        return dead;
    }
}
