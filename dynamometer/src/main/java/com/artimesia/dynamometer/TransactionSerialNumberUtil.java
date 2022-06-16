package com.artimesia.dynamometer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionSerialNumberUtil {

    /**
     * CLASS 이름을 를 정의하는 속성이다.
     */
    static final String CLASS = "TransactionSerialNumberUtil";


    /**
     * 현재 시간에, nanosec의 9자리를 더해서 Timekey를 생성하는 메소드 이다.
     *
     * @return String형 timekey
     */
    public static String createTransactionSerialNo() {
        final String METHOD = CLASS + ".createTransactionSerialNo";

        String simpleFormat = "yyyyMMddHHmmssSSS";
        SimpleDateFormat formatter = new SimpleDateFormat(simpleFormat);

        String currentTime = formatter.format(new Date());
        String nanoTime = Long.toString(System.nanoTime());

        StringBuilder sb = new StringBuilder().append(currentTime).append(nanoTime.substring(nanoTime.length() - 9, nanoTime.length()));

        return sb.toString();
    }
}
