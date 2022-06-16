package com.artimesia.dynamometer;

/**
 *
 */
public class GripStrength {
    private long millisecond;
    private float value;

    public GripStrength(long millisecond, float value) {
        this.millisecond = millisecond;
        this.value = value;
    }
    public GripStrength() {
        this.millisecond = 0L;
        this.value = 0.0F;
    }

    public long getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(long millisecond) {
        this.millisecond = millisecond;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
