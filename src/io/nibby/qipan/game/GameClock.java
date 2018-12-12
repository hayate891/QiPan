package io.nibby.qipan.game;

import org.json.JSONObject;

public abstract class GameClock {

    public static final int SPEED_LIVE = 0;
    public static final int SPEED_BLITZ = 1;
    public static final int SPEED_CORRESPONDENCE = 2;

    private long now;
    private long lastMove;
    private long pausedSince;
    private boolean paused;
    private boolean pausedOnWeekends;   // OGS-specific
    private int speed;                  // OGS-specific

    public void tick() {

    }

    public static class ByoYomi extends GameClock {

        private int mainTime;
        private int periods;
        private int periodTime;

        public int getMainTime() {
            return mainTime;
        }

        public void setMainTime(int mainTime) {
            this.mainTime = mainTime;
        }

        public int getPeriods() {
            return periods;
        }

        public void setPeriods(int periods) {
            this.periods = periods;
        }

        public int getPeriodTime() {
            return periodTime;
        }

        public void setPeriodTime(int periodTime) {
            this.periodTime = periodTime;
        }
    }

    public static class Fischer extends GameClock {

        private long maxTime;
        private long mainTime;
        private long timeIncrement;
        private long initialTime;

        public long getMaxTime() {
            return maxTime;
        }

        public void setMaxTime(long maxTime) {
            this.maxTime = maxTime;
        }

        public long getMainTime() {
            return mainTime;
        }

        public void setMainTime(long mainTime) {
            this.mainTime = mainTime;
        }

        public long getTimeIncrement() {
            return timeIncrement;
        }

        public void setTimeIncrement(long timeIncrement) {
            this.timeIncrement = timeIncrement;
        }

        public long getInitialTime() {
            return initialTime;
        }

        public void setInitialTime(long initialTime) {
            this.initialTime = initialTime;
        }
    }

    public static class Canadian extends GameClock {

    }

    public static class Simple extends GameClock {

    }

    public static class Absolute extends GameClock {

    }

    public static class None extends GameClock {

    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getLastMove() {
        return lastMove;
    }

    public void setLastMove(long lastMove) {
        this.lastMove = lastMove;
    }

    public long getPausedSince() {
        return pausedSince;
    }

    public void setPausedSince(long pausedSince) {
        this.pausedSince = pausedSince;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPausedOnWeekends() {
        return pausedOnWeekends;
    }

    public void setPausedOnWeekends(boolean pausedOnWeekends) {
        this.pausedOnWeekends = pausedOnWeekends;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        switch (speed) {
            case "live":
                this.speed = SPEED_LIVE;
                break;
            case "correspondence":
                this.speed = SPEED_CORRESPONDENCE;
                break;
            case "blitz":
                this.speed = SPEED_BLITZ;
                break;
            default:
                throw new IllegalArgumentException("Unsupported clockSetting speed: " + speed);
        }
    }
}
