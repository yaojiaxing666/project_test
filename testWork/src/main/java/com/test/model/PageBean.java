package com.test.model;

import lombok.Data;

import java.util.List;

@Data
public class PageBean<T> {
    private List<T> data;

    private int currPage;

    private int pageSize;

    private int totalCount;

    private int totalPage;

    private String retCode;

    private String msg;

}
