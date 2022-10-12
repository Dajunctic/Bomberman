package uet.oop.bomberman.maps;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;

public class Minimap extends Entity {
    Color [][] board;
    Color [][] defaultBoard;
    private final int defaultScale = 2;
    private int currentPlayerX;
    private int currentPlayerY;

    public Minimap(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        this.load();
    }

    public void load() {
        int h = Gameplay.height;
        int w = Gameplay.width;

        board = new Color[h][w];
        defaultBoard = new Color[h][w];


        /* Quy ước màu sắc cho minimap */
        for (int i = 0; i < Gameplay.height; i++) {
            for (int j = 0 ; j < Gameplay.width; j++) {

                switch (Gameplay.tile_map[i][j]) {
                    case '2', '!', '#', '@', '(', '&', ';', '=', ':', '$', '%' -> {
                        board[i][j] = new Color(0 ,0 ,0, .5);
                    }
                    case '0', '7', '8', '9' -> {
                        board[i][j] = new Color(70/255.0 ,70/255.0 ,70/255.0, .5);
                    }
                    case 'b', 'm', 'n', 'v' -> {
                        board[i][j] = new Color(141/255.0 ,174/255.0 ,205/255.0, .5);
                    }
                    case 'c', 'e', 'i', 'o' ->{
                        board[i][j] = new Color(0/255.0 ,102/255.0 ,102/255.0, .5);
                    }
                    case 'h', 'j', 'k', 'l' ->{
                        board[i][j] = new Color(230/255.0 ,184/255.0 ,0/255.0, .5);
                    }
                    case 'a', 'q', 'w', 's', 'z' ->{
                        board[i][j] = new Color(0/255.0 ,204/255.0 ,102/255.0, .5);
                    }
                    default -> {
                        board[i][j] = new Color(0, 0, 0, 0);
                    }
                }

                defaultBoard[i][j] = board[i][j];
            }
        }

        this.img = getMatrixImage(board, defaultScale);
        currentPlayerY = 1;
        currentPlayerX = 1;
    }


    @Override
    public void update() {

    }

    public void update(Bomber player) {
        int playerX = (int) Math.round(player.getX() / Sprite.SCALED_SIZE);
        int playerY = (int) Math.round(player.getY() / Sprite.SCALED_SIZE);

        board[currentPlayerY][currentPlayerX] = defaultBoard[currentPlayerY][currentPlayerX];

        int[] dirX = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
        int[] dirY = {-1, -1, -1, 0, 0, 0, 1, 1, 1};

        for (int i = 0 ; i < dirX.length; i++) {
            int incX = dirX[i];
            int incY = dirY[i];

            board[currentPlayerY + incY][currentPlayerX + incX] = defaultBoard[currentPlayerY + incY][currentPlayerX + incX];
        }

        for (int i = 0 ; i < dirX.length; i++) {
            int incX = dirX[i];
            int incY = dirY[i];

            board[playerY + incY][playerX + incX] = new Color(1, 0, 0, 1);
        }

        this.img = getMatrixImage(board, defaultScale);

        currentPlayerX = playerX;
        currentPlayerY = playerY;
    }

    public Image getMatrixImage(Color[][] pixels, int scaleFactor) {
        int h = pixels.length;
        int w = pixels[0].length;

        WritableImage wr = new WritableImage(w, h);
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                pw.setColor(x, y, pixels[y][x]);
            }
        }
        Image input = new ImageView(wr).getImage();

        return resample(input, scaleFactor);
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
