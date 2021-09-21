package com.example.sqstest.consumer.service;

import com.alibaba.fastjson.JSON;
import com.example.sqstest.dto.SimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Slf4j
@Service
public class SqsMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        SimpleMessage simpleMessage = null;
        try {
            simpleMessage = JSON.parseObject(textMessage.getText(), SimpleMessage.class);
        } catch (JMSException e) {
            log.error("Json parse exception: {}", e.getMessage());
        }
        log.info("Simple message was got: {}", simpleMessage.toString());
    }
}
