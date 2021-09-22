package com.example.sqstest.config;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.example.sqstest.consumer.service.SecondSqsMessageListener;
import com.example.sqstest.consumer.service.SqsMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Configuration
public class JmsConfig {

    @Value("${aws.sqs.name}")
    private String queueName;

    @Value("${aws.sqs.name2}")
    private String queueName2;

    @Value("${aws.sqs.endpoint2}")
    private String endpoint2;

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

    @Bean
    public SQSConnectionFactory secondSqsConnectionFactory() {
        return SQSConnectionFactory.builder()
                .withAWSCredentialsProvider(new DefaultAWSCredentialsProviderChain())
                .withEndpoint(endpoint2)
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

    @Bean
    @Autowired
    public DefaultMessageListenerContainer jmsListenerContainer(SQSConnectionFactory sqsConnectionFactory,
                                                                SqsMessageListener sqsMessageListener) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setReceiveTimeout(5);
        container.setDestinationName(queueName);
        container.setMessageListener(sqsMessageListener);
        container.setConnectionFactory(sqsConnectionFactory);
        return container;
    }

    @Bean
    @Autowired
    public  DefaultMessageListenerContainer secondJmsListenerContainer(SQSConnectionFactory sqsConnectionFactory,
                                                                          SecondSqsMessageListener secondSqsMessageListener) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setReceiveTimeout(5);
        container.setDestinationName(queueName2);
        container.setMessageListener(secondSqsMessageListener);
        container.setConnectionFactory(sqsConnectionFactory);
        return container;
    }

}
