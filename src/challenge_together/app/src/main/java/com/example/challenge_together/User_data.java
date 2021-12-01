package com.example.challenge_together;

public class User_data {
    String Nickname;
    String user_email;
    String user_profile;
    String challenge_title;

    public User_data(){}

    public User_data(String nickname, String user_email, String user_profile, String challenge_title) {
        Nickname = nickname;
        this.user_email = user_email;
        this.user_profile = user_profile;
        this.challenge_title = challenge_title;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getChallenge_title() {
        return challenge_title;
    }

    public void setChallenge_title(String challenge_title) {
        this.challenge_title = challenge_title;
    }
}
