package uet.oop.bomberman.others;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

abstract public class Basic {
    public static double inf = 1e9 + 7;
    public static void drawRectangle(GraphicsContext gc, Rectangle rect){
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(rect.getX(),
                rect.getY(),
                rect.getWidth(),
                rect.getHeight());
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
    }

    public static boolean contain(char[] arr, char x) {
        for (char c : arr) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

    public static boolean contain(int[] arr, int x) {
        for (int j : arr) {
            if (x == j) {
                return true;
            }
        }
        return false;
    }


    public static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static Image toGrayScale(Image sourceImage) {
        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage grayImage = new WritableImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixelReader.getArgb(x, y);

                int alpha = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int grayLevel = (int) (0.2162 * red + 0.7152 * green + 0.0722 * blue);
                int gray = (alpha << 24) + (grayLevel << 16) + (grayLevel << 8) + grayLevel;

                grayImage.getPixelWriter().setArgb(x, y, gray);
            }
        }
        return grayImage;
    }

    public static Image toBlueScale(Image sourceImage) {
        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage blueImage = new WritableImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixelReader.getArgb(x, y);

                int alpha = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);
//                51, 153, 255
//                int blueLevel = (int) (0.2 * red + 0.2 * green + 1.0 * blue);
//                int bluePixel = (alpha << 24) + (blueLevel << 16) + (blueLevel << 8) + blueLevel;

                int bluePixel = (alpha << 24) + (blue << 16) + (green << 8) + red;

                blueImage.getPixelWriter().setArgb(x, y, bluePixel);
            }
        }
        return  blueImage;
    }
    public static double mapping(double x1, double y1, double x2, double y2, double pointer) {
        if((pointer - x1) * (pointer - y1) > 0) {
            if(x1 > y1) {
                if(pointer > x1) return x2;
                if(pointer < y1) return y2;
            }
            if(x1 <= y1) {
                if(pointer > y1) return y2;
                if(pointer < x1) return x2;
            }
        }
        return x2 + (y2 - x2)*(pointer - x1) / (y1 - x1);
    }
}
