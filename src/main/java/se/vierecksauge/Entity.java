package se.vierecksauge;

import java.lang.reflect.Constructor;

public class Entity extends Sprite {

    private Animation defaultAnim;
    private Animation despawnAnim;

    public enum STATE {
        NORMAL,
        DESPAWNING,
        DESPAWN,
    }

    private STATE state;
    private static int DESPAWN_TIME = 0;
    private double despawnTimer = DESPAWN_TIME;

    public Entity(Animation defaultAnim) {
        super(defaultAnim);
        this.defaultAnim = defaultAnim;
        this.despawnAnim = defaultAnim;

        state = STATE.NORMAL;
    }

    public Entity(Animation defaultAnim, Animation despawnAnim) {
        super(defaultAnim);
        this.defaultAnim = defaultAnim;
        this.despawnAnim = despawnAnim;

        state = STATE.NORMAL;
    }

    public Object clone() {
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(new Object[] {(Animation)defaultAnim.clone()});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        if (this.state != state) {
            this.state = state;
            despawnTimer = 0;
        }
    }

    public boolean isAlive() {
        return (state != STATE.DESPAWN || state != STATE.DESPAWNING);
    }

    public float getMaxSpeed() {
        return 0.0f;
    }

    public void update(double delta) {
        super.update(delta);
        Animation newAnim = anim;

        if (state == STATE.DESPAWNING) {
            newAnim = despawnAnim;
            despawnTimer -= delta;
            if (despawnTimer <= 0) {
                setState(STATE.DESPAWN);
            }
        }

        if (anim != newAnim) {
            anim = newAnim;
            anim.start();
        }
        else {
            anim.update(delta);
        }

    }

}
