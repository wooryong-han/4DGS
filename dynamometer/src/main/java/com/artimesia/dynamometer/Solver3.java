
package com.artimesia.dynamometer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.ListIterator;

/*
 * 시간별 손가락별 합력(전체합)
 * 시간별 평균, 분산, 표준편차
 */
public class Solver3 {
    private Parameter3 parameter3;

    private String finger; //손가락 지정
    private String user;   //사용자 이름
    private String hand;   //좌,우
    private String mobile; //핸폰

    private ArrayList<GripStrength> indexGripStrengthList;
    private ArrayList<GripStrength> middleGripStrengthList;
    private ArrayList<GripStrength> ringGripStrengthList;
    private ArrayList<GripStrength> littleGripStrengthList;

    private ArrayList<GripStrength> totalGripStrengthList;

    long getTime; //측정 시각

    /**
     * 최대 악력점(Point)
     * Max Voltage
     */
    GripStrength maxGripStrengthPoint;

    /**
     *  10% 시작 악력점(Point)
     */
    GripStrength tenGripStrengthStartPoint;

    /**
     *  10% 악력값(Scalar)
     *  10% Max Out Valtage
     */
    double tenGripStrengthValue;

    /**
     *  10% 종료 악력점(Point)
     */
    GripStrength tenGripStrengthEndPoint;

    /**
     *  평균 악력값(Scalar)
     *  Average Voltage between 10% Max and 90% Max
     *  10% 시작 악력점(Point)와 90% 시작 악력점(Point) 구간의 평균값을 산정
     */
    double avg = 0.0;

    /**
     *  연속적인 누적합
     *  10% 시작 악력점(Point)에서 10% 종료까지 악력값
     *  분산/표준편차
     */
    double sum = 0.0;

    /**
     *  분산/표준편차
     */
    double var = 0.0;       //분산
    double std = 0.0;           //표준편차

    public Solver3(String user, String hand, String mobile, ArrayList<GripStrength> indexList, ArrayList<GripStrength> middleList,
                   ArrayList<GripStrength> ringList, ArrayList<GripStrength> littleList, Parameter3 parameter) {
        indexGripStrengthList = indexList;
        middleGripStrengthList = middleList;
        ringGripStrengthList = ringList;
        littleGripStrengthList = littleList;

        this.maxGripStrengthPoint = new GripStrength();
        this.tenGripStrengthStartPoint = new GripStrength();
        this.tenGripStrengthEndPoint = new GripStrength();

        totalGripStrengthList = new ArrayList<GripStrength>();

        this.user = user;
        this.hand = hand;
        this.mobile = mobile;
        this.parameter3 = parameter;
    }

