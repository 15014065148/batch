package com.eveb.saasops.batch.sys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Configuration
//@EnableConfigurationProperties(JedisConnectionFactory.class)//开启属性注入,通过@autowired注入switch
@Component
public class RedisSwitch {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public StringRedisTemplate stringRedisTemplate_0()
    {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(jedisConnectionFactory.getHostName());
        jedis.setPort(jedisConnectionFactory.getPort());
        jedis.setDatabase(0);
        jedis.setPoolConfig(jedisConnectionFactory.getPoolConfig());
        jedis.afterPropertiesSet();
        StringRedisTemplate template=new StringRedisTemplate();
        template.setConnectionFactory(jedis);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate_3()
    {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(jedisConnectionFactory.getHostName());
        jedis.setPort(jedisConnectionFactory.getPort());
        jedis.setDatabase(3);
        jedis.setPoolConfig(jedisConnectionFactory.getPoolConfig());
        jedis.afterPropertiesSet();
        StringRedisTemplate template=new StringRedisTemplate();
        template.setConnectionFactory(jedis);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate_15()
    {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(jedisConnectionFactory.getHostName());
        jedis.setPort(jedisConnectionFactory.getPort());
        jedis.setDatabase(15);
        jedis.setPoolConfig(jedisConnectionFactory.getPoolConfig());
        jedis.afterPropertiesSet();
        StringRedisTemplate template=new StringRedisTemplate();
        template.setConnectionFactory(jedis);
        return template;
    }

}
