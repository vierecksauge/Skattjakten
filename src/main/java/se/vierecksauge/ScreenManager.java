package se.vierecksauge;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public class ScreenManager {

    GraphicsDevice device;

    public ScreenManager() {
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        setFullScreen(device.getDisplayMode());
    }

    public void setFullScreen(DisplayMode displayMode) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);
        frame.setBackground(Color.white);
        frame.setForeground(Color.black);

        device.setFullScreenWindow(frame);

        if (displayMode != null && device.isFullScreenSupported()) {
            try {
                device.setDisplayMode(displayMode);
            } catch (IllegalArgumentException ignored) {}


            // Fix for Mac OS
            frame.setSize(displayMode.getWidth(), displayMode.getHeight());
        }

        // Avoid potential deadlock in v1.4
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    frame.createBufferStrategy(2);
                }
            });
        } catch (InterruptedException ignored) {}
        catch (InvocationTargetException ignored) {}
    }

    public Graphics2D getGraphics() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            return (Graphics2D) strategy.getDrawGraphics();
        }

        return null;
    }

    public void update() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            if (!strategy.contentsLost()) {
                strategy.show();
            }
        }

        // Sync display on some systems just to be sure
        Toolkit.getDefaultToolkit().sync();
    }

    public JFrame getWindow() {
        return (JFrame)device.getFullScreenWindow();
    }

    public int getWidth() {
        return (device.getFullScreenWindow() != null) ? device.getFullScreenWindow().getWidth() : 0;
    }

    public int getHeight() {
        return (device.getFullScreenWindow() != null) ? device.getFullScreenWindow().getHeight() : 0;
    }

    public void restoreScreen() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            window.dispose();
        }
        device.setFullScreenWindow(null);
    }

    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
        Window window = device.getFullScreenWindow();

        if (window != null) {
            GraphicsConfiguration gc = window.getGraphicsConfiguration();
            return gc.createCompatibleImage(width, height, transparency);
        }
        return null;
    }

}
