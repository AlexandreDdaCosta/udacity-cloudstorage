package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.lib.CustomInvalidParameterException;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.service.FeedbackService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @PostMapping("/note")
    public String addUpdateNote(Authentication authentication, NoteForm noteForm, Model model) {
        String username = authentication.getName();
        Integer userId = userService.getUserIdByName(username);
        String noteIdString = noteForm.getId();
        String message = "";
        if (noteIdString == null || noteIdString.isEmpty()) {
            noteForm.setAuthUsername(username);
            try {
                noteService.addNote(noteForm);
            } catch (CustomInvalidParameterException e) {
                model = feedbackService.addErrors(model, e.getErrorMessages());
                return "result";
            } catch (Exception e) {
                model = feedbackService.addError(model, "Internal server error.");
                return "result";
            }
            message = "New note has been successfully added.";
        } else {
            Integer noteId;
            try {
                noteId = Integer.parseInt(noteIdString);
            } catch (NumberFormatException e) {
                model = feedbackService.addError(model, "Bad note identifier in form.");
                return "result";
            }
            try {
                noteService.updateNote(noteId, userId, noteForm);
            } catch (CustomInvalidParameterException e) {
                model = feedbackService.addErrors(model, e.getErrorMessages());
                return "result";
            } catch (Exception e) {
                model = feedbackService.addError(model, "Internal server error.");
                return "result";
            }
            message = "Existing note has been successfully updated.";
        }
        model = feedbackService.addSuccess(model, message);
        return "result";
    }

    @GetMapping("/note/delete")
    public String deleteNote(Authentication authentication, @RequestParam("id") Integer id, Model model) {
        Integer userId = userService.getUserIdByName(authentication.getName());
        Note note = noteService.getUserNote(id, userId);
        if (note == null) {
            model = feedbackService.addError(model, "Unknown note.");
        }
        else {
            try {
                noteService.deleteNote(id, userId);
                model = feedbackService.addSuccess(model, "Note has been successfully deleted.");
            } catch (Exception e) {
                model = feedbackService.addError(model, "Internal server error.");
            }
        }
        return "result";
    }
}
