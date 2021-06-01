package com.test.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

/*
* 脱敏工具类
* */
public class DesensitizeUtil {

    /*
    * 静态变量使用@Value，需要在set方法上使用，否则值为null
    * */
    private static String salt;

    @Value("${desensitize.salt}")
    public void setSalt(String salt) {
        this.salt = salt;
    }

    //姓名：长度不超过姓名的50%，显示姓名最后的1-2个字。例如：**热巴（迪丽热巴）、**冰（范冰冰）、*彦（田彦）
    public static String processName(String name){
        String result="";
        if (name.length() == 2) {
            result = "*" + name.charAt(1);
        } else if (name.length() == 3) {
            result = "**" + name.charAt(2);
        } else {
            int length = name.length();
            StringBuilder hide = new StringBuilder();
            for (int i = 0; i < length - 2; i++) {
                hide.append("*");
            }
            result = hide.toString() + name.substring(length - 2);
        }
        return result;
    }

    //保留前几位
    public static String retainLeft(String str,int index) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String retainStr = StringUtils.left(str, index);
        return StringUtils.rightPad(retainStr, StringUtils.length(str), "*");
    }

    //保留后几位
    public static String retainRight(String str,int index) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String retainStr = StringUtils.right(str, index);
        return StringUtils.leftPad(retainStr, StringUtils.length(str), "*");
    }

    //手机号码：前3后4
    public static String processPhoneNum(String phoneNum) {
        if (StringUtils.isEmpty(phoneNum) || (phoneNum.length() != 11)) {
            return phoneNum;
        }
        return phoneNum.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    //企业名称：
    // ①长度大于12位，只显示前6位
    // ②长度小于12位，显示不超过50%
    public static String processEntName(String entName) {
        if (StringUtils.isBlank(entName)) {
            return entName;
        }
        int length = entName.length();
        if(length>=12){
            return retainLeft(entName,6);
        }else {
            int retainLength=length/2;
            return retainLeft(entName,retainLength);
        }
    }




    public static void main(String[] args) {
        String s = retainLeft("123456789012345678",4);
        String s2 = retainRight("123456789012345678",4);
        String s1 = processPhoneNum("18326122534");
        String s3 = processEntName("上海市通办信息科技有限公司");
        String s5 = processEntName("通办信息科技有限公司");
        String s6 = processEntName("通办信息的公司");
    }
}
