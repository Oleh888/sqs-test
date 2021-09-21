package com.example.sqstest.producer.config;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Configuration
public class JmsConfig {

    @Value("${aws.sqs.name}")
    private String queueName;

    @Value("${aws.sqs.endpoint}")
    private String endpoint;

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConnectionFactory());
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("3-10");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }

    @Bean
    public SQSConnectionFactory sqsConnectionFactory() {
        return SQSConnectionFactory.builder()
                .withAWSCredentialsProvider(new DefaultAWSCredentialsProviderChain())
                .withEndpoint(endpoint)
                .withAWSCredentialsProvider(new AWSStaticCredentialsProvider(baseCredentials()))
                .withNumberOfMessagesToPrefetch(10).build();
    }

    private AWSCredentials baseCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean("customJmsTemplate")
    public JmsTemplate createJMSTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(sqsConnectionFactory());
        jmsTemplate.setDefaultDestinationName(queueName);
        jmsTemplate.setDeliveryPersistent(false);
        jmsTemplate.setReceiveTimeout(1);
        return jmsTemplate;
    }

}
