package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.model.People;
import com.test.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    private String shspaceTuijian_url="http://one.sipac.gov.cn/zskj/shspace/personaltuijian/tuijian";
    private String borrowingInfoUrl="https://pcc.sipac.gov.cn/gateway/api/borrow/borrowList";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisUtils redisUtils;

    @Value("${self.prop}")
    private String selfProp;

    @GetMapping("/test-suzhou")
    public Object test(String userId,@RequestParam(required = false)String type){
            Map<String, String> map = new HashMap<>(2);
            map.put("userId",userId);
            map.put("type",type);
            String result = restTemplate.getForObject(shspaceTuijian_url+"?userId={userId}&type={type}", String.class, map);
            return result;
    }

    @GetMapping("/test-dev")
    public Object testDev(String param){
        return param+" "+selfProp;
    }

    @GetMapping("/test-redis")
    public void testRedis(){
        boolean lock = redisUtils.lock("redislock", "redislock");
        boolean lock1 = redisUtils.lock("redislock", "redislock");
        boolean lock2 = redisUtils.lock("redislock", "redislock");
        boolean unlock = redisUtils.unlock("redislock", "redislock");
        boolean unlock2 = redisUtils.unlock("redislock", "redislock");
    }

    //借书状态集合
    private Map<String,String> stateMap=new HashMap<>(10);

    @GetMapping("/getBorrowingInfo")
    public Object getBorrowingInfo(HttpServletRequest request,String token){
        HttpSession session = request.getSession();
        session.setAttribute("getTicket-token", token);
        token = (String) request.getSession().getAttribute("getTicket-token");
        HashMap<String,String> param = new HashMap<>();
        param.put("token",token);
        String result = restTemplate.postForObject(borrowingInfoUrl, param, String.class);
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(!"0".equals(code) || !jsonObject.containsKey("obj")){
            return null;
        }
        JSONArray obj = jsonObject.getJSONArray("obj");
        initState();
        for (int i = 0; i < obj.size(); i++) {
            JSONObject object = obj.getJSONObject(i);
            String state = object.getString("state");
            String stateValue=stateMap.get(state);
            object.remove("state");
            object.put("state",stateValue);
        }
        return JSON.toJSONString(obj);
    }

    private void initState(){
        stateMap.put("1","借出状态");
        stateMap.put("2","预借状态，正在找书中");
        stateMap.put("3","图书过期状态");
        stateMap.put("5","图书登记找到状态");
        stateMap.put("6","图书己找到，正送往物流中心");
        stateMap.put("7","图书正在打包");
        stateMap.put("8","你选书买单的图书借出状态");
        stateMap.put("10","预约状态");
        stateMap.put("13","借书架待配书状态");
        stateMap.put("14","送书中及已签收状态");
    }

    @GetMapping("/test")
    public String test(String a){
        // 当调用的接口返回null时，people 转化后的值也是null
        People people = restTemplate.getForObject("http://127.0.0.1:8888/shtb/personInfo/test", People.class);
        return a;
    }

}
