package com.eveb.saasops.batch.game.pgcb.request;

import com.csvreader.CsvReader;
import com.eveb.saasops.batch.game.agin.domain.AginRequestParameterModel;
import com.eveb.saasops.batch.game.pgcb.domain.PgcbRequestParameterModel;
import com.eveb.saasops.batch.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import tk.mybatis.mapper.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 盘古彩播 2018-07-27
 */
@Slf4j
public class PgcbDownloadFile {

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param model FTP服务器上的参数
     * @return
     */
    public static List<String> downFile(PgcbRequestParameterModel model) throws Exception {
        List<String> list = new ArrayList();
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(model.getApi().getSecureCodes().get("ftpIp"));
            //采用默认端口，使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(model.getApi().getSecureCodes().get("ftpUser"), model.getApi().getSecureCodes().get("ftpPass"));//登录
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
        log.info("PGCB请求参数 " + model.getRemotePath() + ":" + model.toString());
        log.info("PGCB返回数据 :" + list.toString());
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
                    String strRead = reader.getRawRecord().replace("<row ", "").replace("/>", "");
                    csvList.add(strRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        }
        return csvList;
    }

    /**
     * 获取所有文件名
     *
     * @param model
     * @return
     * @throws Exception
     */
    public static List<String> downFileName(PgcbRequestParameterModel model) throws Exception {
        List<String> list = new ArrayList();
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(model.getApi().getSecureCodes().get("ftpIp"));
            //采用默认端口，使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(model.getApi().getSecureCodes().get("ftpUser"), model.getApi().getSecureCodes().get("ftpPass"));//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return list;
            }
            ftp.changeWorkingDirectory(model.getRemotePath());//转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();

            Map<String, String> mapFile = new HashMap<>();
            for (FTPFile ff : fs) {
                if (model.getStartDate() != null && model.getEndDate() != null) {//是否输入日期
                    Date fileUpdateDate = DateUtil.addDateHour(ff.getTimestamp().getTime(), 8);//获取文件更新时间,系统文件少8小时
                    //System.out.println(DateUtil.format(fileUpdateDate, DateUtil.FORMAT_18_DATE_TIME));
                    if (model.getStartDate().before(fileUpdateDate) && fileUpdateDate.before(model.getEndDate())) {
                        mapFile.put(ff.getName(), DateUtil.format(fileUpdateDate, DateUtil.FORMAT_18_DATE_TIME));
                    }
                }
            }
            List<Map.Entry<String, String>> mapList = new ArrayList<Map.Entry<String, String>>(mapFile.entrySet());//根据日期排序
            Collections.sort(mapList, new Comparator<Map.Entry<String, String>>() {
                //升序排序
                public int compare(Map.Entry<String, String> o1,
                                   Map.Entry<String, String> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }

            });
            for (Map.Entry<String, String> mapping : mapList) {
                list.add(mapping.getKey());
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
        log.info("PGCB请求参数 " + model.getRemotePath() + ":" + model.toString());
        log.info("PGCB返回数据 :" + list.toString());
        return list;
    }


    /**
     * 获取日期内所有数据
     *
     * @param model
     * @return
     * @throws Exception
     */
    public static List<String> downFileByDate(PgcbRequestParameterModel model) throws Exception {
        List<String> dateList = new ArrayList();
        if (model.getStartDate() != null && model.getEndDate() != null) {
            List<Date> listDate = DateUtil.getDatesBetweenTwoDate(model.getStartDate(), model.getEndDate());//获取跨日时间
            for (Date dateUpdat : listDate) { //文件以日期存储存储,yyyy/dd
                String remotePath = model.getRemotePath();
                String str = DateUtil.format(dateUpdat, DateUtil.FORMAT_8_DATE);
                String fileNameTime = str.substring(0, 6) + "/";
                String fileNameDay = str.substring(str.length() - 2);
                if (StringUtil.isNotEmpty(model.getRemotePath())) {
                    model.setRemotePath(remotePath + fileNameTime + fileNameDay);
                } else {
                    model.setRemotePath(fileNameTime + fileNameDay);
                }
                List<String> fileNameList = downFileName(model);
                if (fileNameList != null && fileNameList.size() > 0) {
                    model.setFileName(fileNameList);
                    List<String> date = PgcbDownloadFile.downFile(model);//获取文件数据
                    dateList.addAll(date);
                }
                model.setRemotePath(remotePath);
            }
        }
        return dateList;
    }
}
