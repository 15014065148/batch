package com.eveb.saasops.batch.sys.service;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

@Slf4j
@Service
public class ElasticSearchService extends ESService {

    @Value("${elasticsearch.url}")
    private String url;
    @Value("${elasticsearch.port}")
    private int clientport;
    @Value("${elasticsearch.rest.port}")
    private int restport;
    @Value("${elasticsearch.name}")
    private String name;
    @Value("${elasticsearch.password}")
    private String password;
    @Value("${elasticsearch.timeout}")
    private int timeout;

    public RestClient restClient;

    public ElasticSearchService() {

    }

    @PostConstruct
    private void init() {
        restClient = initRestClient(url, restport, name, password, timeout);
    }
}
