package com.artimesia.dynamometer;

/*
 * 시간별 Cumulative Sum
 * 시간별 평균치, 분산, 표준편차
 */
public class Parameter3 extends Param{
    double cumlativeSumValue = 0.0;             //누적
    double averageGripStrengthValue = 0.0;      //평균
    double varianGripStrengthValue = 0.0;       //분산
    double sdGripStrengthValue = 0.0;           //표준편차

    public Parameter3()
    {}

    public Parameter3(double sum, double avr, double varian, double sd )
    {
        this.cumlativeSumValue = sum;
        this.averageGripStrengthValue = avr;
        this.varianGripStrengthValue = varian;
        this.sdGripStrengthValue = sd;
    }

    public void setParameterList(double sum, double avr, double varian, double sd )
    {
        this.cumlativeSumValue = sum;
        this.averageGripStrengthValue = avr;
        this.varianGripStrengthValue = varian;
        this.sdGripStrengthValue = sd;
    }

    public void setInfomation(String user, String hand, String finger, String mobile)
    {
        this.user = user;
        this.hand = hand;
        this.finger = finger;
        this.mobile = mobile;
    }

    public double getAvr() {
        return averageGripStrengthValue;
    }

    public double getCumlativeSumValue()
    {
        return cumlativeSumValue;
    }
    public double getVarianValue()
    {
        return varianGripStrengthValue;
    }
    public double getSd()
    {
        return sdGripStrengthValue;
    }
    public String getUser() {
        return user;
    }
    public String getHand() {
        return hand;
    }
    public String getFinger() {
        return finger;
    }
    public String getMobile() {
        return mobile;
    }
    public String toString() {
        /*
        return "{\"Parameter\":{"
                + "\"P1\":\"" + P1.getMillisecond() + ", " + P1.getValue() +"\""
                + ", \"P2\":\"" + P2 + "\""
                + ", \"P3\":\"" + P3 + "\""
                + ", \"P4\":\"" + P4 + "\""
                + ", \"P5\":\"" + P5.getMillisecond() + ", " + P5.getValue() +"\""
                + ", \"P6\":\"" + P6.getMillisecond() + ", " + P6.getValue() +"\""
                + ", \"P7\":\"" + P7.getMillisecond() + ", " + P7.getValue() +"\""
                + ", \"P8\":\"" + P8.getMillisecond() + ", " + P8.getValue() +"\""
                + ", \"P9\":\"" + P9.getMillisecond() + ", " + P9.getValue() +"\""
                + ", \"P10\":\"" + P10.getMillisecond() + ", " + P10.getValue() +"\""
                + ", \"P11\":\"" + P11 + "\""
                + ", \"P12\":\"" + P12 + "\""
                + ", \"P13\":\"" + P13 + "\""
                + ", \"P14\":\"" + P14 + "\""
                + ", \"P15\":\"" + P15 + "\""
                + ", \"P16\":\"" + P16 + "\""
                + ", \"P17\":\"" + P17 + "\""
                + ", \"P18\":\"" + P18 + "\""
                + ", \"P19\":\"" + P19 + "\""
                + ", \"P20\":\"" + P20 + "\""
                + ", \"P21\":\"" + P21 + "\""
                + "}}";
         */
        return "";
    }



}
