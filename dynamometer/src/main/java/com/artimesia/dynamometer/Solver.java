package com.artimesia.dynamometer;

import java.io.*;
import java.util.*;

public class Solver {
    /*
     * Parameter 값 정의
     */
    Parameter parameter;
    /**
     * 손가락 위치를 지정합니다.(Indox, Middle, Ring, Little)
     */
    private String finger;
    /**
     * 최대 악력점(Point)
     * Max Voltage
     */
    GripStrength maxGripStrengthPoint;
    /**
     *  90% 악력값(Scalar)
     *  90% Max Out Valtage
     */
    float ninetyGripStrengthValue;
    /**
     *  10% 악력값(Scalar)
     *  10% Max Out Valtage
     */
    float tenGripStrengthValue;
    /**
     *  평균 악력값(Scalar)
     *  Average Voltage between 10% Max and 90% Max
     *  10% 시작 악력점(Point)와 90% 시작 악력점(Point) 구간의 평균값을 산정
     */
    float averageGripStrengthValue;
    /**
     *  90% 시작 악력점(Point)
     */
    GripStrength ninetyGripStrengthStartPoint;
    /**
     *  90% 종료 악력점(Point)
     */
    GripStrength ninetyGripStrengthEndPoint;
    /**
     *  10% 시작 악력점(Point)
     */
    GripStrength tenGripStrengthStartPoint;
    /**
     *  10% 종료 악력점(Point)
     */
    GripStrength tenGripStrengthEndPoint;
    /**
     *  평균 시작 악력점(Point)
     */
    GripStrength averageGripStrengthStartPoint;
    /**
     *  평균 종료 악력점(Point)
     */
    GripStrength averageGripStrengthEndPoint;
    /**
     *  측정 시간 간격(Scalar)
     *  측정 시작(0)부터 종료(마지막 시간)까지 시간으로 마지막점의 시간 표시
     */
    long measureInterval;
    /**
     *  시작 악력점(Point)
     *  최초의 신호가 시작되는 악력점
     */
    GripStrength startGripStrengthPoint;
    /**
     *  종료 악력점(Point)
     *  신호가 끝나는 악력점
     */
    GripStrength endGripStrengthPoint;
    /**
     *  시작 시간(Scalar)
     *  최초의 신호가 시작되는 기준 시간
     */
    long startTime;
    /**
     *  종료 시간(Scalar)
     *  신호가 끝나는 시간(From startTime)
     */
    long endTime;
    /**
     *  온셋 시간 간격(Scalar)
     *  The Time between 10% Max and 90% Max
     *  10% 시작 악력점(Point)과 90% 시작 악력점(Point)간의 millisecond
     */
    long onsetInterval;
    /**
     *  지속 시간 간격(Scalar)
     *  The Time between 90% Max and Average End
     *  90% 시작 악력점(Point)과 평균 종료 악력점(Point)간의 millisecond
     */
    long enduranceInterval;
    /**
     *  소산 시간 간격(Scalar)
     *  The Time between Average End and Last Point
     *  평균 종료 악력점(Point)과 마지막 측정점간의 millisecond
     */
    long dissipateInterval;
    /**
     *  10% Max가 나타나는 시간 간격(From TIME_START)(Scalar)     *
     *  첫번째 측정점과 10% 시작 악력점(Point) 간의 millisecond
     */
    long tenGripStrengthStartInterval;
    /**
     *  평균 시작 악력점(Point)가 나타나는 시간 간격(From TIME_START)(Scalar)     *
     *  첫번째 측정점과 평균 시작 악력점(Point) 간의 millisecond
     */
    long averageGripStrengthStartInterval;
    /**
     *  90% Max 시작 악력점(Point)이 나타나는 시간 간격(From TIME_START)(Scalar)     *
     *  첫번째 측정점과 90% Max 시작 악력점(Point) 간의 millisecond
     */
    long ninetyGripStrengthStartInterval;
    /**
     *  최대 악력점(Point)이 나타나는시간 간격(From TIME_START)(Scalar)     *
     *  첫번째 측정점과 최대 악력점(Point) 간의 millisecond
     */
    long maxGripStrengthInterval;
    /**
     *  평균 종료 악력점(Point)가 나타나는 상대 시간(From TIME_START)(Scalar)     *
     *  첫번째 측정점과 평균 종료 악력점(Point)간의 millisecond
     */
    long averageGripStrengthEndInterval;

