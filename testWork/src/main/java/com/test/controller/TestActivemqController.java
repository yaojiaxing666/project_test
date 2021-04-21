package com.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.test.producer.ActivemqProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "Activemq测试接口")
@RestController
@RequestMapping("/activemqController")
public class TestActivemqController {

    @Autowired
    private ActivemqProducer producer;

    @ApiOperation("测试activemq")
    @ApiImplicitParam(name = "type",value = "消息类型",defaultValue = "queue")
    @RequestMapping(value = "/activemq",method = RequestMethod.GET)
    public String testActivemq(@RequestParam(defaultValue = "queue") String type){
        if ("queue".equals(type)) {
            producer.sendQueue();
        } else {
            producer.sendTopic();
        }
        return type;
    }

    @ApiOperation("测试activemq队列发送邮件")
    @ApiImplicitParam(name = "type",value = "消息类型",defaultValue = "queue")
    @RequestMapping(value = "/activemqEmail",method = RequestMethod.GET)
    public String testMqEmail(@RequestParam(defaultValue = "queue") String type){
        if ("queue".equals(type)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username","yaojiaxing");
            jsonObject.put("email","yaojiaxing163@163.com");
            producer.sendQueue(jsonObject.toString());
        } else {
            producer.sendTopic();
        }
        return type;
    }
}
