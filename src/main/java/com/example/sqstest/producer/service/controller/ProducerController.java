package com.example.sqstest.producer.service.controller;

import com.example.sqstest.dto.SimpleMessage;
import com.example.sqstest.producer.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/api/producer")
@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @PostMapping("/message/create")
    public void createMessage(@RequestBody SimpleMessage simpleMessage) {
        simpleMessage.setMessageId(UUID.randomUUID().toString());
        producerService.sendMessage(simpleMessage);
    }
}
