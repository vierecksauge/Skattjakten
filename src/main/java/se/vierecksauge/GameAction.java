package se.vierecksauge;

public class GameAction {

    public enum BEHAVIOUR {
        NORMAL,
        DETECT_INITIAL_PRESS_ONLY,
    }

    public enum STATE {
        RELEASED,
        PRESSED,
        WAITING_FOR_RELEASE,
    }

    private String name;
    private BEHAVIOUR behaviour;
    private STATE state;
    private int amount;

    public GameAction(String name) {
        this(name, BEHAVIOUR.NORMAL);
    }

    public GameAction(String name, BEHAVIOUR behaviour) {
        this.name = name;
        this.behaviour = behaviour;
        reset();
    }

    public String getName() {
        return name;
    }

    public void reset() {
        state = STATE.RELEASED;
        amount = 0;
    }

    public synchronized void tap() {
        press();
        release();
    }

    public synchronized void press() {
        press(1);
    }

    public synchronized void press(int amount) {
        if (state != STATE.WAITING_FOR_RELEASE) {
            this.amount += amount;
            state = STATE.PRESSED;
        }
    }

    public synchronized void release() {
        state = STATE.RELEASED;
    }

    public synchronized boolean isPressed() {
        return (getAmount() != 0);
    }


    // For keys, this is the number of times the key was pressed since it was last checked.
    // For mouse movement, this is the distance moved
    public synchronized int getAmount() {
        int retVal = amount;
        if (retVal != 0) {
            if (state == STATE.PRESSED) {
                amount = 0;
            }
            else if (behaviour == BEHAVIOUR.DETECT_INITIAL_PRESS_ONLY) {
                state = STATE.WAITING_FOR_RELEASE;
                amount = 0;
            }
        }

        return retVal;
    }

}
