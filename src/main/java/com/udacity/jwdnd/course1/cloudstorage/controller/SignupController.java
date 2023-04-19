package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.lib.CustomInvalidParameterException;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FeedbackService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller()
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        if (! userService.isUsernameAvailable(user.getUsername())) {
            model = feedbackService.addError(model, "Chosen user name already exists.");
            return "signup";
        }
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("result", "success");
        } catch (CustomInvalidParameterException e) {
            model = feedbackService.addErrors(model, e.getErrorMessages());
            return "signup";
        } catch (Exception e) {
            model = feedbackService.addError(model, "There was an error signing you up. Please try again.");
            return "signup";
        }
        return "redirect:login";
    }
}