package com.udacity.jwdnd.course1.cloudstorage.model;

import com.udacity.jwdnd.course1.cloudstorage.lib.CustomInvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Note {

    private Integer id;
    private String title;
    private String description;
    private Integer userId;

    public Integer getId() {
        return this.id;
    }
    public Note setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }
    public Note setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return this.description;
    }
    public Note setDescription (String description) {
        this.description = description;
        return this;
    }

    public Integer getUserId() {
        return this.userId;
    }
    public Note setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Note validate() {
        List<String> errorMessages = new ArrayList<>(Collections.emptyList());
        if ((userId == null || userId < 1) || (id != null && id < 1)) {
            errorMessages.add("Internal data submission error.");
            throw new CustomInvalidParameterException(errorMessages);
        }
        if (description == null || description.trim().length() == 0) {
            errorMessages.add("Note description is empty.");
        } else if (description.length() > 1000) {
            errorMessages.add("Description is too long; maximum 1000 characters.");
        }
        if (title == null || title.trim().length() == 0) {
            errorMessages.add("Note title is empty.");
        } else {
            title = title.trim();
            if (title.length() > 20) {
                errorMessages.add("Title is too long; maximum 20 characters.");
            }
        }
        if (! errorMessages.isEmpty()) {
            throw new CustomInvalidParameterException(errorMessages);
        }
        return this;
    }
}
