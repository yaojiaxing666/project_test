package com.test.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

//@Component
public class ActivemqConsumer {

    @Autowired
    private JavaMailSender javaMailSender;

    private String toEmail="2386023722@qq.com";

    @JmsListener(destination = "springboot-activemq-queue")
    public void receive(String msg) throws Exception {
        System.out.println("queue监听器收到msg:" + msg);
        /*JSONObject jsonObject = JSONObject.parseObject(msg);
        String username = jsonObject.getString("username");
        String email = jsonObject.getString("email");
        sendSimpleMail(email,username);*/

    }

    @JmsListener(destination = "${activemq-topic}")
    public void receiveTopic(String msg) {
        System.out.println("topic监听器收到msg:" + msg);
    }

    public void sendSimpleMail(String eamil, String userName) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(eamil);
        message.setTo(toEmail);
        message.setSubject("蚂蚁课堂|每特教育");
        message.setText(userName + ",这是一封测试邮件!");
        javaMailSender.send(message);
        System.out.println("邮件发送完成," + JSONObject.toJSONString(message));
    }


}
