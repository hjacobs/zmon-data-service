package de.zalando.zmon.dataservice;

import io.opentracing.contrib.spring.cloud.async.CustomAsyncConfigurerAutoConfiguration;
import io.opentracing.contrib.spring.cloud.async.DefaultAsyncAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by jmussler on 4/21/15.
 */
@SpringBootApplication//(exclude = { CustomAsyncConfigurerAutoConfiguration.class, DefaultAsyncAutoConfiguration.class })
@EnableScheduling
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
