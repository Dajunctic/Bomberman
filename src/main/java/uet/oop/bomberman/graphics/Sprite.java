package uet.oop.bomberman.graphics;

import javafx.scene.image.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Lưu trữ thông tin các pixel của 1 sprite (hình ảnh game).
 */
public class Sprite {

	/** Các loại ảnh. */
	public static final String TILE = "Tile";
	public static final String NORMAL = "Normal";
	public static final String ANIM = "Animation";

	/** Cài đặt mặc định kích cỡ ảnh (Dành cho Tile) */
	public static final int SCALED_SIZE = 48;
    private static final int TRANSPARENT_COLOR = 0xffff00ff;
	/** Dành cho ảnh Tile và Normal */
	private String path;
	private int cropX;
	private int cropY;
	private int w;
	private int h;
	private int scaleFactor;

	private int tileSize;
	public int[] pixels;

	private String type;
	SpriteSheet sheet;
	private Image img;

	/**
	 * |--------------------------------------------------------------------------
	 * | Buff tiles
	 * |--------------------------------------------------------------------------
	 * */
	public static Sprite speed = new Sprite("/sprites/Interactives/Buffs/powerup_speed.png", Sprite.TILE);
	public static Sprite buff_immortal = new Sprite("/sprites/Interactives/Buffs/powerup_bombpass.png", Sprite.TILE);
	public static Sprite buff_bomber = new Sprite("/sprites/Interactives/Buffs/powerup_bombs.png", Sprite.TILE);

	/**
	 * |--------------------------------------------------------------------------
	 * | Entity tiles
	 * |--------------------------------------------------------------------------
	 * */
	public static Sprite wall = new Sprite("/tilemaps/main/main_wall.png", Sprite.TILE);
	public static Sprite wall2 = new Sprite("/tilemaps/main/main_wall2.png", Sprite.TILE);
	public static Sprite floor = new Sprite("/tilemaps/main/main_floor.png", Sprite.TILE);
	public static Sprite floor2 = new Sprite("/tilemaps/main/main_floor2.png", Sprite.TILE);
	public static Sprite floor3 = new Sprite("/tilemaps/main/main_floor3.png", Sprite.TILE);
	public static Sprite floor4 = new Sprite("/tilemaps/main/main_floor4.png", Sprite.TILE);
	public static Sprite space = new Sprite("/tilemaps/main/space.png", Sprite.TILE);
	public static Sprite brick = new Sprite("/tilemaps/main/main_brick.png", Sprite.TILE);
	public static Sprite destroyFloor = new Sprite("/tilemaps/main/destroy_floor.png", Sprite.TILE);

	/**
	 * |--------------------------------------------------------------------------
	 * | Winter Scenery tiles
	 * |--------------------------------------------------------------------------
	 * */

	public static Sprite iceWall = new Sprite("/tilemaps/winter/ice_wall.png", Sprite.TILE); // #
	public static Sprite iceWall2 = new Sprite("/tilemaps/winter/ice_wall2.png", Sprite.TILE);
	public static Sprite iceWall3 = new Sprite("/tilemaps/winter/ice_wall3.png", Sprite.TILE);
	public static Sprite iceTop = new Sprite("/tilemaps/winter/ice_top.png", Sprite.TILE);
	public static Sprite iceFloor = new Sprite("/tilemaps/winter/ice_floor.png", Sprite.TILE);
	public static Sprite iceFloor2 = new Sprite("/tilemaps/winter/ice_floor2.png", Sprite.TILE);
	public static Sprite iceFloor3 = new Sprite("/tilemaps/winter/ice_floor3.png", Sprite.TILE);
	public static Sprite iceFloor4 = new Sprite("/tilemaps/winter/ice_floor4.png", Sprite.TILE);
	public static Sprite iceBrick = new Sprite("/tilemaps/winter/brick.png", Sprite.TILE); // +
	public static Sprite iceDestroyFloor = new Sprite("/tilemaps/winter/destroy_floor.png", Sprite.TILE);

	/**
	 * |--------------------------------------------------------------------------
	 * | Tomb Scenery tiles
	 * |--------------------------------------------------------------------------
	 * */
	public static Sprite tombWall = new Sprite("/tilemaps/tomb/wall.png", Sprite.TILE);
	public static Sprite tombWall2 = new Sprite("/tilemaps/tomb/wall2.png", Sprite.TILE);
	public static Sprite tombWall3 = new Sprite("/tilemaps/tomb/wall3.png", Sprite.TILE);
	public static Sprite tombFloor = new Sprite("/tilemaps/tomb/floor.png", Sprite.TILE);
	public static Sprite tombFloor2 = new Sprite("/tilemaps/tomb/floor2.png", Sprite.TILE);
	public static Sprite tombFloor3 = new Sprite("/tilemaps/tomb/floor3.png", Sprite.TILE);
	public static Sprite tombFloor4 = new Sprite("/tilemaps/tomb/floor4.png", Sprite.TILE);
	public static Sprite tombBrick = new Sprite("/tilemaps/tomb/brick.png", Sprite.TILE);
	public static Sprite tombDestroyFloor = new Sprite("/tilemaps/tomb/destroy_floor.png", Sprite.TILE);

