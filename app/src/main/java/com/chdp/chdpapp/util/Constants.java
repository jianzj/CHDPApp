package com.chdp.chdpapp.util;

public class Constants {
    public static final String VERSION = "1.0";
    public static final String WEB_ROOT = "http://192.168.188.227:8080/CHDPWeb/";
    //public static final String WEB_ROOT = "http://192.168.61.110:8080/CHDPWeb/";
    //public static final String WEB_ROOT = "http://192.168.1.102:8080/CHDPWeb/";

    // These params are for Machine
    public static final int DECOCTION_MACHINE = 1;
    public static final int FILLING_MACHINE = 2;

    //These params are for Herb
    public static final int DECOCT_FIRST = 1;
    public static final int DECOCT_LATER = 2;
    public static final int WRAPPED_DECOCT = 3;

    // 煎药类型
    public static final int DECOCT_ONE = 1;
    public static final int DECOCT_TWO = 2;
    public static final int DECOCT_THREE = 3;

    public static int getHeatTime(int decoct_type) {
        switch (decoct_type) {
            case DECOCT_ONE:
                return 15;
            case DECOCT_TWO:
                return 25;
            case DECOCT_THREE:
                return 35;
            default:
                return 30;
        }
    }

    //These params are for Sex
    public static final int MAN = 1;
    public static final int WOMAN = 2;

    //These params are for Process
    public static final int RECEIVE = 1;
    public static final int CHECK = 2;
    public static final int MIX = 3;
    public static final int MIXCHECK = 4;
    public static final int SOAK = 5;
    public static final int DECOCT = 6;
    public static final int POUR = 7;
    public static final int CLEAN = 8;
    public static final int PACKAGE = 9;
    public static final int SHIP = 10;
    public static final int FINISH = 11;

    public static String getProcessName(int process) {
        switch (process) {
            case RECEIVE:
                return "接方";
            case CHECK:
                return "审方";
            case MIX:
                return "调配";
            case MIXCHECK:
                return "调配审核";
            case SOAK:
                return "浸泡";
            case DECOCT:
                return "煎煮";
            case POUR:
                return "灌装";
            case CLEAN:
                return "清场";
            case PACKAGE:
                return "包装";
            case SHIP:
                return "运输";
            case FINISH:
                return "完成";
            default:
                return "未知";
        }
    }

    public static final int ORDER_BEGIN = 1;
    public static final int ORDER_FINISH = 2;
}
