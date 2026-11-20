package se.vierecksauge;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class Main extends GameCore {

    private TileMap map;
    private ResourceManager resourceManager;
    private TileMapRenderer renderer;

    private InputManager inputManager;
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction exit;

    public static void main(String[] args) {
        new Main().run();
    }

    public void init() {
        super.init();

        initInput();

        resourceManager = new ResourceManager(screen.getWindow().getGraphicsConfiguration());
        renderer = new TileMapRenderer();
        renderer.setBackground(resourceManager.loadImage("background.png"));
        map = resourceManager.loadNextMap();
    }

    private void initInput() {
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        moveUp = new GameAction("moveUp");
        moveDown = new GameAction("moveDown");
        exit = new GameAction("exit");

        inputManager = new InputManager(screen.getWindow());
        inputManager.setCursor(InputManager.INVISBLE_CURSOR);

        inputManager.mapToKey(moveLeft, KeyEvent.VK_A);
        inputManager.mapToKey(moveRight, KeyEvent.VK_D);
        inputManager.mapToKey(moveUp, KeyEvent.VK_W);
        inputManager.mapToKey(moveDown, KeyEvent.VK_S);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
    }

    public void update(double delta) {
        Entity player = (Entity)map.getPlayer();

        if (player.getState() == Entity.STATE.DESPAWN) {
            map = resourceManager.reloadMap();
            return;
        }

        checkInput();
        player.update(delta);

    }

    private void checkInput() {
        if (exit.isPressed()) {
            stop();
        }

        Player player = (Player)map.getPlayer();
        if (player.isAlive()) {
            float velocityX = 0;
            float velocityY = 0;
            if (moveLeft.isPressed()) {
                velocityX -= player.getMaxSpeed();;
            }
            if (moveRight.isPressed()) {
                velocityX += player.getMaxSpeed();
            }
            if (moveUp.isPressed()) {
                velocityY -= player.getMaxSpeed();
            }
            if (moveDown.isPressed()) {
                velocityY += player.getMaxSpeed();
            }
            player.setVelocityX(velocityX);
            player.setVelocityY(velocityY);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        renderer.draw(g, map, screen.getWidth(), screen.getHeight());
    }

}
