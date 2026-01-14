package com.example.wordl_project.models;

import androidx.annotation.NonNull;

public class StringWrapper {

    private String id;
    private String text;

    public StringWrapper() {
    }

    public StringWrapper(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return "StringWrapper{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
