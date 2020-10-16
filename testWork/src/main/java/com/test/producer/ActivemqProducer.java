package com.test.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;

@Component
public class ActivemqProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;
    @Autowired
    private Topic topic;

//    @Scheduled(fixedDelay = 5000)
    public void sendQueue() {
        jmsMessagingTemplate.convertAndSend(queue, "测试消息队列" + System.currentTimeMillis());
    }

    public void sendQueue(String msg) {
        jmsMessagingTemplate.convertAndSend(queue, msg);
    }

//    @Scheduled(fixedDelay = 5000)
    public void sendTopic() {
        jmsMessagingTemplate.convertAndSend(topic, "测试消息主题" + System.currentTimeMillis());
    }

}
