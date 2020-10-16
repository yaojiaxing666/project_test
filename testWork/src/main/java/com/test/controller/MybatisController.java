package com.test.controller;

import com.test.dao.test.TestMapper;
import com.test.model.PageBean;
import com.test.model.TempFilePo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "测试mybatis")
@RequestMapping("/mybatis")
public class MybatisController {

    @Autowired
    private TestMapper testMapper;

    @GetMapping("/selectById")
    @ApiOperation("测试查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",required = true),
    })
    public Object testSelectById(String id){
        TempFilePo tempFilePo = testMapper.selectById(id);
        return tempFilePo;
    }

    @GetMapping("/saveOne")
    @ApiOperation("测试存储")
    public String testSaveOne(){
        TempFilePo tempFilePo = TempFilePo.builder().
                id("111").fileName("存储").fileSize(123).fileType("jpg").downloadUrl("无").build();
        testMapper.saveOne(tempFilePo);
        return tempFilePo.toString();
    }

    @DeleteMapping("/deleteById")
    @ApiOperation("测试删除")
    @ApiImplicitParam(name = "id",value = "文件id",required = true)
    @Transactional("testTransactionManager")
    public String testDeleteById(String id){
        testMapper.deleteById(id);
        int i=1/0;
        return id;
    }

    @GetMapping("/selectAllToPage")
    @ApiOperation("测试分页查询所有")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页"),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数")
    })
    public Object testSelectAll(int currentPage,int pageSize){
        int offSet=(currentPage-1)*pageSize;
        int count = testMapper.selectCount(null);
        List<TempFilePo> tempFilePos = testMapper.selectAll(new RowBounds(offSet,pageSize));
        PageBean<TempFilePo> pageBean = new PageBean<>();
        pageBean.setData(tempFilePos);
        pageBean.setTotalCount(count);
        pageBean.setTotalPage(count%pageSize==0?count/pageSize:count/pageSize+1);
        pageBean.setCurrPage(currentPage);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

    @PostMapping("/selectCount")
    @ApiOperation("按照属性查询记录数")
    public Object testSelectCount(TempFilePo tempFilePo){
        int count = testMapper.selectCount(tempFilePo);
        return count;
    }

}
