package com.eveb.saasops.batch.sys.service;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ESService {

    public TransportClient client;

    public RestClient initRestClient(String url, int restport, String name, String password, int timeout) {
        // 配置信息
        Settings esSetting = Settings.builder()
                //设置ES实例的名称
                .put("cluster.name", "saasops-dbcenter")
                //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                .put("client.transport.sniff", true)
                .build();
        //初始化client较老版本发生了变化，此方法有几个重载方法，初始化插件等。
        client = new PreBuiltTransportClient(esSetting);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(name, password));
        RestClientBuilder builder = RestClient.builder(new HttpHost(url, restport, "http"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                })
                .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                        requestConfigBuilder.setConnectTimeout(timeout);
                        requestConfigBuilder.setSocketTimeout(timeout);
                        requestConfigBuilder.setConnectionRequestTimeout(timeout);
                        return requestConfigBuilder;
                    }
                })/***超时时间设为2分钟**/
                .setMaxRetryTimeoutMillis(timeout);
        return builder.build();
    }
}
