package com.eveb.saasops.batch.bet.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.saasops.batch.bet.dto.OnlineTimeDto;
import com.eveb.saasops.batch.bet.entity.MbrAccount;
import com.eveb.saasops.batch.bet.entity.MbrAccountTime;
import com.eveb.saasops.batch.bet.mapper.ValidBetMapper;
import com.eveb.saasops.batch.common.SchemaCode;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.mapper.SysMapper;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.Collections3;
import com.eveb.saasops.batch.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.eveb.saasops.batch.sys.util.DateUtil.*;


@Slf4j
@Service
@Transactional
public class OnlineTimeService {

    @Autowired
    private ElasticSearchService elasticSearchService;
    @Autowired
    private SysMapper sysMapper;
    @Autowired
    private ValidBetMapper validBetMapper;
    @Autowired
    private SysService sysService;

    /**
     * 15*60*1000
     */
    private static final Long fifteenMinutes = 900000L;

    public void accountOnlineTime() {
        List<String> siteCodes = sysService.getSiteCodeList();
        if (Collections3.isNotEmpty(siteCodes)) {
            siteCodes.stream().forEach(st -> setAccountOnlineTime(st));
        }
    }

    public void setAccountOnlineTime(String siteCode) {
        String schemaPrex = new SchemaCode(sysService.getSchemaName(siteCode)).getSchemaCode();
        List<MbrAccount> accounts = validBetMapper.findMbrAccountList(schemaPrex);
        if (Collections3.isNotEmpty(accounts)) {
            accounts.stream().forEach(as -> {
                tagLoginTime(as, siteCode, schemaPrex);
            });
        }
    }

    @Async("tagLoginTimeAsyncExecutor")
    public void tagLoginTime(MbrAccount account, String siteCode, String schemaPrex) {
        String startTime = validBetMapper.findMbrAccountTimeByAccountId(schemaPrex, account.getId());
        startTime = Objects.nonNull(startTime) ? format(DateUtil.parse(startTime, FORMAT_18_DATE_TIME),
                FORMAT_22_DATE_TIME).replace(" ", "T") + "Z" : null;
        String endTime = format(new Date(), FORMAT_22_DATE_TIME).replace(" ", "T") + "Z";
        List<Date> dates = getBettimeList(siteCode, startTime, endTime, account.getLoginName());
        removeDuplicate(dates);
        if (Collections3.isNotEmpty(dates)) {
            List<OnlineTimeDto> onlineTimeDtos = getOnlineTimeDtos(dates);
            for (int i = 0; i < onlineTimeDtos.size(); i++) {
                addMbrAccountTime(i, onlineTimeDtos, account, schemaPrex);
            }
        }
    }

    private void removeDuplicate(List<Date> dates) {
        LinkedHashSet<Date> set = new LinkedHashSet<Date>(dates.size());
        set.addAll(dates);
        dates.clear();
        dates.addAll(set);
    }

    private void addMbrAccountTime(int i, List<OnlineTimeDto> onlineTimeDtos, MbrAccount account, String schemaPrex) {
        OnlineTimeDto onlineTimeDto = onlineTimeDtos.get(i);
        Long login = onlineTimeDto.getTime();
        Long duration = null, logout = null;
        if (onlineTimeDto.getIsLogin() == 3) {
            duration = fifteenMinutes;
            logout = onlineTimeDto.getTime() + fifteenMinutes;
        }
        if (onlineTimeDto.getIsLogin() == 1) {
            for (int j = i + 1; j < onlineTimeDtos.size(); j++) {
                OnlineTimeDto timeDto = onlineTimeDtos.get(j);
                if (timeDto.getIsLogin() == 2) {
                    duration = timeDto.getTime() - login;
                    logout = timeDto.getTime();
                    break;
                }
            }
        }
        if (onlineTimeDto.getIsLogin() == 3 || onlineTimeDto.getIsLogin() == 1) {
            MbrAccountTime accountTime = new MbrAccountTime();
            accountTime.setSchemaPrex(schemaPrex);
            accountTime.setAccountId(account.getId());
            accountTime.setLoginName(account.getLoginName());
            accountTime.setCreateTime(getCurrentDate(FORMAT_18_DATE_TIME));
            accountTime.setDuration(duration);
            accountTime.setLogin(stampToDate(login, FORMAT_18_DATE_TIME));
            accountTime.setLogout(stampToDate(logout, FORMAT_18_DATE_TIME));
            validBetMapper.insertMbrAccountTime(accountTime);
        }
    }

