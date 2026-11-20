package se.vierecksauge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    public static final Cursor INVISBLE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(""), new Point(0, 0), "invisible");

    public enum MOUSE_INPUT {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        WHEEL_UP,
        WHEEL_DOWN,
        BUTTON_1,
        BUTTON_2,
        BUTTON_3,
    }

    private static final int NUM_KEY_CODES = 600;

    private GameAction[] keyActions =  new GameAction[NUM_KEY_CODES];
    private GameAction[] mouseActions = new GameAction[NUM_KEY_CODES];

    private Point mouseLocation;
    private Point centerLocation;
    private Component comp;
    private Robot robot;
    private boolean isRecentering;

    public InputManager(Component comp) {
        this.comp = comp;
        mouseLocation = new Point();
        centerLocation = new Point();

        comp.addKeyListener(this);
        comp.addMouseListener(this);
        comp.addMouseMotionListener(this);
        comp.addMouseWheelListener(this);

        comp.setFocusTraversalKeysEnabled(false);
    }

    public void setCursor(Cursor cursor) {
        comp.setCursor(cursor);
    }

    public void setRelativeMouseMode(boolean mode) {
        if (mode == isRelativeMouseMode()) {
            return;
        }

        if (!mode) {
            robot = null;
            return;
        }

        try {
            robot = new Robot();
            recenterMouse();
        } catch (AWTException e) {
            robot = null;
        }
    }

    public boolean isRelativeMouseMode() {
        return (robot != null);
    }

    public void recenterMouse() {
        if (robot != null && comp.isShowing()) {
            centerLocation.x = comp.getWidth() / 2;
            centerLocation.y = comp.getHeight() / 2;
            SwingUtilities.convertPointToScreen(centerLocation, comp);
            isRecentering = true;
            robot.mouseMove(centerLocation.x, centerLocation.y);
        }
    }

    public void mapToKey(GameAction gameAction, int keyCode) {
        keyActions[keyCode] = gameAction;
    }

    public void mapToMouse(GameAction gameAction, int mouseCode) {
        mouseActions[mouseCode] = gameAction;
    }

    public void clearMap(GameAction gameAction) {
        for (int i = 0; i < keyActions.length; i++) {
            if (keyActions[i] == gameAction) {
                keyActions[i] = null;
            }
        }

        for (int i = 0; i < mouseActions.length; i++) {
            if (mouseActions[i] == gameAction) {
                mouseActions[i] = null;
            }
        }

        gameAction.reset();
    }

    public ArrayList getMaps(GameAction gameCode) {
        ArrayList list = new ArrayList();

        for (int i = 0; i < keyActions.length; i++) {
            if (keyActions[i] == gameCode) {
                list.add(getKeyName(i));
            }
        }

        for (int i = 0; i < mouseActions.length; i++) {
            if (mouseActions[i] == gameCode) {
                list.add(getMouseName(MOUSE_INPUT.values()[i]));
            }
        }

        return list;
    }

    public static String getKeyName(int keyCode) {
        return KeyEvent.getKeyText(keyCode);
    }

    public static String getMouseName(MOUSE_INPUT mouseCode) {
        switch (mouseCode) {
            case MOVE_LEFT: return "Mouse Left";
            case MOVE_RIGHT: return "Mouse Right";
            case MOVE_UP: return "Mouse Up";
            case MOVE_DOWN: return "Mouse Down";
            case WHEEL_UP: return "Mouse Wheel Up";
            case WHEEL_DOWN: return "Mouse Wheel Down";
            case BUTTON_1: return "Mouse Button 1";
            case BUTTON_2: return "Mouse Button 2";
            case BUTTON_3: return "Mouse Button 3";
            default: return "Unknown mouse code " + mouseCode;
        }
    }

    public int getMouseX() {
        return mouseLocation.x;
    }

    public int getMouseY() {
        return mouseLocation.y;
    }

    private GameAction getKeyAction(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keyActions.length) {
            return keyActions[keyCode];
        }

        return null;
    }

    public static MOUSE_INPUT getMouseButtonCode(MouseEvent e) {
        for (int i = 0; i < MOUSE_INPUT.values().length; i++) {
            if (e.getButton() == i) {
                return MOUSE_INPUT.values()[i];
            }
        }

        return null;
    }

    private GameAction getMouseButtonAction(MouseEvent e) {
        MOUSE_INPUT mouseCode = getMouseButtonCode(e);
        if (mouseCode != null) {
            return mouseActions[mouseCode.ordinal()];
        }

        return null;
    }

    public void keyPressed(KeyEvent e) {
        GameAction gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.press();
        }

        e.consume();
    }

    public void keyReleased(KeyEvent e) {
        GameAction gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.release();
        }

        e.consume();
    }

    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    public void mousePressed(MouseEvent e) {
        GameAction gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.press();
        }
    }

    public void mouseReleased(MouseEvent e) {
        GameAction gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }

    public void  mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public synchronized void mouseMoved(MouseEvent e) {
        if (isRecentering && centerLocation.x == e.getX() && centerLocation.y == e.getY()) {
            isRecentering = false;
        }
        else {
            int dx = e.getX() - centerLocation.x;
            int dy = e.getY() - centerLocation.y;
            mouseHelper(MOUSE_INPUT.MOVE_LEFT, MOUSE_INPUT.MOVE_RIGHT, dx);
            mouseHelper(MOUSE_INPUT.MOVE_UP, MOUSE_INPUT.MOVE_DOWN, dy);

            if (isRelativeMouseMode()) {
                recenterMouse();
            }
        }

        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseHelper(MOUSE_INPUT.WHEEL_UP, MOUSE_INPUT.WHEEL_DOWN, e.getWheelRotation());
    }

    public synchronized void mouseHelper(MOUSE_INPUT codeNeg, MOUSE_INPUT codePos, int amount) {
        GameAction gameAction;
        if (amount < 0) {
            gameAction = mouseActions[codeNeg.ordinal()];
        }
        else {
            gameAction = mouseActions[codePos.ordinal()];
        }

        if (gameAction != null) {
            gameAction.press(Math.abs(amount));
            gameAction.release();
        }
    }

}