package se.vierecksauge;

import java.awt.*;
import java.util.Iterator;

public class TileMapRenderer {

    private static final int TILE_SIZE = 64;

    private Image background;

    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }

    public static int pixelsToTiles(int pixels) {
        return (int)Math.floor((float)pixels / TILE_SIZE);
    }

    public static int tilesToPixels(int numTiles) {
        return numTiles * TILE_SIZE;
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public void draw(Graphics2D g, TileMap map, int screenWidth, int screenHeight) {
        Sprite player = map.getPlayer();

        if (background != null) {
            g.drawImage(background, 0, 0, screenWidth, screenHeight, null);
        }
        else {
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, screenWidth, screenHeight);
        }

        g.drawImage(player.getImage(),
                Math.round(player.getX()),
                Math.round(player.getY()),
                null);

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x <= map.getWidth(); x++) {
                Image image = map.getTile(x, y);
                if (image != null) {
                    g.drawImage(image,
                            tilesToPixels(x),
                            tilesToPixels(y),
                            null);
                }
            }
        }

        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite) i.next();
            g.drawImage(sprite.getImage(), Math.round(sprite.getX()), Math.round(sprite.getY()), null);
        }

    }

}
