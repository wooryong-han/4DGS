package com.artimesia.dynamometer;

import java.util.ArrayList;

/*
 * 1. 오른손/왼손 손가락별 좌우 누적합의 일치도 -> parameter2 사용
 * 2. 오른손/왼손 손가락별 좌우 평균/분산/표준편차 일치도 -> parameter2 사용
 * 3. 오른손/왼손 시간별 손가락 합력에 대한 일치도 -> parameter3 사용
 * 4. 오른손/왼손 시간별 손가락 합력에 대한 평균, 분산, 표준편차  -> parameter3 사용
 */

public class Parameter4 extends Param{
    //손가락별 합, 평균, 분산, 표준편차
    private double[] indexMatchRate = new double[4];  //Sum(합), Avg(평균), Varian(분산), SD(표준편차)
    private double[] middleMatchRate = new double[4]; //Sum(합), Avg(평균), Varian(분산), SD(표준편차)
    private double[] ringMatchRate = new double[4];   //Sum(합), Avg(평균), Varian(분산), SD(표준편차)
    private double[] littleMatchRate = new double[4]; //Sum(합), Avg(평균), Varian(분산), SD(표준편차)

    //시간별 손가락 합, 평균, 분산, 표준편차
    private double[] totalMatchRate = new double[4]; //Sum(합), Avg(평균), Varian(분산), SD(표준편차)

    public Parameter4(){
    }

    public void setIndexMatchRate(double sum, double avg, double varian, double sd) {
        indexMatchRate[0] = sum;
        indexMatchRate[1] = avg;
        indexMatchRate[2] = varian;
        indexMatchRate[3] = sd;
    }

    public double getIndexMatchRateSum() {
        return indexMatchRate[0];
    }

    public double getIndexMatchRateAvg() {
        return indexMatchRate[1];
    }

    public double getIndexMatchRateVarian() {
        return indexMatchRate[2];
    }

    public double getIndexMatchRateSD() {
        return indexMatchRate[3];
    }

    public void setMiddleMatchRate(double sum, double avg, double varian, double sd) {
        middleMatchRate[0] = sum;
        middleMatchRate[1] = avg;
        middleMatchRate[2] = varian;
        middleMatchRate[3] = sd;
    }

    public double getMiddleMatchRateSum() {
        return middleMatchRate[0];
    }

    public double getMiddleMatchRateAvg() {
        return middleMatchRate[1];
    }

    public double getMiddleMatchRateVarian() {
        return middleMatchRate[2];
    }

    public double getMiddleMatchRateSD() {
        return middleMatchRate[3];
    }

    public void setRingMatchRate(double sum, double avg, double varian, double sd) {
        ringMatchRate[0] = sum;
        ringMatchRate[1] = avg;
        ringMatchRate[2] = varian;
        ringMatchRate[3] = sd;
    }

    public double getRingMatchRateSum() {
        return ringMatchRate[0];
    }

    public double getRingMatchRateAvg() {
        return ringMatchRate[1];
    }

    public double getRingMatchRateVarian() {
        return ringMatchRate[2];
    }

    public double getRingMatchRateSD() {
        return ringMatchRate[3];
    }

    public void setLittleMatchRate(double sum, double avg, double varian, double sd) {
        littleMatchRate[0] = sum;
        littleMatchRate[1] = avg;
        littleMatchRate[2] = varian;
        littleMatchRate[3] = sd;
    }

    public double getLittleMatchRateSum() {
        return littleMatchRate[0];
    }

    public double getLittleMatchRateAvg() {
        return littleMatchRate[1];
    }

    public double getLittleMatchRateVarian() {
        return littleMatchRate[2];
    }

    public double getLittleMatchRateSD() {
        return littleMatchRate[3];
    }

    public void setTotalMatchRate(double sum, double avg, double varian, double sd)
    {
        totalMatchRate[0] = sum;
        totalMatchRate[1] = avg;
        totalMatchRate[2] = varian;
        totalMatchRate[3] = sd;
    }

    public double getTotalMatchRateSum() {
        return totalMatchRate[0];
    }

    public double getTotalMatchRateAvg() {
        return totalMatchRate[1];
    }

    public double getTotalMatchRateVarian() {
        return totalMatchRate[2];
    }

    public double getTotalMatchRateSD() {
        return totalMatchRate[3];
    }

    public String toString() {
        return "{\"일치도 \":{"
                + "\"P1[Index Sum Match Rate]\":\""      + String.format("%.1f", indexMatchRate[0]) +"\""
                + ", \"P2[Index Avg Match Rate]\":\""    + String.format("%.1f", indexMatchRate[1]) + "\""
                + ", \"P3[Index Varian Match Rate]\":\"" + String.format("%.1f", indexMatchRate[2]) + "\""
                + ", \"P4[Index SD Match Rate]\":\""     + String.format("%.1f", indexMatchRate[3]) + "\""

                + ", \"P5[Middle Sum Match Rate]\":\""    + String.format("%.1f", middleMatchRate[0]) + "\""
                + ", \"P6[Middle Avg Match Rate]\":\""    + String.format("%.1f", middleMatchRate[1]) + "\""
                + ", \"P7[Middle Varian Match Rate]\":\"" + String.format("%.1f", middleMatchRate[2]) + "\""
                + ", \"P8[Middle SD Match Rate]\":\""     + String.format("%.1f", middleMatchRate[3]) + "\""

                + ", \"P9[Ring Sum Match Rate]\":\""      + String.format("%.1f", ringMatchRate[0]) + "\""
                + ", \"P10[Ring Avg Match Rate]\":\""     + String.format("%.1f", ringMatchRate[1]) + "\""
                + ", \"P11[Ring Varian Match Rate]\":\""  + String.format("%.1f", ringMatchRate[2]) + "\""
                + ", \"P12[Ring SD Match Rate]\":\""      + String.format("%.1f", ringMatchRate[3]) + "\""

                + ", \"P13[Little Sum Match Rate]\":\""     + String.format("%.1f", littleMatchRate[0]) + "\""
                + ", \"P14[Little Avg Match Rate]\":\""     + String.format("%.1f", littleMatchRate[1]) + "\""
                + ", \"P15[Little Varian Match Rate]\":\""  + String.format("%.1f", littleMatchRate[2]) + "\""
                + ", \"P16[Little SD Match Rate]\":\""      + String.format("%.1f", littleMatchRate[3]) + "\""

                + ", \"P17[Total Sum Match Rate]\":\""     + String.format("%.1f", totalMatchRate[0]) + "\""
                + ", \"P18[Total Avg Match Rate]\":\""     + String.format("%.1f", totalMatchRate[1]) + "\""
                + ", \"P19[Total Varian Match Rate]\":\""  + String.format("%.1f", totalMatchRate[2]) + "\""
                + ", \"P20[Total SD Match Rate]\":\""      + String.format("%.1f", totalMatchRate[3]) + "\""
                + "}}";
    }

}
