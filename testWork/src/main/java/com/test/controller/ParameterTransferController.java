package com.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "参数传递测试")
@RestController
@RequestMapping("/login/parameterTransfer")
public class ParameterTransferController {

    @ApiOperation("测试无参数")
    @PostMapping("/noParam")
    public String noParam(String result){
        System.out.println(result);
        return result;
    }

    @ApiOperation("测试post获取参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "map",value = "参数",required = true),
    })
    @PostMapping("/post")
    public String testPost(@RequestBody Map<String,String> map){
        String xix = map.get("xix");
        return map.get("kinfoGuid");
    }

    @ApiOperation("测试get请求参数不传值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "callid",value = "诉求件id",required = true),
            @ApiImplicitParam(name = "code",value = "验证码")
    })
    @GetMapping("/get")
    public String testGet(@RequestParam String callid, @RequestParam(required = false) String code){
        System.out.println("\u90e8\u95e8\u5904\u7406\u6d4b\u8bd5\u63a5\\u53e3");
        code="\u90e8\u95e8\u5904\u7406\u6d4b\u8bd5\u63a5\\u53e3";
        return callid+"\n"+code;
    }

}