	/**
	 * |--------------------------------------------------------------------------
	 * | Spring Scenery tiles
	 * |--------------------------------------------------------------------------
	 * */
	public static Sprite springWall = new Sprite("/tilemaps/spring/spring_wall.png", Sprite.TILE);
	public static Sprite springWall2 = new Sprite("/tilemaps/spring/spring_wall2.png", Sprite.TILE);
	public static Sprite springFloor = new Sprite("/tilemaps/spring/spring_floor.png", Sprite.TILE);
	public static Sprite springFloor2 = new Sprite("/tilemaps/spring/spring_floor2.png", Sprite.TILE);
	public static Sprite springFloor3 = new Sprite("/tilemaps/spring/spring_floor3.png", Sprite.TILE);
	public static Sprite springFloor4 = new Sprite("/tilemaps/spring/spring_floor4.png", Sprite.TILE);
	public static Sprite springFloor5 = new Sprite("/tilemaps/spring/spring_floor5.png", Sprite.TILE);
	public static Sprite springBrick = new Sprite("/tilemaps/spring/spring_brick.png", Sprite.TILE);
	public static Sprite springDestroyFloor = new Sprite("/tilemaps/spring/destroy_floor.png", Sprite.TILE);

	/**
	 * |--------------------------------------------------------------------------
	 * | Spring Scenery tiles
	 * |--------------------------------------------------------------------------
	 * */

	/**
	 * |--------------------------------------------------------------------------
	 * | Castle Scenery tiles
	 * |--------------------------------------------------------------------------
	 * */
	public static Sprite castleWall = new Sprite("/tilemaps/castle/wall.png", Sprite.TILE);
	public static Sprite castleWall2 = new Sprite("/tilemaps/castle/wall2.png", Sprite.TILE);
	public static Sprite castleFloor = new Sprite("/tilemaps/castle/floor.png", Sprite.TILE);
	public static Sprite castleFloor2 = new Sprite("/tilemaps/castle/floor2.png", Sprite.TILE);
	public static Sprite castleFloor3 = new Sprite("/tilemaps/castle/floor3.png", Sprite.TILE);
	public static Sprite castleFloor4 = new Sprite("/tilemaps/castle/floor4.png", Sprite.TILE);
	public static Sprite castleBrick = new Sprite("/tilemaps/castle/brick.png", Sprite.TILE);
	public static Sprite castleDestroyFloor = new Sprite("/tilemaps/castle/destroy_floor.png", Sprite.TILE);

	/** Hàm khởi tạo dành cho Sprite tách ra từ SpriteSheet. */
	public Sprite(SpriteSheet sheet, int cx, int cy, int cropW, int cropH) {
		this.cropX = cx;
		this.cropY = cy;
		this.w = cropW;
		this.h = cropH;

		this.sheet = sheet;
		this.type = Sprite.ANIM;
		load();
	}

	/**
	 * Hàm khởi tạo dành cho ảnh bình thường
	 * VD: Sprite a = new Sprite(path, Sprite.NORMAL)
	 * */
	public Sprite(String path, String type) {
		if (type.equals(Sprite.ANIM)) {
			System.out.println("Hàm khởi tạo không dành cho Sprite.ANIM");
			System.out.println("ERROR TYPE IMAGE:" + path);
			return;
		}

		this.type = type;
		this.path = path;
		this.load();
	}

	/**
	 *  Hàm load ảnh đã hoàn thiện không cần chỉnh sửa.
	 * Có bug liên qua thì thông báo nhóm trưởng */
	private void load() {

		if (this.type.equals(Sprite.ANIM)) {

			this.pixels = new int[w * h];

			for (int j = cropY; j < cropY + h; j++) {
				for (int i = cropX; i < cropX + w; i++) {
					int off_x = i - cropX;
					int off_y = j - cropY;

					this.pixels[off_x + off_y * w] = sheet.getPixel(i + j * sheet.getW());
				}
			}

		} else {
			try {
				URL a = SpriteSheet.class.getResource(this.path);
				BufferedImage image = ImageIO.read(a);
				w = image.getWidth();
				h = image.getHeight();
				this.pixels = new int[w * h];
				image.getRGB(0, 0, w, h, this.pixels, 0, w);

				if (this.type.equals(Sprite.TILE)) {
					this.tileSize = w;
				}

			} catch (IOException e) {
				System.out.println("Can't load image: " + this.path);
				e.printStackTrace();
				System.exit(0);
			}
		}

		this.scaleFactor = 1;
		this.img = this.getFxImage();
	}

	public int getPixel(int i) {
		return pixels[i];
	}

	/** Hàm điều chỉnh tỉ lệ kích thước ảnh x2, x3,..., xn */
	public void setScaleFactor(int factor) {
		this.scaleFactor = factor;
		this.img = this.getFxImage();
	}

	public Image getImg() {
		return this.img;
	}

	/**
	 * Hàm trả về kiểu dữ liệu ảnh chứ không render.
	 * Muốn render lên màn hình thì tạo một Entity và render thông qua nó.
	 * */
	public Image getFxImage() {
        WritableImage wr = new WritableImage(w, h);
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (pixels[x + y * w] == TRANSPARENT_COLOR) {
                    pw.setArgb(x, y, 0);
                }
                else {
                    pw.setArgb(x, y, pixels[x + y * w]);
                }
            }
        }
        Image input = new ImageView(wr).getImage();

		if (this.type.equals(Sprite.TILE)) {

			if (w > SCALED_SIZE || h > SCALED_SIZE) {
				return resample(input, 1);
			}
			return resample(input, SCALED_SIZE / this.tileSize);
		}

		return resample(input, this.scaleFactor);
    }

	private Image resample(Image input, int scaleFactor) {
		final int W = (int) input.getWidth();
		final int H = (int) input.getHeight();
		final int S = scaleFactor;

		WritableImage output = new WritableImage(
				W * S,
				H * S
		);

		PixelReader reader = input.getPixelReader();
		PixelWriter writer = output.getPixelWriter();

		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				final int argb = reader.getArgb(x, y);
				for (int dy = 0; dy < S; dy++) {
					for (int dx = 0; dx < S; dx++) {
						writer.setArgb(x * S + dx, y * S + dy, argb);
					}
				}
			}
		}

		return output;
	}
}
