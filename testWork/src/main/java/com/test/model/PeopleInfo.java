package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PeopleInfo implements Cloneable{

    private String sex;
    private int age;

    public String toString(){
        return sex+"\n"+age;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
