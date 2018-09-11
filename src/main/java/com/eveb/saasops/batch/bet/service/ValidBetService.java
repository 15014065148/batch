package com.eveb.saasops.batch.bet.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.saasops.batch.bet.dto.*;
import com.eveb.saasops.batch.bet.entity.*;
import com.eveb.saasops.batch.bet.mapper.ValidBetMapper;
import com.eveb.saasops.batch.common.SchemaCode;
import com.eveb.saasops.batch.sys.service.ElasticSearchReadService;
import com.eveb.saasops.batch.sys.service.ElasticSearchService;
import com.eveb.saasops.batch.sys.constants.ElasticSearchConstant;
import com.eveb.saasops.batch.sys.domain.TGmGame;
import com.eveb.saasops.batch.sys.mapper.SysMapper;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.*;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.eveb.saasops.batch.sys.util.DateConvert.getAmericaInterval;
import static com.eveb.saasops.batch.sys.util.DateUtil.*;

@Slf4j
@Service
@Transactional
public class ValidBetService {

    @Autowired
    private SysMapper sysMapper;
    @Autowired
    private ValidBetMapper betMapper;
    @Autowired
    private ElasticSearchReadService elasticSearchService;
    @Autowired
    private IDepotBalanceService balanceService;
    @Autowired
    private SysService sysService;

    private static int one = 1;
    private static int zero = 0;
    private static int two = 2;
    private static final int ONE_HUNDRED = 100;
    private static final String ACTIVITY_WATERBONUS = "FS";
    private static final String SYSTEM_USER = "系统审核";
    public static final String waterRebatesCode = "AQ0000005";
    private static final String ACTIVITY_AC = "AC";

    public void castMbrValidbet(String siteCode) {
        String schemaPrex = new SchemaCode(sysService.getSchemaName(siteCode)).getSchemaCode();
        OprActActivity activity = new OprActActivity();
        activity.setUseStart(getPastDate(one, FORMAT_10_DATE));
        activity.setCode5(waterRebatesCode);
        activity.setSchemaPrex(schemaPrex);
        List<OprActActivity> activities = betMapper.findActivityList(activity);
        String[] strings = getAmericaInterval(DateUtil.parse(getPastDate(one, FORMAT_10_DATE), FORMAT_10_DATE));

        for (OprActActivity as : activities) {
            int count = betMapper.findValidbetCount(as.getId(), getCurrentDate(FORMAT_10_DATE), schemaPrex);
            if (count > 0) {
                log.info("返水活动JOB一天只能执行一次，该活动已经执行过，活动ID【" + as.getId() + "】,数据库" + "【" + siteCode + "】");
                continue;
            }
            as.setSiteCode(siteCode);
            List<TGmGame> gmGames = setGameList(setAuditCats(as.getRule()));
            List<Map> mapList = getValidVet(siteCode, strings[0], strings[1], gmGames, null, getScope(as.getRule()));
            if (Collections3.isNotEmpty(mapList)) {
                mapList.stream().forEach(cs -> {
                    validBetThread(cs, as, schemaPrex);
                });
            }
        }
    }

