package com.artimesia.dynamometer;

public class Parameter extends Param{
    // 최대 악력점
    // maxGripStrengthPoint
    private GripStrength P1;
    // 90% 악력값
    // ninetyGripStrengthValue
    private float P2;
    // 10% 악력값
    // tenGripStrengthValue
    private float P3;
    // 평균 악력값
    //  10% 시작 악력점(Point)와 90% 시작 악력점(Point) 구간의 평균값을 산정
    // averageGripStrengthValue
    private float P4;
    // 90% 시작 악력점
    // ninetyGripStrengthStartPoint
    private GripStrength P5;
    // 90% 종료 악력점
    // ninetyGripStrengthEndPoint
    private GripStrength P6;
    // 10% 시작 악력점
    // tenGripStrengthStartPoint
    private GripStrength P7;
    // 10% 종료 악력점
    // tenGripStrengthEndPoint
    private GripStrength P8;
    // 평균 시작 악력점
    // averageGripStrengthStartPoint
    private GripStrength P9;
    // 평균 종료 악력점
    // averageGripStrengthEndPoint
    private GripStrength P10;
    // 측정 시간 간격
    // 측정 시작(0)부터 종료(마지막 시간)까지 시간으로 마지막점의 시간 표시
    // measureInterval
    private long P11;
    // 시작(Start) 시간
    // 최초의 신호가 시작되는 악력점
    // startTime
    private long P12;
    // 종료(End) 시간
    // 신호가 끝나는 악력점
    // endTime
    private long P13;
    // 온셋(Onset) 시간 간격
    // 10% 시작 악력점(Point)과 90% 시작 악력점(Point)간의 millisecond
    // onsetInterval
    private long P14;
    // 지속(Endurance) 시간 간격
    // 90% 시작 악력점(Point)과 평균 종료 악력점(Point)간의 millisecond
    // enduranceInterva
    private long P15;
    // 소산(Dissipate) 시간 간격
    // 평균 종료 악력점(Point)과 마지막 측정점간의 millisecond
    // dissipateInterval
    private long P16;
    // 10% 시작 악력점이 나타나는 시간 간격
    // 첫번째 측정점과 10% 시작 악력점(Point) 간의 millisecond
    // tenGripStrengthStartInterval
    private long P17;
    // 평균 시작 악력점이 나타나는 시간 간격
    // 첫번째 측정점과 평균 시작 악력점(Point) 간의 millisecond
    // averageGripStrengthStartInterval
    private long P18;
    // 90% 시작 악력점이 나타나는 시간 간격
    // 첫번째 측정점과 90% Max 시작 악력점(Point) 간의 millisecond
    // ninetyGripStrengthStartInterval
    private long P19;
    // 최대 악력점이 나타나는 시간 간격
    // 첫번째 측정점과 최대 악력점(Point) 간의 millisecond
    // maxGripStrengthInterval
    private long P20;
    // 평균 종료 악력점이 나타나는 시간 간격
    // 첫번째 측정점과 평균 종료 악력점(Point)간의 millisecond
    // averageGripStrengthEndInterval
    private long P21;

    public Parameter()
    {}

    public Parameter(GripStrength p1, float p2, float p3, float p4, GripStrength p5, GripStrength p6, GripStrength p7, GripStrength p8, GripStrength p9, GripStrength p10, long p11, long p12, long p13, long p14, long p15, long p16, long p17, long p18, long p19, long p20, long p21) {
        P1 = p1;
        P2 = p2;
        P3 = p3;
        P4 = p4;
        P5 = p5;
        P6 = p6;
        P7 = p7;
        P8 = p8;
        P9 = p9;
        P10 = p10;
        P11 = p11;
        P12 = p12;
        P13 = p13;
        P14 = p14;
        P15 = p15;
        P16 = p16;
        P17 = p17;
        P18 = p18;
        P19 = p19;
        P20 = p20;
        P21 = p21;
    }

    public void setParameterList(GripStrength p1, float p2, float p3, float p4, GripStrength p5, GripStrength p6, GripStrength p7, GripStrength p8, GripStrength p9, GripStrength p10, long p11, long p12, long p13, long p14, long p15, long p16, long p17, long p18, long p19, long p20, long p21) {
        P1 = p1;
        P2 = p2;
        P3 = p3;
        P4 = p4;
        P5 = p5;
        P6 = p6;
        P7 = p7;
        P8 = p8;
        P9 = p9;
        P10 = p10;
        P11 = p11;
        P12 = p12;
        P13 = p13;
        P14 = p14;
        P15 = p15;
        P16 = p16;
        P17 = p17;
        P18 = p18;
        P19 = p19;
        P20 = p20;
        P21 = p21;
    }

    public GripStrength getP1() {
        return P1;
    }

    public void setP1(GripStrength p1) {
        P1 = p1;
    }

    public float getP2() {
        return P2;
    }

    public void setP2(float p2) {
        P2 = p2;
    }

    public float getP3() {
        return P3;
    }

    public void setP3(float p3) {
        P3 = p3;
    }

    public float getP4() {
        return P4;
    }

    public void setP4(float p4) {
        P4 = p4;
    }

    public GripStrength getP5() {
        return P5;
    }

    public void setP5(GripStrength p5) {
        P5 = p5;
    }

    public GripStrength getP6() {
        return P6;
    }

    public void setP6(GripStrength p6) {
        P6 = p6;
    }

    public GripStrength getP7() {
        return P7;
    }

    public void setP7(GripStrength p7) {
        P7 = p7;
    }

    public GripStrength getP8() {
        return P8;
    }

    public void setP8(GripStrength p8) {
        P8 = p8;
    }

    public GripStrength getP9() {
        return P9;
    }

    public void setP9(GripStrength p9) {
        P9 = p9;
    }

    public GripStrength getP10() {
        return P10;
    }

    public void setP10(GripStrength p10) {
        P10 = p10;
    }

    public long getP11() {
        return P11;
    }

    public void setP11(long p11) {
        P11 = p11;
    }

    public long getP12() {
        return P12;
    }

    public void setP12(long p12) {
        P12 = p12;
    }

    public long getP13() {
        return P13;
    }

    public void setP13(long p13) {
        P13 = p13;
    }

    public long getP14() {
        return P14;
    }

    public void setP14(long p14) {
        P14 = p14;
    }

    public long getP15() {
        return P15;
    }

    public void setP15(long p15) {
        P15 = p15;
    }

    public long getP16() {
        return P16;
    }

    public void setP16(long p16) {
        P16 = p16;
    }

    public long getP17() {
        return P17;
    }

    public void setP17(long p17) {
        P17 = p17;
    }

    public long getP18() {
        return P18;
    }

    public void setP18(long p18) {
        P18 = p18;
    }

    public long getP19() {
        return P19;
    }

    public void setP19(long p19) {
        P19 = p19;
    }

    public long getP20() {
        return P20;
    }

    public void setP20(long p20) {
        P20 = p20;
    }

    public long getP21() {
        return P21;
    }

    public void setP21(long p21) {
        P21 = p21;
    }

    @Override
    public String toString() {
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
    }
}