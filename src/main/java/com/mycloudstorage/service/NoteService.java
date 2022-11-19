package com.mycloudstorage.service;

import com.mycloudstorage.mapper.NoteMapper;
import com.mycloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int create(Note note) {
        return noteMapper.insertNote(note);
    }

    public List<Note> getByUserId(Integer userId) {
        return noteMapper.getNoteByUserId(userId);
    }

    public Note getById(Integer noteId) {
        return noteMapper.getNoteByNoteId(noteId);
    }

    public void update(Note note) {
        noteMapper.updateNote(note);
    }

    public void delete(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

}
