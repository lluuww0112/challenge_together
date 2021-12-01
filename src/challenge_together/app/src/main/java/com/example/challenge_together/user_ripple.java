package com.example.challenge_together;

public class user_ripple {
    private String user_name;
    private String user_profile;
    private String ripple_text;

    user_ripple(){}

    public user_ripple(String user_name, String user_profile, String ripple_text) {
        this.user_name = user_name;
        this.user_profile = user_profile;
        this.ripple_text = ripple_text;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getRipple_text() {
        return ripple_text;
    }

    public void setRipple_text(String ripple_text) {
        this.ripple_text = ripple_text;
    }
}
