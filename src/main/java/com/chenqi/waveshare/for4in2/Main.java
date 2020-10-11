package com.chenqi.waveshare.for4in2;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("start to run spi");
        try {
            For4in2Driver.getInstance().init(); //初始化
            For4in2Driver.getInstance().clear(); //清空屏幕
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (; ; ) {
            displayPiInfo();
            Thread.sleep(120 * 1000);
        }

    }

    /**
     * 打印树莓派信息
     */
    public static void displayPiInfo() {
        try {
            //打印4灰阶图片
            Epaper4in2DrawImg.getInstance().displayImgWith4GrayScale(GetEpaperImg.getWeatherImg());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
