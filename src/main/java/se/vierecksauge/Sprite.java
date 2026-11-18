package se.vierecksauge;

import java.awt.*;

public class Sprite {

    protected Animation anim;
    private float x;
    private float y;
    private float dx;
    private float dy;

    public Sprite(Animation anim) {
        this.anim = anim;
    }

    public void update(double delta) {
        x += (float) (dx * delta);
        y += (float) (dy * delta);
        anim.update(delta);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return anim.getCurrentImage().getWidth(null);
    }

    public int getHeight() {
        return anim.getCurrentImage().getHeight(null);
    }

    public float getVelocityX() {
        return dx;
    }

    public float getVelocityY() {
        return dy;
    }

    public void setVelocityX(float velocityX) {
        dx = velocityX;
    }

    public void setVelocityY(float velocityY) {
        dy = velocityY;
    }

    public Image getImage() {
        return anim.getCurrentImage();
    }

    public Object clone() {
        return new Sprite(anim);
    }

}
