package com.eveb.saasops.batch.sys.service;

import com.eveb.saasops.batch.sys.constants.ApplicationConstant;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.domain.TGmGame;
import com.eveb.saasops.batch.sys.mapper.SysMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 10:44 2017/12/29
 **/
@Service
public class SysService {

    @Autowired
    private SysMapper sysMapper;

    public Integer insertJobExecuteFailMessage(JobFailMessageModel model) {
        return sysMapper.insertJobExecuteFailMessage(model);
    }

    public void updateJobExecuteStatus(JobFailMessageModel model) {
        sysMapper.updateJobExecuteStatus(model);
    }

    public void saveOrUpdate(JobFailMessageModel model) {
        sysMapper.saveOrUpdate(model);
    }

    public List<JobFailMessageModel> getExecuteFailMessages(String platform, Integer jobid, Integer limit) {
        List<JobFailMessageModel> list=sysMapper.getJobExecuteFailMessages(platform, jobid, limit);
        if(list.size()>0) {
            sysMapper.updateJobExecuteBatch(list, ApplicationConstant.CONSTANT_JOBEXECUTE_PRO);
        }
        return list;
    }

    public List<TGmGame> getGmGameList(TGmGame gmGame) {
        return sysMapper.getGmGameList(gmGame);
    }

    public List<String> getApiPrefixBySiteCode(String siteCode)
    {
        return sysMapper.getApiPrefixBySiteCode(siteCode);
    }

    public List<String> getSiteCodeList(String siteCode){return sysMapper.getSiteCodeList(siteCode, ApplicationConstant.CONSTANT_ISAPI_FALSE);}

    public List<String> getSiteCodeList(){return sysMapper.getSiteCodeList(null, ApplicationConstant.CONSTANT_ISAPI_FALSE);}

    public String getSchemaName(String siteCode){
        return sysMapper.getSchemaName(siteCode);
    }
}
