package com.eveb.saasops.batch.bet.scheduled;

import com.eveb.saasops.batch.bet.entity.MbrAccount;
import com.eveb.saasops.batch.bet.entity.MbrBillDetail;
import com.eveb.saasops.batch.bet.mapper.MbrBillDetailMapper;
import com.eveb.saasops.batch.bet.processor.FinancialCountProcess;
import com.eveb.saasops.batch.bet.service.FinancialCountService;
import com.eveb.saasops.batch.common.SchemaCode;
import com.eveb.saasops.batch.sys.domain.DatasourceManage;
import com.eveb.saasops.batch.sys.service.SysService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.eveb.saasops.batch.sys.util.DateUtil.FORMAT_18_DATE_TIME;
import static com.eveb.saasops.batch.sys.util.DateUtil.getCurrentDate;

/**
 * 对账步骤 :
 * 1 单笔对账
 * 2 同一账户当前操作后的金额是下一笔操作的操作前金额
 * 3 同一账户得到所有操作金额 Sum(收入) - Sum(支出 ) =余额
 * Created by William on 2018/1/15.
 */
@Slf4j
@Service
@JobHander(value = "FinancialCountJobHandler")
public class FinancialCountJobHandler extends IJobHandler {

    @Autowired
    SysService sysService;

    @Resource(name = "stringRedisTemplate_3")
    RedisTemplate redisTemplate;
    @Autowired
    FinancialCountProcess process;

    FinancialCountService financialCountService;

    @Autowired
    MbrBillDetailMapper mbrBillDetailMapper ;

    {
        financialCountService = new FinancialCountService();
    }

