package com.chenqi.weather.sojson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chenqi.basetools.BaseTools;
import com.chenqi.filetools.savetools.ReadFiles;
import com.chenqi.filetools.savetools.SaveFiles;
import com.chenqi.weather.sojson.pojo.CityInfo;
import com.chenqi.weather.sojson.pojo.SojsonForecast;
import com.chenqi.weather.sojson.pojo.SojsonWeather;
import com.chenqi.weather.sojson.pojo.SojsonWeatherSubData;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SojsonWeatherService {

    private static Logger LOG = Logger.getLogger(SojsonWeatherService.class);

    /**
     * 进行网络请求
     *
     * @param cityId
     * @return
     */
    public static String getWeatherFromSojsonNet(String cityId) {
        String targetUrl = "http://t.weather.itboy.net/api/weather/city/" + cityId;
        Document doc = null;
        try {
            doc = Jsoup.connect(targetUrl).ignoreContentType(true)
                    .userAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)").timeout(5000).get();
        } catch (IOException e) {
            LOG.error("please check the internett, url is not connected :" + targetUrl,e);
            return null;
        }
        if (null != doc) {
            String content = doc.getElementsByTag("body").text();
            return content;
        } else {
            return null;
        }
    }

    private static String getSojsonWeatherStr(String cityId) {
        String todayStr = BaseTools.getTimeStr("yyyy-MM-dd-HH");
        String jarPath = BaseTools.getJarPath();
        String weatherFilePath = jarPath + File.separator + "weatherFile" + File.separator + todayStr + ".json";
        LOG.debug("weatherFilePath is : " + weatherFilePath);
        File weatherFile = new File(weatherFilePath);
        if (!weatherFile.exists()) {
            String weatherContent = getWeatherFromSojsonNet(cityId);
            if (null == weatherContent || "".equals(weatherContent.trim())) {
                LOG.error("weatherContent is null!!");
                return null;
            }
            SaveFiles.saveContent2File(weatherFilePath, weatherContent);
        }
        String content = ReadFiles.getFileContent(weatherFilePath);
        return content;
    }

    public static SojsonWeather getWeatherByCityId(String cityId) {
        String result = getSojsonWeatherStr(cityId);
        SojsonWeather sojsonWeather = null;
        if (result != null) {
            JSONObject obj = JSON.parseObject(result);
            /*获取返回状态码*/
            result = obj.getString("status");
            if ("200".equals(result)) {
                String message = obj.getString("message");
                String status = obj.getString("status");
                String date = obj.getString("date");
                String time = obj.getString("time");

                sojsonWeather = new SojsonWeather();
                sojsonWeather.setMessage(message);
                sojsonWeather.setDate(date);
                sojsonWeather.setTime(time);
                sojsonWeather.setStatus(status);

                JSONObject city = obj.getJSONObject("cityInfo");
                if (null != city) {
                    CityInfo cityInfo = getSojsonCityInfo(city);
                    sojsonWeather.setCityInfo(cityInfo);
                }

                JSONObject data = obj.getJSONObject("data");
                if (null != data) {
                    SojsonWeatherSubData sojsonWeatherSubData = getSojsonWeatherSubData(data);
                    sojsonWeather.setData(sojsonWeatherSubData);
                }
            }
        }

        return sojsonWeather;
    }

    private static SojsonWeatherSubData getSojsonWeatherSubData(JSONObject subData) {
        String shidu = subData.getString("shidu");
        String pm25 = subData.getString("pm25");
        String pm10 = subData.getString("pm10");
        String quality = subData.getString("quality");
        String wendu = subData.getString("wendu");
        String ganmao = subData.getString("ganmao");
        List<SojsonForecast> sojsonForecast = getSojsonForecastList(subData);

        SojsonWeatherSubData weatherSubData = new SojsonWeatherSubData();
        weatherSubData.setShidu(shidu);
        weatherSubData.setPm25(pm25);
        weatherSubData.setPm10(pm10);
        weatherSubData.setQuality(quality);
        weatherSubData.setWendu(wendu);
        weatherSubData.setGanmao(ganmao);
        weatherSubData.setSojsonForecast(sojsonForecast);
        return  weatherSubData;
    }

    private static List<SojsonForecast> getSojsonForecastList(JSONObject subData) {
        JSONArray jsonArray = subData.getJSONArray("forecast");
        List<SojsonForecast> sojsonForecasts = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject eachForecast = jsonArray.getJSONObject(i);
            String date = eachForecast.getString("date");
            String high = eachForecast.getString("high");
            String low = eachForecast.getString("low");
            String ymd = eachForecast.getString("ymd");
            String week = eachForecast.getString("week");
            String sunrise = eachForecast.getString("sunrise");
            String sunset = eachForecast.getString("sunset");
            String aqi = eachForecast.getString("aqi");
            String fx = eachForecast.getString("fx");
            String fl = eachForecast.getString("fl");
            String type = eachForecast.getString("type");
            String notice = eachForecast.getString("notice");
            SojsonForecast sojsonForecast = new SojsonForecast();

            sojsonForecast.setDate(date);
            sojsonForecast.setHigh(high);
            sojsonForecast.setLow(low);
            sojsonForecast.setYmd(ymd);
            sojsonForecast.setWeek(week);
            sojsonForecast.setSunrise(sunrise);
            sojsonForecast.setSunset(sunset);
            sojsonForecast.setAqi(aqi);
            sojsonForecast.setFl(fl);
            sojsonForecast.setFx(fx);
            sojsonForecast.setType(type);
            sojsonForecast.setNotice(notice);
            sojsonForecasts.add(sojsonForecast);
        }
        return sojsonForecasts;
    }

    private static CityInfo getSojsonCityInfo(JSONObject cityObj) {
        String city = cityObj.getString("city");
        String citykey = cityObj.getString("citykey");
        String parent = cityObj.getString("parent");
        String updateTime = cityObj.getString("updateTime");

        CityInfo cityInfo = new CityInfo();
        cityInfo.setCity(city);
        cityInfo.setCitykey(citykey);
        cityInfo.setParent(parent);
        cityInfo.setUpdateTime(updateTime);
        return cityInfo;
    }
}
