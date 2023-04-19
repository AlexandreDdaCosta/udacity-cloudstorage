package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE id = #{id} and userid = #{userId}")
    Note getNote(Integer id, Integer userId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getUserNotes(Integer userId);

    @Insert("INSERT INTO NOTES (title, description, userid) " +
            "VALUES (#{title}, #{description}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addNote(Note note);

    @Delete("DELETE FROM NOTES WHERE id = #{id} and userid = #{userId}")
    void deleteNote(Integer id, Integer userId);

    @Update("UPDATE NOTES SET title = #{title}, description = #{description}" +
            "WHERE id = #{id} and userid = #{userId}")
    void updateNote(Note note);
}
