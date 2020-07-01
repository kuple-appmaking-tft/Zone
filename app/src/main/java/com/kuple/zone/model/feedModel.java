package com.kuple.zone.model;
import java.util.List;
public class feedModel {
    private List<Bus> shuttleBus;
    private List<Meal> schoolMeal;
    private List<Timetable> timetable;

    public void setSchoolMeal(List<Meal> schoolMeal) {
        this.schoolMeal = schoolMeal;
    }

    public void setShuttleBus(List<Bus> shuttleBus) {
        this.shuttleBus = shuttleBus;
    }

    public void setTimetable(List<Timetable> timetable) {
        this.timetable = timetable;
    }

    public List<Bus> getShuttleBus() {
        return shuttleBus;
    }

    public List<Meal> getSchoolMeal(){
        return schoolMeal;
    }

    public List<Timetable> getTimetable() {
        return timetable;
    }

    @Override
    public String toString() {
        return "feedModel{" +
                "shuttleBus=" + shuttleBus +
                ", schoolMeal=" + schoolMeal +
                ", timetable=" + timetable +
                '}';
    }

}