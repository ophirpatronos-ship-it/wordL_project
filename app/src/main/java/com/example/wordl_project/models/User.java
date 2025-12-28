package com.example.wordl_project.models;

public class User {
    protected String id;
    protected String username;
    protected String password;
    protected String email;
    public String image;
    protected int score;
    protected int sucssesWordCount;
    protected int faildWordCount;
    protected double sucssesRate;
    protected boolean isAdmin;

    public User() {}

    public User(String id, String username, String password, String email, String image, int score, int sucssesWordCount, int faildWordCount, double sucssesRate, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.image = image;
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

    public int getSucssesWordCount() {
        return sucssesWordCount;
    }

    public void setSucssesWordCount(int sucssesWordCount) {this.sucssesWordCount = sucssesWordCount;}

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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", score=" + score +
                ", sucssesWordCount=" + sucssesWordCount +
                ", faildWordCount=" + faildWordCount +
                ", sucssesRate=" + sucssesRate +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
