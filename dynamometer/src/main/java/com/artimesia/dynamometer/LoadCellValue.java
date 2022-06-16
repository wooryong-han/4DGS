package com.artimesia.dynamometer;

public class LoadCellValue {
    public LoadCellValue(long millisecond, float indexValue, float middleValue, float ringValue, float littleValue) {
        this.millisecond = millisecond;
        this.indexValue = indexValue;
        this.middleValue = middleValue;
        this.ringValue = ringValue;
        this.littleValue = littleValue;
    }

    private long millisecond;
    private float indexValue;
    private float middleValue;
    private float ringValue;
    private float littleValue;

    public long getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(long millisecond) {
        this.millisecond = millisecond;
    }

    public float getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(float indexValue) {
        this.indexValue = indexValue;
    }

    public float getMiddleValue() {
        return middleValue;
    }

    public void setMiddleValue(float middleValue) {
        this.middleValue = middleValue;
    }

    public float getRingValue() {
        return ringValue;
    }

    public void setRingValue(float ringValue) {
        this.ringValue = ringValue;
    }

    public float getLittleValue() {
        return littleValue;
    }

    public void setLittleValue(float littleValue) {
        this.littleValue = littleValue;
    }

}
