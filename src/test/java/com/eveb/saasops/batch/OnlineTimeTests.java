package com.eveb.saasops.batch;

import com.eveb.saasops.batch.bet.dto.OnlineTimeDto;
import com.eveb.saasops.batch.bet.entity.MbrAccount;
import com.eveb.saasops.batch.bet.service.OnlineTimeService;
import com.eveb.saasops.batch.bet.service.UpdateDepotBalance;
import com.eveb.saasops.batch.bet.service.ValidBetService;
import com.eveb.saasops.batch.common.SchemaCode;
import com.eveb.saasops.batch.comparator.service.ComparatorService;
import com.eveb.saasops.batch.sys.util.DateUtil;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.eveb.saasops.batch.sys.util.DateUtil.FORMAT_18_DATE_TIME;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OnlineTimeTests {

    @Autowired
    private ComparatorService comparatorService;

    @Autowired
    private OnlineTimeService onlineTimeService;

    @Autowired
    private ValidBetService validBetService;

    @Autowired
    private UpdateDepotBalance depotBalance;

    @Test
    public void setDepotBalance() {
        depotBalance.setDepotBalance("df");
    }

    @Test
    public void castMbrValidbet() {
        validBetService.castMbrValidbet("test");
    }

    @Test
    public void test() {
      /*  List<Date> dates = onlineTimeService.getBettimeList("df", "2018-02-08T00:03:50.651Z",
                "2018-02-08T23:03:50.651Z", "18075181234j");*/
      /*  List<String> strings = Lists.newArrayList();
        strings.add("2018-02-10 00:00:00");
        strings.add("2018-02-10 00:20:00");
        strings.add("2018-02-10 00:35:00");
        strings.add("2018-02-10 00:51:00");
        List<Date> dates = strings.stream().map(
                st -> {
                    return DateUtil.parse(st,FORMAT_18_DATE_TIME);
                }).collect(Collectors.toList());*/
        MbrAccount account = new MbrAccount();
        account.setLoginName("test");
        account.setId(1);
        onlineTimeService.accountOnlineTime();
    }

    @Test
    public void test2() throws IOException {
        //comparatorService.getReportOrderByUserName("2018-02-14","2018-02-15","PT","caf");
        comparatorService.getReportOrderByKioks("2018-02-14","2018-02-15","PT");
    }

    @Test
    public void test3() throws IOException {
        comparatorService.getPTDataByTime("2017-08-14");
    }
}
