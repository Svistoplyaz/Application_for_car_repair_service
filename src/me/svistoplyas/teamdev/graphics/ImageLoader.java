package me.svistoplyas.teamdev.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class ImageLoader {
    private HashMap<String, BufferedImage> map = new HashMap<>();

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null)
            instance = new ImageLoader();
        return instance;
    }

    public BufferedImage getImage(String str) {
        if (!map.containsKey(str))
            try {
                map.put(str, ImageIO.read(ImageLoader.class.getResourceAsStream(str)));
            } catch (Exception e) {
                e.printStackTrace();
            }

        return map.get(str);
    }
}
