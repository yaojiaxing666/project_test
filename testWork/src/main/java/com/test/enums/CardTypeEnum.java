package com.test.enums;

public enum  CardTypeEnum {

    one(1,"身份证"),
    two(2,"军官证"),
    three(3,"其他证件");

    private int code;
    private String name;

    CardTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code){
        for (CardTypeEnum value : CardTypeEnum.values()) {
            if(code == value.code){
                return value.name;
            }
        }
        return null;
    }



    public static void main(String[] args) {
        int code = CardTypeEnum.valueOf("one").code;
        String name = CardTypeEnum.valueOf("two").name;
        String name1 = CardTypeEnum.getName(2);
    }
}
