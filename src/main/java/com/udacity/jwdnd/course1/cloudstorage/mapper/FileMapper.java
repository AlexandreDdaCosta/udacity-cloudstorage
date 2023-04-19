package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE id = #{id} and userid = #{userId}")
    File getFile(Integer id, Integer userId);

    @Select("SELECT id FROM FILES WHERE id = #{id} and userid = #{userId}")
    Integer getFileId(Integer id, Integer userId);

    @Select("SELECT id, name from FILES WHERE userid = #{userId}")
    List<File> getUserFiles(Integer userId);

    @Insert("INSERT INTO FILES (name, contenttype, filesize, data, userid) " +
            "VALUES(#{name}, #{contentType}, #{fileSize}, #{data}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addFile(File file);

    @Delete("DELETE FROM FILES WHERE id = #{id} and userid = #{userId}")
    void deleteFile(Integer id, Integer userId);
}
