package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.lib.CustomInvalidParameterException;
import com.udacity.jwdnd.course1.cloudstorage.service.FeedbackService;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FileController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @GetMapping(
            path = "/file",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody byte[] getFile(Authentication authentication, @RequestParam("id") Integer id) {
        return fileService.getFile(id, userService.getUserIdByName(authentication.getName())).getData();
    }

    @PostMapping("/file")
    public String addFile(Authentication authentication, FileForm fileForm, Model model) {
        fileForm.setAuthUsername(authentication.getName());
        try {
            fileService.addFile(fileForm);
            model = FeedbackService.addSuccess(model, "New file has been successfully added.");
        } catch (CustomInvalidParameterException e) {
            model = feedbackService.addErrors(model, e.getErrorMessages());
        } catch (IOException e) {
            model = FeedbackService.addError(model, "File was not added due to a file read error.");
        } catch (Exception e) {
            model = FeedbackService.addError(model, "Internal error adding file.");
        }
        return "result";
    }

    @GetMapping("/file/delete")
    public String deleteFile(Authentication authentication, @RequestParam("id") Integer id, Model model) {
        Integer userId = userService.getUserIdByName(authentication.getName());
        Integer fileId  = fileService.getFileById(id, userId);
        if (fileId == null) {
            model = feedbackService.addError(model, "Unknown file.");
        }
        else {
            try {
                fileService.deleteFile(id, userId);
                model = feedbackService.addSuccess(model, "File has been successfully deleted.");
            } catch (Exception e) {
                model = feedbackService.addError(model, "Internal server error.");
            }
        }
        return "result";
    }
}
