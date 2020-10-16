package com.test.model;

import lombok.Data;

@Data
public class People implements Cloneable{
    private String name;
    private PeopleInfo peopleInfo;

    public String toString(){
        return name+"\n"+peopleInfo.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        People clone = (People) super.clone();
        clone.setPeopleInfo((PeopleInfo) this.peopleInfo.clone());
        return clone;
    }
}