    //@Override
    public ReturnT<String> execute11(String... strings) throws Exception {
        log.info("开始执行账单验证---");
        log.info("开始执行单笔对账---");
        String startTime = getCurrentDate(FORMAT_18_DATE_TIME);
        List<String> siteCodeList = sysService.getSiteCodeList();
        Map<String, List<MbrBillDetail>> sqlSessionMap = new HashMap<>();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        for(String siteCode : siteCodeList){
            String FinancialCount = valueOperations.get(siteCode + "Financial");
            String schemaCode =new SchemaCode(sysService.getSchemaName(siteCode)).getSchemaCode();
            sqlSessionMap.put(siteCode, mbrBillDetailMapper.findMbrBillDetail(Integer.parseInt(FinancialCount != null ? FinancialCount : "0"),schemaCode));
            //记录这一次的id最大值
            valueOperations.set(siteCode + "Financial" , mbrBillDetailMapper.findMbrBillDetaiIndex(schemaCode).toString());
        }
        int size = sqlSessionMap.size();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(size);
        FinancialCountService financialCountService = this.financialCountService;
        for (Map.Entry<String, List<MbrBillDetail>> entry : sqlSessionMap.entrySet()) {
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    financialCountService.getAllMbrBillDetail(entry.getValue(), entry.getKey(), financialCountService.getErrorData(), financialCountService.getMbrBillDetailMap());
                }
            });
        }
        log.info("账单验证:单笔对账--开始时间【" + startTime + "】,结束时间【" + getCurrentDate(FORMAT_18_DATE_TIME) + "】");
        startTime = getCurrentDate(FORMAT_18_DATE_TIME);
        log.info("账单验证:总余额验证--开始时间【" + startTime + "】,结束时间【" + getCurrentDate(FORMAT_18_DATE_TIME) + "】");

        log.info("账单验证--开始时间【" + startTime + "】,结束时间【" + getCurrentDate(FORMAT_18_DATE_TIME) + "】");
        System.out.println(financialCountService.getErrorData());
        return ReturnT.SUCCESS;
    }


    /**
     * 任务执行
     * @param strs
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String... strs) throws Exception {
        int readSize = 10000;
        log.info("开始执行账单验证---");

        String startTime = getCurrentDate(FORMAT_18_DATE_TIME);
        List<String> siteCodeList = sysService.getSiteCodeList();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        for(String siteCode : siteCodeList){
            String FinancialCount = valueOperations.get("FinancialCount:" +siteCodeList);
            //获取上一次保存的id
            int beginId = Integer.parseInt(FinancialCount != null ? FinancialCount : "0");
            //创建session连接
            //MbrBillDetailMapper mbrBillDetailMapper=(MbrBillDetailMapper)getSession(dataSourceMap.get(entry.getKey()),"com.eveb.saasops.batch.bet.mapper.MbrBillDetailMapper");
            String schemaCode =new SchemaCode(sysService.getSchemaName(siteCode)).getSchemaCode();
            //计算单笔错误
            financialCountOne(mbrBillDetailMapper,beginId,readSize,schemaCode,valueOperations);
            //验证同一账户的前一次操作前余额是上一次的操作后余额
            fincialCountMemberStepOne(mbrBillDetailMapper,beginId,schemaCode);
        }
        //Map.Entry entry = (Map.Entry) it.next();
        /*
        String sitePrefix="df";
        String FinancialCount = valueOperations.get("FinancialCount:" +"df");
        //获取上一次保存的id
        int beginId = Integer.parseInt(FinancialCount != null ? FinancialCount : "0");
        //创建session连接
        MbrBillDetailMapper mbrBillDetailMapper=(MbrBillDetailMapper)getSession(dataSourceMap.get(sitePrefix),"com.eveb.saasops.batch.bet.mapper.MbrBillDetailMapper");
        //计算单笔错误
        financialCountOne(mbrBillDetailMapper,beginId,readSize,sitePrefix,valueOperations);
        //验证同一账户的前一次操作前余额是上一次的操作后余额
        fincialCountMemberStepOne(mbrBillDetailMapper,beginId,sitePrefix);
        */
        log.info("账单验证--开始时间【" + startTime + "】,结束时间【" + getCurrentDate(FORMAT_18_DATE_TIME) + "】");
        return ReturnT.SUCCESS;
    }

    /**
     * 计算单笔账单错误
     * @param mbrBillDetailMapper
     * @param beginId
     * @param readSize
     * @param schemaCode
     */
    public void financialCountOne(MbrBillDetailMapper mbrBillDetailMapper,int beginId,int readSize,String schemaCode,ValueOperations<String, String> valueOperations){
        log.info("开始执行单笔对账---");
        //获取id的最大值
        Integer biggestId=mbrBillDetailMapper.findMbrBillDetaiIndex(schemaCode);
        //保存到redis数据库
         valueOperations.set("FinancialCount:" +schemaCode ,biggestId.toString());
        //获取当前计算共有多少条数据
        int num = mbrBillDetailMapper.findMbrBillDetaiCount(beginId,schemaCode);
        // 计算线程池执行次数
        int executeCount = num %readSize == 0 ? num / readSize :  num / readSize +1 ;
        for (int x = 0;x < executeCount;x++) {
            int currentNo = x ;
            int beginNo = currentNo;
            process.financialCount(mbrBillDetailMapper,schemaCode,beginId,beginNo,readSize);
        }
    }


    /**
     *验证同一账户的前一次操作前余额是上一次的操作后余额
     */
    public void fincialCountMemberStepOne(MbrBillDetailMapper mbrBillDetailMapper,Integer id,String schemaCode){
        //获取当前数据中有多少会员
        List<MbrAccount> mbrAccountList = mbrBillDetailMapper.findMbrAccountList(schemaCode);
        mbrAccountList.stream().forEach(mbrAccount -> {
            Integer mbrAccountId=mbrAccount.getId();
            //MbrBillDetailMapper mbrBillDetailMapperThread =(MbrBillDetailMapper)getSession(dataSourceMap.get(sitePrefix),"com.eveb.saasops.batch.bet.mapper.MbrBillDetailMapper");
            process.financialCountMemberStepOneprocess(mbrAccountId,id,mbrBillDetailMapper,schemaCode);
            process.financialCountMenberStepTwo(mbrAccountId,mbrBillDetailMapper,schemaCode);
        });
    }
}
