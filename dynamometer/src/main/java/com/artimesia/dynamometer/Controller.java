package com.artimesia.dynamometer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Duration;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

import java.sql.*;
import java.util.Properties;

import java.util.Properties;

public class Controller implements Initializable {
    @FXML ChoiceBox serialPortBox;
    @FXML ChoiceBox serialPortSpeedBox;
    @FXML ChoiceBox serialPortCheckBitBox;
    @FXML ChoiceBox serialPortDataBitBox;
    @FXML ChoiceBox serialPortStopBitBox;
    @FXML Button serialPortOpenBtn;
    @FXML Button recvClear;
    @FXML Button exitBtn;
    @FXML Label recvCount;
    @FXML Button CountReset;
    @FXML TextArea recvTextArea;

    @FXML Label indexLabel;
    @FXML Label middleLabel;
    @FXML Label ringLabel;
    @FXML Label littleLabel;
    @FXML Label sumLabel;
    @FXML Label maxIndexLabel;
    @FXML Label maxMiddleLabel;
    @FXML Label maxRingLabel;
    @FXML Label maxLittleLabel;
    @FXML Label maxSumLabel;
    @FXML Button measureBtn;
    @FXML Button saveBtn;
    @FXML Button compareBtn;  //일치도 분석
    @FXML Button stopMeasureBtn;
    @FXML ProgressBar measureProgressBar;

    @FXML TextField nameTextField;
    @FXML ChoiceBox genderChoiceBox;
    @FXML TextField ageTextField;
    @FXML TextField mobileTextField;
    @FXML ChoiceBox locationChoiceBox;
    // Line Chart
    @FXML LineChart fingerLineChart;
    //@FXML NumberAxis xAxis;
    //@FXML NumberAxis yAxis;

    private static SerialPort serialPort = null;
    private Timeline timeline;
    private static final Integer DURATION = 10;
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(DURATION);

    Solver indexSolver, middleSolver, ringSolver, littleSolver;
    Solver2 indexSolver2, middleSolver2, ringSolver2, littleSolver2;
    Solver3 solver3;
    Solver4 solver4;

    boolean measureFlag = false;
    Float maxIndex = 0.0F, maxMiddle = 0.0F, maxRing = 0.0F, maxLittle=0.0F, maxSum=0.0F;
    long sequence = 0;
    long measureTime = 0;
    HashMap<Long, LoadCellValue> valueMap = new HashMap<>();
    XYChart.Series indexSeries;
    XYChart.Series middleSeries;
    XYChart.Series ringSeries;
    XYChart.Series littleSeries;

    // Kalman Filter
    //private Kalman kalmanIndexValue;
    //private Kalman kalmanMiddleValue;
    //private Kalman kalmanRingValue;
    //private Kalman kalmanLittleValue;

    private Parameter indexParam;
    private Parameter middleParam;
    private Parameter ringParam;
    private Parameter littleParam;

    private Parameter2 indexParam2;
    private Parameter2 middleParam2;
    private Parameter2 ringParam2;
    private Parameter2 littleParam2;

    private Parameter3 param3; //
    private Parameter4 param4;

    private Param[] rightParam = new Param[5];
    private Param[] leftParam = new Param[5];

    private String user;
    private String hand;
    private String mobile;

    private boolean bCompleteFlag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Kalman
        //kalmanIndexValue = new Kalman(0.0F);
        //kalmanMiddleValue = new Kalman(0.0F);
        //kalmanRingValue = new Kalman(0.0F);
        //kalmanLittleValue = new Kalman(0.0F);

        //포트 번호
        String[] ports = SerialPortList.getPortNames();
        measureProgressBar.progressProperty().bind(timeSeconds.divide(DURATION));

        if(ports.length!=0) {
            for (String s : ports) {
                serialPortBox.getItems().add(s);
            }
            serialPortBox.setValue(ports[0]);
        }
        //전송 속도
        String[] speeds = new String[]{
                "100","300","600","1200","2400","4800","9600","14400","19200","38400","56000","57600","115200","128000","256000"
        };
        for (String s:speeds) {
            serialPortSpeedBox.getItems().add(s);
        }
        serialPortSpeedBox.setValue("115200");

        //체크 비트
        String[] checks = new String[]{
                "NONE","ODD","EVEN","MARK","SPACE"
        };
        for (String s:checks) {
            serialPortCheckBitBox.getItems().add(s);
        }
        serialPortCheckBitBox.setValue("NONE");

        //비트 설정
        String[] databits = new String[]{
                "5","6","7","8"
        };
        for (String s:databits) {
            serialPortDataBitBox.getItems().add(s);
        }
        serialPortDataBitBox.setValue("8");

