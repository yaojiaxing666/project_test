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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @GetMapping("/download/zip")
    @ApiOperation("多文件转换成zip下载")
    public void downloadFileZip(HttpServletResponse response) throws IOException {

        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("附件.zip","utf-8"));
//        response.setContentType("application/octet-stream");
        response.setContentType("multipart/form-data");

        String path="C:\\Users\\yaojiaxing\\Desktop\\苏州\\消息中心\\";
        File file1 = new File(path + "北京消息中心.doc");
        File file2 = new File(path + "获取通知消息--中台.docx");
        File file3 = new File(path + "苏州消息中心 .doc");
        long length2 = file1.length();
        ArrayList<File> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);
        files.add(file3);
        File temp = new File(path + "temp.zip");
        FileOutputStream os = new FileOutputStream(temp);
//        ServletOutputStream os = response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os);
        for (File file : files) {
            String name = file.getName();
            ZipEntry zipEntry = new ZipEntry(name);
            zos.putNextEntry(zipEntry);
            zos.write(FileCopyUtils.copyToByteArray(file));

            zos.closeEntry();
        }
        zos.close();

    }





    /**
     * -普通java文件下载方法，适用于所有框架
     * -注意：
     *     1.  response.setContentType设置下载内容类型，常用下载类型：
     *         application/octet-stream（二进制流，未知文件类型）；
     *         application/vnd.ms-excel（excel）；
     *         text/plain（纯文本）； text/xml（xml）；text/html（html）；image/gif（GIF）；image/jpeg（JPG）等
     *         如果不写，则匹配所有；
     *     2.  response.setHeader("Content-Disposition","attachment; filename="+fileName +".zip"); 设置下载文件名；
     *         文件名可能会出现乱码，解决名称乱码：fileName  = new String(fileName.getBytes(), "iso8859-1");
     */
    @GetMapping("/download/zip1")
    public String downloadFilesTest(HttpServletRequest request, HttpServletResponse res) throws IOException {
        try{
            //获取文件根目录，不同框架获取的方式不一样，可自由切换
            String basePath = "C:\\Users\\yaojiaxing\\Desktop\\苏州\\消息中心";

            //获取文件名称（包括文件格式）
            String fileName = "北京消息中心.doc";

            //组合成完整的文件路径
            String targetPath = basePath+File.separator+fileName;

            //模拟多一个文件，用于测试多文件批量下载
            String targetPath1 = basePath+File.separator+"获取通知消息--中台.docx";
            //模拟文件路径下再添加个文件夹，验证穷举
            String targetPath2 = basePath+File.separator+"苏州消息中心 .doc";

            System.out.println("文件名："+fileName);
            System.out.println("文件路径："+targetPath);

            //方法1：IO流实现下载的功能
            res.setCharacterEncoding("UTF-8"); //设置编码字符
            res.setContentType("application/octet-stream;charset=UTF-8"); //设置下载内容类型
            res.setHeader("Content-disposition", "attachment;filename="+fileName);//设置下载的文件名称
            OutputStream out = res.getOutputStream();   //创建页面返回方式为输出流，会自动弹出下载框


	/*	  //方法1-1：IO字节流下载，用于小文件
		    System.out.println("字节流下载");
		    InputStream is = new FileInputStream(targetPath);  //创建文件输入流
		    byte[] Buffer = new byte[2048];  //设置每次读取数据大小，即缓存大小
		    int size = 0;  //用于计算缓存数据是否已经读取完毕，如果数据已经读取完了，则会返回-1
		    while((size=is.read(Buffer)) != -1){  //循环读取数据，如果数据读取完毕则返回-1
		        out.write(Buffer, 0, size); //将每次读取到的数据写入客户端
		    }
		    is.close();
		    */


	/*	  //方法1-2：IO字符流下载，用于大文件
		    System.out.println("字符流");
		    File file = new File(targetPath);  //创建文件
		    FileInputStream fis=new FileInputStream(file);  //创建文件字节输入流
		    BufferedInputStream bis=new BufferedInputStream(fis); //创建文件缓冲输入流
		    byte[] buffer = new byte[bis.available()];//从输入流中读取不受阻塞
			bis.read(buffer);//读取数据文件
			bis.close();
			out.write(buffer);//输出数据文件
			out.flush();//释放缓存
			out.close();//关闭输出流
	*/

		  //方法1-3：将附件中多个文件进行压缩，批量打包下载文件
		    //创建压缩文件需要的空的zip包
		    String zipBasePath="C:\\Users\\yaojiaxing\\Desktop\\苏州\\消息中心";
		    String zipName = "temp.zip";
		    String zipFilePath = zipBasePath+File.separator+zipName;

		    //创建需要下载的文件路径的集合
		    List<String> filePaths = new ArrayList<String>();
		    filePaths.add(targetPath);
		    filePaths.add(targetPath1);
		    filePaths.add(targetPath2);

		    //压缩文件
		    File zip = new File(zipFilePath);
		    if (!zip.exists()){
		        zip.createNewFile();
		    }
		    //创建zip文件输出流
		    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
		    this.zipFile(zipBasePath,zipName, zipFilePath,filePaths,zos);
		    zos.close();
		    res.setHeader("Content-disposition", "attachment;filename="+zipName);//设置下载的压缩文件名称

		    //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
		    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
		    byte[] buff = new byte[bis.available()];
		    bis.read(buff);
		    bis.close();
			out.write(buff);//输出数据文件
			out.flush();//释放缓存
			out.close();//关闭输出流

        }catch(Exception e) {
            e.printStackTrace();
            res.reset();
            res.setCharacterEncoding("UTF-8");
            res.setContentType("text/html;charset=UTF-8");
            res.getWriter().print("<div align=\"center\" style=\"font-size: 30px;font-family: serif;color: red;\">系统内部错误，下载未成功，请联系管理员！</div>"
                    + "<div>错误信息："+e.getMessage()+"</div>");
            res.getWriter().flush();
            res.getWriter().close();
        }
        return null;
    }

    /**
     * 压缩文件
     * @param zipBasePath 临时压缩文件基础路径
     * @param zipName 临时压缩文件名称
     * @param zipFilePath 临时压缩文件完整路径
     * @param filePaths 需要压缩的文件路径集合
     * @throws IOException
     */
    private String zipFile(String zipBasePath, String zipName, String zipFilePath, List<String> filePaths, ZipOutputStream zos) throws IOException {

        //循环读取文件路径集合，获取每一个文件的路径
        for(String filePath : filePaths){
            File inputFile = new File(filePath);  //根据文件路径创建文件
            if(inputFile.exists()) { //判断文件是否存在
                if (inputFile.isFile()) {  //判断是否属于文件，还是文件夹

                    //将文件写入zip内，即将文件进行打包
                    zos.putNextEntry(new ZipEntry(inputFile.getName()));

                    /*//创建输入流读取文件
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
                    //写入文件的方法，同上
                    int size = 0;
                    byte[] buffer = new byte[1024];  //设置读取数据缓存大小
                    while ((size = bis.read(buffer)) > 0) {
                        zos.write(buffer, 0, size);
                    }
                    //关闭输入输出流
                    bis.close();*/
                    zos.write(FileCopyUtils.copyToByteArray(inputFile));
                    zos.closeEntry();

                } else {  //如果是文件夹，则使用穷举的方法获取文件，写入zip
                    try {
                        File[] files = inputFile.listFiles();
                        List<String> filePathsTem = new ArrayList<String>();
                        for (File fileTem:files) {
                            filePathsTem.add(fileTem.toString());
                        }
                        return zipFile(zipBasePath, zipName, zipFilePath, filePathsTem,zos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


}
