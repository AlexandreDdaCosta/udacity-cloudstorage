package com.udacity.jwdnd.course1.cloudstorage.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class FeedbackService {

    public static Model addError(Model model, String error) {
        model.addAttribute("result", "error")
                .addAttribute("error", error);
        return model;
    }

    public static Model addErrors(Model model, List<String> errors) {
        model.addAttribute("result", "error")
                .addAttribute("errors", errors);
        return model;
    }

    public static Model addSuccess(Model model, String message) {
        model.addAttribute("result", "success")
                .addAttribute("message", message);
        return model;
    }
}
