package se.vierecksauge;

import java.awt.*;
import java.util.ArrayList;

public class Animation {

    private ArrayList frames;
    private int currFrameIndex;
    private double animTime;
    private double totalDuration;

    public Animation() {
        this(new ArrayList(), 0);
    }

    private Animation(ArrayList frames, double totalDuration) {
        this.frames = frames;
        this.totalDuration = totalDuration;
        start();
    }

    public Object clone() {
        return new Animation(frames, totalDuration);
    }

    public synchronized void addFrame(Image image, double duration) {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    public synchronized void update(double delta) {
        if (!frames.isEmpty()) {
            animTime += delta;

            if (animTime >= totalDuration) {
                animTime = animTime % totalDuration;
                currFrameIndex = 0;
            }

            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++;
            }
        }
    }

    public synchronized Image getCurrentImage() {
        if (frames.isEmpty()) {
            return null;
        }

        return getFrame(currFrameIndex).image;
    }

    public synchronized AnimFrame getFrame(int index) {
        return (AnimFrame) frames.get(index);
    }

    public synchronized int getCurrentFrameIndex() {
        return currFrameIndex;
    }

    public synchronized void start() {
        animTime = 0;
        currFrameIndex = 0;
    }

    private class AnimFrame {
        Image image;
        double endTime;

        public AnimFrame(Image image, double endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }

}
