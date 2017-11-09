package de.zalando.zmon.dataservice.config;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class SQSConfig {

    @Value("${queue.endpoint}")
    private String endpoint;
    @Value("${queue.name}")
    private String queueName;
    @Bean
    public AmazonSQSClient createSQSClient() {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient(new BasicAWSCredentials("AKIAIKDLIHESA76QFPHA","ZGjxZHDRMP+5pPLFT638kRApw+Ubcde4mgRGTfWH"));
        amazonSQSClient.setEndpoint(endpoint);
        amazonSQSClient.createQueue(queueName);
        return amazonSQSClient;
    }

}
