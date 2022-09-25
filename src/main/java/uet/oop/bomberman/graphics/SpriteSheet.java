package uet.oop.bomberman.graphics;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Tất cả sprite (hình ảnh game) được lưu trữ vào một ảnh duy nhất
 * Class này giúp lấy ra các sprite riêng từ 1 ảnh chung duy nhất đó
 */
public class SpriteSheet {

	private final String path;
	private final int spriteNumber;
	private int w;
	private int h;
	public int[] pixels;
	public BufferedImage image;

	/** Animation Game */
	public static SpriteSheet bomb = new SpriteSheet("/sprites/Player/Bomb/bomb.png", 3);
	public static SpriteSheet explosion = new SpriteSheet("/sprites/Player/Bomb/explosion.png", 6);

	//flame
	public static SpriteSheet flame_right = new SpriteSheet("/sprites/Player/Flame/flame_right.png", 6);
	public static SpriteSheet flame_left = new SpriteSheet("/sprites/Player/Flame/flame_left.png", 6);
	public static SpriteSheet flame_down = new SpriteSheet("/sprites/Player/Flame/flame_down.png", 6);
	public static SpriteSheet flame_up = new SpriteSheet("/sprites/Player/Flame/flame_up.png", 6);

	public SpriteSheet(String path, int spriteNumber) {
		this.path = path;
		this.spriteNumber = spriteNumber;
		this.load();
	}

	public int getSpriteNumber() {
		return this.spriteNumber;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public int[] getPixels() {
		return pixels;
	}
	public int getPixel(int i) {
		return pixels[i];
	}

	private void load() {
		try {
			URL a = SpriteSheet.class.getResource(this.path);
			image = ImageIO.read(a);
			w = image.getWidth();
			h = image.getHeight();
			this.pixels = new int[w * h];
			image.getRGB(0, 0, w, h, this.pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/** Hàm dùng để debug xem pixel load lên đúng chưa.
	 * Không dùng khi không cần thiết, đã fix các lỗi của class */
	public Image getFxImage() {
		WritableImage wr = new WritableImage(w, h);
		PixelWriter pw = wr.getPixelWriter();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				pw.setArgb(x, y, pixels[x + y * w]);
			}
		}

		return new ImageView(wr).getImage();
	}


}
