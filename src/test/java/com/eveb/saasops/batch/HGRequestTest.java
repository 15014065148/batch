package com.eveb.saasops.batch;


import com.eveb.saasops.batch.game.hg.domain.HGBetLog;
import com.eveb.saasops.batch.game.hg.domain.HGRequestParameter;
import com.eveb.saasops.batch.game.hg.request.HGRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HGRequestTest {
    @Autowired
    private HGRequest hGRequest;

    @Test
    public void test() throws Exception{
        /*HGRequestParameter parameter = new HGRequestParameter();
        parameter.setUsername("vbete");
        parameter.setPassword("vbete@liv@");
        parameter.setCasinoId("besxcb12mn8ghhol");
        parameter.setUserId("");
        parameter.setDateval("2018/07/07");
        parameter.setTimeRange("");
        parameter.setStatus("");
        List<HGBetLog> list =  hGRequest.getBetList(parameter);
        System.out.print(list.size());*/
    }
}
