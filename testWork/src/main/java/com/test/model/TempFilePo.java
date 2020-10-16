package com.test.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempFilePo {
    private String id;
    private String fileType;
    private long fileSize;
    private String fileName;
    private String downloadUrl;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    //@DateTimeFormat 注解会将时间类型的 数据格式化成 String 类型
    private String time;
}
