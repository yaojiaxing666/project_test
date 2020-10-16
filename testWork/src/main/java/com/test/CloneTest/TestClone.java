package com.test.CloneTest;

import com.alibaba.fastjson.JSONObject;
import com.test.model.People;
import com.test.model.PeopleInfo;

public class TestClone {

    public static void main(String[] args) throws CloneNotSupportedException {
        testClone();

    }

    public static void testClone() throws CloneNotSupportedException {
        PeopleInfo info1 =new PeopleInfo();
        info1.setAge(20);
        info1.setSex("男");
        People p1 = new People();
        p1.setName("haha");
        p1.setPeopleInfo(info1);
        System.out.println(p1.toString());
        System.out.println();

        // 使用fastjson 完成深克隆，（转化为字节组，在解析成对象）
//        People p2 = JSONObject.parseObject(JSONObject.toJSONBytes(p1), People.class);

        // 使用clone(),实体类实现了cloneable接口
        People p2 =(People) p1.clone();

        System.out.println(p2.toString());
        System.out.println();

        System.out.println("info1=info2吗？"+(p1.getPeopleInfo()==p2.getPeopleInfo()));

        p2.setName("xixixi");
        p2.getPeopleInfo().setSex("女");
        System.out.println(p2.toString());
        System.out.println();
        System.out.println(p1.toString());
        System.out.println();
    }
}
