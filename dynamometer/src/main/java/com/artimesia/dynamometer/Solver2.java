package com.artimesia.dynamometer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * solver2
 * 측정 위치 기준
 * 손가락별 연속적인 데이터 합
 * 손가락별 평균, 분산, 표준편차
 */

public class Solver2 {
    private Parameter2 parameter2;
    private ArrayList<GripStrength> gripStrengthList;

    private String finger; //손가락 지정
    private String user; //사용자 이름
    private String hand; //좌,우
    private String mobile; //핸폰

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
    double avg;

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
    double std = 0.0;       //표준편차

    public Solver2(String name, ArrayList<GripStrength> gripStrengthList, String user, String hand, String mobile, Parameter2 parameter) {
        // 손가락 지정
        this.finger = name;
        // 악력값 리스트 받기
        this.gripStrengthList = gripStrengthList;

        this.user = user;
        this.hand = hand;
        this.mobile = mobile;
        this.parameter2 = parameter;

        this.maxGripStrengthPoint = new GripStrength();
        this.tenGripStrengthStartPoint = new GripStrength();
        this.tenGripStrengthEndPoint = new GripStrength();
    }

    public boolean analyse() {
        ListIterator<GripStrength> iterator;
        iterator = gripStrengthList.listIterator();

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
        tenGripStrengthValue = Double.parseDouble( String.format("%.1f", maxGripStrengthPoint.getValue() * 0.1F ));


        /**
         *  10% 시작 악력점(Point)
         */
        iterator = gripStrengthList.listIterator();
        while (iterator.hasNext()) {
            GripStrength tempGripStrength = iterator.next();
            if(tempGripStrength.getValue() >= tenGripStrengthValue){
                tenGripStrengthStartPoint = tempGripStrength;
                break;
            }
        }

        /**
         *  10% 종료 악력점(Point)
         */
        iterator = gripStrengthList.listIterator(gripStrengthList.size());
        while (iterator.hasPrevious()) {
            GripStrength tempGripStrength = iterator.previous();
            if(tempGripStrength.getValue() >= tenGripStrengthValue){
                tenGripStrengthEndPoint = tempGripStrength;
                break;
            }
        }

        /**
         *  누적 악력값 구하기
         *  평균 악력값(Scalar) 구하기
         *  10% 시작 악력점(Point)와 10% 종료 악력점(Point) 구간의 평균값을 산정
         */
        double sumValue = 0.0;
        int count = 0;

        for(int i = gripStrengthList.indexOf(tenGripStrengthStartPoint); i <= gripStrengthList.indexOf(tenGripStrengthEndPoint); i++){
            if (gripStrengthList.get(i).getValue() > 0) {
                sumValue += gripStrengthList.get(i).getValue();
                count++;
            }
        }

        //Double.parseDouble( String.format("%.1f", maxGripStrengthPoint.getValue() * 0.1F ));
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
        for(int i = gripStrengthList.indexOf(tenGripStrengthStartPoint); i <= gripStrengthList.indexOf(tenGripStrengthEndPoint); i++){
            if (gripStrengthList.get(i).getValue() > 0) {
                float temp = gripStrengthList.get(i).getValue();
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
        parameter2.setParameterList(sum, avg, var, std);
        parameter2.setInfomation( user, hand, finger, mobile);
        return true;
    }

    public void printResult(){
        System.out.println("--------------"+this.user+","+this.hand+","+ this.finger+" 합, 평균, 분산, 표준편차---------------");
        System.out.println("[Check]10% 시작(start) 악력값[값 : "+ tenGripStrengthValue +"]");
        System.out.println("[Check]10% 종료(end) 악력값[값 : "+ tenGripStrengthEndPoint.getValue() +"]");
        System.out.println("[P22]합력[값 : "+ sum +"]");
        System.out.println("[P23]평균[값 : "+ avg +"]");
        System.out.println("[P24]분산[값 : "+ var +"]");
        System.out.println("[P25]표준편차[값 : "+ std +"]");
    }

    public void saveParamCsv(){
        String fileName = user+"_"+ hand+"_" + mobile+"_"+"param2"+".csv";
        OutputStreamWriter bufferedWriter = null;
        FileOutputStream fileOutStrm = null;

        try {
            fileOutStrm = new FileOutputStream(fileName,true);
            bufferedWriter = new OutputStreamWriter( fileOutStrm, "MS949");
            //bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "MS949"));

            bufferedWriter.write("["+this.hand +"'"+ this.finger+"Key Parameter]"); bufferedWriter.write("\n");
            bufferedWriter.write("Contents" + "," + "Value" ); bufferedWriter.write("\n");
            bufferedWriter.write("[P22]악력합[값 "+","+ sum); bufferedWriter.write("\n");
            bufferedWriter.write("[P23]평균[값 "+ "," + avg ); bufferedWriter.write("\n");
            bufferedWriter.write("[P24]분산[값 "+ "," + var ); bufferedWriter.write("\n");
            bufferedWriter.write("[P25]표준편차[값 "+ "," + std ); bufferedWriter.write("\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush(); // 남아있는 데이터까지 보내 준다
                    bufferedWriter.close(); // 사용한 BufferedWriter를 닫아 준다
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}





