    private List<OnlineTimeDto> getOnlineTimeDtos(List<Date> dates) {
        List<OnlineTimeDto> timeDtos = dates.stream().map(
                ds -> {
                    OnlineTimeDto onlineTimeDto = new OnlineTimeDto();
                    onlineTimeDto.setTime(ds.getTime());
                    return onlineTimeDto;
                }).collect(Collectors.toList());
        for (int i = 0; i < timeDtos.size(); i++) {
            setOnlineTimeDtos(i, timeDtos);
        }
        return timeDtos;
    }

    private void setOnlineTimeDtos(int j, List<OnlineTimeDto> timeDtos) {
        if (j < timeDtos.size() - 1) {
            Long time = timeDtos.get(j).getTime();
            Long nextTime = timeDtos.get(j + 1).getTime();
            Long subtractTime = nextTime - time;
            if (subtractTime > fifteenMinutes) {
                if (j == 0) {
                    timeDtos.get(j).setIsLogin(3);
                } else {
                    int isLogin = timeDtos.get(j - 1).getIsLogin();
                    int nLogin = isLogin == 1 || isLogin == 4 ? 2 : 3;
                    timeDtos.get(j).setIsLogin(nLogin);
                }
            } else {
                if (j == 0) {
                    timeDtos.get(j).setIsLogin(1);
                } else {
                    int isLogin = timeDtos.get(j - 1).getIsLogin();
                    int nLogin = isLogin == 1 || isLogin == 4 ? 4 : 1;
                    timeDtos.get(j).setIsLogin(nLogin);
                }
            }
        }
        if (j == timeDtos.size() - 1 && j > 0) {
            int isLogin = timeDtos.get(j - 1).getIsLogin();
            int nLogin = isLogin == 1 || isLogin == 4 ? 2 : 3;
            timeDtos.get(j).setIsLogin(nLogin);
        }
    }

    public List<Date> getBettimeList(String siteCode, String startTime, String endTime, String userlist) {
        List<String> predixs = sysMapper.getApiPrefixBySiteCode(siteCode);
        List<Date> rslist = new ArrayList<>();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termsQuery("userName", userlist));
        /***根据下注时间查询***/
        if (Objects.nonNull(startTime)) {
            query.must(QueryBuilders.rangeQuery("betTime").gte(startTime).lt(endTime));
        }
        query.must(QueryBuilders.termsQuery("sitePrefix", predixs));
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        query.must(builder);
        SearchRequestBuilder searchRequestBuilder = elasticSearchService.client.prepareSearch("report");
        /****顺序排序**/
        searchRequestBuilder.addSort("betTime", SortOrder.ASC);
        searchRequestBuilder.setQuery(query);
        String str = searchRequestBuilder.toString();
        try {
            Response response = elasticSearchService.restClient.performRequest("GET",
                    ElasticSearchConstant.REPORT_INDEX + "/" + ElasticSearchConstant.REPORT_TYPE + "/_search",
                    Collections.singletonMap("_source", "true"),
                    new NStringEntity(str, ContentType.APPLICATION_JSON));
            Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
            JSONArray hits = ((JSONArray) (((Map) map.get("hits")).get("hits")));
            for (Object obj : hits) {
                Map objmap = (Map) obj;
                RptBetModel rpt = JSON.parseObject(objmap.get("_source").toString(), RptBetModel.class);
                rslist.add(rpt.getBetTime());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rslist;
    }
}
