package com.insidion.anl.o3;

public class Stopwatch {
    /**
     * Fields
     */
    private long startTime = -1;
    private long endTime = -1;

    /**
     * Starts the stopwatch
     */
    public void start() {
        // reset it just to be sure
        reset();
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Stops the stopwatch
     */
    public void stop() {
        this.endTime = System.currentTimeMillis();
    }

    /**
     * Gets the result of the stopwatch
     * @return Result of the measurement
     */
    public long result() {
        if(this.startTime == -1 || this.endTime == -1) {
            return -1;
        }

        return this.endTime - this.startTime;
    }

    /**
     * Stops the stopwatch and returns the result
     * @return Result of the measurement
     */
    public long stopAndResult() {
        stop();
        return result();
    }

    /**
     * Resets the stopwatch
     */
    public void reset() {
        this.startTime = -1;
        this.endTime = -1;
    }
}
