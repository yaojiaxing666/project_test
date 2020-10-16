package com.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DepartHandlerRes {

    @JsonProperty
    private String receiverNameList;
    @JsonProperty
    private String endTime;

    private String dispatchId;
    private String callId;
}
