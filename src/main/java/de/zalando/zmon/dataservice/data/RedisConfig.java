package de.zalando.zmon.dataservice.data;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.zalando.zmon.dataservice.config.DataServiceConfigProperties;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import sun.security.ssl.Debug;

@Configuration
public class RedisConfig {

    private final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    JedisPool getPool(DataServiceConfigProperties config) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setMaxTotal(config.getRedisPoolSize());

        return new JedisPool(poolConfig, config.getRedisHost(), config.getRedisPort());
    }

    @Bean
    JedisCluster getJCPool(DataServiceConfigProperties config){

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setMaxTotal(20);

        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();

        //Jedis cluster will attempt to discover cluster nodes automatically
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30001));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30002));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30003));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30004));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30005));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30006));
        JedisCluster jc = new JedisCluster(jedisClusterNodes, poolConfig);

        log.debug("Cluster Nodes " + jc.getClusterNodes().toString());
        return jc;
    }

}
