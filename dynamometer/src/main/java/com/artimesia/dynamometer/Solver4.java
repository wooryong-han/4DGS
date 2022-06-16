package com.artimesia.dynamometer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.ListIterator;

/*
 * 손가락별 합, 평균, 분산, 표준편차 일치도
 *
 * 시간별 손가락별 합력(전체합)
 * 시간별 평균, 분산, 표준편차
 */

public class Solver4{
    private String user;   //사용자 이름
    //private String finger; //손가락 지정
    //private String hand;   //좌,우
    //private String mobile; //핸폰

    private Param[] rightParam;
    private Param[] leftParam;

    private Parameter4 P4;

    public Solver4( String user, Param[] rightParam, Param[] leftParam, Parameter4 P4 ) {
        this.user = user;
        this.rightParam=rightParam;
        this.leftParam=leftParam;
        this.P4 = P4;
    }

    public boolean analyse() {
        matchRateIndex( (Parameter2) rightParam[0], (Parameter2) leftParam[0] );
        matchRateMiddle( (Parameter2) rightParam[1], (Parameter2) leftParam[1] );
        matchRateRing( (Parameter2) rightParam[2], (Parameter2) leftParam[2] );
        matchRateLittle( (Parameter2) rightParam[3], (Parameter2) leftParam[3] );

        matchRateTotal( (Parameter3) rightParam[4], (Parameter3) leftParam[4] );
        return true;
    }

    //Index 일치도 구하기 (합력/평균/분산/표준편차)
    public void matchRateIndex( Parameter2 rightP2, Parameter2 leftP2 )
    {
        double sum = matchRate( rightP2.getCumlativeSumValue(), leftP2.getCumlativeSumValue() );
        double avr = matchRate( rightP2.getAvr(), leftP2.getAvr() );
        double varian = matchRate( rightP2.getVarianValue(), leftP2.getVarianValue());
        double sd = matchRate( rightP2.getSd(), leftP2.getSd() );

        P4.setIndexMatchRate( sum, avr, varian, sd);
    }

    //Middle 일치도 구하기 (합력/평균/분산/표준편차)
    public void matchRateMiddle( Parameter2 rightP2, Parameter2 leftP2 )
    {
        double sum = matchRate( rightP2.getCumlativeSumValue(), leftP2.getCumlativeSumValue() );
        double avr = matchRate( rightP2.getAvr(), leftP2.getAvr() );
        double varian = matchRate( rightP2.getVarianValue(), leftP2.getVarianValue());
        double sd = matchRate( rightP2.getSd(), leftP2.getSd() );

        P4.setMiddleMatchRate( sum, avr, varian, sd);
    }

    //Ring 일치도 구하기 (합력/평균/분산/표준편차)
    public void matchRateRing( Parameter2 rightP2, Parameter2 leftP2 )
    {
        double sum = matchRate( rightP2.getCumlativeSumValue(), leftP2.getCumlativeSumValue() );
        double avr = matchRate( rightP2.getAvr(), leftP2.getAvr() );
        double varian = matchRate( rightP2.getVarianValue(), leftP2.getVarianValue());
        double sd = matchRate( rightP2.getSd(), leftP2.getSd() );

        P4.setRingMatchRate( sum, avr, varian, sd);
    }

    //Little 일치도 구하기 (합력/평균/분산/표준편차)
    public void matchRateLittle( Parameter2 rightP2, Parameter2 leftP2 )
    {
        double sum = matchRate( rightP2.getCumlativeSumValue(), leftP2.getCumlativeSumValue() );
        double avr = matchRate( rightP2.getAvr(), leftP2.getAvr() );
        double varian = matchRate( rightP2.getVarianValue(), leftP2.getVarianValue());
        double sd = matchRate( rightP2.getSd(), leftP2.getSd() );

        P4.setLittleMatchRate( sum, avr, varian, sd);
    }

    //
    public void matchRateTotal( Parameter3 rightP3, Parameter3 leftP3 )
    {
        double sum = matchRate( rightP3.getCumlativeSumValue(), leftP3.getCumlativeSumValue() );
        double avr = matchRate( rightP3.getAvr(), leftP3.getAvr() );
        double varian = matchRate( rightP3.getVarianValue(), leftP3.getVarianValue());
        double sd = matchRate( rightP3.getSd(), leftP3.getSd() );

        P4.setTotalMatchRate( sum, avr, varian, sd);
    }

    double matchRate( double right, double left )
    {
        double result = 100 - Math.abs( (((right-left)/(right)) * 100) ) ;
        return result;
    }

    public void printResult(){
        System.out.println("---------------------"+this.user+"  합, 평균, 분산, 표준편차 일치도---------------------");
        System.out.println("[P30]Index 합력 일치도[값 : "      + String.format("%.1f", P4.getIndexMatchRateSum()) +"]");
        System.out.println("[P31]Index 평균 일치도[값 : "      + String.format("%.1f", P4.getIndexMatchRateAvg()) +"]");
        System.out.println("[P32]Index 분산 일치도[값 : "      + String.format("%.1f", P4.getIndexMatchRateVarian()) +"]");
        System.out.println("[P33]Index 표준편차 일치도[값 : "  + String.format("%.1f", P4.getIndexMatchRateSD()) +"]");

        System.out.println("[P34]Middle 합력 일치도[값 : "     + String.format("%.1f", P4.getMiddleMatchRateSum()) +"]");
        System.out.println("[P35]Middle 평균 일치도[값 : "     + String.format("%.1f", P4.getMiddleMatchRateAvg()) +"]");
        System.out.println("[P36]Middle 분산 일치도[값 : "     + String.format("%.1f", P4.getMiddleMatchRateVarian()) +"]");
        System.out.println("[P37]Middle 표준편차 일치도[값 : " + String.format("%.1f", P4.getMiddleMatchRateSD()) +"]");

        System.out.println("[P38]Ring 합력 일치도[값 : "       + String.format("%.1f", P4.getRingMatchRateSum()) +"]");
        System.out.println("[P39]Ring 평균 일치도[값 : "      + String.format("%.1f", P4.getRingMatchRateAvg()) +"]");
        System.out.println("[P40]Ring 분산 일치도[값 : "      + String.format("%.1f", P4.getRingMatchRateVarian()) +"]");
        System.out.println("[P41]Ring 표준편차 일치도[값 : "  + String.format("%.1f", P4.getRingMatchRateSD()) +"]");

        System.out.println("[P42]Little 합력 일치도[값 : "    + String.format("%.1f", P4.getLittleMatchRateSum()) +"]");
        System.out.println("[P43]Little 평균 일치도[값 : "    + String.format("%.1f", P4.getLittleMatchRateAvg()) +"]");
        System.out.println("[P44]Little 분산 일치도[값 : "    + String.format("%.1f", P4.getLittleMatchRateVarian()) +"]");
        System.out.println("[P45]Little 표준편차 일치도[값 : "+ String.format("%.1f", P4.getLittleMatchRateSD()) +"]");

        System.out.println("[P46]동시간대별 합력 일치도[값 : "    + String.format("%.1f", P4.getTotalMatchRateSum()) +"]");
        System.out.println("[P47]동시간대별 평균 일치도[값 : "    + String.format("%.1f", P4.getTotalMatchRateAvg()) +"]");
        System.out.println("[P48]동시간대별 분산 일치도[값 : "    + String.format("%.1f", P4.getTotalMatchRateVarian()) +"]");
        System.out.println("[P49]동시간대별 표준편차 일치도[값 : "+ String.format("%.1f", P4.getTotalMatchRateSD()) +"]");
    }
}
