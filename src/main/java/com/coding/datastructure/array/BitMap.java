package com.coding.datastructure.array;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BitMap {
    public static void createBitmap() {
        int w = 100;
        int h = 100;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        var ran = new Random();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color color = new Color(0, 0, 0);
                image.setRGB(x, y, color.getRGB());
            }
        }
        File outputFile = new File("src/main/resources/output/bitmap.png");
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        createBitmap();
    }
}
