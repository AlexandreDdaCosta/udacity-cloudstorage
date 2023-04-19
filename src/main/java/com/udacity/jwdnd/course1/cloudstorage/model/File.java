package com.udacity.jwdnd.course1.cloudstorage.model;

import com.udacity.jwdnd.course1.cloudstorage.lib.CustomInvalidParameterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class File {

    private Integer id;
    private String name;
    private String contentType;
    private String fileSize;
    private byte[] data;
    private Integer userId;

    public Integer getId() {
        return id;
    }
    public File setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }
    public File setName(String name) {
        this.name = name;
        return this;
    }

    public String getContentType() {
        return contentType;
    }
    public File setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getFileSize() {
        return fileSize;
    }
    public File setFileSize(String fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public byte[] getData() {
        return data;
    }
    public File setData(byte[] data) {
        this.data = data;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }
    public File setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public File validate() {
        List<String> errorMessages = new ArrayList<>(Collections.emptyList());
        if ((userId == null || userId < 1) || (id != null && id < 1)) {
            errorMessages.add("Internal data submission error.");
            throw new CustomInvalidParameterException(errorMessages);
        }
        if (name == null || name.trim().length() == 0) {
            errorMessages.add("No file was uploaded: name is empty.");
        } else if (name.length() > 200) {
            errorMessages.add("File name is too long; maximum 200 characters.");
        }
        if (fileSize != null) {
            Integer fileSizeAsInt = Integer.parseInt(fileSize);
            if (fileSizeAsInt > 5242880) {
                errorMessages.add("File is too large; maximum 5 MB.");
            }
        }
        if (contentType == null || contentType.trim().length() == 0) {
            errorMessages.add("File has unidentifiable content type.");
        }
        if (! errorMessages.isEmpty()) {
            throw new CustomInvalidParameterException(errorMessages);
        }
        return this;
    }
}

