package com.chenqi.filetools.savetools;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;

public class SaveFiles {

    private static Logger LOG = Logger.getLogger(SaveFiles.class);

    /**
     * 保存电影描述文件
     *
     * @param pathName
     * @param content
     */
    public static void saveContent2File(String pathName, String content) {
        LOG.debug("start to save File : "+ pathName);
        File fp = new File(pathName);
        PrintWriter out = null;
        try {
            if (!fp.exists()) // && fp.isDirectory()
            {
                createFile(pathName);
            }

            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fp), "UTF-8")));
            out.write(content);
            out.flush();

        } catch (FileNotFoundException e) {
            LOG.error(e.toString());
        } catch (IOException e) {
            LOG.error(e.toString());
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 下载图片 成功返回ture
     *
     * @param imgUrl
     * @param filePath
     * @param fileName
     */
    public static boolean savaImage(String imgUrl, String filePath, String fileName) {

        Boolean saveOk = true;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        File dir = new File(filePath);
        try {
            Connection.Response response = Jsoup.connect(imgUrl).timeout(20000).ignoreContentType(true).execute();
            byte[] img = response.bodyAsBytes();
            // 判断文件目录是否存在
            if (!dir.exists()) // && dir.isDirectory()
            {
                // dir.mkdir();
                createFile(filePath);
            }
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(img);
        } catch (IOException e)
        {
            saveOk = false;
            LOG.error(e.toString()+",imgUrl="+imgUrl);
        } catch (Exception e)
        {
            saveOk = false;
            LOG.error(e.toString()+",imgUrl="+imgUrl);
        } catch (Error e)
        {
            saveOk = false;
            LOG.error(e.toString()+",imgUrl="+imgUrl);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    LOG.error(e.toString());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOG.error(e.toString());
                }
            }
        }
        return saveOk;
    }

    private static void createFile(String path) throws IOException {
        if (null != path && !"".equals(path)) {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
    }
}
