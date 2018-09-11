package com.eveb.saasops.batch.sys.mapper;

import com.eveb.saasops.batch.sys.domain.DatasourceManage;
import com.eveb.saasops.batch.sys.domain.JobFailMessageModel;
import com.eveb.saasops.batch.sys.domain.TGmGame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 16:20 2017/12/14
 **/
@Mapper
@Component
public interface SysMapper {

    List<DatasourceManage> selectDatasourceManageAll();

    List<String> selectProxys(@Param("groups") String groups);

    Integer insertJobExecuteFailMessage(@Param("model")JobFailMessageModel model);

    void updateJobExecuteStatus(@Param("model")JobFailMessageModel model);

    void updateJobExecuteBatch(@Param("list")List<JobFailMessageModel> list,@Param("executeStatus")int executeStatus);

    void saveOrUpdate(@Param("model")JobFailMessageModel model);

    List<JobFailMessageModel> getJobExecuteFailMessages(@Param("platform") String platform, @Param("jobid") Integer jobid, @Param("limit") Integer limit);

    List<TGmGame> getGmGameList(@Param("gmGame")TGmGame gmGame);

    List<String> getApiPrefixBySiteCode(@Param("siteCode") String siteCode);

    List<String> getSiteCodeList(@Param("siteCode") String siteCode,@Param("isApi")Integer isApi );

    String getSchemaName(@Param("siteCode") String siteCode);
}
