package com.test.mainTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoadPropertiesTest {

    public static void main(String[] args) throws IOException {
        //通过 PropertiesLoaderUtils 加载
        Properties properties = getProperty();

        //通过 ClassLoader 加载
        Properties property2 = getProperty2();
        Properties property3 = getProperty3();
        Properties property5 = getProperty5();

        String name = properties.getProperty("name");
        String age = properties.getProperty("age");
        String id = properties.getProperty("id");
        String sex = properties.getProperty("sex");
        String policy = properties.getProperty("policy");
        JSONArray objects = JSON.parseArray(policy);

        Map<String,String> map=new HashMap<>((Map) properties);
        String name1 = map.get("name");
    }

    public static Properties getProperty() throws IOException {
        // 不要再前面加 / ，源文件加后缀就写后缀，源文件没加就不要加后缀
        Properties properties = PropertiesLoaderUtils.loadAllProperties("properties/test.properties");
        return properties;
    }

    public static Properties getProperty2() throws IOException {
        String path = LoadPropertiesTest.class.getClassLoader().getResource("properties/test.properties").getPath();
        InputStream in = new FileInputStream(path);
        Properties properties = new Properties();
        properties.load(in);
        return properties;
    }

    public static Properties getProperty3() throws IOException {
        InputStream in = LoadPropertiesTest.class.getClassLoader().getResourceAsStream("properties/test.properties");
        Properties properties = new Properties();
        properties.load(in);
        return properties;
    }

    public static Properties getProperty5() throws IOException {
        InputStream in = LoadPropertiesTest.class.getResourceAsStream("/properties/test.properties");
        Properties properties = new Properties();
        properties.load(in);
        return properties;
    }
}
