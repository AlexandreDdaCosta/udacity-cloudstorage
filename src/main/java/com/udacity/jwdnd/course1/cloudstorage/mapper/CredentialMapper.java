package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE id = #{id} and userid = #{userId}")
    Credential getCredential(Integer id, Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getUserCredentials(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, encodedkey, password, userid) " +
            "VALUES (#{url}, #{username}, #{encodedKey}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE id = #{id} and userid = #{userId}")
    void deleteCredential(Integer id, Integer userId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, encodedkey = #{encodedKey}, " +
            "password = #{password} " +
            "WHERE id = #{id} and userid = #{userId}")
    void updateCredential(Credential credential);
}
