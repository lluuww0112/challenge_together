package com.example.challenge_together;

public class day_Report {
    private String Dday;
    private String Diary;
    private String Total;
    private String challenge_title;
    private int day_cnt;
    private int success;
    private String challenge_day;
    private int view_mode;
    private String challenge_type;
    private String user_name;

    public day_Report(){}

    public day_Report(String dday, String diary, String total, String challenge_title, int day_cnt, int success, String challenge_day, int view_mode, String challenge_type, String user_name) {
        Dday = dday;
        Diary = diary;
        Total = total;
        this.challenge_title = challenge_title;
        this.day_cnt = day_cnt;
        this.success = success;
        this.challenge_day = challenge_day;
        this.view_mode = view_mode;
        this.challenge_type = challenge_type;
        this.user_name = user_name;
    }

    public String getDday() {
        return Dday;
    }

    public void setDday(String dday) {
        Dday = dday;
    }

    public String getDiary() {
        return Diary;
    }

    public void setDiary(String diary) {
        Diary = diary;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getChallenge_title() {
        return challenge_title;
    }

    public void setChallenge_title(String challenge_title) {
        this.challenge_title = challenge_title;
    }

    public int getDay_cnt() {
        return day_cnt;
    }

    public void setDay_cnt(int day_cnt) {
        this.day_cnt = day_cnt;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getChallenge_day() {
        return challenge_day;
    }

    public void setChallenge_day(String challenge_day) {
        this.challenge_day = challenge_day;
    }

    public int getView_mode() {
        return view_mode;
    }

    public void setView_mode(int view_mode) {
        this.view_mode = view_mode;
    }

    public String getChallenge_type() {
        return challenge_type;
    }

    public void setChallenge_type(String challenge_type) {
        this.challenge_type = challenge_type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
