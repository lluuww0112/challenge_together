package com.example.challenge_together;

public class day_Data {
    private String data;
    private int cnt;
    private int succesion;

    public void day_Data(){}

    public day_Data(String data, int cnt, int succesion) {
        this.data = data;
        this.cnt = cnt;
        this.succesion = succesion;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getSuccesion() {
        return succesion;
    }

    public void setSuccesion(int succesion) {
        this.succesion = succesion;
    }
}
