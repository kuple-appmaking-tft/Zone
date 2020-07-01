package com.kuple.zone.model;

public class Meal {
    private int date;
    public String meal;

    public int getDate() {
        return date;
    }

    public String getMeal() {
        return meal;
    }

    public void setDate(int date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "date=" + date +
                ", meal='" + meal + '\'' +
                '}';
    }
}