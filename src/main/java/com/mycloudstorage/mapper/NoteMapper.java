package com.mycloudstorage.mapper;

import com.mycloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Select("SELECT * FROM NOTES")
    List<Note> getNotes();

    @Select("SELECT * FROM NOTES where userid = #{userId}")
    List<Note> getNoteByUserId(Integer userId);

    @Select("SELECT * FROM NOTES WHERE notetitle = #{noteTitle}")
    Note getNoteByTitle(String noteTitle);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNoteByNoteId(Integer noteId);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} " +
            "WHERE noteid = #{noteId}")
    void updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void deleteNote(Integer noteId);
}
