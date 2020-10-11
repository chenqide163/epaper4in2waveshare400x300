package com.chenqi.weather.sojson;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GetSojsonWeather {
    private static Logger LOG = Logger.getLogger(GetSojsonWeather.class);

    public static final Map<String, String> WEATHERTYPE_MAP = initWeatherType();

    public static Map<String, String> initWeatherType() {
        Map<String, String> weatherType = new HashMap<>();
        //晴、多云、阴、阵雨、雷阵雨、雷阵雨伴有冰雹、雨夹雪、小雨、中雨
        weatherType.put("晴", "0");
        weatherType.put("多云", "1");
        weatherType.put("阴", "2");
        weatherType.put("阵雨", "3");
        weatherType.put("雷阵雨", "4");
        weatherType.put("雷阵雨伴有冰雹", "5");
        weatherType.put("雨夹雪", "6");
        weatherType.put("小雨", "7");
        weatherType.put("中雨", "8");

        //大雨、暴雨、大暴雨、特大暴雨、阵雪、小雪、中雪、大雪、暴雪、雾、冻雨
        weatherType.put("大雨", "9");
        weatherType.put("暴雨", "10");
        weatherType.put("大暴雨", "11");
        weatherType.put("特大暴雨", "12");
        weatherType.put("阵雪", "13");
        weatherType.put("小雪", "14");
        weatherType.put("中雪", "15");
        weatherType.put("大雪", "16");
        weatherType.put("暴雪", "17");
        weatherType.put("雾", "18");
        weatherType.put("冻雨", "19");

        //沙尘暴、小到中雨、中到大雨、大到暴雨、暴雨到大暴雨、大暴雨到特大暴雨
        weatherType.put("沙尘暴", "20");
        weatherType.put("小到中雨", "21");
        weatherType.put("中到大雨", "22");
        weatherType.put("大到暴雨", "23");
        weatherType.put("暴雨到大暴雨", "24");
        weatherType.put("大暴雨到特大暴雨", "25");

        //小到中雪、 中到大雪、大到暴雪、浮尘、扬沙、强沙尘暴、霾
        weatherType.put("小到中雪", "26");
        weatherType.put("中到大雪", "27");
        weatherType.put("大到暴雪", "28");
        weatherType.put("浮尘", "29");
        weatherType.put("扬沙", "30");
        weatherType.put("强沙尘暴", "31");

        return weatherType;
    }

}
