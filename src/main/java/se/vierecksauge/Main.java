package se.vierecksauge;

import java.awt.*;

public class Main extends GameCore {

    private TileMap map;
    private ResourceManager resourceManager;
    private TileMapRenderer renderer;

    public static void main(String[] args) {
        new Main().run();
    }

    public void init() {
        super.init();

        resourceManager = new ResourceManager(screen.getWindow().getGraphicsConfiguration());
        renderer = new TileMapRenderer();
        renderer.setBackground(resourceManager.loadImage("background.png"));
        map = resourceManager.loadNextMap();
    }

    @Override
    public void draw(Graphics2D g) {
        renderer.draw(g, map, screen.getWidth(), screen.getHeight());
    }
}