    public String getName() {
        return finger;
    }

    public void setName(String name) {
        this.finger = name;
    }
    /**
     * 측정된 손가락 악력 List
     */
    private ArrayList<GripStrength> gripStrengthList;

    private String user;
    private String hand;
    private String mobile;

    public Solver(String name, ArrayList<GripStrength> gripStrengthList, String user, String hand, String mobile, Parameter parameter) {
        // 손가락 지정
        this.finger = name;
        // 악력값 리스트 받기
        this.gripStrengthList = gripStrengthList;
        // 분석 값에 대한 초기화
        this.ninetyGripStrengthValue = 0.0F;
        this.tenGripStrengthValue = 0.0F;
        this.averageGripStrengthValue = 0.0F;
        this.measureInterval = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.onsetInterval = 0;
        this.enduranceInterval = 0;
        this.dissipateInterval = 0;
        this.tenGripStrengthStartInterval = 0;
        this.averageGripStrengthStartInterval = 0;
        this.ninetyGripStrengthStartInterval = 0;
        this.maxGripStrengthInterval = 0;
        this.averageGripStrengthEndInterval = 0;
        this.startGripStrengthPoint = new GripStrength();
        this.endGripStrengthPoint = new GripStrength();
        this.maxGripStrengthPoint = new GripStrength();
        this.ninetyGripStrengthStartPoint = new GripStrength();
        this.ninetyGripStrengthEndPoint = new GripStrength();
        this.tenGripStrengthStartPoint = new GripStrength();
        this.tenGripStrengthEndPoint = new GripStrength();
        this.averageGripStrengthStartPoint = new GripStrength();
        this.averageGripStrengthEndPoint = new GripStrength();

        this.user = user;
        this.hand = hand;
        this.mobile = mobile;
        this.parameter = parameter;
    }
    /**
     * 분석 작업 시작
     */
    public boolean analyse(){

        ListIterator<GripStrength> iterator = gripStrengthList.listIterator();
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
         *  90% 악력값(Scalar) 구하기
         *  90% Max Out Valtage
         */
        ninetyGripStrengthValue = maxGripStrengthPoint.getValue() * 0.9F;
        /**
         *  10% 악력값(Scalar) 구하기
         *  10% Max Out Valtage
         */
        tenGripStrengthValue = maxGripStrengthPoint.getValue() * 0.1F;
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
         *  90% 시작 악력점(Point)
         */
        iterator = gripStrengthList.listIterator();
        while (iterator.hasNext()) {
            GripStrength tempGripStrength = iterator.next();
            if(tempGripStrength.getValue() >= ninetyGripStrengthValue){
                ninetyGripStrengthStartPoint = tempGripStrength;
                break;
            }
        }
        /**
         *  90% 종료 악력점(Point)
         */
        iterator = gripStrengthList.listIterator(gripStrengthList.size());
        while (iterator.hasPrevious()) {
            GripStrength tempGripStrength = iterator.previous();
            if(tempGripStrength.getValue() >= ninetyGripStrengthValue){
                ninetyGripStrengthEndPoint = tempGripStrength;
                break;
            }
        }
        /**
         *  평균 악력값(Scalar) 구하기
         *  Average Voltage between 10% Max and 90% Max
         *  10% 시작 악력점(Point)와 90% 시작 악력점(Point) 구간의 평균값을 산정
         */
        float sumValue = 0.0F;
        int count = 0;
        for(int i = gripStrengthList.indexOf(tenGripStrengthStartPoint); i <= gripStrengthList.indexOf(ninetyGripStrengthStartPoint); i++){
            if (gripStrengthList.get(i).getValue() > 0) {
                sumValue += gripStrengthList.get(i).getValue();
                count++;
            }
        }
        if (count == 0){
            averageGripStrengthValue = 0;
        } else {
            averageGripStrengthValue = sumValue / count;
        }
        /**
         *  평균 시작 악력점(Point)
         */
        iterator = gripStrengthList.listIterator();
        while (iterator.hasNext()) {
            GripStrength tempGripStrength = iterator.next();
            if(tempGripStrength.getValue() >= averageGripStrengthValue){
                averageGripStrengthStartPoint = tempGripStrength;
                break;
            }
        }
        /**
         *  평균 종료 악력점(Point)
         */
        iterator = gripStrengthList.listIterator(gripStrengthList.size());
        while (iterator.hasPrevious()) {
            GripStrength tempGripStrength = iterator.previous();
            if(tempGripStrength.getValue() >= averageGripStrengthValue){
                averageGripStrengthEndPoint = tempGripStrength;
                break;
            }
        }
        /**
         *  측정 시간 간격(Scalar)
         *  측정 시작(0)부터 종료(마지막 시간)까지 시간으로 마지막점의 시간 표시
         */
        measureInterval = gripStrengthList.get(gripStrengthList.size()-1).getMillisecond();
        /**
         *  시작 시간(Scalar)
         *  최초의 신호가 시작되는 기준 시간
         */
        iterator = gripStrengthList.listIterator();
        while (iterator.hasNext()) {
            GripStrength tempGripStrength = iterator.next();
            if(tempGripStrength.getValue() >  0){
                startGripStrengthPoint = tempGripStrength;
                break;
            }
        }
        startTime = startGripStrengthPoint.getMillisecond();
        /**
         *  종료 시간(Scalar)
         *  신호가 끝나는 시간(From startTime)
         */
        iterator = gripStrengthList.listIterator(gripStrengthList.size());
        while (iterator.hasPrevious()) {
            GripStrength tempGripStrength = iterator.previous();
            if(tempGripStrength.getValue() > 0){
                endGripStrengthPoint = tempGripStrength;
                break;
            }
        }
        endTime = endGripStrengthPoint.getMillisecond();
        /**
         *  온셋 시간 간격(Scalar)
         *  The Time between 10% Max and 90% Max
         *  10% 시작 악력점(Point)과 90% 시작 악력점(Point)간의 millisecond
         */
        onsetInterval = tenGripStrengthStartPoint.getMillisecond() - ninetyGripStrengthStartPoint.getMillisecond() ;
        /**
         *  지속 시간 간격(Scalar)
         *  The Time between 90% Max and Average End
         *  90% 시작 악력점(Point)과 평균 종료 악력점(Point)간의 millisecond
         */
        enduranceInterval = ninetyGripStrengthStartPoint.getMillisecond() - averageGripStrengthEndPoint.getMillisecond();
        /**
         *  소산 시간 간격(Scalar)
         *  The Time between Average End and Last Point
         *  평균 종료 악력점(Point)과 마지막 측정점간의 millisecond
         */
        dissipateInterval = averageGripStrengthEndPoint.getMillisecond() - endGripStrengthPoint.getMillisecond();
        /**
         *  10% Max가 나타나는 시간 간격(From TIME_START)(Scalar)     *
         *  첫번째 측정점과 10% 시작 악력점(Point) 간의 millisecond
         */
        tenGripStrengthStartInterval = startGripStrengthPoint.getMillisecond() - tenGripStrengthStartPoint.getMillisecond();
        /**
         *  평균 시작 악력점(Point)가 나타나는 시간 간격(From TIME_START)(Scalar)     *
         *  첫번째 측정점과 평균 시작 악력점(Point) 간의 millisecond
         */
        averageGripStrengthStartInterval = startGripStrengthPoint.getMillisecond() - averageGripStrengthStartPoint.getMillisecond();
        /**
         *  90% Max 시작 악력점(Point)이 나타나는 시간 간격(From TIME_START)(Scalar)     *
         *  첫번째 측정점과 90% Max 시작 악력점(Point) 간의 millisecond
         */
        ninetyGripStrengthStartInterval = startGripStrengthPoint.getMillisecond() - ninetyGripStrengthStartPoint.getMillisecond();
        /**
         *  최대 악력점(Point)이 나타나는시간 간격(From TIME_START)(Scalar)     *
         *  첫번째 측정점과 최대 악력점(Point) 간의 millisecond
         */
        maxGripStrengthInterval = startGripStrengthPoint.getMillisecond() - maxGripStrengthPoint.getMillisecond();
        /**
         *  평균 종료 악력점(Point)가 나타나는 상대 시간(From TIME_START)(Scalar)     *
         *  첫번째 측정점과 평균 종료 악력점(Point)간의 millisecond
         */
        averageGripStrengthEndInterval = startGripStrengthPoint.getMillisecond() - averageGripStrengthEndPoint.getMillisecond();

        parameter.setParameterList(maxGripStrengthPoint, ninetyGripStrengthValue, tenGripStrengthValue, averageGripStrengthValue,
                ninetyGripStrengthStartPoint, ninetyGripStrengthEndPoint, tenGripStrengthStartPoint, tenGripStrengthEndPoint, averageGripStrengthStartPoint,
                averageGripStrengthEndPoint, measureInterval, startTime, endTime, onsetInterval, enduranceInterval, dissipateInterval,
                tenGripStrengthStartInterval, averageGripStrengthStartInterval, ninetyGripStrengthStartInterval, maxGripStrengthInterval,
                averageGripStrengthEndInterval);

        return true;
    }

