package com.udacity.jwdnd.course1.cloudstorage.model;

public class NoteForm {

    private String id;
    private String description;
    private String title;
    private String authUsername;

    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthUsername() {
        return this.authUsername;
    }
    public void setAuthUsername(String authUsername) {
        this.authUsername = authUsername;
    }
}
