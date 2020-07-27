package com.kuple.zone.model;

public class BusInfo {
    private int date;
    private String time;

    public int getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(int date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "date=" + date +
                ", time='" + time + '\'' +
                '}';
    }
}