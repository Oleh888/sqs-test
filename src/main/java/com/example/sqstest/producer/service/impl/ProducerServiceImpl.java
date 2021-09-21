package com.example.sqstest.producer.service.impl;

import com.alibaba.fastjson.JSON;
import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import com.example.sqstest.dto.SimpleMessage;
import com.example.sqstest.producer.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@Service
public class ProducerServiceImpl implements ProducerService {

    @Value("${aws.sqs.name}")
    private String queueName;

    private final JmsTemplate jmsTemplate;

    public ProducerServiceImpl(@Qualifier("customJmsTemplate") JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendMessage(SimpleMessage message) {
        try {
            Message jmsMessage = new SQSTextMessage(JSON.toJSONString(message));
            jmsTemplate.convertAndSend(queueName, jmsMessage);
            log.info("Simple message {} was sent to queue", message.getMessageId());
        } catch (JMSException e) {
            log.error("JMSException: {}", e.getMessage());
        }
    }

}
