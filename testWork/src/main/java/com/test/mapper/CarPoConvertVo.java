package com.test.mapper;

import com.test.model.CarPo;
import com.test.model.CarVo;
import com.test.model.PeopleInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel="spring")
public interface CarPoConvertVo {

    @Mappings({
            @Mapping(source = "name",target = "carName")
    })
    CarVo poConvertVo(CarPo source);

    List<CarVo> poConvertVoList(List<CarPo> source);

    @Mappings({
            @Mapping(source = "name",target = "sex")
    })
    PeopleInfo peopleInfoConvertCarVo(CarPo source);

    List<PeopleInfo> peopleConvertCarList(List<CarPo> source);


}