    public boolean analyse() {
        ListIterator<GripStrength> iterator;
        ArrayList<GripStrength> list = null;
        float listSum = 0.0f; int count = 0;

        /**
         *  시간대별 손가락 합력을 위한 List
         */
        iterator = indexGripStrengthList.listIterator();
        while (iterator.hasNext()) {
            GripStrength tempGripStrength = iterator.next();
            getTime = tempGripStrength.getMillisecond();
            listSum+= tempGripStrength.getValue();

            for( int i=0; i<3; i++) {
                if( i == 0 ) list = middleGripStrengthList;
                if( i == 1 ) list = ringGripStrengthList;
                if( i == 2 ) list = littleGripStrengthList;
                listSum += searchValueListByTime( getTime, list);
            }

            //시간대별 총합 리스트를 만든다.
            totalGripStrengthList.add(new GripStrength( getTime, (listSum > 0.15F) ? listSum : 0.0F));
            count++;
        }

        /**
         *  누적 악력값 구하기
         *  평균 악력값(Scalar) 구하기
         *  10% 시작 악력점(Point)와 10% 종료 악력점(Point) 구간의 평균값을 산정
         */
        iterator = totalGripStrengthList.listIterator();

        /**
         * 최대 악력점(Point) 구하기
         * Max Voltage
         */
        while (iterator.hasNext()) {
            GripStrength tempGripStrength = iterator.next();
            if(tempGripStrength.getValue() > maxGripStrengthPoint.getValue()){
                maxGripStrengthPoint = tempGripStrength;
            }
        }

        /**
         *  10% 악력값(Scalar) 구하기
         *  10% Max Out Valtage
         */
        tenGripStrengthValue = maxGripStrengthPoint.getValue() * 0.1F;

         //10% 시작 악력점(Point)
        iterator = totalGripStrengthList.listIterator();
        while (iterator.hasNext()) {
            GripStrength tempGripStrength = iterator.next();
            if(tempGripStrength.getValue() >= tenGripStrengthValue){
                tenGripStrengthStartPoint = tempGripStrength;
                break;
            }
        }

         //10% 종료 악력점(Point)
        iterator = totalGripStrengthList.listIterator(totalGripStrengthList.size());
        while (iterator.hasPrevious()) {
            GripStrength tempGripStrength = iterator.previous();
            if(tempGripStrength.getValue() >= tenGripStrengthValue){
                tenGripStrengthEndPoint = tempGripStrength;
                break;
            }
        }

        /**
         *  시간대별 손가락 누적합악력값 구하기
         *  평균 악력값(Scalar) 구하기
         *  10% 시작 악력점(Point)와 10% 종료 악력점(Point) 구간의 평균값을 산정
         */
        double sumValue = 0.0;
        count = 0;

        for(int i = totalGripStrengthList.indexOf(tenGripStrengthStartPoint); i <= totalGripStrengthList.indexOf(tenGripStrengthEndPoint); i++){
            if (totalGripStrengthList.get(i).getValue() > 0) {
                sumValue += totalGripStrengthList.get(i).getValue();
                count++;
            }
        }

        //cumlativeSumValue = sumValue;
        sum = Double.parseDouble( String.format("%.1f", sumValue ));

        if (count == 0){
            avg = 0.0;
        } else {
            //averageGripStrengthValue = sumValue / count;
            avg = Double.parseDouble( String.format("%.1f", sumValue/count ));
        }


        /**
         *  분산
         *  10% 시작 악력점(Point)와 10% 종료 악력점(Point) 구간의 분산
         */
        double  sumVariance = 0.0;
        count = 0;
        for(int i = totalGripStrengthList.indexOf(tenGripStrengthStartPoint); i <= totalGripStrengthList.indexOf(tenGripStrengthEndPoint); i++){
            if (totalGripStrengthList.get(i).getValue() > 0) {
                float temp = totalGripStrengthList.get(i).getValue();
                sumVariance += Math.pow( (temp - avg), 2 );
                count++;
            }
        }
        //varianGripStrengthValue =  sumVariance/count;
        var = Double.parseDouble( String.format("%.1f", sumVariance/count ));

        /**
         *  표준편차
         *  10% 시작 악력점(Point)와 10% 종료 악력점(Point) 구간의 표준편차
         *  */
        //sdGripStrengthValue = Math.sqrt( varianGripStrengthValue );
        std = Double.parseDouble( String.format("%.1f", Math.sqrt( var ) ));

        parameter3.setParameterList(sum, avg, var, std);
        parameter3.setInfomation( user, hand, finger, mobile);
        return true;
    }

    public float searchValueListByTime( long searchTime, ArrayList<GripStrength> list)
    {
        float findValue = 0.0f;
        ListIterator<GripStrength> iterator = list.listIterator();

        while (iterator.hasNext()) {
            GripStrength tempGripStrength = iterator.next();
            if( tempGripStrength.getMillisecond() == searchTime )
            {
                findValue = tempGripStrength.getValue();
                break;
            }
        }
        return findValue;
    }

    public void printResult(){
        System.out.println("--------------"+this.user+","+this.hand+" 동시간별  합, 평균, 분산, 표준편차---------------");
        System.out.println("[Check]10% 시작(start) 악력값[값 : "+ Double.parseDouble( String.format("%.1f", Math.sqrt( tenGripStrengthValue ) )) +"]");
        System.out.println("[Check]10% 종료(end) 악력값[값 : "+ Double.parseDouble( String.format("%.1f", Math.sqrt( tenGripStrengthEndPoint.getValue() ) ))  +"]");
        System.out.println("[P26]동시간별 악력합[값 : "+ sum +"]");
        System.out.println("[P27]동시간별 평균[값 : "+ avg +"]");
        System.out.println("[P28]동시간별 분산[값 : "+ var +"]");
        System.out.println("[P29]동시간별 표준편차[값 : "+ std +"]");
    }
}