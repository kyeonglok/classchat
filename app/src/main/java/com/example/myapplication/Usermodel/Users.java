package com.example.myapplication.Usermodel;

public class Users{

    private String email;
    private String nickname;
    private String profileURL;

    public Users() {
    }

    public Users(String email, String nickname, String profileURL) {
        this.email = email;
        this.nickname = nickname;
        this.profileURL = profileURL;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