    public void printResult(){
        System.out.println("---------------------"+this.user+","+this.hand+","+this.finger+" Information---------------------");
        System.out.println("[P1]최대 악력점[시간 : " +maxGripStrengthPoint.getMillisecond()+", 값 : "+maxGripStrengthPoint.getValue() +"]");
        System.out.println("[P2]90% 악력값[값 : "+ ninetyGripStrengthValue +"]");
        System.out.println("[P3]10% 악력값[값 : "+ tenGripStrengthValue +"]");
        System.out.println("[P4]평균 악력값[값 : "+ averageGripStrengthValue +"]");
        System.out.println("[P5]90% 시작 악력점[시간 : " +ninetyGripStrengthStartPoint.getMillisecond()+", 값 : "+ninetyGripStrengthStartPoint.getValue() +"]");
        System.out.println("[P6]90% 종료 악력점[시간 : " +ninetyGripStrengthEndPoint.getMillisecond()+", 값 : "+ninetyGripStrengthEndPoint.getValue() +"]");
        System.out.println("[P7]10% 시작 악력점[시간 : " +tenGripStrengthStartPoint.getMillisecond()+", 값 : "+tenGripStrengthStartPoint.getValue() +"]");
        System.out.println("[P8]10% 종료 악력점[시간 : " +tenGripStrengthEndPoint.getMillisecond()+", 값 : "+tenGripStrengthEndPoint.getValue() +"]");
        System.out.println("[P9]평균 시작 악력점[시간 : " +averageGripStrengthStartPoint.getMillisecond()+", 값 : "+averageGripStrengthStartPoint.getValue() +"]");
        System.out.println("[P10]평균 종료 악력점[시간 : " +averageGripStrengthEndPoint.getMillisecond()+", 값 : "+averageGripStrengthEndPoint.getValue() +"]");
        System.out.println("[P11]측정 시간 간격[시간 : "+ measureInterval +"]");
        System.out.println("[P12]시작(Start) 시간[시간 : "+ startTime +"]");
        System.out.println("[P13]종료(End) 시간[시간 : "+ endTime +"]");
        System.out.println("[P14]온셋(Onset) 시간 간격[시간 : "+ onsetInterval +"]");
        System.out.println("[P15]지속(Endurance) 시간 간격[시간 : "+ enduranceInterval +"]");
        System.out.println("[P16]소산(Dissipate) 시간 간격[시간 : "+ dissipateInterval +"]");
        System.out.println("[P17]10% 시작 악력점이 나타나는 시간 간격[시간 : "+ tenGripStrengthStartInterval +"]");
        System.out.println("[P18]평균 시작 악력점이 나타나는 시간 간격[시간 "+ averageGripStrengthStartInterval +"]");
        System.out.println("[P19]90% 시작 악력점이 나타나는 시간 간격[시간 : "+ ninetyGripStrengthStartInterval +"]");
        System.out.println("[P20]최대 악력점이 나타나는 시간 간격[시간 : "+ maxGripStrengthInterval +"]");
        System.out.println("[P21]평균 종료 악력점이 나타나는 시간 간격[시간 "+ averageGripStrengthEndInterval +"]");
    }

