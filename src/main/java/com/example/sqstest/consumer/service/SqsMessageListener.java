package com.example.sqstest.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;

@Slf4j
@Service
public class SqsMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {

    }
}
