package com.chenqi.waveshare.for4in2;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

public class Epaper4in2DrawImg {

    private Epaper4in2DrawImg() {
    }

    private static Epaper4in2DrawImg epaper_4In2DrawImg = new Epaper4in2DrawImg();

    public static Epaper4in2DrawImg getInstance() {
        return epaper_4In2DrawImg;
    }

    private For4in2Driver for4In2Driver = For4in2Driver.getInstance();

    final int[] GRAY_SCALE_OF_4 = {0xff / 4, 0xff / 4 * 2, 0xff / 4 * 3, 0xff};

    /**
     * image's resolution must 400x300
     *
     * @param grayImage resolution must 400x300 ,and type is BufferedImage.TYPE_BYTE_GRAY
     * @throws IOException
     * @throws InterruptedException
     */
    public void displayImgWith4GrayScale(BufferedImage grayImage) throws IOException, InterruptedException {
        System.out.println("displayImgWith4GrayScale ===");

        if(grayImage.getHeight() != For4in2Driver.HEIGHT ||grayImage.getWidth() != For4in2Driver.WIDTH || grayImage.getType() != BufferedImage.TYPE_BYTE_GRAY)
        {
            System.out.println("displayImgWith4GrayScale not a gray img ===");
            return;
        }
        for4In2Driver.sendCommand(0x92);
        for4In2Driver.sendCommand(0x10);

        final byte[] pixels = ((DataBufferByte) grayImage.getRaster().getDataBuffer()).getData();
        int step = 0;
        int temp = 0;

        for (int i = 0; i < For4in2Driver.WIDTH * For4in2Driver.HEIGHT; i++) {
            int grayNum = pixels[i] & 0xFF;
            if (grayNum < 0xff / 2) {
                temp |= 0;
            } else {
                temp |= 1;
            }

            step++;
            if (step != 8) {
                temp = temp << 1;
            } else {
                for4In2Driver.sendData(temp);
                temp = 0;
                step = 0;
            }
        }
        temp = 0;
        for4In2Driver.sendCommand(0x13);
        for (int i = 0; i < For4in2Driver.WIDTH * For4in2Driver.HEIGHT; i++) {
            int grayNum = pixels[i] & 0xFF;
            if (grayNum < GRAY_SCALE_OF_4[0]) {
                temp |= 0;
            } else if(grayNum < GRAY_SCALE_OF_4[1]){
                temp |= 1;
            }else if (grayNum < GRAY_SCALE_OF_4[2]) {
                temp |= 0;
            } else if(grayNum <= GRAY_SCALE_OF_4[3]){
                temp |= 1;
            }

            step++;
            if (step != 8) {
                temp = temp << 1;
            } else {
                step = 0;
                for4In2Driver.sendData(temp);
                temp = 0;
            }
        }
        graySetLut();
        for4In2Driver.sendCommand(0x12);
        Thread.sleep(200);
        for4In2Driver.readBusy();
    }


    private void graySetLut() throws IOException {
        for4In2Driver.sendCommand(0x20);
        for (int data : EPD_4IN2_4GRAY_LUT_VCOM) {
            for4In2Driver.sendData(data);
        }
        for4In2Driver.sendCommand(0x21);
        for (int data : EPD_4IN2_4GRAY_LUT_WW) {
            for4In2Driver.sendData(data);
        }
        for4In2Driver.sendCommand(0x22);
        for (int data : EPD_4IN2_4GRAY_LUT_BW) {
            for4In2Driver.sendData(data);
        }
        for4In2Driver.sendCommand(0x23);
        for (int data : EPD_4IN2_4GRAY_LUT_WB) {
            for4In2Driver.sendData(data);
        }
        for4In2Driver.sendCommand(0x24);
        for (int data : EPD_4IN2_4GRAY_LUT_BB) {
            for4In2Driver.sendData(data);
        }
        for4In2Driver.sendCommand(0x25);
        for (int data : EPD_4IN2_4GRAY_LUT_WW) {
            for4In2Driver.sendData(data);
        }
    }

    private final int[] EPD_4IN2_4GRAY_LUT_VCOM = {
            0x00, 0x0A, 0x00, 0x00, 0x00, 0x01,
            0x60, 0x14, 0x14, 0x00, 0x00, 0x01,
            0x00, 0x14, 0x00, 0x00, 0x00, 0x01,
            0x00, 0x13, 0x0A, 0x01, 0x00, 0x01,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    private final int[] EPD_4IN2_4GRAY_LUT_WW = {
            0x40, 0x0A, 0x00, 0x00, 0x00, 0x01,
            0x90, 0x14, 0x14, 0x00, 0x00, 0x01,
            0x10, 0x14, 0x0A, 0x00, 0x00, 0x01,
            0xA0, 0x13, 0x01, 0x00, 0x00, 0x01,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    private final int[] EPD_4IN2_4GRAY_LUT_BW = {
            0x40, 0x0A, 0x00, 0x00, 0x00, 0x01,
            0x90, 0x14, 0x14, 0x00, 0x00, 0x01,
            0x00, 0x14, 0x0A, 0x00, 0x00, 0x01,
            0x99, 0x0C, 0x01, 0x03, 0x04, 0x01,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    private final int[] EPD_4IN2_4GRAY_LUT_WB = {
            0x40, 0x0A, 0x00, 0x00, 0x00, 0x01,
            0x90, 0x14, 0x14, 0x00, 0x00, 0x01,
            0x00, 0x14, 0x0A, 0x00, 0x00, 0x01,
            0x99, 0x0B, 0x04, 0x04, 0x01, 0x01,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    final static int[] EPD_4IN2_4GRAY_LUT_BB = {
            0x80, 0x0A, 0x00, 0x00, 0x00, 0x01,
            0x90, 0x14, 0x14, 0x00, 0x00, 0x01,
            0x20, 0x14, 0x0A, 0x00, 0x00, 0x01,
            0x50, 0x13, 0x01, 0x00, 0x00, 0x01,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
}
