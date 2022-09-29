package uet.oop.bomberman.graphics;

import javafx.scene.image.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

	//Tiles path
	private static String floor_path = "/sprites/Bg/Floor/";
	private static String wall_path = "/sprites/Obstacles/Wall/";
	private static String destroyed_path = "/sprites/Bg/Destroyed_floor/";

	/**
	 * |--------------------------------------------------------------------------
	 * | Board tiles
	 * |--------------------------------------------------------------------------
	 * */
	public static Sprite portal = new Sprite("/sprites/Interactives/Functional/portal.png", Sprite.TILE);

	//list of properties
	public static List<Sprite> floors = new ArrayList<>();
	public static List<Sprite> walls = new ArrayList<>();
	public static List<Sprite> destroyed = new ArrayList<>();
	/**
	 * |--------------------------------------------------------------------------
	 * | Effect tiles
	 * |--------------------------------------------------------------------------
	 * */
	public static Sprite superSayan = new Sprite("/sprites/Player/Effect/super_sayan.png", Sprite.NORMAL);

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

	/** Hàm dùng để debug ảnh tạo từ Sprite Sheet */
	public Image getSheetFxImage() {
		int w = sheet.getW();
		int h = sheet.getH();

		WritableImage wr = new WritableImage(w, h);
		PixelWriter pw = wr.getPixelWriter();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if ( sheet.getPixel(x + y * w) == TRANSPARENT_COLOR) {
					pw.setArgb(x, y, 0);
				}
				else {
					pw.setArgb(x, y, sheet.getPixel(x + y * w));
				}
			}
		}
		Image input = new ImageView(wr).getImage();

		if (this.type.equals(Sprite.TILE)) {
			return resample(input, SCALED_SIZE / this.tileSize);
		}

		return resample(input, this.scaleFactor);
	}

	/** load tiles */
	public static void load_tiles(int floor_styles, int wall_styles) {
		for(int i = 0;i < floor_styles;i ++) {
			char index = (char) (i + '0');
			String path = floor_path + index + ".png";
			floors.add(new Sprite(path, Sprite.TILE));
		}

		for(int i = 0;i < wall_styles;i ++) {
			char index = (char) (i + '0');
			String path = wall_path + index + ".png";
			walls.add(new Sprite(path, Sprite.TILE));
		}

		for(int i = 0; i < floor_styles; i++) {
			char index = (char) (i + '0');
			String path = destroyed_path + index + ".png";
			destroyed.add(new Sprite(path, Sprite.TILE));
		}
		System.out.println(String.format("Load success, %d floor styles, %d destroyed tiles and %d wall styles", floors.size(), destroyed.size(),walls.size()));
	}
}