    //File csv = new File(nameTextField.getText()+"_"+locationChoiceBox.getValue().toString()+"_"+mobileTextField.getText()+".csv");
    public void saveParamCsv(){
        //File csv = new File(user+"_"+ hand+"_" + mobile+"_"+"Param"+".csv");
        String fileName = user+"_"+ hand+"_" + mobile+"_"+"param"+".csv";

        //BufferedWriter bufferedWriter = null; // 출력 스트림 생성

        OutputStreamWriter bufferedWriter = null;
        FileOutputStream fileOutStrm = null;


        try {
            fileOutStrm = new FileOutputStream(fileName,true);
            bufferedWriter = new OutputStreamWriter( fileOutStrm, "MS949");
            //bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "MS949"));

            bufferedWriter.write("["+this.finger+" - Key Parameter]"); bufferedWriter.write("\n");
            // CSV 파일의 header를 저장합니다.
            bufferedWriter.write("Contents" + "," + "Time" +"," + "Value" ); bufferedWriter.write("\n");
            bufferedWriter.write("[P1]최대 악력점"+","+ maxGripStrengthPoint.getMillisecond()+","+maxGripStrengthPoint.getValue() ); bufferedWriter.write("\n");
            bufferedWriter.write("[P2]90% 악력값"+ "," +" "+","+ ninetyGripStrengthValue ); bufferedWriter.write("\n");
            bufferedWriter.write("[P3]10% 악력값"+ "," +" "+","+ tenGripStrengthValue ); bufferedWriter.write("\n");
            bufferedWriter.write("[P4]평균 악력값"+ "," +" "+","+ averageGripStrengthValue ); bufferedWriter.write("\n");
            bufferedWriter.write("[P5]90% 시작 악력점"+ "," +ninetyGripStrengthStartPoint.getMillisecond()+ ","+ ninetyGripStrengthStartPoint.getValue() ); bufferedWriter.write("\n");
            bufferedWriter.write("[P6]90% 종료 악력점"+ "," +ninetyGripStrengthEndPoint.getMillisecond()+ ","+ ninetyGripStrengthEndPoint.getValue() ); bufferedWriter.write("\n");
            bufferedWriter.write("[P7]10% 시작 악력점"+ "," +tenGripStrengthStartPoint.getMillisecond()+ ","+ tenGripStrengthStartPoint.getValue() ); bufferedWriter.write("\n");
            bufferedWriter.write("[P8]10% 종료 악력점"+ "," +tenGripStrengthEndPoint.getMillisecond()+ ","+ tenGripStrengthEndPoint.getValue() ); bufferedWriter.write("\n");
            bufferedWriter.write("[P9]평균 시작 악력점" +","+averageGripStrengthStartPoint.getMillisecond()+","+averageGripStrengthStartPoint.getValue()); bufferedWriter.write("\n");
            bufferedWriter.write("[P10]평균 종료 악력점" +","+averageGripStrengthEndPoint.getMillisecond()+","+averageGripStrengthEndPoint.getValue()); bufferedWriter.write("\n");
            bufferedWriter.write("[P11]측정 시간 간격" + "," +" "+","+measureInterval); bufferedWriter.write("\n");
            bufferedWriter.write("[P12]시작(Start) 시간" + "," +" "+","+startTime); bufferedWriter.write("\n");
            bufferedWriter.write("[P13]종료(End) 시간" + "," +" "+","+endTime); bufferedWriter.write("\n");
            bufferedWriter.write("[P14]온셋(Onset) 시간 간격" + "," +" "+","+onsetInterval); bufferedWriter.write("\n");
            bufferedWriter.write("[P15]지속(Endurance) 시간 간격" + "," +" "+","+enduranceInterval); bufferedWriter.write("\n");
            bufferedWriter.write("[P16]소산(Dissipate) 시간 간격" + "," +" "+","+dissipateInterval); bufferedWriter.write("\n");
            bufferedWriter.write("[P17]10% 시작 악력점이 나타나는 시간 간격" + "," +" "+","+tenGripStrengthStartInterval); bufferedWriter.write("\n");
            bufferedWriter.write("[P18]평균 시작 악력점이 나타나는 시간 간격" + "," +" "+","+averageGripStrengthStartInterval); bufferedWriter.write("\n");
            bufferedWriter.write("[P19]90% 시작 악력점이 나타나는 시간 간격" + "," +" "+","+ninetyGripStrengthStartInterval); bufferedWriter.write("\n");
            bufferedWriter.write("[P20]최대 악력점이 나타나는 시간 간격" + "," +" "+","+maxGripStrengthInterval); bufferedWriter.write("\n");
            bufferedWriter.write("[P21]평균 종료 악력점이 나타나는 시간 간격" + "," +" "+","+averageGripStrengthEndInterval); bufferedWriter.write("\n");
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