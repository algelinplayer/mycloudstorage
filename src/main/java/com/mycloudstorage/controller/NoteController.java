package com.mycloudstorage.controller;

import com.mycloudstorage.model.Note;
import com.mycloudstorage.model.User;
import com.mycloudstorage.service.NoteService;
import com.mycloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private UserService userService;
    private NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping
    public String createOrEditNote(Authentication authentication, Note note, Model model) {

        String username = authentication.getName();
        User user = userService.getUser(username);
        note.setUserId(user.getUserId());
        Integer noteId = note.getNoteId();

        if(noteId == null) {
          if (createNote(note, model, user) < 0) {
            return redirectSuccess(false);
          }
        } else {
          // TODO: Handle update errors and return "redirectSuccess(false)"
          updateNote(note);
        }

        return redirectSuccess(true);
    }

    private static String redirectHome() {
      return "redirect:/home";
    }

    private static String redirectSuccess(boolean x) {
      return "redirect:/result?isSuccess=" + x;
    }

    private void updateNote(Note note) {
        noteService.update(note);
    }

    private int createNote(Note note, Model model, User user) {
      int inserted = noteService.create(note);
      model.addAttribute("notes", noteService.getByUserId(user.getUserId()));
      model.addAttribute("isNotesActive", true);
      return inserted;
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Model model) {
        // TODO: Handle delete errors and return "redirectSuccess(false)"
        noteService.delete(noteId);
        return redirectSuccess(true);
    }
}
