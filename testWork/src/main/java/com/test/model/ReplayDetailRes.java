package com.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReplayDetailRes {

    @JsonProperty
    private String replyerName;
    @JsonProperty
    private String replyMsg;
    @JsonProperty
    private String replyTime;
}
