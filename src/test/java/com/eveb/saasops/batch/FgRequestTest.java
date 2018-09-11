package com.eveb.saasops.batch;


import com.alibaba.fastjson.JSON;
import com.eveb.saasops.batch.game.fg.domain.FgBetLog;
import com.eveb.saasops.batch.sys.util.OkHttpProxyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FgRequestTest {
    @Autowired
    private OkHttpProxyUtils okHttpProxyUtils;

    /**
     * 检测用户是否存在
     *
     * @throws Exception
     */
    @Test
    public void testCheckUser() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        /*Map<String,String> mapParam =new HashMap<>();
        mapParam.put("merchantcode","196f947f9c00ee8e20af23cc0d5b5926");*/

        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/player_names/jeff123");
        System.out.println(result);
    }

    /**
     * 创建用户
     *
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("member_code", "ybhjeff123");
        mapParam.put("password", "123456");

        String result = okHttpProxyUtils.postForm(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/players", mapParam);

        System.out.println(result);
    }

    /**
     * 踢玩家下线
     *
     * @throws Exception
     */
    @Test
    public void testDelate() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("openid", "332aG5BKV3dKJwKM_2FcJmzwqy0smC_2BsbQEuOJNXqG0PYqX5aq");
        //mapHead.put("merchantcode","196f947f9c00ee8e20af23cc0d5b5926");

        String result = okHttpProxyUtils.delete(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/player_sessions/332aG5BKV3dKJwKM_2FcJmzwqy0smC_2BsbQEuOJNXqG0PYqX5aq", "");

        System.out.println(result);
    }


    /**
     * 存取玩家筹码
     *
     * @throws Exception
     */
    @Test
    public void testRecharge() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        Map<String, String> mapParam = new HashMap<>();
        //mapParam.put("openid","332aG5BKV3dKJwKM_2FcJmzwqy0smC_2BsbQEuOJNXqG0PYqX5aq");
        //mapHead.put("merchantcode","196f947f9c00ee8e20af23cc0d5b5926");
        mapParam.put("amount", "100000");
        mapParam.put("externaltransactionid", "9");

        String result = okHttpProxyUtils.putForm(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/player_uchips/332aG5BKV3dKJwKM_2FZcwwgO40sqG_2FsHUQrKOMymG0PYqXZmr", mapParam);
        System.out.println(result);
    }

    /**
     * 查询玩家筹码
     *
     * @throws Exception
     */
    @Test
    public void testQueryChip() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");


        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/player_chips/332aG5BKV3dKJwKM_2FcJmzwqy0smC_2BsbQEuOJNXqG0PYqX5aq");
        System.out.println(result);
    }

    /**
     * 验证存取玩家筹码状态
     *
     * @throws Exception
     */
    @Test
    public void testCheckChip() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");


        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/player_uchips_check/8");
        System.out.println(result);
    }


    /**
     * 游戏大厅
     *
     * @throws Exception
     */
    @Test
    public void testLoginHall() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/app/get_token_qr/332aG5BKV3dKJwKM_2FcJmzwqy0smC_2BsbQEuOJNXqG0PYqX5aq");

        System.out.println(result);
    }

    /**
     * 获取游戏列表
     *
     * @throws Exception
     */
    @Test
    public void testGameList() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/games/game_type/h5/language/zh-cn");
        System.out.println(result);
    }

    /**
     * 开始游戏
     *
     * @throws Exception
     */
    @Test
    public void testGameStart() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("openid", "332aG5BKV3dKJwKM_2FZcwwgO40sqG_2FsHUQrKOMymG0PYqXZmr");
        //mapParam.put("member_code","196f947f9c00ee8e20af23cc0d5b5926");
        mapParam.put("game_code", "fish_3D");
        mapParam.put("game_type", "h5");
        mapParam.put("language", "zh-cn");
        mapParam.put("ip", "202.61.86.189");
        //mapParam.put("ip","192.138.5.208");

        mapParam.put("return_url", "http://baidu.com");
        //mapParam.put("owner_id","");
        String result = okHttpProxyUtils.postForm(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/launch_game", mapParam);
        System.out.println(result);
    }

    /**
     * 试玩游戏
     *
     * @throws Exception
     */
    @Test
    public void testFreeGameStart() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("member_code", "196f947f9c00ee8e20af23cc0d5b5926");
        mapParam.put("game_code", "fish_3D");
        mapParam.put("game_type", "h5");
        mapParam.put("language", "zh-cn");
        mapParam.put("ip", "202.61.86.189");
        //mapParam.put("ip","192.138.5.208");
        mapParam.put("return_url", "http://baidu.com");

        String result = okHttpProxyUtils.postForm(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/launch_free_game", mapParam);
        System.out.println(result);
    }


    /**
     * 投注日志
     *
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception {
        List<FgBetLog> resultList = new ArrayList<FgBetLog>();
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/agent/log_by_page/gt/fish");
        Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(result);
        String page_key = jsonMap.get("page_key").toString();
        resultList = JSON.parseArray(jsonMap.get("data").toString(), FgBetLog.class);

        while (page_key != null && !page_key.equals("none")){ //page_key 为none 无下一页
            //String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead),"https://api.ppro.98078.net/v2/agent/log_by_page/gt/fish/"+id);
            String resultPage = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/agent/log_by_page/gt/fish/page_key/" + page_key);
            Map<String, Object> jsonMapPage = (Map<String, Object>) JSON.parse(resultPage);
            page_key = jsonMapPage.get("page_key").toString();
            resultList.addAll(JSON.parseArray(jsonMapPage.get("data").toString(), FgBetLog.class));
        }

        System.out.println(resultList);
    }


    /**
     * 根据时间获取游戏总的记录数
     *
     * @throws Exception
     */
    @Test
    public void testAgentCount() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/agent/log_by_count/gt/fish/start_time/1505207707/end_time/1505207776/");
        System.out.println(result);
    }

    /**
     * 捕猎排行派彩
     *
     * @throws Exception
     */
    @Test
    public void testPlayerRank() throws Exception {
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("Accept", "application/json");
        mapHead.put("merchantname", "uat_wyb1111");
        mapHead.put("merchantcode", "196f947f9c00ee8e20af23cc0d5b5926");

        String result = okHttpProxyUtils.get(okHttpProxyUtils.initHeadClient(mapHead), "https://api.ppro.98078.net/v2/fish/player_rank/game_id/5006/");
        System.out.println(result);
    }

}
