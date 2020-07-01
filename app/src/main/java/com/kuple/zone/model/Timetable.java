package com.kuple.zone.model;

public class Timetable {
    private int time;
    private int code;
    private String subject;

    public int getCode() {
        return code;
    }

    public int getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "time=" + time +
                ", code=" + code +
                ", subject='" + subject + '\'' +
                '}';
    }
}