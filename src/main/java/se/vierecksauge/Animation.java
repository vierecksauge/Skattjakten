package se.vierecksauge;

import java.awt.*;
import java.util.ArrayList;

public class Animation {

    private ArrayList frames;
    private int currFrameIndex;
    private long animTime;
    private long totalDuration;

    public Animation() {
        this(new ArrayList(), 0);
    }

    private Animation(ArrayList frames, long totalDuration) {
        this.frames = frames;
        this.totalDuration = totalDuration;
        start();
    }

    public Object clone() {
        return new Animation(frames, totalDuration);
    }

    public synchronized void addFrame(Image image, long duration) {
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

    public synchronized void start() {
        animTime = 0;
        currFrameIndex = 0;
    }

    private class AnimFrame {
        Image image;
        long endTime;

        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }

}
