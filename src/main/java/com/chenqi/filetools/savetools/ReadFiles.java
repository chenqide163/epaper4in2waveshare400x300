package com.chenqi.filetools.savetools;

import org.apache.log4j.Logger;

import java.io.*;

public class ReadFiles {
    private static Logger log = Logger.getLogger(ReadFiles.class);

    public static String getFileContent(String filePath) {
        log.debug("start to read File = " + filePath);
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (UnsupportedEncodingException e) {
            log.debug(e.toString());
        } catch (FileNotFoundException e) {
            log.debug(e.toString());
        } catch (Exception e) {
            log.debug(e.toString());
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
