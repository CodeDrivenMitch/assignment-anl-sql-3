package com.insidion.anl.o3;

import java.util.Date;

public class Stopwatch {
    /**
     * Fields
     */
    private Date startTime;
    private Date endTime;

    /**
     * Starts the stopwatch
     */
    public void start() {
        // reset it just to be sure
        reset();
        this.startTime = new Date();
    }

    /**
     * Stops the stopwatch
     */
    public void stop() {
        this.endTime = new Date();
    }

    /**
     * Gets the result of the stopwatch
     * @return Result of the measurement
     */
    public long result() {
        if(this.startTime == null || this.endTime == null) {
            return -1;
        }

        return this.endTime.getTime() - this.startTime.getTime();
    }

    /**
     * Stops the stopwatch and returns the result
     * @return Result of the measurement
     */
    public long stopAndResult() {
        this.endTime = new Date();
        return result();
    }

    /**
     * Resets the stopwatch
     */
    public void reset() {
        this.startTime = null;
        this.endTime = null;
    }
}
