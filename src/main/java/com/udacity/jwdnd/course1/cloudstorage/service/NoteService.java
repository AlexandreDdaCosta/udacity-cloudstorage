package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NoteMapper noteMapper;

    public Note getUserNote(Integer id, Integer userId) {
        return noteMapper.getNote(id, userId);
    }

    public List<Note> getUserNotes(Integer userId) {
        return noteMapper.getUserNotes(userId);
    }

    public void addNote(NoteForm noteForm) {
        Note note = new Note()
                .setTitle(noteForm.getTitle())
                .setDescription(noteForm.getDescription())
                .setUserId(userMapper.getUserIdByName(noteForm.getAuthUsername()))
                .validate();
        noteMapper.addNote(note);
    }

    public void deleteNote(Integer id, Integer userId) {
        noteMapper.deleteNote(id, userId);
    }

    public void updateNote(Integer id, Integer userId, NoteForm noteForm) {
        Note note = new Note()
                .setId(id)
                .setTitle(noteForm.getTitle())
                .setDescription(noteForm.getDescription())
                .setUserId(userId)
                .validate();
        noteMapper.updateNote(note);
    }
}
