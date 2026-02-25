package com.example.wordl_project.models;

import com.google.firebase.database.Exclude;

public class User {
    public String image;
    protected String id;
    protected String username;
    protected String password;
    protected String email;
    protected int score;
    protected int successesWordCount;
    protected int totalWordCount;
    protected boolean isAdmin;

    public User() {
    }

    public User(String id, String username, String password, String email, String image, int score, int successesWordCount, int totalWordCount, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.image = image;
        this.score = score;
        this.successesWordCount = successesWordCount;
        this.totalWordCount = totalWordCount;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSuccessesWordCount() {
        return successesWordCount;
    }

    public void setSuccessesWordCount(int successesWordCount) {
        this.successesWordCount = successesWordCount;
    }

    public int getFaildWordCount() {
        return totalWordCount;
    }

    public void setFaildWordCount(int faildWordCount) {
        this.totalWordCount = faildWordCount;
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", score=" + score +
                ", sucssesWordCount=" + successesWordCount +
                ", faildWordCount=" + totalWordCount +
                ", isAdmin=" + isAdmin +
                '}';
    }


    public void addScore(int score) {
        this.score += score;
    }

    public void addSuccessesWordCount(int sucssesWordCount) {
        this.successesWordCount++;
    }

    public void addTotalWordCount(int totalWordCount) {
        this.totalWordCount++;
    }

    @Exclude
    public double getSuccessesRate() {
        if (totalWordCount == 0) return 0;
        return (double) successesWordCount / totalWordCount;
    }


}
