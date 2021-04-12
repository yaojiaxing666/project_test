package com.test.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String sex;
    private String age;
    private String idCard;
}