        //정지 비트
        String[] stopbits = new String[]{
                "1","2"
        };
        for (String s:stopbits) {
            serialPortStopBitBox.getItems().add(s);
        }
        serialPortStopBitBox.setValue("1");

        //성별
        String[] gender = new String[]{
                "남","여"
        };
        for (String s:gender) {
            genderChoiceBox.getItems().add(s);
        }
        genderChoiceBox.setValue("남");

        //오른쪽 왼쪽 위치
        String[] handLocation = new String[]{
                "RIGHT","LEFT"
        };
        for (String s:handLocation) {
            locationChoiceBox.getItems().add(s);
        }
        locationChoiceBox.setValue("RIGHT");

        //defining the axes
        NumberAxis xAxis = new NumberAxis(); // we are gonna plot against time
        xAxis.setAutoRanging(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);
        xAxis.setLabel("milliseconds");
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("kg");
        yAxis.setAnimated(false); // axis animations are removed
        //creating the line chart with two axis created above
        //fingerLineChart = LineChart(xAxis, yAxis);
        fingerLineChart.setCreateSymbols(false);    // diable drawing point
        fingerLineChart.setStyle(".thick-chart .chart-series-line {    \n"
                + "    -fx-stroke-width: 1px;\n"
                + "}");
        fingerLineChart.getStyleClass().add("thick-chart");
        fingerLineChart.setAnimated(false);  // disable animations

        indexSeries = new XYChart.Series();
        middleSeries = new XYChart.Series();
        ringSeries = new XYChart.Series();
        littleSeries = new XYChart.Series();
        //defining a series to display data
        indexSeries.setName("index");
        middleSeries.setName("middle");
        ringSeries.setName("ring");
        littleSeries.setName("little");

        ObservableList<XYChart.Series<Number, Number>> list = FXCollections.observableArrayList();
        list.addAll(indexSeries, middleSeries, ringSeries, littleSeries);
        fingerLineChart.setData(list);
        fingerLineChart.setLegendVisible(true);

        // add series to chart
        //strengthLineChart.getData().add(indexSeries);
        //strengthLineChart.getData().add(middleSeries);
        //strengthLineChart.getData().add(ringSeries);
        //strengthLineChart.getData().add(littleSeries);

        serialPortOpenBtn.setOnAction((ActionEvent event) -> {
            measureFlag = false;
            if( serialPort!=null&&serialPort.isOpened()) try {
                serialPort.closePort();
                serialPortOpenBtn.setText("열기");
                serialPortBox.setDisable(false);
                serialPortSpeedBox.setDisable(false);
                serialPortCheckBitBox.setDisable(false);
                serialPortDataBitBox.setDisable(false);
                serialPortStopBitBox.setDisable(false);
                return;
            } catch (SerialPortException e) {
                new AlertBox().display("통신 포트 오류", e.getMessage());
            }
            serialPort = new SerialPort((String) serialPortBox.getValue());
            try {
                serialPort.openPort();
                serialPort.setParams(
                        Integer.valueOf((String)serialPortSpeedBox.getValue()) ,
                        Integer.valueOf((String)serialPortDataBitBox.getValue()),
                        Integer.valueOf((String)serialPortStopBitBox.getValue()),
                        serialPortSpeedBox.getValue().equals("NONE")? 0: serialPortSpeedBox.getValue().equals("ODD")?1 :
                                serialPortSpeedBox.getValue().equals("EVEN")?2: serialPortSpeedBox.getValue().equals("SPACE")?3: 0);
                serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
                serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
                UartRXEvent();
                serialPortOpenBtn.setText("닫기");
                serialPortBox.setDisable(true);
                serialPortSpeedBox.setDisable(true);
                serialPortCheckBitBox.setDisable(true);
                serialPortDataBitBox.setDisable(true);
                serialPortStopBitBox.setDisable(true);

            } catch (SerialPortException e) {
                new AlertBox().display("포튼 오픈 에러", e.getMessage());

            }
        });

        recvClear.setOnAction(event -> {
            measureFlag = false;
            valueMap.clear();
            sequence = 0L;
            measureTime = 0;
            maxIndex = 0.0F;
            maxMiddle = 0.0F;
            maxRing = 0.0F;
            maxLittle=0.0F;
            maxSum=0.0F;
            indexLabel.setText("0.0");
            middleLabel.setText("0.0");
            ringLabel.setText("0.0");
            littleLabel.setText("0.0");
            sumLabel.setText("0.0");
            maxIndexLabel.setText("0.0");
            maxMiddleLabel.setText("0.0");
            maxRingLabel.setText("0.0");
            maxLittleLabel.setText("0.0");
            maxSumLabel.setText("0.0");
            indexSeries.getData().clear();
            middleSeries.getData().clear();
            ringSeries.getData().clear();
            littleSeries.getData().clear();

            recvTextArea.setText("");
        });

