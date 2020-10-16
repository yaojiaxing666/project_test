package com.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.test.model.CallDetailRes;
import com.test.model.DepartHandlerRes;
import com.test.model.PageBean;
import com.test.model.ReplayDetailRes;
import com.test.producer.ActivemqProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(description = "部门处理测试接口")
@RestController
@RequestMapping("/departHandler")
public class TestDepartHandler {

    @Autowired
    private ActivemqProducer producer;

    @PostMapping("/dispatchList")
    public Object getDepartHandlerList(String callid){
        System.out.println(callid);
        PageBean<DepartHandlerRes> pageBean = new PageBean<>();
        DepartHandlerRes handlerRes = new DepartHandlerRes();
        handlerRes.setEndTime("2017-12-20 17:42");
        handlerRes.setCallId("1111111");
        handlerRes.setDispatchId("dis1111111");
        handlerRes.setReceiverNameList("鼓楼区五凤街道");
        ArrayList<DepartHandlerRes> list = new ArrayList<>();
        list.add(handlerRes);
        list.add(handlerRes);
        pageBean.setData(list);
        pageBean.setPageSize(2);
        pageBean.setMsg("部门分发列表");
        pageBean.setRetCode("1");
        pageBean.setTotalCount(2);
        pageBean.setCurrPage(0);
        return pageBean;
    }

    @PostMapping("/callDetail")
    public Object getCallDetail(String callid){
        System.out.println(callid);
        PageBean<CallDetailRes> pageBean = new PageBean<>();
        CallDetailRes callDetailRes = new CallDetailRes();
        callDetailRes.setHandlerStatus(0);
        callDetailRes.setIsDisplay(1);
        callDetailRes.setVaildateCode(1235);
        ArrayList<CallDetailRes> callDetailRes1 = new ArrayList<>();
        callDetailRes1.add(callDetailRes);
        pageBean.setData(callDetailRes1);
        pageBean.setMsg("部门分发列表");
        pageBean.setRetCode("1");
        return pageBean;
    }

    @PostMapping("/replayList")
    public Object getReplayList(String callid){
        System.out.println(callid);
        PageBean<ReplayDetailRes> pageBean = new PageBean<>();
        ReplayDetailRes replayDetailRes = new ReplayDetailRes();
        replayDetailRes.setReplyerName("张三");
        replayDetailRes.setReplyMsg("通过");
        replayDetailRes.setReplyTime("2017-12-20 17:42");
        ArrayList<ReplayDetailRes> list = new ArrayList<>();
        // list 添加 两个元素
        list.add(replayDetailRes);
        list.add(replayDetailRes);
        pageBean.setData(list);
        pageBean.setPageSize(2);
        pageBean.setMsg("回复详情列表");
        pageBean.setRetCode("1");
        pageBean.setTotalCount(2);
        pageBean.setCurrPage(0);
        return pageBean;
    }

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
