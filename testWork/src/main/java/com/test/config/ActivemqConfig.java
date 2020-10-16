package com.test.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;
import javax.jms.Topic;

@Configuration
public class ActivemqConfig {

    @Value("${activemq-queue}")
    private String queue;

    @Value("${activemq-topic}")
    private String topic;

    @Bean
    public Queue logQueue() {
        return new ActiveMQQueue(queue);
    }

    @Bean
    public Topic logTopic() {
        return new ActiveMQTopic(topic);
    }

}
