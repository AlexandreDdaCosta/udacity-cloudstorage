package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.lib.CustomInvalidParameterException;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.service.FeedbackService;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private UserService userService;

    @PostMapping("/credential")
    public String addUpdateCredential(Authentication authentication, CredentialForm credentialForm, Model model) {
        String username = authentication.getName();
        Integer userId = userService.getUserIdByName(username);
        String credentialIdString = credentialForm.getId();
        String message = "";
        if (credentialIdString == null || credentialIdString.isEmpty()) {
            credentialForm.setAuthUsername(username);
            try {
                credentialService.addCredential(credentialForm);
            } catch (CustomInvalidParameterException e) {
                model = feedbackService.addErrors(model, e.getErrorMessages());
                return "result";
            } catch (Exception e) {
                model = feedbackService.addError(model, "Internal server error.");
                return "result";
            }
            message = "New credential has been successfully added.";
        } else {
            Integer credentialId;
            try {
                credentialId = Integer.parseInt(credentialIdString);
            } catch (NumberFormatException e) {
                model = feedbackService.addError(model, "Bad credential identifier in form.");
                return "result";
            }
            try {
                credentialService.updateCredential(credentialId, userId, credentialForm);
            } catch (CustomInvalidParameterException e) {
                model = feedbackService.addErrors(model, e.getErrorMessages());
                return "result";
            } catch (Exception e) {
                model = feedbackService.addError(model, "Internal server error.");
                return "result";
            }
            message = "Existing credential has been successfully updated.";
        }
        model = feedbackService.addSuccess(model, message);
        return "result";
    }

    @GetMapping("/credential/delete")
    public String deleteCredential(Authentication authentication, @RequestParam("id") Integer id, Model model) {
        Integer userId = userService.getUserIdByName(authentication.getName());
        Credential credential = credentialService.getUserCredential(id, userId);
        if (credential == null) {
            model = feedbackService.addError(model, "Unknown credential.");
        }
        else {
            try {
                credentialService.deleteCredential(id, userId);
                model = feedbackService.addSuccess(model, "Credential has been successfully deleted.");
            } catch (Exception e) {
                model = feedbackService.addError(model, "Internal server error.");
            }
        }
        return "result";
    }
}