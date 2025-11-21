package com.example.wordl_project.models;

import androidx.annotation.NonNull;

public class User {

    protected String id;
    protected String password;
    public String email;
    public int score;
    public int sucssesWordCount;
    public int faildWordCount;
    public double sucssesRate;
    public boolean isAdmin;


    public User() {
    }

    public User(String id, String password, String email, int score,
                int sucssesWordCount, int faildWordCount, double sucssesRate,
                boolean isAdmin) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.score = score;
        this.sucssesWordCount = sucssesWordCount;
        this.faildWordCount = faildWordCount;
        this.sucssesRate = sucssesRate;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSucssesWordCount() {
        return sucssesWordCount;
    }

    public void setSucssesWordCount(int sucssesWordCount) {
        this.sucssesWordCount = sucssesWordCount;
    }

    public int getFaildWordCount() {
        return faildWordCount;
    }

    public void setFaildWordCount(int faildWordCount) {
        this.faildWordCount = faildWordCount;
    }

    public double getSucssesRate() {
        return sucssesRate;
    }

    public void setSucssesRate(double sucssesRate) {
        this.sucssesRate = sucssesRate;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", score=" + score +
                ", sucssesWordCount=" + sucssesWordCount +
                ", faildWordCount=" + faildWordCount +
                ", sucssesRate=" + sucssesRate +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
