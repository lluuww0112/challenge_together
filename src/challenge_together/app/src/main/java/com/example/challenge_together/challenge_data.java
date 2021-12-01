package com.example.challenge_together;

public class challenge_data {
    private String title;
    private String goal;
    private String challenge_type;
    private int date;
    private int challengers_cnt = 0;
    private String Start_date;
    private String End_date;

    public challenge_data(){}

    public challenge_data(String title, String goal, String challenge_type, int date, int challengers_cnt, String start_date, String end_date) {
        this.title = title;
        this.goal = goal;
        this.challenge_type = challenge_type;
        this.date = date;
        this.challengers_cnt = challengers_cnt;
        Start_date = start_date;
        End_date = end_date;
    }

    public String getStart_date() {
        return Start_date;
    }

    public void setStart_date(String start_date) {
        Start_date = start_date;
    }

    public String getEnd_date() {
        return End_date;
    }

    public void setEnd_date(String end_date) {
        End_date = end_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getChallenge_type() {
        return challenge_type;
    }

    public void setChallenge_type(String challenge_type) {
        this.challenge_type = challenge_type;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getChallengers_cnt() {
        return challengers_cnt;
    }

    public void setChallengers_cnt(int challengers_cnt) {
        this.challengers_cnt = challengers_cnt;
    }
}
