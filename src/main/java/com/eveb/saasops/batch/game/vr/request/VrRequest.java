package com.eveb.saasops.batch.game.vr.request;

import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.gns.model.IParameterModel;
import com.eveb.saasops.batch.game.vr.domain.VrLotteryBetLog;
import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.util.AesUtil;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class VrRequest {

    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    /**
     * 获取投注记录
     */
    public VrLotteryBetLog getBetList(IParameterModel iParameterModel, VrLotteryBetLog vrLotteryBetLog) throws Exception {
        OkHttpClient okHttpClient = okHttpProxyUtils.proxyClient;
        /**请求url*/
        String url = iParameterModel.getApi().getPcUrl();
        /**POST请求实体*/
        Map<String, String> RequestMapForm = getRequestMapForm(iParameterModel);
        /**发送请求*/
        String resultJson = okHttpProxyUtils.postJson(okHttpClient, url, RequestMapForm);
        log.info("=====>线路 ：【 " + iParameterModel.getApi().getAgyAcc() + " 】请求VR拉取数据的URL为 : 【 " + url + " 】 ");
        log.info("=====>线路 ：【 " + iParameterModel.getApi().getAgyAcc() + " 】请求VR拉取数据的实体内容为 : 【 " + RequestMapForm + " 】 ");

        /**解密返回结果*/
        resultJson = AesUtil.aesDecrypt32(resultJson, iParameterModel.getApi().getMd5Key());
        /**封装对象*/
        VrLotteryBetLog subVrLotteryBetLog = JSON.parseObject(resultJson, VrLotteryBetLog.class);
        log.info("=====>线路【  " + iParameterModel.getApi().getAgyAcc() + " 】本次拉取的数据数量为：【 " + subVrLotteryBetLog.getBetRecords().size() + " 】条");
        log.info("=====>*************VR平台*************");
        vrLotteryBetLog.setRecordCountPerPage(vrLotteryBetLog.getRecordCountPerPage() + subVrLotteryBetLog.getRecordCountPerPage());
        vrLotteryBetLog.setTotalRecords(vrLotteryBetLog.getTotalRecords() + subVrLotteryBetLog.getTotalRecords());
        vrLotteryBetLog.setRecordPage(subVrLotteryBetLog.getRecordPage());
        vrLotteryBetLog.getBetRecords().addAll(subVrLotteryBetLog.getBetRecords());
        if (vrLotteryBetLog.getBetRecords().size() < 1000) {
            return vrLotteryBetLog;
        }
        iParameterModel.setStartTime(ApplicationConstant.DateFormat.SDF_YYYYMMdd_HHmmss.format((subVrLotteryBetLog.getBetRecords()).get(subVrLotteryBetLog.getBetRecords().size() - 1).getCreateTime()));
        /**尾递归请求*/
        return getBetList(iParameterModel, vrLotteryBetLog);
    }


    /**
     * 封装请求实体
     */
    public Map<String, String> getRequestMapForm(IParameterModel iParameterModel) throws Exception {
        Map<String, String> map = new HashMap();
        map.put("version", ApplicationConstant.CONSTANT_VR_VERSION);
        map.put("id", iParameterModel.getApi().getAgyAcc());
        String url = "{\n" +
                "\"startTime\": \"%s\",\n" +
                "\"endTime\": \"%s\",\n" +
                "\"channelId\": -1,\n" +
                "\"issueNumber\": \"\",\n" +
                "\"playerName\": \"\",\n" +
                "\"serialNumber\": \"\",\n" +
                "\"state\": -1,\n" +
                "\"isUpdateTime\": false,\n" +
                "\"recordCountPerPage\": 0,\n" +
                "\"recordPage\": 1000\n" +
                "}";
        String requestUrl = String.format(url, iParameterModel.getStartTime(), iParameterModel.getEndTime());
        String data = AesUtil.aesEncrypt32(requestUrl, iParameterModel.getApi().getMd5Key());
        map.put("data", data);
        return map;
    }
}
