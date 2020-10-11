package com.chenqi.waveshare.for4in2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.chenqi.ap.OperateAp;
import com.chenqi.basetools.BaseTools;
import com.chenqi.weather.sojson.GetSojsonWeather;
import com.chenqi.weather.sojson.SojsonWeatherService;
import com.chenqi.weather.sojson.pojo.SojsonForecast;
import com.chenqi.weather.sojson.pojo.SojsonWeather;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;

public class GetEpaperImg {

    private static Logger LOG = Logger.getLogger(GetEpaperImg.class);


    /**
     * 获取天气预报图片
     *
     * @return
     * @throws IOException
     */
    public static BufferedImage getWeatherImg() throws IOException {
        LOG.debug("start to getFutureWeatherImg ");
        int width = For4in2Driver.WIDTH;
        int height = For4in2Driver.HEIGHT;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = image.createGraphics();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, 0xffffff);
            }
        }

        String piIp = OperateAp.getRaspiIP();
        g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        g.setColor(new Color(0x0));
        g.drawString("IP:" + piIp, 0, 16);

        String week = BaseTools.getWeekStr(new Date());
        String date = BaseTools.getTimeStr("MM-dd ") + week;
        String time = BaseTools.getTimeStr("HH:mm");
        g.setFont(new Font("微软雅黑", Font.PLAIN, 40));
        g.setColor(new Color(0x0));
        g.drawString(date, 0, 60);

        g.setFont(new Font("微软雅黑", Font.PLAIN, 72));
        g.setColor(new Color(0x0));
        g.drawString(time, 215, 62);

        //画上横线
        g.drawLine(0, 69, 400, 69);
        //画上竖线
        g.drawLine(205, 0, 205, 69);
        //画下竖线
        g.drawLine(205, 69, 205, 267);
        //画下横线
        g.drawLine(0, 267, 400, 267);

        //画右上横线
        g.drawLine(205, 135, 400, 135);
        //画右下横线
        g.drawLine(205, 201, 400, 201);

        //101190101 南京
        SojsonWeather result = SojsonWeatherService.getWeatherByCityId("101190101");

        String city = result.getCityInfo().getCity().substring(0, 2);
        String quality = "空气质量 : " + result.getData().getQuality();
        String humility = result.getData().getShidu();
        String type = result.getData().getSojsonForecast().get(0).getType();
        String fb = GetSojsonWeather.WEATHERTYPE_MAP.get(type);
        String hiTemp = result.getData().getSojsonForecast().get(0).getHigh().split(" ")[1];
        String lowTemp = result.getData().getSojsonForecast().get(0).getLow().split(" ")[1];
        String temperature = "温度 : " + lowTemp + "~" + hiTemp;
        String notice = result.getData().getSojsonForecast().get(0).getNotice();

        g.setFont(new Font("微软雅黑", Font.BOLD, 40));
        g.drawString("南京", 32, 108);

        g.setFont(new Font("微软雅黑", Font.BOLD, 26));
        g.drawString(type, 122, 110);

        String wind = result.getData().getSojsonForecast().get(0).getFx() +
                result.getData().getSojsonForecast().get(0).getFl();
        g.setFont(new Font("微软雅黑", Font.PLAIN, 22));
        g.drawString(wind, 2, 137);
        g.drawString(quality, 2, 163);
        g.drawString(temperature, 2, 189);

        BufferedImage fbImg = BaseTools.getResourceImg("/weatherIcon/a_" + fb + ".gif");
        BufferedImage tianqiImg = new BufferedImage(70, 65, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < fbImg.getWidth(); x++) {
            for (int y = 0; y < fbImg.getHeight(); y++) {
                int rgb = fbImg.getRGB(x, y);
                int blue = rgb & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                if (blue > 200 && red > 200 && green > 200) {
                    tianqiImg.setRGB(x, y, 0xffffff);
                } else {
                    tianqiImg.setRGB(x, y, 0x0);
                }
            }
        }

        g.drawImage(tianqiImg, 20, 194, 70, 65, null);

        //画坐标图案
        g.drawImage(BaseTools.getResourceImg("/weatherIcon/zuobiao.jpg"), 2, 72, 28, 40, null);

        //下雨天则画雨伞
        if (Integer.parseInt(fb) > 2 || Integer.parseInt(fb) > 2) {
            g.drawImage(BaseTools.getResourceImg("/weatherIcon/umbrella.jpg"), 100, 202, 50, 46, null);
        } else {
            //g.drawImage(BaseTools.getResourceImg("/weatherIcon/umbrella.jpg"), 100, 202, 50, 46, null);
        }

        SojsonForecast tomorrowForecast = result.getData().getSojsonForecast().get(1);
        SojsonForecast twoDaysLaterForecast = result.getData().getSojsonForecast().get(2);
        SojsonForecast thirdDaysLaterForecast = result.getData().getSojsonForecast().get(3);

        drawEveryFutureWeather(tomorrowForecast, g, 69);
        drawEveryFutureWeather(twoDaysLaterForecast, g, 135);
        drawEveryFutureWeather(thirdDaysLaterForecast, g, 201);


        g.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        g.drawString(notice, 2, 290);
        return image;
    }

    /**
     * 做每天的天气预报图
     *
     * @param futureWeather
     * @param graphics2D
     * @param y
     * @throws IOException
     */
    private static void drawEveryFutureWeather(SojsonForecast futureWeather, Graphics2D graphics2D, int height) throws IOException {
        String weather = futureWeather.getType();
        String hiTemp = futureWeather.getHigh().split(" ")[1];
        String lowTemp = futureWeather.getLow().split(" ")[1];
        String temperature = "温度:" + lowTemp + "~" + hiTemp;

        String week = futureWeather.getWeek();

        String fb = GetSojsonWeather.WEATHERTYPE_MAP.get(weather);

        graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 20));

        //如果是下雨天，展示红色字体，如果是不下雨的天，展示绿色字体
        Color color = null;
        if (Integer.parseInt(fb) > 2) {
            graphics2D.drawImage(BaseTools.getResourceImg("/weatherIcon/umbrella.jpg"), 315, height+3, 38, 38, null);
        } else {

        }
        week = week.replace("星期","周");
        graphics2D.drawString(week + " " + weather, 210, height + 40);
        graphics2D.drawString(temperature, 210, height + 62);

        BufferedImage fbImg = BaseTools.getResourceImg("/weatherIcon/b_" + fb + ".gif");
        BufferedImage tianqiImg = new BufferedImage(fbImg.getWidth(), fbImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < fbImg.getWidth(); x++) {
            for (int y = 0; y < fbImg.getHeight(); y++) {
                int rgb = fbImg.getRGB(x, y);
                int blue = rgb & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                if (blue > 200 && red > 200 && green > 200) {
                    tianqiImg.setRGB(x, y, 0xffffff);
                } else {
                    tianqiImg.setRGB(x, y, 0x0);
                }
            }
        }

        graphics2D.drawImage(tianqiImg, 360, height + 3, 40, 37, null);
    }

    public static void main(String[] args) throws IOException {
        BufferedImage img = GetEpaperImg.getWeatherImg();
        ImageIO.write(img, "jpg", new File("D:\\test3.jpg"));
    }
}