    @Async("validBetActivityAsyncExecutor")
    public void validBetThread(Map cs, OprActActivity as, String schemaPrex) {
        Iterator it = cs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String loginName = entry.getKey().toString();
            MbrAccount account = betMapper.findMbrAccountOne(loginName, schemaPrex);
            if (Objects.nonNull(account)) {
                MbrValidbet mbrValidbet = new MbrValidbet();
                mbrValidbet.setAccountId(account.getId());
                mbrValidbet.setActivityId(as.getId());
                mbrValidbet.setValidBet(Objects.nonNull(entry.getValue())
                        ? new BigDecimal(entry.getValue().toString()) : BigDecimal.ZERO);
                mbrValidbet.setLoginName(loginName);
                mbrValidbet.setTime(getCurrentDate(FORMAT_18_DATE_TIME));

                waterBonusActivity(schemaPrex, account, as, mbrValidbet.getValidBet());
                mbrValidbet.setSchemaPrex(schemaPrex);
                betMapper.insertValidBet(mbrValidbet);
            }
        }


    }

    public void waterBonusActivity(String schemaPrex, MbrAccount account, OprActActivity actActivity, BigDecimal bigDecimal) {
        JWaterRebatesDto dto = new Gson().fromJson(actActivity.getRule(), JWaterRebatesDto.class);
        Boolean isWaterBonus = checkoutAccountMsg(schemaPrex, account, dto);
        if (Boolean.TRUE.equals(isWaterBonus)) {
            OprActBonus bonus = insertOprActBonus(schemaPrex, account, actActivity, dto, bigDecimal);
            if (Objects.nonNull(bonus) && Boolean.FALSE.equals(dto.getIsAudit())) {
                grantOprActBonus(schemaPrex, bonus, actActivity.getSiteCode());
            }
        }
    }

    public void grantOprActBonus(String schemaPrex, OprActBonus bonus, String siteCode) {
        MbrBillDetail mbrBillDetail = new MbrBillDetail();
        mbrBillDetail.setLoginName(bonus.getLoginName());
        mbrBillDetail.setAccountId(bonus.getAccountId());
        mbrBillDetail.setAmount(bonus.getBonusAmount());
        mbrBillDetail.setFinancialCode(ACTIVITY_WATERBONUS);
        mbrBillDetail.setOpType((byte) one);
        mbrBillDetail.setOrderNo(new SnowFlake().nextId());
        mbrBillDetail.setOrderTime(getCurrentDate(FORMAT_25_DATE_TIME));
        mbrBillDetail.setDepotId(zero);

        MbrWallet mbrWallet = new MbrWallet();
        mbrWallet.setBalance(mbrBillDetail.getAmount());
        mbrWallet.setAccountId(mbrBillDetail.getAccountId());
        mbrWallet.setSchemaPrex(schemaPrex);
        betMapper.updateMbrWalletAdd(mbrWallet);
        MbrWallet entity = new MbrWallet();
        entity.setAccountId(mbrWallet.getAccountId());
        entity.setSchemaPrex(schemaPrex);
        entity = betMapper.selectMbrWalletOne(entity);

        mbrBillDetail.setAfterBalance(entity.getBalance());
        mbrBillDetail.setBeforeBalance(adjustScale(entity.getBalance().subtract(mbrBillDetail.getAmount())));
        mbrBillDetail.setSchemaPrex(schemaPrex);
        betMapper.insertMbrBillDetail(mbrBillDetail);

        bonus.setStatus(one);
        bonus.setAuditUser(SYSTEM_USER);
        bonus.setAuditTime(getCurrentDate(FORMAT_18_DATE_TIME));
        bonus.setBillDetailId(mbrBillDetail.getId());
        bonus.setSchemaPrex(schemaPrex);
        betMapper.updateOprActBonus(bonus);

        validBetMsg(siteCode, bonus.getAccountId(), bonus.getBonusAmount());
    }

    private void validBetMsg(String siteCode, Integer accountId, BigDecimal acvitityMoney) {
        CompletableFuture.runAsync(() -> {
            balanceService.validBetMsg(SiteCodeAesUtil.encrypt(siteCode),
                    accountId, acvitityMoney);
        });
    }

    private OprActBonus insertOprActBonus(String schemaPrex, MbrAccount account, OprActActivity actActivity, JWaterRebatesDto dto, BigDecimal bigDecimal) {
        WaterRebatesRuleListDto rebatesRuleListDto = getWaterRebatesRuleListDto(account, dto, bigDecimal);
        if (Objects.nonNull(rebatesRuleListDto)) {
            OprActBonus waterBonus = new OprActBonus();
            waterBonus.setStatus(two);
            waterBonus.setActivityId(actActivity.getId());
            waterBonus.setAccountId(account.getId());
            waterBonus.setLoginName(account.getLoginName());
            waterBonus.setApplicationTime(getCurrentDate(FORMAT_18_DATE_TIME));
            waterBonus.setValidBet(bigDecimal);
            waterBonus.setDonateRatio(rebatesRuleListDto.getDonateRatio());
            BigDecimal bonusAmount = adjustScale(rebatesRuleListDto.getDonateRatio().divide(
                    new BigDecimal(ONE_HUNDRED)).multiply(bigDecimal));
            waterBonus.setBonusAmount(bonusAmount);
            waterBonus.setSchemaPrex(schemaPrex);
            waterBonus.setIsShow(one);
            waterBonus.setRuleId(actActivity.getRuleId());
            waterBonus.setOrderNo(new SnowFlake().nextId());
            waterBonus.setOrderPrefix(ACTIVITY_AC);
            betMapper.insertOprActBonus(waterBonus);
            return waterBonus;
        }
        return null;
    }

    private WaterRebatesRuleListDto getWaterRebatesRuleListDto(MbrAccount account, JWaterRebatesDto dto, BigDecimal amount) {
        List<WaterRebatesRuleListDto> ruleDtos = dto.getRuleListDtos();
        ruleDtos.sort((r1, r2) -> r2.getValidAmountMax().compareTo(r1.getValidAmountMax()));
        for (WaterRebatesRuleListDto rs : ruleDtos) {
            Boolean isActivity = compareAmount(amount, rs.getValidAmountMin(), rs.getValidAmountMax());
            if (Boolean.TRUE.equals(isActivity)) {
                return rs;
            }
        }
        log.info("有效投注额不符合参加活动，会员【" + account.getLoginName() + "】，有效投注额【" + amount + "】");
        return null;
    }

    private Boolean checkoutAccountMsg(String schemaPrex, MbrAccount account, JWaterRebatesDto dto) {
        ActivityScopeDto scopeDto = dto.getScopeDto();
        if (Boolean.FALSE.equals(scopeDto.getIsAccAll()) && !scopeDto.getAccIds().contains(account.getGroupId())) {
            log.info("所在会员组没有权限参加活动，会员【" + account.getLoginName() + "】");
            return Boolean.FALSE;
        }
        if (Boolean.FALSE.equals(scopeDto.getIsAgyTopAll()) && !scopeDto.getAgyTopIds().contains(account.getTagencyId())) {
            log.info("所属总代没有权限参加活动，会员【" + account.getLoginName() + "】");
            return Boolean.FALSE;
        }
        if (Boolean.FALSE.equals(scopeDto.getIsAgyAll()) && !scopeDto.getAgyIds().contains(account.getCagencyId())) {
            log.info("所属代理没有权限参加活动，会员【" + account.getLoginName() + "】");
            return Boolean.FALSE;
        }
        if (Boolean.TRUE.equals(dto.getIsName()) && StringUtil.isEmpty(account.getRealName())) {
            log.info("请先填写真实姓名，会员【" + account.getLoginName() + "】");
            return Boolean.FALSE;
        }
        if (Boolean.TRUE.equals(dto.getIsMobile()) && zero == account.getIsVerifyMoblie()) {
            log.info("请先验证手机号码，会员【" + account.getLoginName() + "】");
            return Boolean.FALSE;
        }
        if (Boolean.TRUE.equals(dto.getIsMail()) && zero == account.getIsVerifyEmail()) {
            log.info("请先验证邮箱，会员【" + account.getLoginName() + "】");
            return Boolean.FALSE;
        }
        if (Boolean.TRUE.equals(dto.getIsBank())) {
            MbrBankcard bankcard = new MbrBankcard();
            bankcard.setAccountId(account.getId());
            bankcard.setAvailable((byte) one);
            bankcard.setIsDel((byte) zero);
            bankcard.setSchemaPrex(schemaPrex);
            int count = betMapper.selectBankcardCount(bankcard);
            if (count == 0) {
                log.info("请先绑定银行卡，会员【" + account.getLoginName() + "】");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private BigDecimal adjustScale(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
    }

    private Boolean compareAmount(BigDecimal amount, BigDecimal amountMin, BigDecimal amountMax) {
        if (amount.compareTo(amountMax) == 0 || amount.compareTo(amountMax) == 1) {
            return Boolean.TRUE;
        }
        if (amount.compareTo(amountMin) == 0 || amount.compareTo(amountMin) == 1) {
            return Boolean.TRUE;
        }
        return null;
    }

    public List<AuditCat> setAuditCats(String rule) {
        if (StringUtils.isNotEmpty(rule)) {
            AuditRule dto = new Gson().fromJson(rule, AuditRule.class);
            List<AuditCat> auditCats = dto.getRuleDtos().stream().
                    filter(c -> Boolean.TRUE.equals(c.getIsAll())
                            || !Collections3.isEmpty(c.getDepots()))
                    .collect(Collectors.toList());
            if (Collections3.isNotEmpty(auditCats)) {
                auditCats.stream().forEach(as -> {
                    if (Objects.isNull(as.getDepots())) {
                        List<AuditDepot> depots = Lists.newArrayList();
                        AuditDepot auditDepot = new AuditDepot();
                        auditDepot.setDepotId(zero);
                        auditDepot.setGames(Lists.newArrayList(zero));
                        depots.add(auditDepot);
                        as.setDepots(depots);
                    }
                    as.getDepots().stream().forEach(ds -> {
                        if (Objects.isNull(ds.getDepotId())) {
                            ds.setDepotId(zero);
                        }
                        if (Objects.isNull(ds.getGames())) {
                            ds.setGames(Lists.newArrayList(zero));
                        }
                    });
                });
            }
            return auditCats;
        }
        return null;
    }

    public List<TGmGame> setGameList(List<AuditCat> list) {
        if (Collections3.isNotEmpty(list)) {
            List<TGmGame> gamelist = Lists.newArrayList();
            list.stream().forEach(ls -> {
                ls.getDepots().stream().forEach(ds -> {
                    ds.getGames().stream().forEach(gs -> {
                        TGmGame game = new TGmGame();
                        game.setDepotId(ds.getDepotId());
                        game.setCatId(ls.getCatId());
                        game.setSubCatId(gs);
                        gamelist.addAll(sysMapper.getGmGameList(game));
                    });
                });
            });
            return gamelist;
        }
        return null;
    }

    public List<Map> getValidVet(String siteCode, String startTime, String endTime, List<TGmGame> gamelist,
                                 List<String> userlist, String status) {
        /***获取该站点下所有api前缀***/
        List<String> predixs = sysMapper.getApiPrefixBySiteCode(siteCode);
        List<Map> rslist = new ArrayList<>();
        TermsAggregationBuilder agg = AggregationBuilders.terms("username").field("userName").
                subAggregation(AggregationBuilders.sum("validBet").field("validBet"));
        agg.size(ElasticSearchConstant.SEARCH_COUNT);
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (Collections3.isNotEmpty(userlist)) {
            query.must(QueryBuilders.termsQuery("userName", toLowerCase(userlist)));
        }
        if (StringUtil.isNotEmpty(status)) {
            /**查询输赢**/
            query.must(QueryBuilders.termsQuery("result", status));
        }
        if (Objects.nonNull(startTime)) {
            query.must(QueryBuilders.rangeQuery("orderDate").gte(startTime).lt(endTime));
        }
        query.must(QueryBuilders.termsQuery("sitePrefix", predixs));
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (Collections3.isNotEmpty(gamelist)) {
            gamelist.stream().forEach(gmGame -> {
                builder.should(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("platform",
                        gmGame.getDepotName().toLowerCase())).must(QueryBuilders.termsQuery("gameType", gmGame.getGameCode().toLowerCase())));
            });
        }
        query.must(builder);
        SearchRequestBuilder searchRequestBuilder = elasticSearchService.client.prepareSearch("report");
        searchRequestBuilder.setQuery(query);
        searchRequestBuilder.addAggregation(agg);
        String str = searchRequestBuilder.toString();
        try {
            Response response = elasticSearchService.restClient_Read.performRequest("GET",
                    ElasticSearchConstant.REPORT_INDEX + "/" + ElasticSearchConstant.REPORT_TYPE + "/_search",
                    Collections.singletonMap("_source", "true"),
                    new NStringEntity(str, ContentType.APPLICATION_JSON));
            Map map = (Map) JSON.parse(EntityUtils.toString(response.getEntity()));
            log.info("返水JOB查询语句：" + str);
            log.info("返水JOB查询结果：" + ((Map) map.get("aggregations")).get("userName"));
            if (((Map) map.get("aggregations")).get("username") != null) {
                for (Object obj : (JSONArray) ((Map) ((Map) map.get("aggregations")).get("username")).get("buckets")) {
                    Map rs = new HashMap();
                    Map objmap = (Map) obj;
                    rs.put(objmap.get("key"), ((BigDecimal) ((Map) objmap.get("validBet")).get("value"))
                            .setScale(2, BigDecimal.ROUND_DOWN));
                    rslist.add(rs);
                }
            }
        } catch (Exception e) {
            log.error("返水JOB异常", e);
        }
        return rslist;
    }

    private String getScope(String rule) {
        if (StringUtils.isNotEmpty(rule)) {
            AuditRule dto = new Gson().fromJson(rule, AuditRule.class);
            if (Objects.nonNull(dto.getScope())) {
                return dto.getScope() == 0 ? "输" : dto.getScope() == 1 ? "赢" : dto.getScope() == 2 ? null : null;
            }
        }
        return null;
    }

    public List toLowerCase(List list) {
        List newList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            newList.add(String.valueOf(it.next()).toLowerCase());
        }
        return newList;
    }
}