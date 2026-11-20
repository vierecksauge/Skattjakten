package se.vierecksauge;

import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {

    private ArrayList tiles = new ArrayList();
    private int currentMap;
    private GraphicsConfiguration gc;

    private Sprite playerSprite;

    public ResourceManager(GraphicsConfiguration gc) {
        this.gc = gc;
        loadTileImages();
        loadEntitySprites();
    }

    public Image loadImage(String name) {
        String filename = "/images/" + name;
        return new ImageIcon(getClass().getResource(filename)).getImage();
    }

    public Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }

    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }

    public Image getScaledImage(Image image, int x, int y) {
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate((double) ((x - 1) * image.getWidth(null)) / 2, (double) ((y - 1) * image.getHeight(null)) / 2);

        Image newImage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.BITMASK);

        Graphics2D g = (Graphics2D) newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }

    public TileMap loadNextMap() {
        TileMap map = null;
        while (map == null) {
            currentMap++;
            try {
                map = loadMap("/maps/map" + currentMap + ".txt");
            } catch (IOException e) {
                if (currentMap == 1) {
                    return null; // No maps to load
                }

                currentMap = 0;
            }
        }

        return map;
    }

    public TileMap reloadMap() {
        try {
            return loadMap("/maps/map" + currentMap + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TileMap loadMap(String filename) throws IOException {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;


        InputStream stream = getClass().getResourceAsStream(filename);

        if (stream == null) {
            throw new FileNotFoundException("Resource not found: " + filename);
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream)
        );
        while (true) {
            String line = reader.readLine();

            if (line == null) {
                reader.close();
                break;
            }

            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y = 0; y <  height; y++) {
            String line = (String)lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char ch = line.charAt(x);

                // Check if the char represents Tile A, B, C, etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }


            }
        }

        Sprite player = (Sprite)playerSprite.clone();
        player.setX(0);
        player.setY(0);
        newMap.setPlayer(player);

        return newMap;
    }

    private void addSprite(TileMap map, Sprite hostSprite, int tileX, int tileY) {
        if (hostSprite != null) {
            Sprite sprite = (Sprite)hostSprite.clone();

            sprite.setX(TileMapRenderer.tilesToPixels(tileX) + (float) (TileMapRenderer.tilesToPixels(1) - sprite.getWidth()) / 2);
            sprite.setY(TileMapRenderer.tilesToPixels(tileY + 1) - sprite.getHeight());

            map.addSprite(sprite);
        }
    }

    public void loadTileImages() {
        tiles = new ArrayList();
        char ch = 'A';
        while (true) {
            String name = "tile_" + ch + ".png";
            if (getClass().getResource("/images/" + name) == null) {
                break;
            }
            tiles.add(loadImage(name));
            ch++;
        }
    }

    public void loadEntitySprites() {
        Image[] playerFrames = new Image[]{
            loadImage("player1.png"),
            loadImage("player2.png"),
            loadImage("Player3.png"),
        };
        Animation playerAnim = createPlayerAnim(playerFrames[0], playerFrames[1], playerFrames[2]);
        playerSprite = new Player(playerAnim);
    }

    private Animation createPlayerAnim(Image player1,
                                       Image player2, Image player3)
    {
        Animation anim = new Animation();
        anim.addFrame(player1, 250);
        anim.addFrame(player2, 150);
        anim.addFrame(player1, 150);
        anim.addFrame(player2, 150);
        anim.addFrame(player3, 200);
        anim.addFrame(player2, 150);
        return anim;
    }

}
