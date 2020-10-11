package com.chenqi.ap;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OperateAp {
    private static Logger LOG = Logger.getLogger(OperateAp.class);

    public static void startAP() {
        if(isRaspiApStart())
        {
            LOG.debug("RaspiAp is already start");
            return ;
        }
        try {
            String commandStr = "sudo nohup create_ap -n wlan0 pi &";
            LOG.info("start AP :" + commandStr);
            Process pro = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", commandStr});
            pro.waitFor();
            LOG.info("success to start AP!");
            pro.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void killAP() {
        try {
            String commandStr = "sudo create_ap -n wlan0 pi &";
            LOG.info("start AP :" + commandStr);
            Process pro = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", commandStr});
            pro.waitFor();
            LOG.info("success to start AP!");
            pro.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isRaspiApStart(){
        InputStream in = null;
        BufferedReader read = null;
        try {
            String command = "ps -ef | grep create_";
            Process pro = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",command});
            pro.waitFor();
            in = pro.getInputStream();
            read = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;

            while ((line = read.readLine()) != null) {
                sb.append(line + "\n");
            }

            LOG.debug("ap info is : "+ sb);
            if(sb.toString().contains("create_ap")){
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(null != in){
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (read != null){
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;

    }

    public static String getRaspiIP() {
        InputStream in = null;
        BufferedReader read = null;
        try {
            String command = "hostname -I | cut -d' ' -f1";
            Process pro = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",command});
            pro.waitFor();
            in = pro.getInputStream();
            read = new BufferedReader(new InputStreamReader(in));
            String result = "";
            String line;

            while ((line = read.readLine()) != null) {
                result = result + line + "\n";
            }
            LOG.info("getRaspiIP is : " + result);
            return result;
        } catch (Exception e) {
            LOG.error(e);
            return "do not get the IP!";
        }
        finally {
            if(null != in){
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (read != null){
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
