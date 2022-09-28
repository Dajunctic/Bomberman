package uet.oop.bomberman.graphics;


/** Animation chạy numLoop lần xong biến mất */
public class DeadAnim extends Anim{

    /**
     * Tạo Animation từ SpriteSheet
     * VD: Anim a = new Anim(new SpriteSheet(path, number_sprites), frameTime)
     */

    private long numLoop;
    private int currentLoop = 0;
    private boolean dead;

    public DeadAnim(SpriteSheet sheet, int frameTime, int numLoop) {
        super(sheet, frameTime);
        this.numLoop = numLoop;
        this.dead = false;
    }

    public DeadAnim(SpriteSheet sheet, int frameTime, double timer) {
        super(sheet, frameTime);
        this.numLoop = Math.round(timer * 60 / (frameTime * sheet.getSpriteNumber()));
        this.dead = false;
    }

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
