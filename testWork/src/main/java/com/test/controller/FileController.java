package com.test.controller;

import com.test.dao.test.TestMapper;
import com.test.model.TempFilePo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RequestMapping("/file")
@RestController
@Api(description = "文件上传下载")
public class FileController {
    @Value("${file.tempPath}")
    private String tempPath;

    @Autowired
    private TestMapper testMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("文件上传 测试post以formData形式传递参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s",value = "单个参数",required = false),
//            @ApiImplicitParam(name = "file1",value = "文件1",dataType = "File",required = true),
//            @ApiImplicitParam(name = "file2",value = "文件2",dataType = "File",required = false),
    })
    @PostMapping("/uploadFile")
    public String uploadFile(String s,
                               MultipartFile file1, MultipartFile file2) throws IOException {

        String downLoadUrl = saveFile(file1);

        return downLoadUrl;
    }

    private String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        // 最后一个点的位置
        int index = originalFilename.lastIndexOf(".");
        // 文件后缀名
        String suffix=originalFilename.substring(index+1);
        String uuid = UUID.randomUUID().toString();
        //存储的路径
        String filePath= tempPath + File.separator+ uuid + "." + suffix;
        File tempFile = new File(filePath);
        file.transferTo(tempFile);
        TempFilePo tempFilePo = TempFilePo.builder().fileName(originalFilename)
                .downloadUrl("http://127.0.0.1:8088/test/file/download/"+uuid)
                .fileSize(file.getSize()).fileType(suffix).id(uuid).build();
        //存入redis
//        redisTemplate.opsForValue().set(uuid,tempFile);
        //存入mysql
        testMapper.saveOne(tempFilePo);
        return tempFilePo.getDownloadUrl();
    }

    @GetMapping("/download/{id}")
    @ApiOperation("文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文件id",required = true),
    })
    public void downloadFile(@PathVariable String id, HttpServletResponse response) throws IOException {
        TempFilePo tempFilePo = testMapper.selectById(id);
        ServletOutputStream outputStream = response.getOutputStream();
        //存储的文件
        File storeFile = new File(tempPath + File.separator + id + "." + tempFilePo.getFileType());
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(tempFilePo.getFileName()));
        response.addHeader("Content-Length", "" + tempFilePo.getFileSize());
        response.setContentType("application/octet-stream");

        outputStream.write(FileCopyUtils.copyToByteArray(storeFile));
    }
}
