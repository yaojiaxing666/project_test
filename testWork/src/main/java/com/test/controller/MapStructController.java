package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.mapper.CarPoConvertVo;
import com.test.model.CarPo;
import com.test.model.CarVo;
import io.swagger.annotations.Api;
import lombok.extern.java.Log;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(description = "测试 MapStruct")
@RequestMapping("/mapStruct")
@Log
public class MapStructController {

    @Autowired
    private CarPoConvertVo carPoConvertVo;

    @GetMapping("/convert")
    public String poConvertVo(){
        CarPo carPo = CarPo.builder().branch("马自达").id(1).name("DB707").build();
        CarVo carVo = carPoConvertVo.poConvertVo(carPo);
        return carVo.toString();
    }

    @GetMapping("/listConvert")
    public Object poConvertVoList(){
        ArrayList<CarPo> carPos = new ArrayList<>(3);
        CarPo carPo = CarPo.builder().branch("马自达").id(1).name("DB707").build();
        CarPo carPo2 = CarPo.builder().branch("斯柯达").id(2).name("DB708").build();
        CarPo carPo3 = CarPo.builder().branch("马达啦").id(3).name("DB709").build();
        log.info("车子的参数是："+carPo.toString());
        carPos.add(carPo);
        carPos.add(carPo2);
        carPos.add(carPo3);
        List<CarVo> carVos = carPoConvertVo.poConvertVoList(carPos);
        String s = JSON.toJSONString(carVos);
        JSONObject jsonObject = JSON.parseArray(s).getJSONObject(0);
        HashMap<String, Object> map = new HashMap<>();
        map.put("json",jsonObject);
        return map;
    }

    public static void main(String[] args) {
        CarPo carPo = CarPo.builder().branch("马自达").id(1).name("DB707").build();
        CarVo carVo = new CarVo();
//        BeanUtils.copyProperties(carPo,carVo);

        CarPoConvertVo mapper = Mappers.getMapper(CarPoConvertVo.class);
        CarVo carVo1 = mapper.poConvertVo(carPo);
        System.out.println(carVo1);
    }
}
