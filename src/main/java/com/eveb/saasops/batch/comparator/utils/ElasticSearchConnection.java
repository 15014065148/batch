package com.eveb.saasops.batch.comparator.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ElasticSearchConnection2")
public class ElasticSearchConnection {


    public static TransportClient client;

    public static RestClient restClient;

    public ElasticSearchConnection() {

    }


    private static  void init() throws Exception {
        // 配置信息
        Settings esSetting = Settings.builder()
                .put("cluster.name", "saasops-dbcenter")//设置ES实例的名称
                .put("client.transport.sniff", true)//自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
//                .put("network.host", "localhost")
                .build();
        client = new PreBuiltTransportClient(esSetting);//初始化client较老版本发生了变化，此方法有几个重载方法，初始化插件等。
        //此步骤添加IP，至少一个，其实一个就够了，因为添加了自动嗅探配置
//        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(url), clientHost));
//        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("161.202.230.40"), 9302));
//        restClient = RestClient.builder(new HttpHost("161.202.230.40", 9000, "http")).build();
//        restClient = RestClient.builder(new HttpHost("10.132.199.110", 9231, "http")).build();
        restClient = RestClient.builder(new HttpHost("admin:1ZgA8g9PhrYLg5qG@161.202.230.40", 9000, "http")).build();
    }
}