        CountReset.setOnAction(event -> {
            recvCount.setText("0");
        });

        exitBtn.setOnAction(event -> {
            Platform.exit();
        });

        measureBtn.setOnAction(event ->{
            measureFlag = true;
            valueMap.clear();
            indexSeries.getData().clear();
            middleSeries.getData().clear();
            ringSeries.getData().clear();
            littleSeries.getData().clear();
            sequence = 0L;
            measureTime = 0;
            maxIndex = 0.0F;
            maxMiddle = 0.0F;
            maxRing = 0.0F;
            maxLittle=0.0F;
            maxSum=0.0F;
            IntegerProperty seconds = new SimpleIntegerProperty();
            measureProgressBar.progressProperty().bind(seconds.divide(5.0));
            timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(seconds, 0)),
                    new KeyFrame(Duration.seconds(5), e-> {
                        // do anything you need here on completion...
                        System.out.println("5 seconds over");
                        measureFlag = false;
                    }, new KeyValue(seconds, 5))
            );
            timeline.playFromStart();
        });

        stopMeasureBtn.setOnAction(event -> {
            if (timeline != null){
                timeline.stop();
            }
            measureFlag = false;
            valueMap.clear();
            indexSeries.getData().clear();
            middleSeries.getData().clear();
            ringSeries.getData().clear();
            littleSeries.getData().clear();
            sequence = 0L;
            measureTime = 0;
            maxIndex = 0.0F;
            maxMiddle = 0.0F;
            maxRing = 0.0F;
            maxLittle=0.0F;
            maxSum=0.0F;
            indexLabel.setText("0.0");
            middleLabel.setText("0.0");
            ringLabel.setText("0.0");
            littleLabel.setText("0.0");
            sumLabel.setText("0.0");
            maxIndexLabel.setText("0.0");
            maxMiddleLabel.setText("0.0");
            maxRingLabel.setText("0.0");
            maxLittleLabel.setText("0.0");
            maxSumLabel.setText("0.0");
        });

        //분석
        saveBtn.setOnAction(event ->{
            measureFlag = false;
            if (valueMap.isEmpty() || valueMap.size() == 0) {
                new AlertBox().display("정보", "저장할 데이터가 없습니다.");
                return;
            } else {
                if (nameTextField.getText().isEmpty()) {
                    new AlertBox().display("정보", "이름을 입력하시기 바랍니다.");
                    return;
                }
                if (mobileTextField.getText().isEmpty()) {
                    new AlertBox().display("정보", "휴대폰 번호를 입력하시기 바랍니다.");
                    return;
                }
                // 2022.04.06
                // zedo
                // 분석 로직 추가
                // 손가락 악력에 대한 배열 생성
                ArrayList<GripStrength> indexGripStrengthList = new ArrayList<>();
                ArrayList<GripStrength> middleGripStrengthList = new ArrayList<>();
                ArrayList<GripStrength> ringGripStrengthList = new ArrayList<>();
                ArrayList<GripStrength> littleGripStrengthList = new ArrayList<>();

                Iterator<Map.Entry<Long, LoadCellValue>> iterator = valueMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Long, LoadCellValue> entry = (Map.Entry<Long, LoadCellValue>) iterator.next();
                    Long key = entry.getKey();
                    LoadCellValue value = entry.getValue();
                    // 2022.04.05
                    // 손가락 악력에 대한 배열에 대하여 값을 추가함
                    // 로드셀 보정(Calibration)을 위하여 0.05보다 큰 값에 대하여 반영함, 그렇지 않으면 0으로 설정
                    indexGripStrengthList.add(new GripStrength(value.getMillisecond(), (value.getIndexValue() > 0.15F) ? value.getIndexValue() : 0.0F));
                    middleGripStrengthList.add(new GripStrength(value.getMillisecond(), (value.getMiddleValue() > 0.15F) ? value.getMiddleValue() : 0.0F));
                    ringGripStrengthList.add(new GripStrength(value.getMillisecond(), (value.getRingValue() > 0.15F) ? value.getRingValue() : 0.0F));
                    littleGripStrengthList.add(new GripStrength(value.getMillisecond(), (value.getLittleValue() > 0.15F) ? value.getLittleValue() : 0.0F));
                    // System.out.println(key + "," + value.getMillisecond() +"," + value.getIndexValue() +
                    //        "," + value.getMiddleValue() + "," + value.getRingValue() + "," + value.getLittleValue());
                }

                user = nameTextField.getText();
                hand = locationChoiceBox.getValue().toString();
                mobile = mobileTextField.getText();

                indexParam = new Parameter();
                indexSolver = new Solver("Index", indexGripStrengthList, user, hand, mobile, indexParam);
                if (indexSolver.analyse()) {
                    indexSolver.printResult();
                    indexSolver.saveParamCsv();
                    System.out.println(indexSolver.parameter.toString());
                } else{
                    System.out.println("Index 분석 실패");
                }

                middleParam = new Parameter();
                middleSolver = new Solver("Middle", middleGripStrengthList, user, hand, mobile, middleParam);
                if (middleSolver.analyse()) {
                    middleSolver.printResult();
                    middleSolver.saveParamCsv();
                    System.out.println(middleSolver.parameter.toString());
                } else{
                    System.out.println("Middle 분석 실패");
                }

                ringParam = new Parameter();
                ringSolver = new Solver("Ring", ringGripStrengthList, user, hand, mobile, ringParam);
                if (ringSolver.analyse()) {
                    ringSolver.printResult();
                    ringSolver.saveParamCsv();
                    System.out.println(ringSolver.parameter.toString());
                } else{
                    System.out.println("Ring 분석 실패");
                }

                littleParam = new Parameter();
                littleSolver = new Solver("Little", littleGripStrengthList, user, hand, mobile, littleParam);
                if (littleSolver.analyse()) {
                    littleSolver.printResult();
                    littleSolver.saveParamCsv();
                    System.out.println(littleSolver.parameter.toString());
                } else{
                    System.out.println("Little 분석 실패");
                }

                ///*
                //* Solver2
                //* Finger별 Cumulative Sum, 평균, 분산, 표준편차
                //*/
                indexParam2 = new Parameter2();
                indexSolver2 = new Solver2("Index", indexGripStrengthList, user, hand, mobile, indexParam2 );
                if (indexSolver2.analyse()) {
                    indexSolver2.printResult();
                    //indexSolver2.saveParamCsv();
                    //System.out.println(indexSolver2.parameter.toString());
                } else{
                    System.out.println("Index Solver2 분석 실패");
                }

                middleParam2 = new Parameter2();
                middleSolver2 = new Solver2("Middle", middleGripStrengthList, user, hand, mobile, middleParam2);
                if (middleSolver2.analyse()) {
                    middleSolver2.printResult();
                    //middleSolver2.saveParamCsv();
                    //System.out.println(middleSolver2.parameter.toString());
                } else{
                    System.out.println("Middle Solver2 분석 실패");
                }

                ringParam2 = new Parameter2();
                ringSolver2 = new Solver2("Ring", ringGripStrengthList, user, hand, mobile, ringParam2);
                if (ringSolver2.analyse()) {
                    ringSolver2.printResult();
                    //ringSolver2.saveParamCsv();
                    //System.out.println(ringSolver2.parameter.toString());
                } else {
                    System.out.println("Ring Solver2 분석 실패");
                }

                littleParam2 = new Parameter2();
                littleSolver2 = new Solver2("Little", littleGripStrengthList, user, hand, mobile, littleParam2);
                if (littleSolver2.analyse()) {
                    littleSolver2.printResult();
                    //littleSolver2.saveParamCsv();
                    //System.out.println(littleSolver2.parameter.toString());
                } else {
                    System.out.println("Little Solver2 분석 실패");
                }

                //
                //Solver3
                //시간별 좌우 손가락 Cumulative Sum
                //
                param3 = new Parameter3();
                solver3 = new Solver3(user, hand, mobile, indexGripStrengthList, middleGripStrengthList, ringGripStrengthList, littleGripStrengthList, param3);
                if (solver3.analyse()) {
                    solver3.printResult();
                    //indexSolver2.saveParamCsv();
                    //System.out.println(indexSolver2.parameter.toString());
                } else {
                    System.out.println("Solver3 분석 실패");
                }

                //
                // Solver4
                //일치도 파라메터 저장하기
                //
                if( "RIGHT" == hand ) {
                    rightParam[0] = indexParam2;
                    rightParam[1] = middleParam2;
                    rightParam[2] = ringParam2;
                    rightParam[3] = littleParam2;
                    rightParam[4] = param3;


                    bCompleteFlag = false;
                }else {
                    leftParam[0] = indexParam2;
                    leftParam[1] = middleParam2;
                    leftParam[2] = ringParam2;
                    leftParam[3] = littleParam2;
                    leftParam[4] = param3;

                    bCompleteFlag = true;
                }

                //insertDatabase();
                saveFile();
                new AlertBox().display("정보", "분석이 완료되었습니다.");
                valueMap.clear();
            }
        });

        //일치도 Solver4
        compareBtn.setOnAction(event ->{
            measureFlag = false;

            if (bCompleteFlag) {
                param4 = new Parameter4();

                solver4 = new Solver4(user, rightParam, leftParam, param4);
                if (solver4.analyse()) {
                    solver4.printResult();
                    //indexSolver2.saveParamCsv();
                    //System.out.println(indexSolver2.parameter.toString());
                } else {
                    System.out.println("Solver4 분석 실패");
                }
            }else {
                System.out.println("Solver4 분석 실패->오른손/왼손 진행");
            }
        });
    }

    /*
    public void UartRXEvent(){
        try {
            serialPort.addEventListener(serialPortEvent -> {
                try {

                    byte[] bytes=serialPort.readBytes(1024);
                    if(bytes!=null) {
                        String str= new String(bytes);
                        Platform.runLater(()->{
                            if(measureFlag) {
                                parseMessage(bytes);
                            }
                            // String newStr = recvTextArea.getText().isEmpty() ? ("" + str) : (recvTextArea.getText() + str );
                            int characterCount = recvTextArea.getLength();
                            if (characterCount > 20000){
                                String copy = recvTextArea.getText().substring(characterCount - 10000, characterCount);
                                recvTextArea.setText(copy);
                            }
                            String newStr = (recvTextArea.getText() + str );
                            recvTextArea.setText(newStr);
                            //recvTextArea.setScrollTop(recvTextArea.getMaxHeight());
                            recvTextArea.setScrollTop(Double.MAX_VALUE);

                            recvCount.setText(String.valueOf((Integer.parseInt(recvCount.getText())+bytes.length)));
                        });
                    }
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    public void UartRXEvent(){
        try {
            serialPort.addEventListener( new SerialPortEventListener()
            {
                @Override
                public void serialEvent(SerialPortEvent event)
                {
                    if(event.isRXCHAR())
                    {//If data is available
                        //System.out.printf("getLength->"+event.getEventValue()+"\n");
                        if (event.getEventValue() >= 512)
                        {//Check bytes count in the input buffer
                            try
                            {
                                byte bytes[] = serialPort.readBytes(512 );
                                //LogByteToHex(bytes);

                                String str= new String(bytes);
                                saveTxtComms(bytes);
                                saveCsvComms(bytes);

                                Platform.runLater(() -> {
                                    if (measureFlag) {
                                        parseMessage(bytes);
                                    }
                                    // String newStr = recvTextArea.getText().isEmpty() ? ("" + str) : (recvTextArea.getText() + str );
                                    int characterCount = recvTextArea.getLength();
                                    if (characterCount > 20000) {
                                        String copy = recvTextArea.getText().substring(characterCount - 10000, characterCount);
                                        recvTextArea.setText(copy);
                                    }
                                    String newStr = (recvTextArea.getText() + str);
                                    recvTextArea.setText(newStr);
                                    //recvTextArea.setScrollTop(recvTextArea.getMaxHeight());
                                    recvTextArea.setScrollTop(Double.MAX_VALUE);

                                    recvCount.setText(String.valueOf((Integer.parseInt(recvCount.getText()) + bytes.length)));
                                });

                            } catch (SerialPortException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LogByteToHex(byte[] bytes)
    {
        //byte[] bytes = {-1, 0, 1, 2, 3 };
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        sb.append("\n");
        System.out.println(sb.toString());
        // prints "FF 00 01 02 03 "
        saveTxtCommsHex(sb, bytes);
    }

    public void saveTxtCommsHex(StringBuilder recv, byte[] recvBytes)
    {
        File csv = new File("D:/990_log/CommsRecvHex"+".txt");
        BufferedWriter bufferedWriter = null; // 출력 스트림 생성

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(csv, true));
            String coverted = new String(recvBytes);
            bufferedWriter.write( coverted );
            bufferedWriter.newLine();
            bufferedWriter.write( recv.toString() );
            bufferedWriter.newLine();
            bufferedWriter.flush();

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

    public void saveTxtComms(byte[] recv)
    {
        File csv = new File("D:/990_log/CommsRecv"+".txt");
        BufferedWriter bufferedWriter = null; // 출력 스트림 생성

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(csv, true));
            String coverted = new String(recv);
            coverted.trim();
            coverted.replaceAll("\\p{Z}", "");

            bufferedWriter.write( coverted );
            bufferedWriter.newLine();
            bufferedWriter.flush();

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

    public void saveCsvComms(byte[] recv)
    {
        File csv = new File("D:/990_log/CommsRecv"+".csv");
        BufferedWriter bufferedWriter = null; // 출력 스트림 생성

        try {
            // csv파일의 기존 값에 이어쓰려면 위처럼 true를 지정하고, 기존 값을 덮어쓰려면 true를 삭제한다
            bufferedWriter = new BufferedWriter(new FileWriter(csv, true));
            String coverted = new String(recv);
            coverted.trim();
            coverted.replaceAll("\\p{Z}", "");

            bufferedWriter.write( coverted );
            bufferedWriter.newLine();
            bufferedWriter.flush();

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

    public void saveFile(){
        // 파일 저장
        File csv = new File(nameTextField.getText()+"_"+locationChoiceBox.getValue().toString()+"_"+mobileTextField.getText()+".csv");
        BufferedWriter bufferedWriter = null; // 출력 스트림 생성
        try {
            // csv파일의 기존 값에 이어쓰려면 위처럼 true를 지정하고, 기존 값을 덮어쓰려면 true를 삭제한다
            bufferedWriter = new BufferedWriter(new FileWriter(csv, false));
            // CSV 파일의 header를 저장합니다.
            bufferedWriter.write("sequence" + "," + "millisecond" +"," + "index" +
                    "," + "middle" + "," + "ring" + "," + "little");
            bufferedWriter.newLine();
            // ValueMap의 내용을 저장합니다.
            Iterator<Map.Entry<Long, LoadCellValue>> iterator = valueMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, LoadCellValue> entry = (Map.Entry<Long, LoadCellValue>) iterator.next();
                Long key = entry.getKey();
                LoadCellValue value = entry.getValue();
                float indexValue = (value.getIndexValue() > 0.15F) ? value.getIndexValue() : 0.0F;
                float middleValue = (value.getMiddleValue() > 0.15F) ? value.getMiddleValue() : 0.0F;
                float ringValue = (value.getRingValue() > 0.15F) ? value.getRingValue() : 0.0F;
                float littleValue = (value.getLittleValue() > 0.15F) ? value.getLittleValue() : 0.0F;
                bufferedWriter.write(key + "," + value.getMillisecond() +"," + indexValue +
                        "," + middleValue + "," + ringValue + "," + littleValue);
                bufferedWriter.newLine();
            }
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

    public void insertDatabase(){
        Properties connConfig = new Properties();
        connConfig.setProperty("user", "root");
        connConfig.setProperty("password", "jeios");
        String name = nameTextField.getText();
        String mobileNumber = mobileTextField.getText();
        String gender = genderChoiceBox.getValue().equals("남") ? "M" : "F";
        int userID = 0;
        int measureCount=0;
        int sensorLocation = locationChoiceBox.getValue().equals("RIGHT")? 1: 0;

        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://221.164.14.233:13306/motioncore_gripstrength", connConfig)) {
            // Enable Auto-Commit
            conn.setAutoCommit(true);
            try (Statement stmt = conn.createStatement()) {
                try (PreparedStatement prep = conn.prepareStatement("SELECT USER_ID FROM TB_USER_MST WHERE USER_NAME = ? AND MOBILE_NO = ?")) {
                    prep.setString(1, name);
                    prep.setString(2, mobileNumber);
                    ResultSet userIDRecord = prep.executeQuery();
                    if(userIDRecord.next()){
                        userID = userIDRecord.getInt("USER_ID");
                    } else {
                        try (PreparedStatement prepSub = conn.prepareStatement("SELECT MAX(USER_ID) AS VALUE FROM TB_USER_MST")) {
                            ResultSet maxUserIDRecord = prepSub.executeQuery();
                            if (maxUserIDRecord.next()) {
                                userID = maxUserIDRecord.getInt("VALUE") + 1;
                            } else {
                                userID = 1;
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        // INSERT TB_USER_MST
                        try (PreparedStatement prepSub = conn.prepareStatement(
                                "INSERT INTO TB_USER_MST(USER_ID, ROLE_ID, USER_NAME, MOBILE_NO, LAST_CHANGED_DATE, LAST_CHANGED_USER_ID)" +
                                "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                            prepSub.setInt(1, userID);
                            prepSub.setString(2, "ST");
                            prepSub.setString(3, name);
                            prepSub.setString(4, mobileNumber);
                            long timeNow = Calendar.getInstance().getTimeInMillis();
                            prepSub.setTimestamp(5, new java.sql.Timestamp(timeNow));
                            prepSub.setInt(6,userID);
                            int result = prepSub.executeUpdate();
                            if (result == 0) {
                                System.out.println("TB_USER_MST INSERT Failed");
                            } else {
                                System.out.println("TB_USER_MST INSERT Success");
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                        // INSERT TB_SUBJECT_MST
                        try (PreparedStatement prepSub = conn.prepareStatement(
                                "INSERT INTO TB_SUBJECT_MST(USER_ID, SHOES_SIZE, SEX, BIRTH_DATE, HEIGHT, WEIGHT, ETC_COMMENT, LAST_CHANGED_DATE, LAST_CHANGED_USER_ID) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                            prepSub.setInt(1, userID);
                            prepSub.setInt(2, 0);
                            prepSub.setString(3, gender);
                            prepSub.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                            prepSub.setInt(5,99);
                            prepSub.setInt(6,99);
                            prepSub.setString(7, "4 Digit Grip Strength Dynamometer");
                            long timeNow = Calendar.getInstance().getTimeInMillis();
                            prepSub.setTimestamp(8, new java.sql.Timestamp(timeNow));
                            prepSub.setInt(9,userID);
                            int result = prepSub.executeUpdate();
                            if (result == 0) {
                                System.out.println("TB_SUBJECT_MST INSERT Failed");
                            } else {
                                System.out.println("TB_SUBJECT_MST INSERT Success");
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // 2022.04.19
                    // Sensor Location(오른쪽, 완쪽)을 고려하여 측정 횟수를 구합니다.
                    try (PreparedStatement prepSub = conn.prepareStatement("SELECT MAX(MEASURE_COUNT) AS VALUE FROM TB_DAQ_INFO WHERE SUBJECT_ID = ? AND SENSOR_LOCATION = ?")) {
                        prepSub.setInt(1, userID);
                        prepSub.setInt(2, sensorLocation);
                        ResultSet maxMeasureCountRecord = prepSub.executeQuery();
                        if (maxMeasureCountRecord.next()) {
                            measureCount = maxMeasureCountRecord.getInt("VALUE")+1;
                        } else {
                            measureCount = 1;
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    Iterator<Map.Entry<Long, LoadCellValue>> iterator = valueMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Long, LoadCellValue> entry = (Map.Entry<Long, LoadCellValue>) iterator.next();
                        sequence = entry.getKey();
                        LoadCellValue value = entry.getValue();
                        // 2022.04.05
                        // 손가락 악력 측정 결과를 TB_DAQ_INFO에 삽입합니다.
                        // 로드셀 보정(Calibration)을 위하여 0.05보다 큰 값에 대하여 반영함, 그렇지 않으면 0으로 설정
                        try (PreparedStatement prepSub = conn.prepareStatement(
                                "INSERT INTO TB_DAQ_INFO (SUBJECT_ID, INSPECTOR_ID, MEASURE_COUNT, SENSOR_LOCATION, SEQUENCE, INDEX_FINGER, MIDDLE_FINGER, RING_FINGER, LITTLE_FINGER, LAST_TRANSACTION_SERIAL_NO, LAST_TRANSACTION_CODE, LAST_TRANSACTION_DATE, LAST_TRANSACTION_USER_ID) " +
                                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                            prepSub.setInt(1, userID);
                            prepSub.setInt(2, userID);
                            prepSub.setInt(3, measureCount);
                            prepSub.setInt(4, sensorLocation);
                            prepSub.setInt(5, (int)sequence);
                            prepSub.setFloat(6,(value.getIndexValue() > 0.15F) ? value.getIndexValue() : 0.0F);
                            prepSub.setFloat(7,(value.getMiddleValue() > 0.15F) ? value.getIndexValue() : 0.0F);
                            prepSub.setFloat(8,(value.getRingValue() > 0.15F) ? value.getIndexValue() : 0.0F);
                            prepSub.setFloat(9,(value.getLittleValue() > 0.15F) ? value.getIndexValue() : 0.0F);
                            prepSub.setString(10, TransactionSerialNumberUtil.createTransactionSerialNo());
                            prepSub.setString(11, "I");
                            long timeNow = Calendar.getInstance().getTimeInMillis();
                            prepSub.setTimestamp(12, new java.sql.Timestamp(timeNow));
                            prepSub.setInt(13, userID);
                            int result = prepSub.executeUpdate();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try (PreparedStatement prepSub = conn.prepareStatement(
                            "INSERT INTO TB_ANALYSIS_INFO (SUBJECT_ID, INSPECTOR_ID, MEASURE_COUNT, SENSOR_LOCATION, INDEX_RESULT, MIDDLE_RESULT, RING_RESULT, LITTLE_RESULT, LAST_TRANSACTION_SERIAL_NO, LAST_TRANSACTION_CODE, LAST_TRANSACTION_DATE, LAST_TRANSACTION_USER_ID) " +
                                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                        prepSub.setInt(1, userID);
                        prepSub.setInt(2, userID);
                        prepSub.setInt(3, measureCount);
                        prepSub.setInt(4, sensorLocation);
                        prepSub.setString(5, indexSolver.parameter.toString());
                        prepSub.setString(6, middleSolver.parameter.toString());
                        prepSub.setString(7, ringSolver.parameter.toString());
                        prepSub.setString(8, littleSolver.parameter.toString());
                        prepSub.setString(9, TransactionSerialNumberUtil.createTransactionSerialNo());
                        prepSub.setString(10, "I");
                        long timeNow = Calendar.getInstance().getTimeInMillis();
                        prepSub.setTimestamp(11, new java.sql.Timestamp(timeNow));
                        prepSub.setInt(12, userID);
                        int result = prepSub.executeUpdate();
                        if (result == 0) {
                            System.out.println("TB_ANALYSIS_INFO INSERT Failed");
                        } else {
                            System.out.println("TB_ANALYSIS_INFO INSERT Success");
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseMessage(byte[] message) {
        //byte[] rawData = message.getBytes(StandardCharsets.US_ASCII);
        int length = message.length;
        //System.out.println(("message length...." + length));

        Float index = 0.0F, middle = 0.0F, ring = 0.0F, little=0.0F, sum=0.0F;
        Long millisecond = 0L;

        // Copy the rowData to tempData
        byte[] value = new byte[5];
        byte[] rawData = new byte[length];
        System.arraycopy(message, 0, rawData, 0, length);

        /*
         [0123 ~~~ 30]
          I00.00M00.00R00.00L00.00T0.036;
        */
        //LineBy filter
        int MAXCHAR = 30; Boolean bFlag = false;
        byte[] lineData = new byte[length];

        for( int lineIndex = 0; lineIndex < length; lineIndex++ ) {
            if( (lineIndex + 30 ) >= length )  break; //Array Index out of Exception

            byte start = rawData[lineIndex];
            byte end = rawData[lineIndex + 30];

            if( start == 'I' && end == ';') {
                System.arraycopy(rawData, lineIndex, lineData, 0, MAXCHAR);
                for(int colIndex=0; colIndex<30; colIndex++)
                {
                    if (lineData[colIndex] == 'I'){
                        System.arraycopy(lineData, 1, value, 0, 5);
                        index = Float.parseFloat(convertByteToString(value));
                    }
                    if (lineData[colIndex] == 'M'){
                        System.arraycopy(lineData, colIndex+1, value, 0, 5);
                        middle = Float.parseFloat(convertByteToString(value));
                    }
                    if (lineData[colIndex] == 'R'){
                        System.arraycopy(lineData, colIndex+1, value, 0, 5);
                        ring = Float.parseFloat(convertByteToString(value));
                    }
                    if (lineData[colIndex] == 'L'){
                        System.arraycopy(lineData, colIndex+1, value, 0, 5);
                        little = Float.parseFloat(convertByteToString(value));
                    }
                    if (lineData[colIndex] == 'T'){
                        System.arraycopy(lineData, colIndex+1, value, 0, 5);
                        millisecond = (long) (Float.parseFloat(convertByteToString(value)) * 1000);
                    }

                    bFlag = true;
                }

                if( bFlag ) {
                    if (maxIndex < index)
                        maxIndex = index;
                    if (maxMiddle < middle)
                        maxMiddle = middle;
                    if (maxRing < ring)
                        maxRing = ring;
                    if (maxLittle < little)
                        maxLittle = little;

                    indexLabel.setText(String.format("%.02f", index));
                    middleLabel.setText(String.format("%.02f", middle));
                    ringLabel.setText(String.format("%.02f", ring));
                    littleLabel.setText(String.format("%.02f", little));
                    sum = index + middle + ring + little;
                    sumLabel.setText(String.format("%.02f", sum));
                    // Insert Map
                    measureTime = measureTime + millisecond;
                    valueMap.put(sequence++, new LoadCellValue(measureTime, index, middle, ring, little));
                    // put series value
                    if (index != 0.0) {
                        indexSeries.getData().add(new XYChart.Data<>(measureTime, index));
                    }
                    if (middle != 0.0) {

                        middleSeries.getData().add(new XYChart.Data<>(measureTime, middle));
                    }
                    if (ring != 0.0) {
                        ringSeries.getData().add(new XYChart.Data<>(measureTime, ring));
                    }
                    if (little != 0.0) {
                        littleSeries.getData().add(new XYChart.Data<>(measureTime, little));
                    }

                    maxIndexLabel.setText(String.format("%.02f", maxIndex));
                    maxMiddleLabel.setText(String.format("%.02f", maxMiddle));
                    maxRingLabel.setText(String.format("%.02f", maxRing));
                    maxLittleLabel.setText(String.format("%.02f", maxLittle));
                    maxSum = maxIndex + maxMiddle + maxRing + maxLittle;
                    maxSumLabel.setText(String.format("%.02f", maxSum));
                }
            }
        }
    }

    String convertByteToString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length);
        for (int i = 0; i < data.length; ++ i) {
            if (data[i] < 0) throw new IllegalArgumentException();
            sb.append((char) data[i]);
        }
        return sb.toString();
    }
}