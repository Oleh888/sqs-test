package com.example.sqstest.producer.service;

import com.example.sqstest.dto.SimpleMessage;

public interface ProducerService {

    void sendMessage(SimpleMessage message);

}
