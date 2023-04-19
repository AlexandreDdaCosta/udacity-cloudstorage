package com.udacity.jwdnd.course1.cloudstorage.model;

import org.springframework.web.multipart.MultipartFile;

public class FileForm {

    private MultipartFile fileUpload;
    private String authUsername;

    public MultipartFile getFileUpload() {
        return fileUpload;
    }
    public void setFileUpload(MultipartFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getAuthUsername() {
        return this.authUsername;
    }
    public void setAuthUsername(String authUsername) {
        this.authUsername = authUsername;
    }
}
