package com.test.dao.test;

import com.test.model.TempFilePo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository
public interface TestMapper {

    @Select("select * from TempFile where id=#{id}")
    TempFilePo selectById(String id);

    @Select("select * from TempFile")
    List<TempFilePo> selectAll(RowBounds rowBounds);

//    @Select("select count(1) from TempFile")
    @SelectProvider(type = TempFileDaoProvider.class,method = "selectCountByColumn")
    int selectCount(TempFilePo tempFilePo);

    @Insert("insert into tempFile(id,fileType,fileSize,fileName,downloadUrl) " +
            "values(#{id},#{fileType},#{fileSize},#{fileName},#{downloadUrl})")
    void saveOne(TempFilePo po);

    @Delete("delete from tempfile where id = #{id}")
    void deleteById(String id);



    class TempFileDaoProvider {
        public String selectCountByColumn(TempFilePo tempFilePo) {
            String sql = "SELECT count(1) FROM tempFile where 1=1 ";
            if(tempFilePo==null){
                return sql;
            }
            if(tempFilePo.getFileType()!=null){
                sql += " and fileType = #{fileType} ";
            }
            if(tempFilePo.getFileName()!=null){
                sql += " and fileName = #{fileName} ";
            }
            if(tempFilePo.getFileSize()!=0){
                sql += " and fileSize >= #{fileSize} ";
            }
            System.out.println(sql);
            return sql;
        }
    }
}
