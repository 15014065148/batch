package com.eveb.saasops.batch.game.agin.request;

import com.csvreader.CsvReader;
import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AginDownloadFile {

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param model FTP服务器上的参数
     * @return
     */
    public static List<String> downFile(AginRequestParameterModel model) throws Exception {
        List<String> list = new ArrayList();
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(model.getUrl());
            //采用默认端口，使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(model.getUsername(), model.getPassword());//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return list;
            }
            ftp.changeWorkingDirectory(model.getRemotePath());//转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            /****读取多个文件***/
            if (model.getFileName().size() > 0) {
                for (String name : model.getFileName()) {
                    InputStream is = ftp.retrieveFileStream(name);
                    list.addAll(csv(is));
                    if (is != null) {
                        ftp.completePendingCommand();
                        is.close();
                    }
                }
            } else {
                /****读取单个数据文件***/
                FTPFile ff = fs[fs.length - 1];
                InputStream is = ftp.retrieveFileStream(ff.getName());//不传入文件名的情况下默认读取最后一个文件
                list.addAll(csv(is));
                if (is != null) {
                    ftp.completePendingCommand();
                    is.close();
                }
            }
            ftp.logout();
        } catch (IOException e) {
            throw e;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        log.info("AGIN请求参数 :" + model.toString());
        log.info("AGIN返回数据 :" + list.toString());
        return list;
    }

    /**
     * <b>将一个IO流解析，转化数组形式的集合<b>
     *
     * @param in 文件inputStream流
     */
    private static ArrayList<String> csv(InputStream in) {
        ArrayList<String> csvList = new ArrayList<String>();
        if (null != in) {
            CsvReader reader = new CsvReader(in, '>', Charset.forName("UTF-8"));
            try {
                while (reader.readRecord()) {
                    //获取的为每一行的信息
                    csvList.add(reader.getRawRecord().replace("<row ", "").replace("/>", ""));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        }
        return csvList;
    }

}
