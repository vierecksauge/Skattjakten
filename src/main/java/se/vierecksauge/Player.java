package se.vierecksauge;

public class Player extends Entity {

    public Player(Animation defaultAnim) {
        super(defaultAnim);
    }

    public float getMaxSpeed() {
        return 0.5f;
    }

}
